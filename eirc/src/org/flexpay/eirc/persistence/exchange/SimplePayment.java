package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.AccountRecordService;
import org.flexpay.eirc.service.OrganisationService;
import org.flexpay.eirc.service.SPService;

import java.util.List;

public class SimplePayment extends ContainerOperation {

	private ServiceOperationsFactory factory;
	private Long organisationId;

	public SimplePayment(ServiceOperationsFactory factory, List<String> datum) {
		super(Integer.parseInt(datum.get(0)));

		this.organisationId = Long.parseLong(datum.get(1));

		this.factory = factory;
	}

	/**
	 * Process payment operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws FlexPayException if failure occurs
	 */
	public void process(SpRegistry registry, RegistryRecord record) throws FlexPayException {

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
	private AccountRecord createRecordStub(SpRegistry registry, RegistryRecord record) throws FlexPayException {
		AccountRecord accountRecord = new AccountRecord();
		accountRecord.setOperationDate(record.getOperationDate());
		accountRecord.setAmount(record.getAmount());

		int registryTypeID = registry.getRegistryType().getCode();
		if (registryTypeID != RegistryType.TYPE_CASH_PAYMENTS) {
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
		Organisation organisation = organisationService.getOrganisation(new Stub<Organisation>(organisationId));
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
