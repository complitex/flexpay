package org.flexpay.eirc.actions.registry.corrections;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.DataCorrection.*;

public class SetCorrectionAction extends org.flexpay.payments.actions.registry.corrections.SetCorrectionAction {

	private RawConsumersDataSource consumersDataSource;

    @Override
    public boolean saveCorrection(RegistryRecord record, Stub<DataSourceDescription> sd) {

        RawConsumerData data = consumersDataSource.getById(String.valueOf(record.getId()));
		DataCorrection correction;

		if (CORRECT_TYPE_STREET.equals(type)) {
			correction = correctionsService.getStub(data.getStreetId(), new Street(object.getId()), sd);
        } else if (CORRECT_TYPE_STREET_TYPE.equals(type)) {
            correction = correctionsService.getStub(data.getStreetTypeId(), new StreetType(object.getId()), sd);
		} else if (CORRECT_TYPE_BUILDING.equals(type)) {
			correction = correctionsService.getStub(data.getBuildingId(), new BuildingAddress(object.getId()), sd);
		} else if (CORRECT_TYPE_APARTMENT.equals(type)) {
			correction = correctionsService.getStub(data.getApartmentId(), new Apartment(object.getId()), sd);
		} else if (CORRECT_TYPE_PERSON.equals(type)) {
			correction = correctionsService.getStub(data.getPersonFIOId(), new Person(object.getId()), sd);
		} else {
			log.warn("Incorrect type parameter {}", type);
			addActionError(getText("payments.error.registry.incorrect_correction_type"));
			return false;
		}

        correctionsService.save(correction);

		return true;
    }

	@Required
	public void setConsumersDataSource(RawConsumersDataSource consumersDataSource) {
		this.consumersDataSource = consumersDataSource;
	}

}
