package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TypeDateInterval;

import java.util.Date;

import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;

public class TownTypeTemporal extends TypeDateInterval<TownType, TownTypeTemporal> {

	public TownTypeTemporal() {
		super(new TownType());
	}

	/**
	 * Copy constructs a new TownTypeTemporal.
	 *
	 * @param di Another name temporal
	 */
	private TownTypeTemporal(TypeDateInterval<TownType, TownTypeTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	public TownTypeTemporal(Date beginDate, TownType townType) {
		super(beginDate, getFutureInfinite(), townType);
	}

	public TownTypeTemporal(Date beginDate, Stub<TownType> typeStub) {
		this(beginDate, new TownType(typeStub.getId()));
	}

    @Override
	protected TownTypeTemporal doGetCopy(TypeDateInterval<TownType, TownTypeTemporal> di) {
		return new TownTypeTemporal(di);
	}

	public Town getTown() {
		return (Town) getObject();
	}

	public void setTown(Town town) {
		setObject(town);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof TownTypeTemporal)) {
			return false;
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
