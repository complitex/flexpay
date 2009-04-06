package org.flexpay.common.persistence.registry;

import org.flexpay.common.persistence.DomainObject;

import java.util.HashMap;
import java.util.Map;

public class RegistryStatus extends DomainObject {

	private static final Map<Integer, String> typeToName = new HashMap<Integer, String>();

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

	static {
		typeToName.put(LOADING, "eirc.registry.status.LOADING");
		typeToName.put(LOADED, "eirc.registry.status.LOADED");
		typeToName.put(LOADING_CANCELED, "eirc.registry.status.LOADING_CANCELED");
		typeToName.put(LOADED_WITH_ERROR, "eirc.registry.status.LOADED_WITH_ERROR");
		typeToName.put(PROCESSING, "eirc.registry.status.PROCESSING");
		typeToName.put(PROCESSING_WITH_ERROR, "eirc.registry.status.PROCESSING_WITH_ERROR");
		typeToName.put(PROCESSED, "eirc.registry.status.PROCESSED");
		typeToName.put(PROCESSED_WITH_ERROR, "eirc.registry.status.PROCESSED_WITH_ERROR");
		typeToName.put(PROCESSING_CANCELED, "eirc.registry.status.PROCESSING_CANCELED");
		typeToName.put(ROLLBACKING, "eirc.registry.status.ROLLBACKING");
		typeToName.put(ROLLBACKED, "eirc.registry.status.ROLLBACKED");
		typeToName.put(CREATING, "eirc.registry.status.CREATING");
		typeToName.put(CREATED, "eirc.registry.status.CREATED");
		typeToName.put(CREATING_CANCELED, "eirc.registry.status.CREATING_CANCELLED");
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
