package org.flexpay.admin.service;

/**
 * Set of predefined security roles for module
 */
public abstract class Roles {

	public static final String PREFIX = "ROLE_ADMIN_";

	// menu roles
	public static final String MENU_ADMIN_PREFIX = "ROLE_MENU_ADMIN";

	// level 1
	public static final String ROLE_MENU_ADMIN = MENU_ADMIN_PREFIX;

	// level 1
	public static final String ROLE_MENU_ADMIN_USERS = MENU_ADMIN_PREFIX + "_USERS";
}
