/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.depgraph;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetResolver;
import com.opengamma.engine.function.CompiledFunctionDefinition;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.LiveDataSourcingFunction;
import com.opengamma.engine.function.ParameterizedFunction;
import com.opengamma.engine.function.resolver.CompiledFunctionResolver;
import com.opengamma.engine.function.resolver.DefaultCompiledFunctionResolver;
import com.opengamma.engine.livedata.LiveDataAvailabilityProvider;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.tuple.Pair;

/**
 * 
 */
public class DependencyGraphBuilder {
  private static final Logger s_logger = LoggerFactory.getLogger(DependencyGraphBuilder.class);
  // Injected Inputs:
  private String _calculationConfigurationName;
  private LiveDataAvailabilityProvider _liveDataAvailabilityProvider;
  private ComputationTargetResolver _targetResolver;
  private CompiledFunctionResolver _functionResolver;
  private FunctionCompilationContext _compilationContext;
  // State:
  private DependencyGraph _graph;

  /**
   * @return the calculationConfigurationName
   */
  public String getCalculationConfigurationName() {
    return _calculationConfigurationName;
  }

  /**
   * @param calculationConfigurationName the calculationConfigurationName to set
   */
  public void setCalculationConfigurationName(String calculationConfigurationName) {
    _calculationConfigurationName = calculationConfigurationName;
    _graph = new DependencyGraph(_calculationConfigurationName);
  }

  /**
   * @return the liveDataAvailabilityProvider
   */
  public LiveDataAvailabilityProvider getLiveDataAvailabilityProvider() {
    return _liveDataAvailabilityProvider;
  }

  /**
   * @param liveDataAvailabilityProvider the liveDataAvailabilityProvider to set
   */
  public void setLiveDataAvailabilityProvider(LiveDataAvailabilityProvider liveDataAvailabilityProvider) {
    _liveDataAvailabilityProvider = liveDataAvailabilityProvider;
  }

  /**
   * @return the functionResolver
   */
  public CompiledFunctionResolver getFunctionResolver() {
    return _functionResolver;
  }

  /**
   * @param functionResolver the functionResolver to set
   */
  public void setFunctionResolver(CompiledFunctionResolver functionResolver) {
    _functionResolver = functionResolver;
  }

  /**
   * @return the targetResolver
   */
  public ComputationTargetResolver getTargetResolver() {
    return _targetResolver;
  }

  /**
   * @param targetResolver the targetResolver to set
   */
  public void setTargetResolver(ComputationTargetResolver targetResolver) {
    _targetResolver = targetResolver;
  }

  /**
   * @return the compilationContext
   */
  public FunctionCompilationContext getCompilationContext() {
    return _compilationContext;
  }

  /**
   * @param compilationContext the compilationContext to set
   */
  public void setCompilationContext(FunctionCompilationContext compilationContext) {
    _compilationContext = compilationContext;
  }

  protected void checkInjectedInputs() {
    ArgumentChecker.notNullInjected(getLiveDataAvailabilityProvider(), "liveDataAvailabilityProvider");
    ArgumentChecker.notNullInjected(getFunctionResolver(), "functionResolver");
    ArgumentChecker.notNullInjected(getTargetResolver(), "targetResolver");
    ArgumentChecker.notNullInjected(getCalculationConfigurationName(), "calculationConfigurationName");
  }

  public void addTarget(ValueRequirement requirement) {
    addTarget(Collections.singleton(requirement));
  }

  public void addTarget(Set<ValueRequirement> requirements) {
    ArgumentChecker.notNull(requirements, "Value requirements");
    checkInjectedInputs();

    for (ValueRequirement requirement : requirements) {
      final ResolutionState resolutionState = resolveValueRequirement(requirement, null);
      Pair<DependencyNode, ValueSpecification> terminalNode = addTargetRequirement(resolutionState);
      s_logger.debug("Terminal node {} producing {}", terminalNode.getFirst(), terminalNode.getSecond());
      _graph.addTerminalOutputValue(terminalNode.getSecond());
    }
  }

  private DependencyNode createDependencyNode(final ComputationTarget target, final DependencyNode dependent) {
    DependencyNode node = new DependencyNode(target);
    if (dependent != null) {
      node.addDependentNode(dependent);
    }
    return node;
  }

