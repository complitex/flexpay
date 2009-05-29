package org.flexpay.ab.persistence;

import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class District extends NameTimeDependentChild<DistrictName, DistrictNameTemporal> {

	private Set<Street> streets = Collections.emptySet();

	public District() {
	}

	public District(Long id) {
		super(id);
	}

	public District(Stub<District> district) {
		super(district.getId());
	}

	protected DistrictNameTemporal getEmptyTemporal() {
		return new DistrictNameTemporal(this);
	}

	public Set<Street> getStreets() {
		return streets;
	}

	public void setStreets(Set<Street> streets) {
		this.streets = streets;
	}

	public void setNameForDate(DistrictName name, Date beginDate) {
		setNameForDates(name, beginDate, ApplicationConfig.getFutureInfinite());
	}

	public void setNameForDates(DistrictName name, Date beginDate, Date endDate) {

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

		DistrictNameTemporal temporal = new DistrictNameTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(name);
		temporal.setObject(this);

		addNameTemporal(temporal);
	}

	@NotNull
	public Town getTown() {
		return (Town) getParent();
	}

	@NotNull
	public Stub<Town> getTownStub() {
		return new Stub<Town>(getTown());
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof District && super.equals(obj);
	}

}
