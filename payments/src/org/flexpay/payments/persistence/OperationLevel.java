package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class OperationLevel extends DomainObject {

	public static final int LOWEST = 1;
	public static final int LOW = 2;
	public static final int AVERAGE = 3;
	public static final int HIGH = 4;
	public static final int SUSPENDED = 5;

	private int code;
	private Set<OperationLevelTranslation> translations = set();

	public Set<OperationLevelTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<OperationLevelTranslation> translations) {
		this.translations = translations;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
