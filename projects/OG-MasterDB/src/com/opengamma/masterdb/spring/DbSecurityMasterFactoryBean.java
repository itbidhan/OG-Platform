/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.spring;

import java.util.Map;

import net.sf.ehcache.CacheManager;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.core.change.JmsChangeManager;
import com.opengamma.masterdb.security.DbSecurityMaster;
import com.opengamma.masterdb.security.EHCachingSecurityMasterDetailProvider;
import com.opengamma.masterdb.security.hibernate.HibernateSecurityMasterDetailProvider;
import com.opengamma.util.db.DbConnector;
import com.opengamma.util.jms.JmsConnector;
import com.opengamma.util.spring.SpringFactoryBean;

/**
 * Spring factory bean to create the database security master.
 */
@BeanDefinition
public class DbSecurityMasterFactoryBean extends SpringFactoryBean<DbSecurityMaster> {

  /**
   * The database connector.
   */
  @PropertyDefinition
  private DbConnector _dbConnector;
  /**
   * The JMS connector, null for no JMS change manager.
   */
  @PropertyDefinition
  private JmsConnector _jmsConnector;
  /**
   * The cache manager for the detail.
   */
  @PropertyDefinition
  private CacheManager _cacheManager;
  /**
   * The config for the scheme used by the {@code UniqueId}.
   */
  @PropertyDefinition
  private String _uniqueIdScheme;
  /**
   * The config for the maximum number of retries when updating.
   */
  @PropertyDefinition
  private Integer _maxRetries;

  /**
   * Creates an instance.
   */
  public DbSecurityMasterFactoryBean() {
    super(DbSecurityMaster.class);
  }

  //-------------------------------------------------------------------------
  @Override
  public DbSecurityMaster createObject() {
    DbSecurityMaster master = new DbSecurityMaster(getDbConnector());
    if (getUniqueIdScheme() != null) {
      master.setUniqueIdScheme(getUniqueIdScheme());
    }
    if (getMaxRetries() != null) {
      master.setMaxRetries(getMaxRetries());
    }
    if (getJmsConnector() != null) {
      master.setChangeManager(new JmsChangeManager(getJmsConnector(), "SecurityMaster"));
    }
    if (getCacheManager() != null) {
      master.setDetailProvider(new EHCachingSecurityMasterDetailProvider(new HibernateSecurityMasterDetailProvider(), getCacheManager()));
    }
    return master;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code DbSecurityMasterFactoryBean}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static DbSecurityMasterFactoryBean.Meta meta() {
    return DbSecurityMasterFactoryBean.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(DbSecurityMasterFactoryBean.Meta.INSTANCE);
  }

