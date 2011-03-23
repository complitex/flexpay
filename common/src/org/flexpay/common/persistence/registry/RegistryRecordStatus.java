package org.flexpay.common.persistence.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;

import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.PairUtil.pair;

public class RegistryRecordStatus extends DomainObject {

	public static final int LOADED = 1;
	public static final int PROCESSED_WITH_ERROR = 2;
	public static final int FIXED = 3;
	public static final int PROCESSED = 4;

	@SuppressWarnings ({"unchecked"})
	private static final Map<Integer, String> typeToName = map(
			pair(LOADED, "common.registry.record.status.LOADED"),
			pair(PROCESSED_WITH_ERROR, "common.registry.record.status.PROCESSED_WITH_ERROR"),
			pair(FIXED, "common.registry.record.status.FIXED"),
			pair(PROCESSED, "common.registry.record.status.PROCESSED")
	);

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
				append("id", getId()).
				append("code", code).
				append("i18nName", getI18nName()).
				toString();
	}

}
