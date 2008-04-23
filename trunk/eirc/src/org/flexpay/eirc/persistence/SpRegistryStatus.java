package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

public class SpRegistryStatus extends DomainObject {
	public static final int LOADING = 0;
	public static final int LOADED = 1;
	public static final int LOADING_CANCELED = 2;
	public static final int LOADED_WITH_ERROR = 3;
	
	public static final int PROCESSING = 4;
	public static final int PROCESSING_WITH_ERROR = 5;
	public static final int PROCESSED = 6;
	public static final int PROCESSED_WITH_ERROR = 7;
	public static final int PROCESSING_CANCELED = 8;
	
	public static final int ROLLBACKING = 9;
	public static final int ROLLBACKED = 10;
	
	public static final int CREATING = 11;
	public static final int CREATED = 12;
	public static final int CREATING_CANCELED = 13;
	
	private int code;
	
	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
}
