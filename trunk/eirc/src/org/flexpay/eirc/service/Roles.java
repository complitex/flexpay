package org.flexpay.eirc.service;

public abstract class Roles {

	public static final String PREFIX = "ROLE_EIRC_";

	public static final String SERVICE_ORGANIZATION_READ_SERVED_BUILDINGS = PREFIX + "SERVICE_ORGANIZATION_READ_SERVED_BUILDINGS";
	public static final String SERVICE_ORGANIZATION_ADD_SERVED_BUILDINGS = PREFIX + "SERVICE_ORGANIZATION_ADD_SERVED_BUILDINGS";
	public static final String SERVICE_ORGANIZATION_REMOVE_SERVED_BUILDINGS = PREFIX + "SERVICE_ORGANIZATION_REMOVE_SERVED_BUILDINGS";

	public static final String ACCOUNT_READ = PREFIX + "ACCOUNT_READ";
	public static final String ACCOUNT_CHANGE = PREFIX + "ACCOUNT_CHANGE";
	public static final String ACCOUNT_ADD = PREFIX + "ACCOUNT_ADD";
	public static final String ACCOUNT_DELETE = PREFIX + "ACCOUNT_DELETE";

	// todo setup permissions
	public static final String FILE_UPLOAD = PREFIX + "FILE_UPLOAD";
	// todo setup permissions
	public static final String FILE_IMPORT = PREFIX + "FILE_IMPORT";
	// todo setup permissions
	public static final String REGISTRY_PROCESS = PREFIX + "REGISTRY_PROCESS";
	// todo setup permissions
	public static final String REGISTRY_SET_CORRESPONDENCE = PREFIX + "REGISTRY_SET_CORRESPONDENCE";

	public static final String QUITTANCE_CREATE = PREFIX + "QUITTANCE_CREATE";
	public static final String QUITTANCE_CREATE_PRINT_FILE = PREFIX + "QUITTANCE_CREATE_PRINT_FILE";
	public static final String QUITTANCE_READ = PREFIX + "QUITTANCE_READ";
	public static final String QUITTANCE_RECEIVE = PREFIX + "QUITTANCE_RECEIVE";

	public static final String QUITTANCE_PAYMENT_READ = PREFIX + "QUITTANCE_PAYMENT_READ";
	public static final String QUITTANCE_PAY = PREFIX + "QUITTANCE_PAY";
}
