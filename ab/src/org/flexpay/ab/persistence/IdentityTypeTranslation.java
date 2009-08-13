package org.flexpay.ab.persistence;

import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;

/**
 * IdentityTypeTranslation is a translation of IdentityType to particular language
 */
public class IdentityTypeTranslation extends Translation {

	public IdentityTypeTranslation() {
	}

	public IdentityTypeTranslation(String name) {
		super(name, ApplicationConfig.getDefaultLanguage());
	}

	public IdentityTypeTranslation(String name, Language language) {
		super(name, language);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof IdentityTypeTranslation && super.equals(o);
	}

}
