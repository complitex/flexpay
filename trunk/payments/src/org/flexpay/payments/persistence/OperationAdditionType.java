package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.ValueObject;

import java.util.Set;
import java.util.Collections;

public class OperationAdditionType extends DomainObject {

	private Set<OperationAdditionTypeTranslation> translations = Collections.emptySet();

	public Set<OperationAdditionTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<OperationAdditionTypeTranslation> translations) {
		this.translations = translations;
	}
}
