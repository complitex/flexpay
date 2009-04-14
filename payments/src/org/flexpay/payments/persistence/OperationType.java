package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Set;
import java.util.Collections;

public class OperationType extends DomainObject {

	public static final int SERVICE_CASH_PAYMENT = 1;
	public static final int SERVICE_CASHLESS_PAYMENT = 2;
	public static final int SERVICE_CASH_RETURN = 3;
	public static final int SERVICE_CASHLESS_RETURN = 4;
	public static final int QUITTANCE_CASH_PAYMENT = 5;
	public static final int QUITTANCE_CASHLESS_PAYMENT = 6;
	public static final int QUITTANCE_CASH_RETURN = 7;
	public static final int QUITTANCE_CASHLESS_RETURN = 8;


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
