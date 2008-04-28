package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.AccountRecordService;
import org.flexpay.eirc.service.OrganisationService;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.SpRegistryTypeService;
import org.apache.log4j.Logger;

import java.util.List;

public class SimplePayment extends ContainerOperation {

	private static Logger log = Logger.getLogger(SimplePayment.class);

	private ServiceOperationsFactory factory;
	private String organisationId;

	public SimplePayment(ServiceOperationsFactory factory, List<String> datum) {
		super(Integer.parseInt(datum.get(0)));

		this.organisationId = datum.get(1);

		this.factory = factory;
	}

	/**
	 * Process payment operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws FlexPayException if failure occurs
	 */
	public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {

		AccountRecord accountRecord = createRecordStub(registry, record);

		AccountRecordService recordService = factory.getAccountRecordService();
		AccountRecord registeredRecord = recordService.findRegisteredRecord(accountRecord);

		// record not registered before, so just create it
		if (registeredRecord == null) {
			log.info("No registered record, creating a new one");
			recordService.create(accountRecord);
			return;
		}

		if (log.isInfoEnabled()) {
			log.info("Skipping already registered record: " + accountRecord);
		}
	}

	/**
	 * Setup necessary record information
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @return AccountRecord stub
	 * @throws FlexPayException if failure occurs
	 */
	private AccountRecord createRecordStub(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {
		AccountRecord accountRecord = new AccountRecord();
		accountRecord.setOperationDate(record.getOperationDate());
		accountRecord.setAmount(record.getAmount());

		int registryTypeID = registry.getRegistryType().getCode();
		if (registryTypeID != SpRegistryTypeService.NALICHNIE_OPLATI) {
			throw new IllegalOperationStateException(
					"Illegal registry type #" + registryTypeID + " for simple payment operation");
		}

		// setup payment record type
		SPService spService = factory.getSpService();
		AccountRecordType type = spService.getRecordType(AccountRecordType.TYPE_PAYMENT);
		if (type == null) {
			throw new IllegalOperationStateException("Not found simple payment type, was DB inited?");
		}
		accountRecord.setRecordType(type);

		// setup organisation
		OrganisationService organisationService = factory.getOrganisationService();
		Organisation organisation = organisationService.getOrganisation(organisationId);
		if (organisation == null) {
			throw new FlexPayException("Organisation id is invalid: " + organisationId);
		}
		accountRecord.setOrganisation(organisation);
		accountRecord.setConsumer(record.getConsumer());
		accountRecord.setSourceRegistryRecord(record);
		return accountRecord;
	}

	/**
	 * Get container string representation TODO: implement me
	 *
	 * @return container string representation
	 */
	public String getStringFormat() {
		return null;
	}
}
