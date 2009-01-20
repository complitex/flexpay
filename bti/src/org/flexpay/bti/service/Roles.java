package org.flexpay.bti.service;

public abstract class Roles {
    // sewer type related permissions
    private static final String SEWER_TYPE_PREFIX = "TC_SEWER_TYPE_";
    public static final String SEWER_TYPE_READ = SEWER_TYPE_PREFIX + "READ";
    public static final String SEWER_TYPE_CHANGE = SEWER_TYPE_PREFIX + "CHANGE";
    public static final String SEWER_TYPE_ADD = SEWER_TYPE_PREFIX + "ADD";
    public static final String SEWER_TYPE_DELETE = SEWER_TYPE_PREFIX + "DELETE";

    // sewer material type related permissions
    private static final String SEWER_MATERIAL_TYPE_PREFIX = "TC_SEWER_MATERIAL_TYPE_";
    public static final String SEWER_MATERIAL_TYPE_READ = SEWER_MATERIAL_TYPE_PREFIX + "READ";
    public static final String SEWER_MATERIAL_TYPE_CHANGE = SEWER_MATERIAL_TYPE_PREFIX + "CHANGE";
    public static final String SEWER_MATERIAL_TYPE_ADD = SEWER_MATERIAL_TYPE_PREFIX + "ADD";
    public static final String SEWER_MATERIAL_TYPE_DELETE = SEWER_MATERIAL_TYPE_PREFIX + "DELETE";
}
