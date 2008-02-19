package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Language;

public class AccountStatus extends DomainObject {

	private String value;
	private Language language;

	/**
	 * Constructs a new DomainObject.
	 */
	public AccountStatus() {
	}

	public AccountStatus(Long id) {
		super(id);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
}
