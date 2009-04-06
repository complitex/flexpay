package org.flexpay.common.persistence.registry;

import org.flexpay.common.persistence.DomainObject;

public class RegistryArchiveStatus extends DomainObject {
	public static final int NONE = 0;
	public static final int ARCHIVING = 1;
	public static final int ARCHIVED = 2;
	public static final int CANCELED = 3;
	
	
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
