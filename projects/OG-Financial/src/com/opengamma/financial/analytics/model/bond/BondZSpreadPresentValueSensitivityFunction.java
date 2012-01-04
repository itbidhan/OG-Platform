/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.bond;

import java.util.Collections;
import java.util.Set;

import javax.time.calendar.ZonedDateTime;

import com.google.common.collect.Sets;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.holiday.HolidaySource;
import com.opengamma.core.region.RegionSource;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.function.AbstractFunction;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.OpenGammaCompilationContext;
import com.opengamma.financial.analytics.conversion.BondSecurityConverter;
import com.opengamma.financial.convention.ConventionBundleSource;
import com.opengamma.financial.instrument.bond.BondFixedSecurityDefinition;
import com.opengamma.financial.interestrate.YieldCurveBundle;
import com.opengamma.financial.interestrate.bond.definition.BondFixedSecurity;
import com.opengamma.financial.interestrate.bond.method.BondSecurityDiscountingMethod;
import com.opengamma.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.financial.security.FinancialSecurityUtils;
import com.opengamma.financial.security.bond.BondSecurity;
import com.opengamma.util.money.Currency;

/**
 * 
 */
public abstract class BondZSpreadPresentValueSensitivityFunction extends AbstractFunction.NonCompiledInvoker {
  private static final BondSecurityDiscountingMethod CALCULATOR = BondSecurityDiscountingMethod.getInstance();
  private BondSecurityConverter _visitor;

  @Override
  public void init(final FunctionCompilationContext context) {
    final HolidaySource holidaySource = OpenGammaCompilationContext.getHolidaySource(context);
    final ConventionBundleSource conventionSource = OpenGammaCompilationContext.getConventionBundleSource(context);
    final RegionSource regionSource = OpenGammaCompilationContext.getRegionSource(context);
    _visitor = new BondSecurityConverter(holidaySource, conventionSource, regionSource);
  }

  @Override
  public Set<ComputedValue> execute(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target, final Set<ValueRequirement> desiredValues) {
    final ZonedDateTime date = executionContext.getValuationClock().zonedDateTime();
    final BondSecurity security = (BondSecurity) target.getSecurity();
    if (desiredValues.size() != 1) {
      throw new OpenGammaRuntimeException("This function " + getShortName() + " only provides a single output");
    }
    final ValueRequirement desiredValue = desiredValues.iterator().next();
    final String riskFreeCurveName = desiredValue.getConstraint(BondFunction.PROPERTY_RISK_FREE_CURVE);
    final String curveName = desiredValue.getConstraint(ValuePropertyNames.CURVE);
    final Object riskFreeCurveObject = inputs.getValue(getCurveRequirement(target, riskFreeCurveName));
    if (riskFreeCurveObject == null) {
      throw new OpenGammaRuntimeException("Risk free curve was null");
    }
    final Object curveObject = inputs.getValue(getCurveRequirement(target, curveName));
    if (curveObject == null) {
      throw new OpenGammaRuntimeException("Curve was null");
    }
    final Object cleanPriceObject = inputs.getValue(getCleanPriceRequirement(target, desiredValue));
    if (cleanPriceObject == null) {
      throw new OpenGammaRuntimeException("Could not get clean price requirement");
    }
    final Double cleanPrice = (Double) cleanPriceObject;
    final ValueProperties.Builder properties = getResultProperties(riskFreeCurveName, curveName);
    final ValueSpecification resultSpec = new ValueSpecification(ValueRequirementNames.PRESENT_VALUE_Z_SPREAD_SENSITIVITY, target.toSpecification(), properties.get());
    final BondFixedSecurityDefinition definition = (BondFixedSecurityDefinition) security.accept(_visitor);
    final BondFixedSecurity bond = definition.toDerivative(date, curveName, riskFreeCurveName);
    final YieldAndDiscountCurve curve = (YieldAndDiscountCurve) curveObject;
    final YieldAndDiscountCurve riskFreeCurve = (YieldAndDiscountCurve) riskFreeCurveObject;
    final YieldCurveBundle data = new YieldCurveBundle(new String[] {curveName, riskFreeCurveName}, new YieldAndDiscountCurve[] {curve, riskFreeCurve});
    return Sets.newHashSet(new ComputedValue(resultSpec, CALCULATOR.presentValueZSpreadSensitivityFromCurvesAndClean(bond, data, cleanPrice)));
  }

  @Override
  public ComputationTargetType getTargetType() {
    return ComputationTargetType.SECURITY;
  }

  @Override
  public boolean canApplyTo(final FunctionCompilationContext context, final ComputationTarget target) {
    if (target.getType() != ComputationTargetType.SECURITY) {
      return false;
    }
    return target.getSecurity() instanceof BondSecurity;
  }

  @Override
  public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target) {
    final ValueProperties.Builder properties = getResultProperties();
    return Collections.singleton(new ValueSpecification(ValueRequirementNames.PRESENT_VALUE_Z_SPREAD_SENSITIVITY, target.toSpecification(), properties.get()));
  }

  protected abstract ValueRequirement getCleanPriceRequirement(final ComputationTarget target, final ValueRequirement desiredValue);

  protected abstract String getCalculationMethod();

  protected ValueRequirement getCurveRequirement(final ComputationTarget target, final String curveName) {
    final Currency currency = FinancialSecurityUtils.getCurrency(target.getSecurity());
    final ValueProperties.Builder properties = ValueProperties.with(ValuePropertyNames.CURVE, curveName);
    return new ValueRequirement(ValueRequirementNames.YIELD_CURVE, ComputationTargetType.PRIMITIVE, currency.getUniqueId(), properties.get());
  }

  private ValueProperties.Builder getResultProperties() {
    return createValueProperties()
        .withAny(BondFunction.PROPERTY_RISK_FREE_CURVE)
        .withAny(ValuePropertyNames.CURVE);
  }

  private ValueProperties.Builder getResultProperties(final String riskFreeCurveName, final String curveName) {
    return createValueProperties()
        .with(BondFunction.PROPERTY_RISK_FREE_CURVE, riskFreeCurveName)
        .with(ValuePropertyNames.CURVE, curveName);
  }
}
