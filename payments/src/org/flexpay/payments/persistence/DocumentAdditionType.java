package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Collections;
import java.util.Set;

public class DocumentAdditionType extends DomainObject {

	private Set<DocumentAdditionTypeTranslation> translations = Collections.emptySet();

	public Set<DocumentAdditionTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<DocumentAdditionTypeTranslation> translations) {
		this.translations = translations;
	}
}
