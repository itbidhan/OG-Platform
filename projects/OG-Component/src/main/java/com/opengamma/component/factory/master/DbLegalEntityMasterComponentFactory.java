/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.master;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.master.legalentity.impl.DataTrackingLegalEntityMaster;
import com.opengamma.masterdb.legalentity.DbLegalEntityBeanMaster;
import com.opengamma.util.rest.AbstractDataResource;
import com.opengamma.master.legalentity.LegalEntityMaster;
import com.opengamma.master.legalentity.impl.DataLegalEntityMasterResource;
import com.opengamma.master.legalentity.impl.RemoteLegalEntityMaster;

/**
 * Component factory for the database legal entity master.
 */
@BeanDefinition
public class DbLegalEntityMasterComponentFactory extends AbstractDocumentDbMasterComponentFactory<LegalEntityMaster, DbLegalEntityBeanMaster> {


  
  public DbLegalEntityMasterComponentFactory() {
    super("len", LegalEntityMaster.class, RemoteLegalEntityMaster.class);
  }
  

  @Override
  protected DbLegalEntityBeanMaster createDbDocumentMaster() {
    return new DbLegalEntityBeanMaster(getDbConnector());
  }
  
  @Override
  protected AbstractDataResource createPublishedResource(DbLegalEntityBeanMaster dbMaster, LegalEntityMaster postProcessedMaster) {
    return new DataLegalEntityMasterResource((LegalEntityMaster) postProcessedMaster);
  }
      

  @Override
  protected LegalEntityMaster wrapMasterWithTrackingInterface(LegalEntityMaster postProcessedMaster) {
    return new DataTrackingLegalEntityMaster(postProcessedMaster);
  }


  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code DbLegalEntityMasterComponentFactory}.
   * @return the meta-bean, not null
   */
  public static DbLegalEntityMasterComponentFactory.Meta meta() {
    return DbLegalEntityMasterComponentFactory.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(DbLegalEntityMasterComponentFactory.Meta.INSTANCE);
  }

  @Override
  public DbLegalEntityMasterComponentFactory.Meta metaBean() {
    return DbLegalEntityMasterComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public DbLegalEntityMasterComponentFactory clone() {
    return (DbLegalEntityMasterComponentFactory) super.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(32);
    buf.append("DbLegalEntityMasterComponentFactory{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  @Override
  protected void toString(StringBuilder buf) {
    super.toString(buf);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code DbLegalEntityMasterComponentFactory}.
   */
  public static class Meta extends AbstractDocumentDbMasterComponentFactory.Meta<LegalEntityMaster, DbLegalEntityBeanMaster> {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap());

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends DbLegalEntityMasterComponentFactory> builder() {
      return new DirectBeanBuilder<DbLegalEntityMasterComponentFactory>(new DbLegalEntityMasterComponentFactory());
    }

    @Override
    public Class<? extends DbLegalEntityMasterComponentFactory> beanType() {
      return DbLegalEntityMasterComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
