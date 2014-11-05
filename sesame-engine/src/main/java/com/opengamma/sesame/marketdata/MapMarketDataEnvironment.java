/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.marketdata;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableMap;

/**
 * A market data environment backed by a map.
 */
@BeanDefinition(builderScope = "private")
public class MapMarketDataEnvironment<T> implements MarketDataEnvironment<T>, ImmutableBean {

  // TODO change the value type to MarketDataBundle when the MarketDataEnvironment changes are merged
  @PropertyDefinition(validate = "notNull", get = "private")
  private final Map<T, MapMarketDataBundle> _scenarioBundles;

  /**
   * Creates a new environment backed by a map.
   *
   * @param scenarioBundles a map of market data bundles keyed by scenario ID. Bundles are stored in the map in the order
   *   the scenarios were added
   */
  MapMarketDataEnvironment(LinkedHashMap<T, MapMarketDataBundle> scenarioBundles) {
    _scenarioBundles = Collections.unmodifiableMap(new LinkedHashMap<>(scenarioBundles));
  }

  @Override
  public Map<T, MapMarketDataBundle> getData() {
    return _scenarioBundles;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MapMarketDataEnvironment}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("rawtypes")
  public static MapMarketDataEnvironment.Meta meta() {
    return MapMarketDataEnvironment.Meta.INSTANCE;
  }

  /**
   * The meta-bean for {@code MapMarketDataEnvironment}.
   * @param <R>  the bean's generic type
   * @param cls  the bean's generic type
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static <R> MapMarketDataEnvironment.Meta<R> metaMapMarketDataEnvironment(Class<R> cls) {
    return MapMarketDataEnvironment.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(MapMarketDataEnvironment.Meta.INSTANCE);
  }

  /**
   * Restricted constructor.
   * @param builder  the builder to copy from, not null
   */
  protected MapMarketDataEnvironment(MapMarketDataEnvironment.Builder<T> builder) {
    JodaBeanUtils.notNull(builder._scenarioBundles, "scenarioBundles");
    this._scenarioBundles = ImmutableMap.copyOf(builder._scenarioBundles);
  }

  @SuppressWarnings("unchecked")
  @Override
  public MapMarketDataEnvironment.Meta<T> metaBean() {
    return MapMarketDataEnvironment.Meta.INSTANCE;
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
   * Gets the scenarioBundles.
   * @return the value of the property, not null
   */
  private Map<T, MapMarketDataBundle> getScenarioBundles() {
    return _scenarioBundles;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MapMarketDataEnvironment<?> other = (MapMarketDataEnvironment<?>) obj;
      return JodaBeanUtils.equal(getScenarioBundles(), other.getScenarioBundles());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getScenarioBundles());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("MapMarketDataEnvironment{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("scenarioBundles").append('=').append(JodaBeanUtils.toString(getScenarioBundles())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MapMarketDataEnvironment}.
   * @param <T>  the type
   */
  public static class Meta<T> extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    @SuppressWarnings("rawtypes")
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code scenarioBundles} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<T, MapMarketDataBundle>> _scenarioBundles = DirectMetaProperty.ofImmutable(
        this, "scenarioBundles", MapMarketDataEnvironment.class, (Class) Map.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "scenarioBundles");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1649679743:  // scenarioBundles
          return _scenarioBundles;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends MapMarketDataEnvironment<T>> builder() {
      return new MapMarketDataEnvironment.Builder<T>();
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Class<? extends MapMarketDataEnvironment<T>> beanType() {
      return (Class) MapMarketDataEnvironment.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code scenarioBundles} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Map<T, MapMarketDataBundle>> scenarioBundles() {
      return _scenarioBundles;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1649679743:  // scenarioBundles
          return ((MapMarketDataEnvironment<?>) bean).getScenarioBundles();
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
   * The bean-builder for {@code MapMarketDataEnvironment}.
   * @param <T>  the type
   */
  private static class Builder<T> extends DirectFieldsBeanBuilder<MapMarketDataEnvironment<T>> {

    private Map<T, MapMarketDataBundle> _scenarioBundles = new HashMap<T, MapMarketDataBundle>();

    /**
     * Restricted constructor.
     */
    protected Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1649679743:  // scenarioBundles
          return _scenarioBundles;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder<T> set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -1649679743:  // scenarioBundles
          this._scenarioBundles = (Map<T, MapMarketDataBundle>) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder<T> set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder<T> setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder<T> setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder<T> setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public MapMarketDataEnvironment<T> build() {
      return new MapMarketDataEnvironment<T>(this);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("MapMarketDataEnvironment.Builder{");
      int len = buf.length();
      toString(buf);
      if (buf.length() > len) {
        buf.setLength(buf.length() - 2);
      }
      buf.append('}');
      return buf.toString();
    }

    protected void toString(StringBuilder buf) {
      buf.append("scenarioBundles").append('=').append(JodaBeanUtils.toString(_scenarioBundles)).append(',').append(' ');
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
