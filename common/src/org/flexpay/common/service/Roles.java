package org.flexpay.common.service;

public abstract class Roles {

	public static final String PREFIX = "ROLE_COMMON_";

	public static final String BASIC = PREFIX + "BASIC";

	public static final String DEVELOPER = PREFIX + "DEVELOPER";

	public static final String PROCESS_READ = PREFIX + "PROCESS_READ";
	public static final String PROCESS_DELETE = PREFIX + "PROCESS_DELETE";
	public static final String PROCESS_START = PREFIX + "PROCESS_START";
	public static final String PROCESS_END = PREFIX + "PROCESS_END";
	public static final String PROCESS_DEFINITION_UPLOAD_NEW = PREFIX + "PROCESS_DEFINITION_UPLOAD_NEW";
	public static final String PROCESS_DEFINITION_READ = PREFIX + "PROCESS_DEFINITION_READ";
	public static final String PROCESS_COMPLETE_HUMAN_TASK = PREFIX + "COMPLETE_HUMAN_TASK";

	public static final String USER_ROLE_READ = PREFIX + "USER_ROLE_READ";
}
