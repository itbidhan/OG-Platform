/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.util.component;

/**
 * A principal component of the OpenGamma system.
 */
public interface Component {

  /**
   * Gets the component type representing the available functionality.
   * 
   * @return the type defining the functionality, not null
   */
  ComponentType getType();

  /**
   * Gets the classifier of the component.
   * <p>
   * This acts as a key to disambiguate multiple options for the same component type.
   * 
   * @return the classifier, not null
   */
  String getClassifier();

  /**
   * Gets the instance that provides the functionality.
   * 
   * @return the underlying instance providing the functionality, not null
   */
  Object getProvider();

  /**
   * Gets the instance that provides the functionality for a specified level of access.
   * <p>
   * The access defines how the component will be connected to.
   * The access level of LOCAL must return an instance of the component type.
   * 
   * @param access  the access that is required
   * @return the underlying instance providing the service, null if not available
   */
  Object getRemoteProvider(ComponentAccess access);

}
