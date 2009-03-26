package org.flexpay.accounting.service;

public abstract class Roles {
	
	public static final String PREFIX = "ROLE_ACC_";

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

}
