/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.user;

import com.google.common.collect.Lists;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.AbstractSearchResult;
import com.opengamma.util.PublicSPI;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Result from searching for roles.
 * <p/>
 * The returned documents will match the search criteria.
 * See {@link com.opengamma.master.user.RoleSearchRequest} for more details.
 */
@PublicSPI
@BeanDefinition
public class RoleSearchResult extends AbstractSearchResult<RoleDocument> {

  /**
   * Creates an instance.
   */
  public RoleSearchResult() {
  }

  /**
   * Creates an instance from a collection of documents.
   *
   * @param coll the collection of documents to add, not null
   */
  public RoleSearchResult(Collection<RoleDocument> coll) {
    super(coll);
  }

  /**
   * Creates an instance specifying the version-correction searched for.
   *
   * @param versionCorrection the version-correction of the data, not null
   */
  public RoleSearchResult(VersionCorrection versionCorrection) {
    setVersionCorrection(versionCorrection);
  }

  //-------------------------------------------------------------------------

  /**
   * Gets the returned roles from within the documents.
   *
   * @return the roles, not null
   */
  public List<ManageableOGRole> getRoles() {
    List<ManageableOGRole> result = Lists.newArrayList();
    if (getDocuments() != null) {
      for (RoleDocument doc : getDocuments()) {
        result.add(doc.getRole());
      }
    }
    return result;
  }

  /**
   * Gets the first role, or null if no documents.
   *
   * @return the first role, null if none
   */
  public ManageableOGRole getFirstRole() {
    return getDocuments().size() > 0 ? getDocuments().get(0).getRole() : null;
  }

  /**
   * Gets the single result expected from a query.
   * <p/>
   * This throws an exception if more than 1 result is actually available.
   * Thus, this method implies an assumption about uniqueness of the queried role.
   *
   * @return the matching role, not null
   * @throws IllegalStateException if no role was found
   */
  public ManageableOGRole getSingleRole() {
    if (getDocuments().size() != 1) {
      throw new OpenGammaRuntimeException("Expecting zero or single resulting match, and was " + getDocuments().size());
    } else {
      return getDocuments().get(0).getRole();
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code RoleSearchResult}.
   * @return the meta-bean, not null
   */
  public static RoleSearchResult.Meta meta() {
    return RoleSearchResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(RoleSearchResult.Meta.INSTANCE);
  }

  @Override
  public RoleSearchResult.Meta metaBean() {
    return RoleSearchResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  @Override
  public RoleSearchResult clone() {
    return (RoleSearchResult) super.clone();
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
    buf.append("RoleSearchResult{");
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
   * The meta-bean for {@code RoleSearchResult}.
   */
  public static class Meta extends AbstractSearchResult.Meta<RoleDocument> {
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
    public BeanBuilder<? extends RoleSearchResult> builder() {
      return new DirectBeanBuilder<RoleSearchResult>(new RoleSearchResult());
    }

    @Override
    public Class<? extends RoleSearchResult> beanType() {
      return RoleSearchResult.class;
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
