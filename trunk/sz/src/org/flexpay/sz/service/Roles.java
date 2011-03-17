package org.flexpay.sz.service;

/**
 * Set of predefined security roles for module
 */
public abstract class Roles {

    public static final String PREFIX = "ROLE_SZ_";

    public static final String BASIC = PREFIX + "BASIC";

    // sz file related permissions
    public static final String SZ_FILE_PREFIX = PREFIX + "SZ_FILE_";

    public static final String SZ_FILE_READ = SZ_FILE_PREFIX + "READ";
    public static final String SZ_FILE_DELETE = SZ_FILE_PREFIX + "DELETE";
    public static final String SZ_FILE_DOWNLOAD_FILE = SZ_FILE_PREFIX + "DOWNLOAD_FILE";
    public static final String SZ_FILE_UPLOAD_FILE = SZ_FILE_PREFIX + "UPLOAD_FILE";
    public static final String SZ_FILE_LOAD_TO_DB = SZ_FILE_PREFIX + "LOAD_TO_DB";
    public static final String SZ_FILE_LOAD_FROM_DB = SZ_FILE_PREFIX + "LOAD_FROM_DB";
    public static final String SZ_FILE_DELETE_FROM_DB = SZ_FILE_PREFIX + "DELETE_FROM_DB";

    // menu roles
    public static final String MENU_SZ_PREFIX = "ROLE_MENU_SZ";
    public static final String MENU_SZ_DICTS_PREFIX = MENU_SZ_PREFIX + "_DICTS";

    // level 1
    public static final String ROLE_MENU_SZ = MENU_SZ_PREFIX;

    // level 2
    public static final String ROLE_MENU_SZ_DICTS = MENU_SZ_PREFIX + "_DICTS";
    public static final String ROLE_MENU_SZ_IMPORT = MENU_SZ_PREFIX + "_IMPORT";

    // level 3
    public static final String ROLE_MENU_SZ_OTHER_DICTS = MENU_SZ_PREFIX + "_OTHER_DICTS";
    public static final String ROLE_MENU_SZ_IMPORT2 = MENU_SZ_PREFIX + "_IMPORT2";

    // level 4
    public static final String ROLE_MENU_SZ_DICTS_IMPORTED_FILES = MENU_SZ_DICTS_PREFIX + "_IMPORTED_FILES";
    public static final String ROLE_MENU_SZ_IMPORT_UPLOAD = MENU_SZ_PREFIX + "_IMPORT_UPLOAD";

}
