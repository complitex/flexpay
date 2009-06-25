package org.flexpay.eirc.actions.registry.corrections;

import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.springframework.beans.factory.annotation.Required;

public class CorrectStreetAction extends org.flexpay.payments.actions.registry.corrections.CorrectStreetAction {

	private RawConsumersDataSource consumersDataSource;

    @Override
    protected void saveCorrection(Stub<DataSourceDescription> sd) {
        RawConsumerData data = consumersDataSource.getById(String.valueOf(record.getId()));

        // add correction for street
        DataCorrection correction = correctionsService.getStub(data.getStreetId(), object, sd);
        correctionsService.save(correction);
    }

	@Required
	public void setConsumersDataSource(RawConsumersDataSource consumersDataSource) {
		this.consumersDataSource = consumersDataSource;
	}
}
