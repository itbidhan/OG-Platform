/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.view;

import com.google.common.base.Supplier;
import com.opengamma.core.config.ConfigSource;
import com.opengamma.engine.depgraph.DependencyGraphBuilderFactory;
import com.opengamma.engine.function.CompiledFunctionService;
import com.opengamma.engine.function.resolver.FunctionResolver;
import com.opengamma.engine.marketdata.InMemoryLKVMarketDataProvider;
import com.opengamma.engine.marketdata.MarketDataInjector;
import com.opengamma.engine.marketdata.OverrideOperationCompiler;
import com.opengamma.engine.marketdata.availability.MarketDataAvailabilityProvider;
import com.opengamma.engine.marketdata.resolver.MarketDataProviderResolver;
import com.opengamma.engine.marketdata.resolver.MarketDataProviderResolverWithOverride;
import com.opengamma.engine.view.cache.ViewComputationCacheSource;
import com.opengamma.engine.view.calc.DependencyGraphExecutorFactory;
import com.opengamma.engine.view.calc.EngineResourceManagerInternal;
import com.opengamma.engine.view.calc.SingleComputationCycle;
import com.opengamma.engine.view.calc.ViewComputationJobFactory;
import com.opengamma.engine.view.calc.stats.GraphExecutorStatisticsGathererProvider;
import com.opengamma.engine.view.calcnode.JobDispatcher;
import com.opengamma.engine.view.compilation.ViewCompilationServices;
import com.opengamma.engine.view.permission.ViewPermissionProvider;
import com.opengamma.id.UniqueId;
import com.opengamma.util.ArgumentChecker;

/**
 * Encapsulates the context required by a view process.
 */
public class ViewProcessContext {

  private final UniqueId _processId;
  private final ConfigSource _configSource;
  private final ViewPermissionProvider _viewPermissionProvider;
  private final CompiledFunctionService _functionCompilationService;
  private final FunctionResolver _functionResolver;
  private final ViewComputationCacheSource _computationCacheSource;
  private final JobDispatcher _computationJobDispatcher;
  private final ViewComputationJobFactory _viewComputationJobFactory;
  private final DependencyGraphBuilderFactory _dependencyGraphBuilderFactory;
  private final DependencyGraphExecutorFactory<?> _dependencyGraphExecutorFactory;
  private final GraphExecutorStatisticsGathererProvider _graphExecutorStatisticsGathererProvider;

  // TODO: Need to rethink this for distribution if the workers for the process are remote so they receive injections from the original process. The
  // workers only need read access via the _marketDataProviderResolver. Only the original process requires the injector.
  private final MarketDataInjector _liveDataOverrideInjector;

  private final MarketDataProviderResolver _marketDataProviderResolver;
  private final OverrideOperationCompiler _overrideOperationCompiler;

  // TODO: Need to rethink this for distribution if the workers for the process are remote. Remote workers will create their own cycles and references
  // to them. The viewProcessor will need to use the identifier scheme to direct queries to the correct nodes.
  private final EngineResourceManagerInternal<SingleComputationCycle> _cycleManager;

  private final Supplier<UniqueId> _cycleIdentifiers;

  // TODO: Need to rethink this for distribution if the workers for the process are remote. Workers only need to read the log settings. The original
  // process is the one that updates them.
  private final ExecutionLogModeSource _executionLogModeSource = new ExecutionLogModeSource();

  public ViewProcessContext(
      final UniqueId processId,
      final ConfigSource configSource,
      final ViewPermissionProvider viewPermissionProvider,
      final MarketDataProviderResolver marketDataProviderResolver,
      final CompiledFunctionService functionCompilationService,
      final FunctionResolver functionResolver,
      final ViewComputationCacheSource computationCacheSource,
      final JobDispatcher computationJobDispatcher,
      final ViewComputationJobFactory viewComputationJobFactory,
      final DependencyGraphBuilderFactory dependencyGraphBuilderFactory,
      final DependencyGraphExecutorFactory<?> dependencyGraphExecutorFactory,
      final GraphExecutorStatisticsGathererProvider graphExecutorStatisticsProvider,
      final OverrideOperationCompiler overrideOperationCompiler,
      final EngineResourceManagerInternal<SingleComputationCycle> cycleManager,
      final Supplier<UniqueId> cycleIdentifiers) {
    ArgumentChecker.notNull(processId, "processId");
    ArgumentChecker.notNull(configSource, "configSource");
    ArgumentChecker.notNull(viewPermissionProvider, "viewPermissionProvider");
    ArgumentChecker.notNull(marketDataProviderResolver, "marketDataSnapshotProviderResolver");
    ArgumentChecker.notNull(functionCompilationService, "functionCompilationService");
    ArgumentChecker.notNull(functionResolver, "functionResolver");
    ArgumentChecker.notNull(computationCacheSource, "computationCacheSource");
    ArgumentChecker.notNull(computationJobDispatcher, "computationJobDispatcher");
    ArgumentChecker.notNull(viewComputationJobFactory, "viewComputationJobFactory");
    ArgumentChecker.notNull(dependencyGraphBuilderFactory, "dependencyGraphBuilderFactory");
    ArgumentChecker.notNull(dependencyGraphExecutorFactory, "dependencyGraphExecutorFactory");
    ArgumentChecker.notNull(graphExecutorStatisticsProvider, "graphExecutorStatisticsProvider");
    ArgumentChecker.notNull(overrideOperationCompiler, "overrideOperationCompiler");
    ArgumentChecker.notNull(cycleManager, "cycleManager");
    ArgumentChecker.notNull(cycleIdentifiers, "cycleIdentifiers");
    _processId = processId;
    _configSource = configSource;
    _viewPermissionProvider = viewPermissionProvider;
    final InMemoryLKVMarketDataProvider liveDataOverrideInjector = new InMemoryLKVMarketDataProvider();
    _liveDataOverrideInjector = liveDataOverrideInjector;
    _marketDataProviderResolver = new MarketDataProviderResolverWithOverride(marketDataProviderResolver, liveDataOverrideInjector);
    _functionCompilationService = functionCompilationService;
    _functionResolver = functionResolver;
    _computationCacheSource = computationCacheSource;
    _computationJobDispatcher = computationJobDispatcher;
    _viewComputationJobFactory = viewComputationJobFactory;
    _dependencyGraphBuilderFactory = dependencyGraphBuilderFactory;
    _dependencyGraphExecutorFactory = dependencyGraphExecutorFactory;
    _graphExecutorStatisticsGathererProvider = graphExecutorStatisticsProvider;
    _overrideOperationCompiler = overrideOperationCompiler;
    _cycleManager = cycleManager;
    _cycleIdentifiers = cycleIdentifiers;
  }