  // Note the order requirements are considered can affect function choices and resultant graph construction (see [ENG-259]).
  private ResolutionState resolveValueRequirement(final ValueRequirement requirement, final DependencyNode dependent) {
    final ComputationTarget target = getTargetResolver().resolve(requirement.getTargetSpecification());
    if (target == null) {
      throw new UnsatisfiableDependencyGraphException(requirement);
    }
    s_logger.info("Resolving target requirement for {} on {}", requirement, target);
    // Find existing nodes in the graph
    final Collection<Pair<DependencyNode, ValueSpecification>> existingNodes = _graph.getNodesSatisfying(requirement);
    final ResolutionState resolutionState;
    if (existingNodes != null) {
      s_logger.debug("{} existing nodes found", existingNodes.size());
      for (Pair<DependencyNode, ValueSpecification> node : existingNodes) {
        s_logger.debug("{} producing {}", node.getFirst(), node.getSecond());
      }
      resolutionState = new ResolutionState(requirement);
      // If it's live data, stop now. Otherwise fall through to the functions as they may be more generic than the
      // composed specifications attached to the nodes found
      if (getLiveDataAvailabilityProvider().isAvailable(requirement)) {
        resolutionState.addExistingNodes(existingNodes);
        return resolutionState;
      }
    } else {
      // Find live data
      if (getLiveDataAvailabilityProvider().isAvailable(requirement)) {
        s_logger.debug("Live Data : {} on {}", requirement, target);
        LiveDataSourcingFunction function = new LiveDataSourcingFunction(requirement);
        return new ResolutionState(requirement, function.getResult(), new ParameterizedFunction(function, function.getDefaultParameters()), createDependencyNode(target, dependent));
      }
      resolutionState = new ResolutionState(requirement);
    }
    // Find functions that can do this
    final DependencyNode node = createDependencyNode(target, dependent);
    final Iterator<Pair<ParameterizedFunction, ValueSpecification>> itr = getFunctionResolver().resolveFunction(requirement, node);
    resolutionState.setLazyPopulator(new ResolutionState.LazyPopulator() {

      private DependencyNode _node = node;

      @Override
      protected boolean more() {
        while (itr.hasNext()) {
          final Pair<ParameterizedFunction, ValueSpecification> resolvedFunction = itr.next();
          final CompiledFunctionDefinition functionDefinition = resolvedFunction.getFirst().getFunction();
          final Set<ValueSpecification> outputValues = functionDefinition.getResults(getCompilationContext(), target);
          final ValueSpecification originalOutput = resolvedFunction.getSecond();
          final ValueSpecification resolvedOutput = originalOutput.compose(requirement);
          final DependencyNode existingNode = _graph.getNodeProducing(resolvedOutput);
          if (existingNode != null) {
            // Resolved function output already in the graph
            s_logger.debug("Found {} - already in graph", resolvedFunction.getSecond());
            resolutionState.addExistingNode(existingNode, resolvedOutput);
            return true;
          }
          if (_node == null) {
            _node = createDependencyNode(target, dependent);
          }
          if (originalOutput.equals(resolvedOutput)) {
            _node.addOutputValues(outputValues);
          } else {
            for (ValueSpecification outputValue : outputValues) {
              if (originalOutput.equals(outputValue)) {
                s_logger.debug("Substituting {} with {}", outputValue, resolvedOutput);
                _node.addOutputValue(resolvedOutput);
              } else {
                _node.addOutputValue(outputValue);
              }
            }
          }
          resolutionState.addFunction(resolvedOutput, resolvedFunction.getFirst(), _node);
          _node = null;
          return true;
        }
        return false;
      }
    });
    return resolutionState;
  }

