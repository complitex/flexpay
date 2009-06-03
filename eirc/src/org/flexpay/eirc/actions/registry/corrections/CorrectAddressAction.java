package org.flexpay.eirc.actions.registry.corrections;

import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.springframework.beans.factory.annotation.Required;

public class CorrectAddressAction extends org.flexpay.payments.actions.registry.corrections.CorrectAddressAction {

	private RawConsumersDataSource consumersDataSource;

    @Override
    protected void saveCorrection(DataSourceDescription sd) {
        RawConsumerData data = consumersDataSource.getById(String.valueOf(record.getId()));

        // add correction for apartment
        DataCorrection correction = correctionsService.getStub(data.getPersonFIOId(), object, sd);
        correctionsService.save(correction);
    }

	@Required
	public void setConsumersDataSource(RawConsumersDataSource consumersDataSource) {
		this.consumersDataSource = consumersDataSource;
	}
}