  public UniqueId getProcessId() {
    return _processId;
  }

  /**
   * Gets the config source
   * 
   * @return the config source, not null
   */
  public ConfigSource getConfigSource() {
    return _configSource;
  }

  /**
   * Gets the view permission provider
   * 
   * @return the view permission provider, not null
   */
  public ViewPermissionProvider getViewPermissionProvider() {
    return _viewPermissionProvider;
  }

  /**
   * Gets the market data provider resolver.
   * 
   * @return the market data provider resolver, not null
   */
  public MarketDataProviderResolver getMarketDataProviderResolver() {
    return _marketDataProviderResolver;
  }

  /**
   * Gets the live data override injector.
   * 
   * @return the live data override injector, not null
   */
  public MarketDataInjector getLiveDataOverrideInjector() {
    return _liveDataOverrideInjector;
  }

  public CompiledFunctionService getFunctionCompilationService() {
    return _functionCompilationService;
  }

  /**
   * Gets the function resolver.
   * 
   * @return the function resolver, not null
   */
  public FunctionResolver getFunctionResolver() {
    return _functionResolver;
  }

  /**
   * Gets the dependency graph builder factory.
   * 
   * @return the dependency graph builder, not null
   */
  public DependencyGraphBuilderFactory getDependencyGraphBuilderFactory() {
    return _dependencyGraphBuilderFactory;
  }

  /**
   * Gets the computation cache source.
   * 
   * @return the computation cache source, not null
   */
  public ViewComputationCacheSource getComputationCacheSource() {
    return _computationCacheSource;
  }

  /**
   * Gets the computation job dispatcher.
   * 
   * @return the computation job dispatcher, not null
   */
  public JobDispatcher getComputationJobDispatcher() {
    return _computationJobDispatcher;
  }

  public ViewComputationJobFactory getViewComputationJobFactory() {
    return _viewComputationJobFactory;
  }

  /**
   * Gets the dependency graph executor factory.
   * 
   * @return the dependency graph executor factory, not null
   */
  public DependencyGraphExecutorFactory<?> getDependencyGraphExecutorFactory() {
    return _dependencyGraphExecutorFactory;
  }

  public GraphExecutorStatisticsGathererProvider getGraphExecutorStatisticsGathererProvider() {
    return _graphExecutorStatisticsGathererProvider;
  }

  public OverrideOperationCompiler getOverrideOperationCompiler() {
    return _overrideOperationCompiler;
  }

  public EngineResourceManagerInternal<SingleComputationCycle> getCycleManager() {
    return _cycleManager;
  }

  public Supplier<UniqueId> getCycleIdentifiers() {
    return _cycleIdentifiers;
  }

  public ExecutionLogModeSource getExecutionLogModeSource() {
    return _executionLogModeSource;
  }

  // -------------------------------------------------------------------------
  /**
   * Uses this context to form a {@code ViewCompliationServices} instance.
   * 
   * @param marketDataAvailabilityProvider the availability provider corresponding to the desired source of market data, not null
   * @return the services, not null
   */
  public ViewCompilationServices asCompilationServices(final MarketDataAvailabilityProvider marketDataAvailabilityProvider) {
    return new ViewCompilationServices(marketDataAvailabilityProvider, getFunctionResolver(), getFunctionCompilationService().getFunctionCompilationContext(), getFunctionCompilationService()
        .getExecutorService(), getDependencyGraphBuilderFactory());
  }

}
