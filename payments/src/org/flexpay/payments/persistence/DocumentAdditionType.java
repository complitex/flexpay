package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.ValueObject;

import java.util.Set;
import java.util.Collections;

public class DocumentAdditionType extends DomainObject {

	private Set<DocumentAdditionTypeTranslation> translations = Collections.emptySet();

	public Set<DocumentAdditionTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<DocumentAdditionTypeTranslation> translations) {
		this.translations = translations;
	}
}
