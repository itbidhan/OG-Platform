/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.marketdata.manipulator.dsl;

import java.util.HashSet;
import java.util.Set;

import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.PropertyDefinition;

import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.marketdata.manipulator.function.StructureManipulator;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.currency.CurrencyPair;
import com.opengamma.util.ArgumentChecker;
import java.util.Map;
import java.util.NoSuchElementException;
import org.joda.beans.Bean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 *
 */
@BeanDefinition
public final class SpotRateScaling implements StructureManipulator<Double>, ImmutableBean {

  @PropertyDefinition(validate = "notNull")
  private final Double _scalingFactor;

  @PropertyDefinition(validate = "notNull")
  private final Set<CurrencyPair> _currencyPairs;

  @ImmutableConstructor
  /* package */ SpotRateScaling(Number scalingFactor, Set<CurrencyPair> currencyPairs) {
    _currencyPairs = ArgumentChecker.notEmpty(currencyPairs, "currencyPairs");
    _scalingFactor = ArgumentChecker.notNull(scalingFactor, "scalingFactor").doubleValue();
  }

  @Override
  public Double execute(Double spotRate,
                        ValueSpecification valueSpecification,
                        FunctionExecutionContext executionContext) {
    CurrencyPair currencyPair = SimulationUtils.getCurrencyPair(valueSpecification);
    // add 1 to scaling factor o be consistent with curves and allow shits to be specified as 10.pc instead of 1.1
    double scalingFactor = 1.0 + _scalingFactor;
    if (_currencyPairs.contains(currencyPair)) {
      return spotRate * scalingFactor;
    } else if (_currencyPairs.contains(currencyPair.inverse())) {
      double inverseRate = 1 / spotRate;
      double scaledRate = inverseRate * scalingFactor;
      return 1 / scaledRate;
    } else {
      throw new IllegalArgumentException("Currency pair " + currencyPair + " shouldn't match " + _currencyPairs);
    }
  }

  @Override
  public Class<Double> getExpectedType() {
    return Double.class;
  }
  
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SpotRateScaling}.
   * @return the meta-bean, not null
   */
  public static SpotRateScaling.Meta meta() {
    return SpotRateScaling.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(SpotRateScaling.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static SpotRateScaling.Builder builder() {
    return new SpotRateScaling.Builder();
  }

  @Override
  public SpotRateScaling.Meta metaBean() {
    return SpotRateScaling.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the scalingFactor.
   * @return the value of the property, not null
   */
  public Double getScalingFactor() {
    return _scalingFactor;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currencyPairs.
   * @return the value of the property, not null
   */
  public Set<CurrencyPair> getCurrencyPairs() {
    return _currencyPairs;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public SpotRateScaling clone() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SpotRateScaling other = (SpotRateScaling) obj;
      return JodaBeanUtils.equal(getScalingFactor(), other.getScalingFactor()) &&
          JodaBeanUtils.equal(getCurrencyPairs(), other.getCurrencyPairs());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getScalingFactor());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCurrencyPairs());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("SpotRateScaling{");
    buf.append("scalingFactor").append('=').append(getScalingFactor()).append(',').append(' ');
    buf.append("currencyPairs").append('=').append(JodaBeanUtils.toString(getCurrencyPairs()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SpotRateScaling}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code scalingFactor} property.
     */
    private final MetaProperty<Double> _scalingFactor = DirectMetaProperty.ofImmutable(
        this, "scalingFactor", SpotRateScaling.class, Double.class);
    /**
     * The meta-property for the {@code currencyPairs} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Set<CurrencyPair>> _currencyPairs = DirectMetaProperty.ofImmutable(
        this, "currencyPairs", SpotRateScaling.class, (Class) Set.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "scalingFactor",
        "currencyPairs");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -794828874:  // scalingFactor
          return _scalingFactor;
        case 1094810440:  // currencyPairs
          return _currencyPairs;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public SpotRateScaling.Builder builder() {
      return new SpotRateScaling.Builder();
    }

    @Override
    public Class<? extends SpotRateScaling> beanType() {
      return SpotRateScaling.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code scalingFactor} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> scalingFactor() {
      return _scalingFactor;
    }

    /**
     * The meta-property for the {@code currencyPairs} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Set<CurrencyPair>> currencyPairs() {
      return _currencyPairs;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -794828874:  // scalingFactor
          return ((SpotRateScaling) bean).getScalingFactor();
        case 1094810440:  // currencyPairs
          return ((SpotRateScaling) bean).getCurrencyPairs();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code SpotRateScaling}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<SpotRateScaling> {

    private Double _scalingFactor;
    private Set<CurrencyPair> _currencyPairs = new HashSet<CurrencyPair>();

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(SpotRateScaling beanToCopy) {
      this._scalingFactor = beanToCopy.getScalingFactor();
      this._currencyPairs = new HashSet<CurrencyPair>(beanToCopy.getCurrencyPairs());
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -794828874:  // scalingFactor
          return _scalingFactor;
        case 1094810440:  // currencyPairs
          return _currencyPairs;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -794828874:  // scalingFactor
          this._scalingFactor = (Double) newValue;
          break;
        case 1094810440:  // currencyPairs
          this._currencyPairs = (Set<CurrencyPair>) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public SpotRateScaling build() {
      return new SpotRateScaling(
          _scalingFactor,
          _currencyPairs);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code scalingFactor} property in the builder.
     * @param scalingFactor  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder scalingFactor(Double scalingFactor) {
      JodaBeanUtils.notNull(scalingFactor, "scalingFactor");
      this._scalingFactor = scalingFactor;
      return this;
    }

    /**
     * Sets the {@code currencyPairs} property in the builder.
     * @param currencyPairs  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder currencyPairs(Set<CurrencyPair> currencyPairs) {
      JodaBeanUtils.notNull(currencyPairs, "currencyPairs");
      this._currencyPairs = currencyPairs;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("SpotRateScaling.Builder{");
      buf.append("scalingFactor").append('=').append(JodaBeanUtils.toString(_scalingFactor)).append(',').append(' ');
      buf.append("currencyPairs").append('=').append(JodaBeanUtils.toString(_currencyPairs));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
