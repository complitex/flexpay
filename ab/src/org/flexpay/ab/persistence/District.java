package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Collections;
import java.util.Date;

public class District extends NameTimeDependentChild<DistrictName, DistrictNameTemporal> {

	private Set<Street> streets = Collections.emptySet();

	/**
	 * Constructs a new District.
	 */
	public District() {
	}

	public District(Long id) {
		super(id);
	}

	public District(Stub<District> district) {
		super(district.getId());
	}

	/**
	 * Create a new empty temporal
	 *
	 * @return empty temporal
	 */
	protected DistrictNameTemporal getEmptyTemporal() {
		return new DistrictNameTemporal(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", getId())
				.append("Status", getStatus())
				.append("Names", getNamesTimeLine())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof Region && super.equals(obj);

	}

	/**
	 * Getter for property 'streets'.
	 *
	 * @return Value for property 'streets'.
	 */
	public Set<Street> getStreets() {
		return streets;
	}

	/**
	 * Setter for property 'streets'.
	 *
	 * @param streets Value to set for property 'streets'.
	 */
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
	public Stub<Town> getTownStub() {
		return new Stub<Town>((Town) getParent());
	}
}
