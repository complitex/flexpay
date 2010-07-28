package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

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

	public Region(@NotNull Stub<Region> stub) {
		super(stub.getId());
	}

	/**
	 * Create a new empty temporal
	 *
	 * @return empty temporal
	 */
    @Override
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
		return new Stub<Country>(getCountry());
	}

	public void setCountry(Country country) {
		setParent(country);
	}

	public Country getCountry() {
		return (Country) getParent();
	}

	public void setNameForDate(RegionName name, Date beginDate) {
		setNameForDates(name, beginDate, getFutureInfinite());
	}

	public void setNameForDates(RegionName name, Date beginDate, Date endDate) {
		if (beginDate.after(endDate)) {
			throw new RuntimeException("Invalid begin-end dates: [" + DateUtil.format(beginDate) +
									   ", " + DateUtil.format(endDate) + "]");
		}
		if (beginDate.before(getPastInfinite())) {
			beginDate = getPastInfinite();
		}
		if (endDate.after(getFutureInfinite())) {
			endDate = getFutureInfinite();
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
