package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Set;
import java.util.Collections;

public class OperationStatus extends DomainObject {

	public static final int CREATED = 1;
	public static final int REGISTERED = 2;
	public static final int DELETED = 3;
	public static final int RETURNED = 4;
	public static final int ERROR = 5;

	private int code;
	private Set<OperationStatusTranslation> translations = Collections.emptySet();

	public Set<OperationStatusTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<OperationStatusTranslation> translations) {
		this.translations = translations;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}	
}
