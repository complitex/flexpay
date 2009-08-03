package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.flexpay.ab.util.config.ApplicationConfig;

import java.util.Collections;
import java.util.Set;
import java.util.Date;

/**
 * Region
 */
public class Region extends NameTimeDependentChild<RegionName, RegionNameTemporal> {

	private Set<Town> towns = Collections.emptySet();

	public Region() {
	}

	public Region(Long id) {
		super(id);
	}

	/**
	 * Create a new empty temporal
	 *
	 * @return empty temporal
	 */
	protected RegionNameTemporal getEmptyTemporal() {
		return new RegionNameTemporal(this);
	}

	public Set<Town> getTowns() {
		return this.towns;
	}

	public void setTowns(Set<Town> towns) {
		this.towns = towns;
	}

	public Stub<Country> getCountryStub() {
		return new Stub<Country>(getParent().getId());
	}

	public void setCountry(Country country) {
		setParent(country);
	}

	public Country getCountry() {
		return (Country) getParent();
	}

	public void setNameForDate(RegionName name, Date beginDate) {
		setNameForDates(name, beginDate, ApplicationConfig.getFutureInfinite());
	}

	public void setNameForDates(RegionName name, Date beginDate, Date endDate) {
		if (beginDate.after(endDate)) {
			throw new RuntimeException("Invalid begin-end dates: [" + DateUtil.format(beginDate) +
									   ", " + DateUtil.format(endDate) + "]");
		}
		if (beginDate.before(ApplicationConfig.getPastInfinite())) {
			beginDate = ApplicationConfig.getPastInfinite();
		}
		if (endDate.after(ApplicationConfig.getFutureInfinite())) {
			endDate = ApplicationConfig.getFutureInfinite();
		}

		name.setObject(this);

		RegionNameTemporal temporal = new RegionNameTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(name);
		temporal.setObject(this);

		addNameTemporal(temporal);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof Region && super.equals(obj);
	}
}
