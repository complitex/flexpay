package org.flexpay.orgs.service;

/**
 * Set of predefined security roles for module
 */
public abstract class Roles {

	public static final String PREFIX = "ROLE_ORGS_";

	public static final String BASIC = PREFIX + "BASIC";

	public static final String ORGANIZATION_READ = PREFIX + "ORGANIZATION_READ";
	public static final String ORGANIZATION_CHANGE = PREFIX + "ORGANIZATION_CHANGE";
	public static final String ORGANIZATION_ADD = PREFIX + "ORGANIZATION_ADD";
	public static final String ORGANIZATION_DELETE = PREFIX + "ORGANIZATION_DELETE";

	public static final String BANK_READ = PREFIX + "BANK_READ";
	public static final String BANK_CHANGE = PREFIX + "BANK_CHANGE";
	public static final String BANK_ADD = PREFIX + "BANK_ADD";
	public static final String BANK_DELETE = PREFIX + "BANK_DELETE";

    public static final String SERVICE_ORGANIZATION_READ = PREFIX + "SERVICE_ORGANIZATION_READ";
    public static final String SERVICE_ORGANIZATION_CHANGE = PREFIX + "SERVICE_ORGANIZATION_CHANGE";
    public static final String SERVICE_ORGANIZATION_ADD = PREFIX + "SERVICE_ORGANIZATION_ADD";
    public static final String SERVICE_ORGANIZATION_DELETE = PREFIX + "SERVICE_ORGANIZATION_DELETE";

	public static final String SERVICE_PROVIDER_READ = PREFIX + "SERVICE_PROVIDER_READ";
	public static final String SERVICE_PROVIDER_CHANGE = PREFIX + "SERVICE_PROVIDER_CHANGE";
	public static final String SERVICE_PROVIDER_ADD = PREFIX + "SERVICE_PROVIDER_ADD";
	public static final String SERVICE_PROVIDER_DELETE = PREFIX + "SERVICE_PROVIDER_DELETE";

	public static final String PAYMENT_COLLECTOR_READ = PREFIX + "PAYMENT_COLLECTOR_READ";
	public static final String PAYMENT_COLLECTOR_CHANGE = PREFIX + "PAYMENT_COLLECTOR_CHANGE";
	public static final String PAYMENT_COLLECTOR_ADD = PREFIX + "PAYMENT_COLLECTOR_ADD";
	public static final String PAYMENT_COLLECTOR_DELETE = PREFIX + "PAYMENT_COLLECTOR_DELETE";

	public static final String PAYMENT_POINT_READ = PREFIX + "PAYMENT_POINT_READ";
	public static final String PAYMENT_POINT_CHANGE = PREFIX + "PAYMENT_POINT_CHANGE";
	public static final String PAYMENT_POINT_ADD = PREFIX + "PAYMENT_POINT_ADD";
	public static final String PAYMENT_POINT_DELETE = PREFIX + "PAYMENT_POINT_DELETE";

	public static final String CASHBOX_READ = PREFIX + "CASHBOX_READ";
	public static final String CASHBOX_CHANGE = PREFIX + "CASHBOX_CHANGE";
	public static final String CASHBOX_ADD = PREFIX + "CASHBOX_ADD";
	public static final String CASHBOX_DELETE = PREFIX + "CASHBOX_DELETE";
	public static final String CASHBOX_READ_ALIAS_PAYMENT_COLLECTORS = PREFIX + "ALIAS_PAYMENT_COLLECTORS";

}
