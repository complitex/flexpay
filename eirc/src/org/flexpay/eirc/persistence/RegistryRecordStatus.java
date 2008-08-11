package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;

import java.util.Map;

public class RegistryRecordStatus extends DomainObject {

	public static final int LOADED = 1;
	public static final int PROCESSED_WITH_ERROR = 2;
	public static final int FIXED = 3;
	public static final int PROCESSED = 4;

	private static final Map<Integer, String> typeToName = map(
			ar(LOADED, PROCESSED_WITH_ERROR, FIXED, PROCESSED),
			ar(
					"eirc.registry.record.status.LOADED",
					"eirc.registry.record.status.PROCESSED_WITH_ERROR",
					"eirc.registry.record.status.FIXED",
					"eirc.registry.record.status.PROCESSED"));

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
