package org.flexpay.payments.service;

/**
 * Set of predefined security roles for module
 */
public abstract class Roles {

	public static final String PREFIX = "ROLE_PAYMENTS_";

	public static final String BASIC = PREFIX + "BASIC";

	public static final String DEVELOPER = PREFIX + "DEVELOPER";

	public static final String DOCUMENT_READ = PREFIX + "DOCUMENT_READ";
	public static final String DOCUMENT_CHANGE = PREFIX + "DOCUMENT_CHANGE";
	public static final String DOCUMENT_ADD = PREFIX + "DOCUMENT_ADD";
	public static final String DOCUMENT_DELETE = PREFIX + "DOCUMENT_DELETE";

	public static final String DOCUMENT_TYPE_READ = PREFIX + "DOCUMENT_TYPE_READ";
	public static final String DOCUMENT_TYPE_CHANGE = PREFIX + "DOCUMENT_TYPE_CHANGE";
	public static final String DOCUMENT_TYPE_ADD = PREFIX + "DOCUMENT_TYPE_ADD";
	public static final String DOCUMENT_TYPE_DELETE = PREFIX + "DOCUMENT_TYPE_DELETE";

	public static final String OPERATION_READ = PREFIX + "OPERATION_READ";
	public static final String OPERATION_CHANGE = PREFIX + "OPERATION_CHANGE";
	public static final String OPERATION_ADD = PREFIX + "OPERATION_ADD";
	public static final String OPERATION_DELETE = PREFIX + "OPERATION_DELETE";

	public static final String EIRC_SUBJECT_READ = PREFIX + "EIRC_SUBJECT_READ";
	public static final String EIRC_SUBJECT_CHANGE = PREFIX + "EIRC_SUBJECT_CHANGE";
	public static final String EIRC_SUBJECT_ADD = PREFIX + "EIRC_SUBJECT_ADD";
	public static final String EIRC_SUBJECT_DELETE = PREFIX + "EIRC_SUBJECT_DELETE";

	public static final String SERVICE_READ = PREFIX + "SERVICE_READ";
	public static final String SERVICE_CHANGE = PREFIX + "SERVICE_CHANGE";
	public static final String SERVICE_ADD = PREFIX + "SERVICE_ADD";
	public static final String SERVICE_DELETE = PREFIX + "SERVICE_DELETE";

	public static final String SERVICE_TYPE_READ = PREFIX + "SERVICE_TYPE_READ";
	public static final String SERVICE_TYPE_CHANGE = PREFIX + "SERVICE_TYPE_CHANGE";
	public static final String SERVICE_TYPE_ADD = PREFIX + "SERVICE_TYPE_ADD";
	public static final String SERVICE_TYPE_DELETE = PREFIX + "SERVICE_TYPE_DELETE";

	public static final String PAYMENTS_REPORT = PREFIX + "PAYMENTS_REPORT";

	// menu roles
	// level 1
	public static final String MENU_PAYMENTS_PREFIX = "ROLE_MENU_PAYMENTS";
	public static final String ROLE_MENU_PAYMENTS = MENU_PAYMENTS_PREFIX;
	public static final String ROLE_MENU_PAYMENTS_ACCOUNTANT = MENU_PAYMENTS_PREFIX + "_ACCOUNTANT";

	// level 2
	public static final String ROLE_MENU_PAYMENTS_ADDRESS = MENU_PAYMENTS_PREFIX + "_ADDRESS";
	public static final String ROLE_MENU_PAYMENTS_ACCOUNT = MENU_PAYMENTS_PREFIX + "_ACCOUNT";
	public static final String ROLE_MENU_PAYMENTS_QUITTANCE = MENU_PAYMENTS_PREFIX + "_QUITTANCE";

	public static final String ROLE_MENU_PAYMENTS_REGISTERS = MENU_PAYMENTS_PREFIX + "_REGISTERS";
	public static final String ROLE_MENU_PAYMENTS_REPORTS = MENU_PAYMENTS_PREFIX + "_REPORTS";

	// level 3
	public static final String ROLE_MENU_PAYMENTS_OPERATIONS = MENU_PAYMENTS_PREFIX + "_OPERATIONS";

	// level 4
	public static final String ROLE_MENU_PAYMENTS_OPERATIONS_LIST = MENU_PAYMENTS_PREFIX + "_OPERATIONS_LIST";
	public static final String ROLE_MENU_PAYMENTS_REPORTS_IMPORT = MENU_PAYMENTS_PREFIX + "_REPORTS_IMPORT";
	public static final String ROLE_MENU_PAYMENTS_REPORTS_RECEIVED = MENU_PAYMENTS_PREFIX + "_REPORTS_RECEIVED";
}