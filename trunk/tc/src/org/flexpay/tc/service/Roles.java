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

    public static final String MENU_TC_PREFIX = "ROLE_MENU_TC";
    public static final String ROLE_MENU_TC = MENU_TC_PREFIX;

    public static final String ROLE_MENU_TC_DICTS = MENU_TC_PREFIX + "_DICTS";
    public static final String ROLE_MENU_TC_IMPORTEXPORT = MENU_TC_PREFIX + "_IMPORTEXPORT";
    public static final String ROLE_MENU_TC_DATA = MENU_TC_PREFIX + "_DATA";

    public static final String ROLE_MENU_TC_OTHER_DICTS = MENU_TC_PREFIX + "_OTHER_DICTS";
    public static final String ROLE_MENU_TC_IMPORTEXPORT2 = MENU_TC_PREFIX + "_IMPORTEXPORT2";
    public static final String ROLE_MENU_TC_PROCESSES = MENU_TC_PREFIX + "_PROCESSES";
    public static final String ROLE_MENU_TC_DATA2 = MENU_TC_PREFIX + "_DATA2";

    public static final String ROLE_MENU_TC_DICTS_CALC_FILES_RULES = MENU_TC_PREFIX + "_DICTS_CALC_FILES_RULES";
    public static final String ROLE_MENU_TC_DICTS_BUILDING_ATTRIBUTES = MENU_TC_PREFIX + "_DICTS_BUILDING_ATTRIBUTES";

    public static final String ROLE_MENU_TC_IMPORTEXPORT_CALC_RESULTS_EXPORT = MENU_TC_PREFIX + "_IMPORTEXPORT_CALC_RESULTS_EXPORT";
    public static final String ROLE_MENU_TC_IMPORTEXPORT_ATTRIBUTES_IMPORT = MENU_TC_PREFIX + "_IMPORTEXPORT_ATTRIBUTES_IMPORT";
    public static final String ROLE_MENU_TC_IMPORTEXPORT_PROCESSES = MENU_TC_PREFIX + "_IMPORTEXPORT_PROCESSES";
    public static final String ROLE_MENU_TC_IMPORTEXPORT_PROCESS_DEF_DEPLOY = MENU_TC_PREFIX + "_IMPORTEXPORT_PROCESS_DEF_DEPLOY";

    public static final String ROLE_MENU_TC_DATA_BUILDING_ATTRIBUTES = MENU_TC_PREFIX + "_DATA_BUILDING_ATTRIBUTES";

}
