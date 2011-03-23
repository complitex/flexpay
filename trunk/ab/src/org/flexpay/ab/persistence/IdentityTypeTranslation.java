package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

/**
 * IdentityTypeTranslation is a translation of IdentityType to particular language
 */
public class IdentityTypeTranslation extends Translation {

	public IdentityTypeTranslation() {
	}

	public IdentityTypeTranslation(String name) {
		super(name, getDefaultLanguage());
	}

	public IdentityTypeTranslation(String name, Language language) {
		super(name, language);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof IdentityTypeTranslation && super.equals(o);
	}

}