  @Override
  public DbSecurityMasterFactoryBean.Meta metaBean() {
    return DbSecurityMasterFactoryBean.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 39794031:  // dbConnector
        return getDbConnector();
      case -1495762275:  // jmsConnector
        return getJmsConnector();
      case -1452875317:  // cacheManager
        return getCacheManager();
      case -1737146991:  // uniqueIdScheme
        return getUniqueIdScheme();
      case -2022653118:  // maxRetries
        return getMaxRetries();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 39794031:  // dbConnector
        setDbConnector((DbConnector) newValue);
        return;
      case -1495762275:  // jmsConnector
        setJmsConnector((JmsConnector) newValue);
        return;
      case -1452875317:  // cacheManager
        setCacheManager((CacheManager) newValue);
        return;
      case -1737146991:  // uniqueIdScheme
        setUniqueIdScheme((String) newValue);
        return;
      case -2022653118:  // maxRetries
        setMaxRetries((Integer) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      DbSecurityMasterFactoryBean other = (DbSecurityMasterFactoryBean) obj;
      return JodaBeanUtils.equal(getDbConnector(), other.getDbConnector()) &&
          JodaBeanUtils.equal(getJmsConnector(), other.getJmsConnector()) &&
          JodaBeanUtils.equal(getCacheManager(), other.getCacheManager()) &&
          JodaBeanUtils.equal(getUniqueIdScheme(), other.getUniqueIdScheme()) &&
          JodaBeanUtils.equal(getMaxRetries(), other.getMaxRetries()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getDbConnector());
    hash += hash * 31 + JodaBeanUtils.hashCode(getJmsConnector());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCacheManager());
    hash += hash * 31 + JodaBeanUtils.hashCode(getUniqueIdScheme());
    hash += hash * 31 + JodaBeanUtils.hashCode(getMaxRetries());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the database connector.
   * @return the value of the property
   */
  public DbConnector getDbConnector() {
    return _dbConnector;
  }

  /**
   * Sets the database connector.
   * @param dbConnector  the new value of the property
   */
  public void setDbConnector(DbConnector dbConnector) {
    this._dbConnector = dbConnector;
  }

  /**
   * Gets the the {@code dbConnector} property.
   * @return the property, not null
   */
  public final Property<DbConnector> dbConnector() {
    return metaBean().dbConnector().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the JMS connector, null for no JMS change manager.
   * @return the value of the property
   */
  public JmsConnector getJmsConnector() {
    return _jmsConnector;
  }

  /**
   * Sets the JMS connector, null for no JMS change manager.
   * @param jmsConnector  the new value of the property
   */
  public void setJmsConnector(JmsConnector jmsConnector) {
    this._jmsConnector = jmsConnector;
  }

  /**
   * Gets the the {@code jmsConnector} property.
   * @return the property, not null
   */
  public final Property<JmsConnector> jmsConnector() {
    return metaBean().jmsConnector().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cache manager for the detail.
   * @return the value of the property
   */
  public CacheManager getCacheManager() {
    return _cacheManager;
  }

  /**
   * Sets the cache manager for the detail.
   * @param cacheManager  the new value of the property
   */
  public void setCacheManager(CacheManager cacheManager) {
    this._cacheManager = cacheManager;
  }

  /**
   * Gets the the {@code cacheManager} property.
   * @return the property, not null
   */
  public final Property<CacheManager> cacheManager() {
    return metaBean().cacheManager().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the config for the scheme used by the {@code UniqueId}.
   * @return the value of the property
   */
  public String getUniqueIdScheme() {
    return _uniqueIdScheme;
  }

  /**
   * Sets the config for the scheme used by the {@code UniqueId}.
   * @param uniqueIdScheme  the new value of the property
   */
  public void setUniqueIdScheme(String uniqueIdScheme) {
    this._uniqueIdScheme = uniqueIdScheme;
  }

  /**
   * Gets the the {@code uniqueIdScheme} property.
   * @return the property, not null
   */
  public final Property<String> uniqueIdScheme() {
    return metaBean().uniqueIdScheme().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the config for the maximum number of retries when updating.
   * @return the value of the property
   */
  public Integer getMaxRetries() {
    return _maxRetries;
  }

  /**
   * Sets the config for the maximum number of retries when updating.
   * @param maxRetries  the new value of the property
   */
  public void setMaxRetries(Integer maxRetries) {
    this._maxRetries = maxRetries;
  }

  /**
   * Gets the the {@code maxRetries} property.
   * @return the property, not null
   */
  public final Property<Integer> maxRetries() {
    return metaBean().maxRetries().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code DbSecurityMasterFactoryBean}.
   */
  public static class Meta extends SpringFactoryBean.Meta<DbSecurityMaster> {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code dbConnector} property.
     */
    private final MetaProperty<DbConnector> _dbConnector = DirectMetaProperty.ofReadWrite(
        this, "dbConnector", DbSecurityMasterFactoryBean.class, DbConnector.class);
    /**
     * The meta-property for the {@code jmsConnector} property.
     */
    private final MetaProperty<JmsConnector> _jmsConnector = DirectMetaProperty.ofReadWrite(
        this, "jmsConnector", DbSecurityMasterFactoryBean.class, JmsConnector.class);
    /**
     * The meta-property for the {@code cacheManager} property.
     */
    private final MetaProperty<CacheManager> _cacheManager = DirectMetaProperty.ofReadWrite(
        this, "cacheManager", DbSecurityMasterFactoryBean.class, CacheManager.class);
    /**
     * The meta-property for the {@code uniqueIdScheme} property.
     */
    private final MetaProperty<String> _uniqueIdScheme = DirectMetaProperty.ofReadWrite(
        this, "uniqueIdScheme", DbSecurityMasterFactoryBean.class, String.class);
    /**
     * The meta-property for the {@code maxRetries} property.
     */
    private final MetaProperty<Integer> _maxRetries = DirectMetaProperty.ofReadWrite(
        this, "maxRetries", DbSecurityMasterFactoryBean.class, Integer.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "dbConnector",
        "jmsConnector",
        "cacheManager",
        "uniqueIdScheme",
        "maxRetries");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 39794031:  // dbConnector
          return _dbConnector;
        case -1495762275:  // jmsConnector
          return _jmsConnector;
        case -1452875317:  // cacheManager
          return _cacheManager;
        case -1737146991:  // uniqueIdScheme
          return _uniqueIdScheme;
        case -2022653118:  // maxRetries
          return _maxRetries;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends DbSecurityMasterFactoryBean> builder() {
      return new DirectBeanBuilder<DbSecurityMasterFactoryBean>(new DbSecurityMasterFactoryBean());
    }

    @Override
    public Class<? extends DbSecurityMasterFactoryBean> beanType() {
      return DbSecurityMasterFactoryBean.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code dbConnector} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<DbConnector> dbConnector() {
      return _dbConnector;
    }

    /**
     * The meta-property for the {@code jmsConnector} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<JmsConnector> jmsConnector() {
      return _jmsConnector;
    }

    /**
     * The meta-property for the {@code cacheManager} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CacheManager> cacheManager() {
      return _cacheManager;
    }

    /**
     * The meta-property for the {@code uniqueIdScheme} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> uniqueIdScheme() {
      return _uniqueIdScheme;
    }

    /**
     * The meta-property for the {@code maxRetries} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Integer> maxRetries() {
      return _maxRetries;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
