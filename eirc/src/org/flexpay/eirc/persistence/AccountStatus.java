package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Set;

public class AccountStatus extends DomainObjectWithStatus {

	private Set<AccountStatusTranslation> translations = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public AccountStatus() {
	}

	public AccountStatus(Long id) {
		super(id);
	}

	public Set<AccountStatusTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<AccountStatusTranslation> translations) {
		this.translations = translations;
	}
}
