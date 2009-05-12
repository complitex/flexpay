package org.flexpay.tc.service;

public abstract class Roles {

	// tariff related permissions
	private static final String TARIFF_PREFIX = "ROLE_TC_TARIFF_";
	public static final String TARIFF_READ = TARIFF_PREFIX + "READ";
	public static final String TARIFF_CHANGE = TARIFF_PREFIX + "CHANGE";
	public static final String TARIFF_ADD = TARIFF_PREFIX + "ADD";
	public static final String TARIFF_DELETE = TARIFF_PREFIX + "DELETE";

	// tariff calculation result related permissions
	private static final String TARIFF_CALCULATION_RESULT_PREFIX = "ROLE_TC_TARIFF_CALCULATION_RESULT_";
	public static final String TARIFF_CALCULATION_RESULT_READ = TARIFF_CALCULATION_RESULT_PREFIX + "READ";
	public static final String TARIFF_CALCULATION_RESULT_ADD = TARIFF_CALCULATION_RESULT_PREFIX + "ADD";
	public static final String TARIFF_CALCULATION_RESULT_UPDATE = TARIFF_CALCULATION_RESULT_PREFIX + "UPDATE";


	// tariff calculation rules file related permissions
	private static final String TARIFF_CALCULATION_RULES_FILE_PREFIX = "ROLE_TC_TARIFF_CALCULATION_RULES_FILE_";
	public static final String TARIFF_CALCULATION_RULES_FILE_READ = TARIFF_CALCULATION_RULES_FILE_PREFIX + "READ";
	public static final String TARIFF_CALCULATION_RULES_FILE_CHANGE = TARIFF_CALCULATION_RULES_FILE_PREFIX + "CHANGE";
	public static final String TARIFF_CALCULATION_RULES_FILE_ADD = TARIFF_CALCULATION_RULES_FILE_PREFIX + "ADD";
	public static final String TARIFF_CALCULATION_RULES_FILE_DELETE = TARIFF_CALCULATION_RULES_FILE_PREFIX + "DELETE";
	public static final String TARIFF_CALCULATION_RULES_FILE_TSRIFF_CALCULATE = TARIFF_CALCULATION_RULES_FILE_PREFIX + "TARIFF_CALCULATE";

	// tariff export log record permissions
	private static final String TARIFF_EXPORT_LOG_RECORD_PREFIX = "ROLE_TC_TARIFF_EXPORT_LOG_RECORD_";
	public static final String TARIFF_EXPORT_LOG_RECORD_READ = TARIFF_EXPORT_LOG_RECORD_PREFIX + "READ";
	public static final String TARIFF_EXPORT_LOG_RECORD_CHANGE = TARIFF_EXPORT_LOG_RECORD_PREFIX + "CHANGE";
	public static final String TARIFF_EXPORT_LOG_RECORD_ADD = TARIFF_EXPORT_LOG_RECORD_PREFIX + "ADD";
	public static final String TARIFF_EXPORT_LOG_RECORD_DELETE = TARIFF_EXPORT_LOG_RECORD_PREFIX + "DELETE";
}