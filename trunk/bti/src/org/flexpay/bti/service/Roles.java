package org.flexpay.bti.service;

public abstract class Roles {

	public static final String PREFIX = "ROLE_TC_";

	public static final String SEWER_TYPE_READ = PREFIX + "ORGANIZATION_READ";
	public static final String SEWER_TYPE_CHANGE = PREFIX + "ORGANIZATION_CHANGE";
	public static final String SEWER_TYPE_ADD = PREFIX + "ORGANIZATION_ADD";
	public static final String SEWER_TYPE_DELETE = PREFIX + "ORGANIZATION_DELETE";

}
