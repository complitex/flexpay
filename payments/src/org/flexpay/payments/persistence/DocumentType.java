package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Collections;
import java.util.Set;

public class DocumentType extends DomainObject {

	public static final int CASH_PAYMENT = 1;
	public static final int CASH_RETURN = 2;
	public static final int CASHLESS_PAYMENT = 3;
	public static final int CASHLESS_PAYMENT_RETURN = 4;

	private int code;
	private Set<DocumentTypeTranslation> translations = Collections.emptySet();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Set<DocumentTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<DocumentTypeTranslation> translations) {
		this.translations = translations;
	}
}
