package org.flexpay.bti.persistence.apartment;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Date;

public abstract class ApartmentAttributeBase extends DomainObject {

	private BtiApartment apartment;
	private ApartmentAttributeType attributeType;

	public String getCurrentValue() {
		return getValueForDate(DateUtil.now());
	}

	public abstract String getValueForDate(Date td);

    public Stub<ApartmentAttributeType> getAttributeTypeStub() {
        return stub(attributeType);
    }

	public void setCurrentValue(String value) {
		setValueForDate(value, DateUtil.now());
	}

	public void setValueForDate(String value, Date date) {
		setValueForDates(value, date, ApplicationConfig.getFutureInfinite());
	}

	public abstract void setValueForDates(String value, Date beginDt, Date endDt);

	public BtiApartment getApartment() {
		return apartment;
	}

	public void setApartment(BtiApartment apartment) {
		this.apartment = apartment;
	}

	public ApartmentAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(ApartmentAttributeType attributeType) {
		this.attributeType = attributeType;
	}

}
