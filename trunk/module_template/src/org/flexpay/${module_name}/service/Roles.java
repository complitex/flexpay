package org.flexpay.${module_name}.service;

/**
 * Set of predefined security roles for module
 */
public abstract class Roles {

	public static final String PREFIX = "ROLE_" + "${module_name}".toUpperCase() + "_";

	public static final String BASIC = PREFIX + "BASIC";

}
