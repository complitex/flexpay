package org.flexpay.sz.service;

public abstract class Roles {

    // sz file related permissions
    private static final String SZ_FILE_PREFIX = "ROLE_SZ_SZ_FILE_";
	public static final String SZ_FILE_READ = SZ_FILE_PREFIX + "READ";
	public static final String SZ_FILE_DELETE = SZ_FILE_PREFIX + "DELETE";
    public static final String SZ_FILE_DOWNLOAD_FILE = SZ_FILE_PREFIX + "DOWNLOAD_FILE";
    public static final String SZ_FILE_UPLOAD_FILE = SZ_FILE_PREFIX + "UPLOAD_FILE";
    public static final String SZ_FILE_LOAD_TO_DB = SZ_FILE_PREFIX + "LOAD_TO_DB";
    public static final String SZ_FILE_LOAD_FROM_DB = SZ_FILE_PREFIX + "LOAD_FROM_DB";
	public static final String SZ_FILE_DELETE_FROM_DB = SZ_FILE_PREFIX + "DELETE_FROM_DB";

}
