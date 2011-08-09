package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TypeDateInterval;

import java.util.Date;

import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;

public class StreetTypeTemporal extends TypeDateInterval<StreetType, StreetTypeTemporal> {

	public StreetTypeTemporal() {
		super(new StreetType());
	}

	/**
	 * Copy constructs a new RegionNameTemporal.
	 *
	 * @param di Another name temporal
	 */
	private StreetTypeTemporal(TypeDateInterval<StreetType, StreetTypeTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

    public StreetTypeTemporal(Date beginDate, StreetType streetType) {
        super(beginDate, getFutureInfinite(), streetType);
    }

    public StreetTypeTemporal(Date beginDate, Stub<StreetType> typeStub) {
        this(beginDate, new StreetType(typeStub.getId()));
    }

    @Override
	protected StreetTypeTemporal doGetCopy(TypeDateInterval<StreetType, StreetTypeTemporal> di) {
		return new StreetTypeTemporal(di);
	}

	public Street getStreet() {
		return (Street) getObject();
	}

	public void setStreet(Street street) {
		setObject(street);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof StreetNameTemporal)) {
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
