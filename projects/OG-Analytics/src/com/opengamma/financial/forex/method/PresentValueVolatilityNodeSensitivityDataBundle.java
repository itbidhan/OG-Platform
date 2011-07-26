/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.forex.method;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;

import com.opengamma.math.matrix.DoubleMatrix1D;
import com.opengamma.math.matrix.DoubleMatrix2D;
import com.opengamma.util.money.Currency;
import com.opengamma.util.tuple.ObjectsPair;
import com.opengamma.util.tuple.Pair;

/**
 * Class describing the present value sensitivity to a Forex currency pair volatility grid.
 */
public class PresentValueVolatilityNodeSensitivityDataBundle {

  /**
   * The currency pair.
   */
  private final Pair<Currency, Currency> _currencyPair;
  /**
   * The volatility sensitivity as a matrix with same dimension as the input. The sensitivity value is in second/domestic currency.
   */
  private final DoubleMatrix2D _vega;
  private final DoubleMatrix1D _expiries;
  private final DoubleMatrix1D _strikes;

  /**
   * Constructor with empty sensitivities for a given currency pair.
   * @param ccy1 First currency, not null
   * @param ccy2 Second currency, not null
   * @param numberExpiry The number of expiries, not negative
   * @param numberStrike The number of strikes, not negative
   */
  public PresentValueVolatilityNodeSensitivityDataBundle(final Currency ccy1, final Currency ccy2, final int numberExpiry, final int numberStrike) {
    Validate.notNull(ccy1, "currency 1");
    Validate.notNull(ccy2, "currency 2");
    Validate.isTrue(numberExpiry >= 0);
    Validate.isTrue(numberStrike >= 0);
    _currencyPair = ObjectsPair.of(ccy1, ccy2);
    _expiries = new DoubleMatrix1D(new double[numberExpiry]);
    _strikes = new DoubleMatrix1D(new double[numberStrike]);
    _vega = new DoubleMatrix2D(numberExpiry, numberStrike);
  }

  /**
   * Constructor with initial sensitivities for a given currency pair.
   * @param ccy1 First currency, not null
   * @param ccy2 Second currency, not null
   * @param expiries The expiries for the vega matrix, not null
   * @param strikes The strikes for the vega matrix, not null
   * @param vega The initial sensitivity, not null
   */
  public PresentValueVolatilityNodeSensitivityDataBundle(final Currency ccy1, final Currency ccy2, final DoubleMatrix1D expiries, final DoubleMatrix1D strikes, final DoubleMatrix2D vega) {
    Validate.notNull(ccy1, "currency 1");
    Validate.notNull(ccy2, "currency 2");
    Validate.notNull(expiries, "expiries");
    Validate.notNull(strikes, "strikes");
    Validate.notNull(vega, "Matrix");
    Validate.isTrue(vega.getNumberOfRows() == expiries.getNumberOfElements(), "Number of rows did not match number of expiries");
    Validate.isTrue(vega.getNumberOfColumns() == strikes.getNumberOfElements(), "Number of columns did not match number of strikes");
    _currencyPair = ObjectsPair.of(ccy1, ccy2);
    _expiries = expiries;
    _strikes = strikes;
    _vega = vega;
  }

  /**
   * Gets the currency pair.
   * @return The currency pair.
   */
  public Pair<Currency, Currency> getCurrencyPair() {
    return _currencyPair;
  }

  /**
   * Gets the volatility sensitivity (vega) map.
   * @return The sensitivity.
   */
  public DoubleMatrix2D getVega() {
    return _vega;
  }

  public DoubleMatrix1D getExpiries() {
    return _expiries;
  }

  public DoubleMatrix1D getStrikes() {
    return _strikes;
  }

  //TODO Add possibility to add a sensitivity?

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + _currencyPair.hashCode();
    result = prime * result + _expiries.hashCode();
    result = prime * result + _strikes.hashCode();
    result = prime * result + _vega.hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PresentValueVolatilityNodeSensitivityDataBundle other = (PresentValueVolatilityNodeSensitivityDataBundle) obj;
    if (!ObjectUtils.equals(_currencyPair, other._currencyPair)) {
      return false;
    }
    if (!ObjectUtils.equals(_vega, other._vega)) {
      return false;
    }
    if (!ObjectUtils.equals(_expiries, other._expiries)) {
      return false;
    }
    if (!ObjectUtils.equals(_strikes, other._strikes)) {
      return false;
    }
    return true;
  }

}
