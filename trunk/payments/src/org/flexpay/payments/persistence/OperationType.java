package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Set;
import java.util.Collections;

public class OperationType extends DomainObject {

	public static final int CASH_PAYMENT = 1;
	public static final int CASH_RETURN = 2;
	public static final int ELECTRONIC_PAYMENT = 3;
	public static final int ELECTRONIC_RETURN = 4;
	public static final int SERVICE_FEE = 5;

	private int code;
	private Set<OperationTypeTranslation> translations = Collections.emptySet();

	public Set<OperationTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<OperationTypeTranslation> translations) {
		this.translations = translations;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
