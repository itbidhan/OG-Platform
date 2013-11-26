/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.proxy;

import static com.opengamma.sesame.FailureStatus.ERROR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;
import org.threeten.bp.Period;

import com.google.common.collect.ImmutableMap;
import com.opengamma.financial.currency.CurrencyPair;
import com.opengamma.sesame.CurrencyPairsFunction;
import com.opengamma.sesame.FunctionResult;
import com.opengamma.sesame.ResultStatus;
import com.opengamma.sesame.marketdata.MarketDataProviderFunction;
import com.opengamma.sesame.marketdata.MarketDataRequirement;
import com.opengamma.sesame.marketdata.MarketDataSeries;
import com.opengamma.sesame.marketdata.MarketDataValues;
import com.opengamma.util.money.Currency;
import com.opengamma.util.money.UnorderedCurrencyPair;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.time.LocalDateRange;

@Test(groups = TestGroup.UNIT)
public class ProxyGeneratorTest {

  private ProxyGenerator _proxyGenerator = new ProxyGenerator();

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void delegateMustNotBeNull() {
    _proxyGenerator.generate(null, Map.class);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void interfaceMustNotBeNull() {
    _proxyGenerator.generate(ImmutableMap.of("this", "that"), null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void cannotGenerateProxyForClasses() {
    _proxyGenerator.generate("S1", String.class);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void canGenerateProxyForInterface() {
    Map<String, String> proxy = _proxyGenerator.generate(ImmutableMap.of("this", "that"), Map.class);
    assertThat(proxy instanceof Proxy, is(true));
    assertThat(proxy.get("this"), is("that"));
  }

  @Test
  public void methodReturningFunctionResultWillHaveExceptionsIntercepted() {

    final String message = "Oops, thrown my toys out";
    CurrencyPairsFunction cpf = new CurrencyPairsFunction() {
      @Override
      public FunctionResult<CurrencyPair> getCurrencyPair(Currency currency1, Currency currency2) {
        throw new RuntimeException(message);
      }

      @Override
      public FunctionResult<CurrencyPair> getCurrencyPair(UnorderedCurrencyPair pair) {
        return null;
      }
    };

    try {
      cpf.getCurrencyPair(Currency.USD, Currency.GBP);
      fail("Should have thrown an exception");
    } catch (Exception e) {
      assertThat(e.getMessage(), containsString(message));
    }

    CurrencyPairsFunction proxy = _proxyGenerator.generate(cpf, CurrencyPairsFunction.class);
    FunctionResult<CurrencyPair> result = proxy.getCurrencyPair(Currency.USD, Currency.GBP);
    assertThat(result.getStatus(), is((ResultStatus) ERROR));
    assertThat(result.getFailureMessage(), containsString(message));
  }

  @Test
  public void methodReturningMarketDataFunctionResultWillHaveExceptionsIntercepted() {

    final String message = "Oops, thrown my toys out";
    MarketDataProviderFunction mdpf = new MarketDataProviderFunction() {
      @Override
      public FunctionResult<MarketDataValues> requestData(MarketDataRequirement requirement) {
        throw new RuntimeException(message);
      }

      @Override
      public FunctionResult<MarketDataValues> requestData(Set<MarketDataRequirement> requirements) {
        throw new RuntimeException(message);
      }

      @Override
      public FunctionResult<MarketDataSeries> requestData(MarketDataRequirement requirement, LocalDateRange dateRange) {
        throw new RuntimeException(message);
      }

      @Override
      public FunctionResult<MarketDataSeries> requestData(Set<MarketDataRequirement> requirements, LocalDateRange dateRange) {
        throw new RuntimeException(message);
      }

      @Override
      public FunctionResult<MarketDataSeries> requestData(MarketDataRequirement requirement, Period seriesPeriod) {
        throw new RuntimeException(message);
      }

      @Override
      public FunctionResult<MarketDataSeries> requestData(Set<MarketDataRequirement> requirements,
                                                          Period seriesPeriod) {
        throw new RuntimeException(message);
      }
    };

    try {
      mdpf.requestData((MarketDataRequirement) null);
      fail("Should have thrown an exception");
    } catch (Exception e) {
      assertThat(e.getMessage(), containsString(message));
    }

    MarketDataProviderFunction proxy = _proxyGenerator.generate(mdpf, MarketDataProviderFunction.class);
    FunctionResult<MarketDataValues> result = proxy.requestData((MarketDataRequirement) null);
    assertThat(result.getStatus(), is((ResultStatus) ERROR));
    assertThat(result.getFailureMessage(), containsString(message));
  }

  @Test
  public void methodNotReturningFunctionResultThrowsExceptions() {

    final String message = "Oops, thrown my toys out";
    ActionListener al= new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        throw new RuntimeException(message);
      }
    };

    try {
      al.actionPerformed(null);
      fail("Should have thrown an exception");
    } catch (Exception e) {
      assertThat(e.getMessage(), containsString(message));
    }

    try {
      ActionListener proxy = _proxyGenerator.generate(al, ActionListener.class);
      proxy.actionPerformed(null);
      fail("Should have thrown an exception");
    } catch (Exception e) {
      assertThat(e.getMessage(), containsString(message));
    }
  }
}
