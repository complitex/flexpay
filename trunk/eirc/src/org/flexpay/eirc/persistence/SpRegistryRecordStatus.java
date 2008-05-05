package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.HashMap;
import java.util.Map;

public class SpRegistryRecordStatus extends DomainObject {

	private static Map<Integer, String> typeToName = new HashMap<Integer, String>();

	public static final int LOADED = 1;
	public static final int LOADED_WITH_ERROR = 2;
	public static final int FIXED = 3;
	public static final int PROCESSED = 4;

	static {
		typeToName.put(LOADED, "eirc.registry.record.status.LOADED");
		typeToName.put(LOADED_WITH_ERROR, "eirc.registry.record.status.LOADED_WITH_ERROR");
		typeToName.put(FIXED, "eirc.registry.record.status.FIXED");
		typeToName.put(PROCESSED, "eirc.registry.record.status.PROCESSED");
	}

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

	public String getI18nName() {
		return typeToName.get(code);
	}
}
