package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Collections;
import java.util.Set;

public class OperationAdditionType extends DomainObject {

	private Set<OperationAdditionTypeTranslation> translations = Collections.emptySet();

	public Set<OperationAdditionTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<OperationAdditionTypeTranslation> translations) {
		this.translations = translations;
	}
}
