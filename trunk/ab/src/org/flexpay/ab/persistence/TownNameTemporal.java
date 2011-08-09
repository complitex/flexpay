package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameDateInterval;

import java.util.Date;

import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;

public class TownNameTemporal extends NameDateInterval<TownName, TownNameTemporal> {

	public TownNameTemporal() {
		super(new TownName());
	}

	public TownNameTemporal(Date beginDate, TownName townName) {
		super(beginDate, getFutureInfinite(), townName);
	}

	private TownNameTemporal(NameDateInterval<TownName, TownNameTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	public TownNameTemporal(Town town) {
		this();
		setObject(town);
	}

    public void setTown(Town town) {
        setObject(town);
    }

    @Override
	protected TownNameTemporal doGetCopy(NameDateInterval<TownName, TownNameTemporal> di) {
		return new TownNameTemporal(di);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TownNameTemporal && super.equals(obj);
	}
}
