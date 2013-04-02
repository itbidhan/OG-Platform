/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial;

import java.util.List;

import com.opengamma.engine.function.config.AbstractRepositoryConfigurationBean;
import com.opengamma.engine.function.config.CombiningRepositoryConfigurationSource;
import com.opengamma.engine.function.config.FunctionConfiguration;
import com.opengamma.engine.function.config.FunctionConfigurationSource;
import com.opengamma.financial.aggregation.AggregationFunctions;
import com.opengamma.financial.analytics.AnalyticsFunctions;
import com.opengamma.financial.currency.CurrencyFunctions;
import com.opengamma.financial.property.PropertyFunctions;
import com.opengamma.financial.target.TargetFunctions;
import com.opengamma.financial.value.ValueFunctions;
import com.opengamma.financial.view.ViewFunctions;

/**
 * Function repository configuration source for the functions contained in this package and sub-packages.
 */
public class FinancialFunctions extends AbstractRepositoryConfigurationBean {

  /**
   * Default instance of a repository configuration source exposing the functions from this package and its sub-packages.
   *
   * @return the configuration source exposing functions from this package and its sub-packages
   */
  public static FunctionConfigurationSource instance() {
    return new FinancialFunctions().getObjectCreating();
  }

  @Override
  protected void addAllConfigurations(final List<FunctionConfiguration> functions) {
    // Nothing in this package, just the sub-packages
  }

  protected FunctionConfigurationSource aggregationFunctionConfiguration() {
    return AggregationFunctions.instance();
  }

  protected FunctionConfigurationSource analyticsFunctionConfiguration() {
    return AnalyticsFunctions.instance();
  }

  protected FunctionConfigurationSource currencyFunctionConfiguration() {
    return CurrencyFunctions.instance();
  }

  protected FunctionConfigurationSource propertyFunctionConfiguration() {
    return PropertyFunctions.instance();
  }

  protected FunctionConfigurationSource valueFunctionConfiguration() {
    return ValueFunctions.instance();
  }

  protected FunctionConfigurationSource targetFunctionConfiguration() {
    return TargetFunctions.instance();
  }

  protected FunctionConfigurationSource viewFunctionConfiguration() {
    return ViewFunctions.instance();
  }

  @Override
  protected FunctionConfigurationSource createObject() {
    return CombiningRepositoryConfigurationSource.of(super.createObject(), aggregationFunctionConfiguration(), analyticsFunctionConfiguration(), currencyFunctionConfiguration(),
        propertyFunctionConfiguration(), valueFunctionConfiguration(), targetFunctionConfiguration(), viewFunctionConfiguration());
  }

}
