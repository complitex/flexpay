package org.flexpay.common.persistence.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getI18nName() {
		return typeToName.get(code);
	}

	public boolean isProcessedWithError() {
		return getCode() == PROCESSED_WITH_ERROR;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("RegistryRecordStatus {").
				append("id", getId()).
				append("code", code).
				append("i18nName", getI18nName()).
				append("}").toString();
	}

}
