package org.flexpay.eirc.service;

public abstract class Roles {

	public static final String PREFIX = "ROLE_EIRC_";

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

	public static final String SERVICE_ORGANIZATION_READ_SERVED_BUILDINGS = PREFIX + "SERVICE_ORGANIZATION_READ_SERVED_BUILDINGS";
	public static final String SERVICE_ORGANIZATION_ADD_SERVED_BUILDINGS = PREFIX + "SERVICE_ORGANIZATION_ADD_SERVED_BUILDINGS";
	public static final String SERVICE_ORGANIZATION_REMOVE_SERVED_BUILDINGS = PREFIX + "SERVICE_ORGANIZATION_REMOVE_SERVED_BUILDINGS";

	public static final String SERVICE_PROVIDER_READ = PREFIX + "SERVICE_PROVIDER_READ";
	public static final String SERVICE_PROVIDER_CHANGE = PREFIX + "SERVICE_PROVIDER_CHANGE";
	public static final String SERVICE_PROVIDER_ADD = PREFIX + "SERVICE_PROVIDER_ADD";
	public static final String SERVICE_PROVIDER_DELETE = PREFIX + "SERVICE_PROVIDER_DELETE";

	public static final String ACCOUNT_READ = PREFIX + "ACCOUNT_READ";
	public static final String ACCOUNT_CHANGE = PREFIX + "ACCOUNT_CHANGE";
	public static final String ACCOUNT_ADD = PREFIX + "ACCOUNT_ADD";
	public static final String ACCOUNT_DELETE = PREFIX + "ACCOUNT_DELETE";

	public static final String SERVICE_READ = PREFIX + "SERVICE_READ";
	public static final String SERVICE_CHANGE = PREFIX + "SERVICE_CHANGE";
	public static final String SERVICE_ADD = PREFIX + "SERVICE_ADD";
	public static final String SERVICE_DELETE = PREFIX + "SERVICE_DELETE";

	public static final String SERVICE_TYPE_READ = PREFIX + "SERVICE_TYPE_READ";
	public static final String SERVICE_TYPE_CHANGE = PREFIX + "SERVICE_TYPE_CHANGE";
	public static final String SERVICE_TYPE_ADD = PREFIX + "SERVICE_TYPE_ADD";
	public static final String SERVICE_TYPE_DELETE = PREFIX + "SERVICE_TYPE_DELETE";

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
}
