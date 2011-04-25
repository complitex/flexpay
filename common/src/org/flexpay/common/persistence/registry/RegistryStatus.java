package org.flexpay.common.persistence.registry;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.util.CollectionUtils;

import java.util.Map;

public class RegistryStatus extends DomainObject {

	private static final Map<Integer, String> typeToName = CollectionUtils.map();

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

	public static final int PROCESSED_IMPORT_CONSUMER = 14;
	public static final int PROCESSED_IMPORT_CONSUMER_WITH_ERROR = 15;
	public static final int PROCESSING_IMPORT_CONSUMER = 16;
	public static final int PROCESSING_IMPORT_CONSUMER_WITH_ERROR = 17;

	public static final int START_PROCESSING = 18;

	static {
		typeToName.put(LOADING, "common.registry.status.LOADING");
		typeToName.put(LOADED, "common.registry.status.LOADED");
		typeToName.put(LOADING_CANCELED, "common.registry.status.LOADING_CANCELED");
		typeToName.put(LOADED_WITH_ERROR, "common.registry.status.LOADED_WITH_ERROR");
		typeToName.put(START_PROCESSING, "common.registry.status.START_PROCESSING");
		typeToName.put(PROCESSING, "common.registry.status.PROCESSING");
		typeToName.put(PROCESSING_IMPORT_CONSUMER, "common.registry.status.PROCESSING_IMPORT_CONSUMER");
		typeToName.put(PROCESSING_WITH_ERROR, "common.registry.status.PROCESSING_WITH_ERROR");
		typeToName.put(PROCESSING_IMPORT_CONSUMER_WITH_ERROR, "common.registry.status.PROCESSING_IMPORT_CONSUMER_WITH_ERROR");
		typeToName.put(PROCESSED, "common.registry.status.PROCESSED");
		typeToName.put(PROCESSED_IMPORT_CONSUMER, "common.registry.status.PROCESSED_IMPORT_CONSUMER");
		typeToName.put(PROCESSED_IMPORT_CONSUMER_WITH_ERROR, "common.registry.status.PROCESSED_IMPORT_CONSUMER_WITH_ERROR");
		typeToName.put(PROCESSED_WITH_ERROR, "common.registry.status.PROCESSED_WITH_ERROR");
		typeToName.put(PROCESSING_CANCELED, "common.registry.status.PROCESSING_CANCELED");
		typeToName.put(ROLLBACKING, "common.registry.status.ROLLBACKING");
		typeToName.put(ROLLBACKED, "common.registry.status.ROLLBACKED");
		typeToName.put(CREATING, "common.registry.status.CREATING");
		typeToName.put(CREATED, "common.registry.status.CREATED");
		typeToName.put(CREATING_CANCELED, "common.registry.status.CREATING_CANCELLED");
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
