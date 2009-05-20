package org.flexpay.bti.persistence.building;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Date;

public abstract class BuildingAttributeBase extends DomainObject {

	private BtiBuilding building;
	private BuildingAttributeType attributeType;

	public String getCurrentValue() {
		return getValueForDate(DateUtil.now());
	}

	public abstract String getValueForDate(Date td);

    public Stub<BuildingAttributeType> getAttributeTypeStub() {
        return stub(attributeType);
    }

	public void setCurrentValue(String value) {
		setValueForDate(value, DateUtil.now());
	}

	public void setValueForDate(String value, Date date) {
		setValueForDates(value, date, ApplicationConfig.getFutureInfinite());
	}

	public abstract void setValueForDates(String value, Date beginDt, Date endDt);


	public BtiBuilding getBuilding() {
		return building;
	}

	public void setBuilding(BtiBuilding building) {
		this.building = building;
	}

	public BuildingAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(BuildingAttributeType attributeType) {
		this.attributeType = attributeType;
	}

}
