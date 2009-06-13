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

	public static final String MENU_EIRC_PREFIX = "ROLE_MENU_EIRC";
	public static final String ROLE_MENU_EIRC = MENU_EIRC_PREFIX;

	public static final String ROLE_MENU_EIRC_DICTS = MENU_EIRC_PREFIX + "_DICTS";
	public static final String ROLE_MENU_EIRC_IMPORT = MENU_EIRC_PREFIX + "_IMPORT";
	public static final String ROLE_MENU_EIRC_PROCESSING = MENU_EIRC_PREFIX + "_PROCESSING";
	public static final String ROLE_MENU_EIRC_QUITTANCES = MENU_EIRC_PREFIX + "_QUITTANCES";

	public static final String ROLE_MENU_EIRC_ORGANIZATIONS_DICTS = MENU_EIRC_PREFIX + "_ORGANIZATIONS_DICTS";
	public static final String ROLE_MENU_EIRC_SERVICES_DICTS = MENU_EIRC_PREFIX + "_SERVICES_DICTS";
	public static final String ROLE_MENU_EIRC_CASHBOXES_DICTS = MENU_EIRC_PREFIX + "_CASHBOXES_DICTS";
	public static final String ROLE_MENU_EIRC_IMPORT2 = MENU_EIRC_PREFIX + "_IMPORT2";
	public static final String ROLE_MENU_EIRC_PROCESSING2 = MENU_EIRC_PREFIX + "_PROCESSING2";
	public static final String ROLE_MENU_EIRC_QUITTANCES2 = MENU_EIRC_PREFIX + "_QUITTANCES2";
	public static final String ROLE_MENU_EIRC_QUITTANCES_PAYFORM = MENU_EIRC_PREFIX + "_QUITTANCES_PAYFORM";
	public static final String ROLE_MENU_EIRC_QUITTANCES_PACKETS = MENU_EIRC_PREFIX + "_QUITTANCE_PACKETS";

	public static final String ROLE_MENU_EIRC_DICTS_ORGANIZATIONS = MENU_EIRC_PREFIX + "_DICTS_ORGANIZATIONS";
	public static final String ROLE_MENU_EIRC_DICTS_BANKS = MENU_EIRC_PREFIX + "_DICTS_BANKS";
	public static final String ROLE_MENU_EIRC_DICTS_SERVICE_ORGANIZATIONS = MENU_EIRC_PREFIX + "_DICTS_SERVICE_ORGANIZATIONS";
	public static final String ROLE_MENU_EIRC_DICTS_SERVICE_PROVIDERS = MENU_EIRC_PREFIX + "_DICTS_SERVICE_PROVIDERS";
	public static final String ROLE_MENU_EIRC_DICTS_PAYMENT_COLLECTORS = MENU_EIRC_PREFIX + "_DICTS_PAYMENT_COLLECTORS";
	public static final String ROLE_MENU_EIRC_DICTS_PAYMENT_POINTS = MENU_EIRC_PREFIX + "_DICTS_PAYMENT_POINTS";
	public static final String ROLE_MENU_EIRC_DICTS_EIRC_ACCOUNTS = MENU_EIRC_PREFIX + "_DICTS_EIRC_ACCOUNTS";
	public static final String ROLE_MENU_EIRC_DICTS_SERVICE_TYPES = MENU_EIRC_PREFIX + "_DICTS_SERVICE_TYPES";
	public static final String ROLE_MENU_EIRC_DICTS_SERVICES = MENU_EIRC_PREFIX + "_DICTS_SERVICES";
	public static final String ROLE_MENU_EIRC_DICTS_CASHBOXES = MENU_EIRC_PREFIX + "_DICTS_CASHBOXES";

	public static final String ROLE_MENU_EIRC_IMPORT_SP_FILES_IMPORT = MENU_EIRC_PREFIX + "_IMPORT_SP_FILES_IMPORT";
	public static final String ROLE_MENU_EIRC_IMPORT_IMPORTED_SP_FILES = MENU_EIRC_PREFIX + "_IMPORT_IMPORTED_SP_FILES";
	public static final String ROLE_MENU_EIRC_IMPORT_REGISTRIES = MENU_EIRC_PREFIX + "_IMPORT_REGISTRIES";

	public static final String ROLE_MENU_EIRC_PROCESSING_PROCESSES = MENU_EIRC_PREFIX + "_PROCESSING_PROCESSES";
	public static final String ROLE_MENU_EIRC_PROCESSING_PROCESS_DEFINITION = MENU_EIRC_PREFIX + "_PROCESSING_PROCESS_DEFINITION";

	public static final String ROLE_MENU_EIRC_QUITTANCES_GENERATE = MENU_EIRC_PREFIX + "_QUITTANCES_GENERATE";
	public static final String ROLE_MENU_EIRC_QUITTANCES_PRINT = MENU_EIRC_PREFIX + "_QUITTANCES_PRINT";

}