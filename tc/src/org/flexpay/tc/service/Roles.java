package org.flexpay.tc.service;

public abstract class Roles {

    // tariff related permissions
    private static final String TARIFF_PREFIX = "TC_TARIFF_";
    public static final String TARIFF_READ = TARIFF_PREFIX + "READ";
    public static final String TARIFF_CHANGE = TARIFF_PREFIX + "CHANGE";
    public static final String TARIFF_ADD = TARIFF_PREFIX + "ADD";
    public static final String TARIFF_DELETE = TARIFF_PREFIX + "DELETE";

    // tariff calculation result related permissions
    private static final String TARIFF_CALCULATION_RESULT_PREFIX = "TC_TARIFF_CALCULATION_RESULT_";
    public static final String TARIFF_CALCULATION_RESULT_READ = TARIFF_CALCULATION_RESULT_PREFIX + "READ";
    public static final String TARIFF_CALCULATION_RESULT_ADD = TARIFF_CALCULATION_RESULT_PREFIX + "ADD";

	// tariff calculation regulation related permissions
	private static final String TARIFF_CALCULATION_REGULATION_PREFIX = "TC_TARIFF_CALCULATION_REGULATION_";
	public static final String TARIFF_CALCULATION_REGULATION_READ = TARIFF_CALCULATION_REGULATION_PREFIX + "READ";
	public static final String TARIFF_CALCULATION_REGULATION_CHANGE = TARIFF_CALCULATION_REGULATION_PREFIX + "CHANGE";
	public static final String TARIFF_CALCULATION_REGULATION_ADD = TARIFF_CALCULATION_REGULATION_PREFIX + "ADD";
	public static final String TARIFF_CALCULATION_REGULATION_DELETE = TARIFF_CALCULATION_REGULATION_PREFIX + "DELETE";

}