  private Pair<DependencyNode, ValueSpecification> addTargetRequirement(final ResolutionState resolved) {
    do {
      if (resolved.isEmpty()) {
        return resolved.getLastValid();
      }
      final ResolutionState.Node resolutionNode = resolved.getFirst();
      boolean strictConstraints = resolutionNode.getValueSpecification().getProperties().isStrict();
      if (strictConstraints) {
        // If backtracking and/or substitution has taken place, an exact match may have already been added to the graph
        final DependencyNode existingNode = _graph.getNodeProducing(resolutionNode.getValueSpecification());
        if (existingNode != null) {
          s_logger.debug("Existing node {} produces {}", existingNode, resolutionNode.getValueSpecification());
          return Pair.of(existingNode, resolutionNode.getValueSpecification());
        }
      }
      final DependencyNode graphNode = resolutionNode.getDependencyNode();
      // Resolve and add the inputs, or just add them if repeating because of a backtrack
      if (resolutionNode.getInputStates() == null) {
        if (resolutionNode.getParameterizedFunction() != null) {
          try {
            final CompiledFunctionDefinition functionDefinition = resolutionNode.getParameterizedFunction().getFunction();
            Set<ValueRequirement> inputRequirements = functionDefinition.getRequirements(getCompilationContext(), graphNode.getComputationTarget(), resolved.getValueRequirement());
            resolutionNode.dimInputState(inputRequirements.size());
            boolean pendingInputStates = false;
            for (ValueRequirement inputRequirement : inputRequirements) {
              final ResolutionState inputState = resolveValueRequirement(inputRequirement, graphNode);
              resolutionNode.addInputState(inputState);
              final Pair<DependencyNode, ValueSpecification> inputValue = addTargetRequirement(inputState);
              graphNode.addInputNode(inputValue.getFirst());
              graphNode.addInputValue(inputValue.getSecond());
              if (!inputState.isEmpty()) {
                pendingInputStates = true;
              }
              if (!inputState.getValueRequirement().getConstraints().isStrict()) {
                strictConstraints = false;
              }
            }
            if (!pendingInputStates && resolved.isSingle()) {
              resolved.removeFirst();
            }
          } catch (Throwable t) {
            // Note catch Throwable rather than the UnsatisfiedDependencyGraphException in case functionDefinition.getRequirements went wrong
            s_logger.debug("Backtracking on dependency graph error", t);
            graphNode.clearInputs();
            resolved.removeFirst();
            if (resolved.isEmpty()) {
              throw new UnsatisfiableDependencyGraphException(resolved.getValueRequirement(), t);
            }
            continue;
          }
        } else {
          s_logger.debug("Existing Node : {} on {}", resolved.getValueRequirement(), graphNode.getComputationTarget());
          final ValueSpecification originalOutput = resolutionNode.getValueSpecification();
          final ValueSpecification resolvedOutput = originalOutput.compose(resolved.getValueRequirement());
          if (originalOutput.equals(resolvedOutput)) {
            final Pair<DependencyNode, ValueSpecification> result = Pair.of(graphNode, resolvedOutput);
            if (resolved.isSingle()) {
              resolved.removeFirst();
              resolved.setLastValid(result);
            }
            return result;
          }
          // TODO: update the node to reduce the specification & then return the node with the resolvedOutput
          throw new UnsupportedOperationException("In-situ specification reduction not implemented");
        }
      } else {
        // TODO factor the guarded code into a method and only set up the try/catch overhead if we're not a single resolve
        try {
          // This is a "retry" following a backtrack so clear any previous inputs and remove this node from the graph
          _graph.removeDependencyNode(graphNode);
          graphNode.clearInputs();
          boolean pendingInputStates = false;
          for (ResolutionState inputState : resolutionNode.getInputStates()) {
            final Pair<DependencyNode, ValueSpecification> inputValue = addTargetRequirement(inputState);
            graphNode.addInputNode(inputValue.getFirst());
            graphNode.addInputValue(inputValue.getSecond());
            if (!inputState.isEmpty()) {
              pendingInputStates = true;
            }
            if (!inputState.getValueRequirement().getConstraints().isStrict()) {
              strictConstraints = false;
            }
          }
          if (!pendingInputStates && resolved.isSingle()) {
            resolved.removeFirst();
          }
        } catch (UnsatisfiableDependencyGraphException e) {
          s_logger.debug("Backtracking on dependency graph error", e);
          graphNode.clearInputs();
          resolved.removeFirst();
          if (resolved.isEmpty()) {
            throw new UnsatisfiableDependencyGraphException(resolved.getValueRequirement(), e);
          }
          continue;
        }
      }
      // Late resolution of the output based on the actual inputs used
      final CompiledFunctionDefinition functionDefinition = resolutionNode.getParameterizedFunction().getFunction();
      ValueSpecification resolvedOutput = resolutionNode.getValueSpecification();
      Set<ValueSpecification> originalOutputValues = null;
      if (!strictConstraints) {
        final Set<ValueSpecification> newOutputValues;
        try {
          newOutputValues = functionDefinition.getResults(getCompilationContext(), graphNode.getComputationTarget(), graphNode.getInputValues());
        } catch (Throwable t) {
          // detect failure from .getResults
          s_logger.debug("Deep backtracking at late resolution failure", t);
          graphNode.clearInputs();
          if (resolved.isEmpty() || !resolved.removeDeepest()) {
            throw new UnsatisfiableDependencyGraphException(resolved.getValueRequirement(), t);
          }
          continue;
        }
        if (!graphNode.getOutputValues().equals(newOutputValues)) {
          originalOutputValues = new HashSet<ValueSpecification>(graphNode.getOutputValues());
          graphNode.clearOutputValues();
          resolvedOutput = null;
          for (ValueSpecification outputValue : newOutputValues) {
            if ((resolvedOutput == null) && resolved.getValueRequirement().isSatisfiedBy(outputValue)) {
              resolvedOutput = outputValue.compose(resolved.getValueRequirement());
              s_logger.debug("Raw output {} resolves to {}", outputValue, resolvedOutput);
              graphNode.addOutputValue(resolvedOutput);
            } else {
              graphNode.addOutputValue(outputValue);
            }
          }
          if (resolvedOutput != null) {
            final DependencyNode existingNode = _graph.getNodeProducing(resolvedOutput);
            if (existingNode != null) {
              // Node has reduced to an existing node
              s_logger.debug("Reduce {} to existing node {}", graphNode, existingNode);
              graphNode.clearInputs();
              graphNode.clearOutputValues();
              graphNode.addOutputValues(originalOutputValues);
              final Pair<DependencyNode, ValueSpecification> result = Pair.of(existingNode, resolvedOutput);
              if (resolved.isEmpty()) {
                resolved.setLastValid(result);
              }
              return result;
            }
          } else {
            s_logger.debug("Deep backtracking as provisional specification {} no longer in output after late resolution of {}", resolutionNode.getValueSpecification(), resolved.getValueRequirement());
            graphNode.clearInputs();
            graphNode.clearOutputValues();
            graphNode.addOutputValues(originalOutputValues);
            if (resolved.isEmpty() || !resolved.removeDeepest()) {
              throw new UnsatisfiableDependencyGraphException(resolved.getValueRequirement());
            }
            continue;
          }
        }
        // Fetch any additional input requirements now needed as a result of input and output resolution
        try {
          Set<ValueRequirement> additionalRequirements = functionDefinition.getAdditionalRequirements(getCompilationContext(), graphNode.getComputationTarget(), graphNode.getInputValues(), graphNode
              .getOutputValues());
          if (!additionalRequirements.isEmpty()) {
            for (ValueRequirement inputRequirement : additionalRequirements) {
              final ResolutionState inputState = resolveValueRequirement(inputRequirement, graphNode);
              final Pair<DependencyNode, ValueSpecification> inputValue = addTargetRequirement(inputState);
              graphNode.addInputNode(inputValue.getFirst());
              graphNode.addInputValue(inputValue.getSecond());
            }
          }
        } catch (Throwable e) {
          // Catch Throwable in case getAdditionalRequirements fails
          s_logger.debug("Deep backtracking on dependency graph error", e);
          graphNode.clearInputs();
          if (originalOutputValues != null) {
            graphNode.clearOutputValues();
            graphNode.addOutputValues(originalOutputValues);
          }
          if (resolved.isEmpty() || !resolved.removeDeepest()) {
            throw new UnsatisfiableDependencyGraphException(resolved.getValueRequirement(), e);
          }
          continue;
        }
      }
      // Add to the graph
      s_logger.debug("Adding {} to graph", graphNode);
      _graph.addDependencyNode(graphNode);
      final Pair<DependencyNode, ValueSpecification> result = Pair.of(graphNode, resolvedOutput);
      if (resolved.isEmpty()) {
        resolved.setLastValid(result);
      }
      return result;
    } while (true);
  }

  // DON'T CHECK IN WITH =true
  private static final boolean DEBUG_DUMP_DEPENDENCY_GRAPH = false;

  public DependencyGraph getDependencyGraph() {
    if (DEBUG_DUMP_DEPENDENCY_GRAPH) {
      try {
        final PrintStream ps = new PrintStream(new FileOutputStream("/tmp/dependencyGraph.txt"));
        _graph.dumpStructureASCII(ps);
        ps.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return _graph;
  }

}
