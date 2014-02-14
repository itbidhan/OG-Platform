/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.marketdatasnapshot;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeSerializer;
import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * A key used to identify a yield curve.
 * <p>
 * This class is immutable and thread-safe.
 */
@BeanDefinition
public final class CurveKey implements ImmutableBean, StructuredMarketDataKey, Comparable<CurveKey>, Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The curve name.
   */
  @PropertyDefinition
  private final String _name;

  /**
   * Creates an instance with a name.
   *
   * @param name  the name
   */
  @ImmutableConstructor
  private  CurveKey(String name) {
    super();
    _name = name;
  }
  
  /**
   * Creates an instance with a name.
   *
   * @param name  the name
   * @return the curve key, not null
   */
  public static CurveKey of(String name) {
    return new CurveKey(name);
  }

  //-------------------------------------------------------------------------
  /**
   * Compares this key to another, by currency then name.
   *
   * @param other  the other key, not null
   * @return the comparison value
   */
  @Override
  public int compareTo(CurveKey other) {
    return _name.compareTo(other.getName());
  }

  @Override
  public <T> T accept(final Visitor<T> visitor) {
    return visitor.visitCurveKey(this);
  }

  public MutableFudgeMsg toFudgeMsg(final FudgeSerializer serializer) {
    final MutableFudgeMsg msg = serializer.newMessage();
    msg.add("name", _name);
    return msg;
  }

  public static CurveKey fromFudgeMsg(final FudgeDeserializer deserializer, final FudgeMsg msg) {
    return new CurveKey(msg.getString("name"));
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CurveKey}.
   * @return the meta-bean, not null
   */
  public static CurveKey.Meta meta() {
    return CurveKey.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(CurveKey.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static CurveKey.Builder builder() {
    return new CurveKey.Builder();
  }

  @Override
  public CurveKey.Meta metaBean() {
    return CurveKey.Meta.INSTANCE;
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
   * Gets the curve name.
   * @return the value of the property
   */
  public String getName() {
    return _name;
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
  public CurveKey clone() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CurveKey other = (CurveKey) obj;
      return JodaBeanUtils.equal(getName(), other.getName());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getName());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("CurveKey{");
    buf.append("name").append('=').append(JodaBeanUtils.toString(getName()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CurveKey}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofImmutable(
        this, "name", CurveKey.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "name");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return _name;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public CurveKey.Builder builder() {
      return new CurveKey.Builder();
    }

    @Override
    public Class<? extends CurveKey> beanType() {
      return CurveKey.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> name() {
      return _name;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return ((CurveKey) bean).getName();
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
   * The bean-builder for {@code CurveKey}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<CurveKey> {

    private String _name;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(CurveKey beanToCopy) {
      this._name = beanToCopy.getName();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return _name;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          this._name = (String) newValue;
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
    public CurveKey build() {
      return new CurveKey(
          _name);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code name} property in the builder.
     * @param name  the new value
     * @return this, for chaining, not null
     */
    public Builder name(String name) {
      this._name = name;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("CurveKey.Builder{");
      buf.append("name").append('=').append(JodaBeanUtils.toString(_name));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
