package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.ab.util.config.ApplicationConfig;

import java.util.Date;

public class TownNameTemporal extends NameDateInterval<TownName, TownNameTemporal> {

	/**
	 * Constructs a new RegionNameTemporal.
	 */
	public TownNameTemporal() {
		super(new TownName());
	}

	/**
	 * Constructs a new RegionNameTemporal.
	 *
	 * @param beginDate temporal begin date
	 * @param townName Town name to setup
	 */
	public TownNameTemporal(Date beginDate, TownName townName) {
		super(beginDate, ApplicationConfig.getFutureInfinite(), townName);
	}

	/**
	 * Copy constructs a new RegionNameTemporal.
	 *
	 * @param di Another name temporal
	 */
	private TownNameTemporal(NameDateInterval<TownName, TownNameTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	protected TownNameTemporal doGetCopy(NameDateInterval<TownName, TownNameTemporal> di) {
		return new TownNameTemporal(di);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof TownNameTemporal && super.equals(obj);
	}

	public void setTown(Town town) {
		setObject(town);
	}
}
