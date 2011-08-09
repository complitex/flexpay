package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class OperationStatus extends DomainObject {

	public static final int CREATED = 1;
	public static final int REGISTERED = 2;
	public static final int DELETED = 3;
	public static final int RETURNED = 4;
	public static final int ERROR = 5;
	public static final int BLANK = 6;

	private int code;
	private Set<OperationStatusTranslation> translations = set();

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
