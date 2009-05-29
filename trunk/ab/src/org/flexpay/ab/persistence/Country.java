package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Set;

/**
 * Country
 */
public class Country extends DomainObjectWithStatus {

	private Set<CountryNameTranslation> countryNames = Collections.emptySet();
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

	public Set<CountryNameTranslation> getCountryNames() {
		return countryNames;
	}

	public void setCountryNames(Set<CountryNameTranslation> countryNames) {
		this.countryNames = countryNames;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof Country && super.equals(obj);

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).toString();
	}

}
