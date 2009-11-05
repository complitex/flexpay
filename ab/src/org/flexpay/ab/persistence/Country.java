package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Country entity class
 */
public class Country extends DomainObjectWithStatus {

	private Set<CountryTranslation> translations = Collections.emptySet();
	private Set<Region> regions = Collections.emptySet();

	public Country() {
	}

	public Country(Long id) {
		super(id);
	}

	public Set<Region> getRegions() {
		return regions;
	}

	public void setRegions(Set<Region> regions) {
		this.regions = regions;
	}

	public Set<CountryTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<CountryTranslation> translations) {
		this.translations = translations;
	}

	public void setTranslation(@NotNull CountryTranslation translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof Country && super.equals(obj);

	}

}
