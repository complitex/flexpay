package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Collections;
import java.util.Set;

public class DocumentAdditionType extends DomainObject {

	public static final int CODE_ERC_ACCOUNT = 1;

	private int code;
	private Set<DocumentAdditionTypeTranslation> translations = Collections.emptySet();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Set<DocumentAdditionTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<DocumentAdditionTypeTranslation> translations) {
		this.translations = translations;
	}
}
