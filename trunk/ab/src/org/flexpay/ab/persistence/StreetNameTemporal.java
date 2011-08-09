package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameDateInterval;

import java.util.Date;

import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;

public class StreetNameTemporal extends NameDateInterval<StreetName, StreetNameTemporal> {

	public StreetNameTemporal() {
		super(new StreetName());
	}

    public StreetNameTemporal(Date beginDate, StreetName streetName) {
        super(beginDate, getFutureInfinite(), streetName);
    }

	private StreetNameTemporal(NameDateInterval<StreetName, StreetNameTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	public StreetNameTemporal(Street street) {
		this();
		setObject(street);
	}

    public void setStreet(Street street) {
        setObject(street);
    }

    @Override
	protected StreetNameTemporal doGetCopy(NameDateInterval<StreetName, StreetNameTemporal> di) {
		return new StreetNameTemporal(di);
	}

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StreetNameTemporal && super.equals(obj);
    }
}
