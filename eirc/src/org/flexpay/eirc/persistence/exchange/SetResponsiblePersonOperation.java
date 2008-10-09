package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataUtil;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.importexport.ImportUtil;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.eirc.sp.RegistryUtil;

import java.util.List;

/**
 * Setup account responsible person first middle last names (FIO)
 */
public class SetResponsiblePersonOperation extends AbstractChangePersonalAccountOperation {

	private ServiceOperationsFactory factory;

	public SetResponsiblePersonOperation(ServiceOperationsFactory factory, List<String> datum)
			throws InvalidContainerException {

		super(datum);
		this.factory = factory;
	}

	/**
	 * Process operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public void process(SpRegistry registry, RegistryRecord record) throws FlexPayException {

		if (record.getConsumer() == null) {
			throw new FlexPayException("Consumer was not set up, cannot change FIO");
		}

		// find consumer and set FIO here
		Consumer consumer = (Consumer) record.getConsumer();
		ConsumerInfo info = consumer.getConsumerInfo();

		List<String> fields = RegistryUtil.parseFIO(newValue);
		String fName = fields.get(0);
		String mName = fields.get(1);
		String lName = fields.get(2);

		info.setFirstName(fName);
		info.setMiddleName(mName);
		info.setLastName(lName);
		factory.getConsumerInfoService().save(info);

		EircAccountService accountService = factory.getAccountService();
		EircAccount account = accountService.readFull(consumer.getEircAccountStub());
		if (account == null) {
			throw new IllegalStateException("EIRC account not found for consumer: " + consumer);
		}

		// setup consumers responsible person
		Person person = findResponsiblePerson(record, fName, mName, lName);
		setupResponsiblePerson(info, person, account);
		saveConsumers(account, info);

		// check if need to setup eirc account responsible person
		if (info.equals(account.getConsumerInfo())) {
			account.setPerson(person);
			saveAccount(account);
		}

		// update corrections
		DataSourceDescription sd = registry.getServiceProvider().getDataSourceDescription();
		updateCorrections(info, record, account, sd);
	}

	/**
	 * Save updated consumers
	 *
	 * @param account EircAccount that consumers to be saved
	 * @param info	ConsumerInfo used to find consumers to save
	 * @throws FlexPayException if failure occurs
	 */
	private void saveConsumers(EircAccount account, ConsumerInfo info) throws FlexPayException {

		try {
			ConsumerService service = factory.getConsumerService();
			for (Consumer consumer : account.getConsumers()) {
				if (info.equals(consumer.getConsumerInfo())) {
					service.save(consumer);
				}
			}
		} catch (FlexPayExceptionContainer e) {
			throw e.getFirstException();
		}
	}

	/**
	 * Save eirc account
	 *
	 * @param account Eirc account to save
	 * @throws FlexPayException if failure occurs
	 */
	private void saveAccount(EircAccount account) throws FlexPayException {

		try {
			factory.getAccountService().save(account);
		} catch (FlexPayExceptionContainer e) {
			throw e.getFirstException();
		}
	}

	private Person findResponsiblePerson(RegistryRecord record, String fName, String mName, String lName) {
		ImportUtil importUtil = factory.getImportUtil();
		ImportError error = new ImportError();
		Person person = importUtil.findPersonByFIO(record.getApartmentStub(),
				fName, mName, lName, error);
		if (error.getErrorId() != null) {
			log.warn("Responsible person not found " + error.getErrorId());
		}
		return person;
	}

	/**
	 * Set responsible person to each consumer referencing the same consumer info
	 *
	 * @param info	ConsumerInfo
	 * @param person  Responsible person to set up
	 * @param account EircAccount that consumers are to be updated
	 */
	private void setupResponsiblePerson(ConsumerInfo info, Person person, EircAccount account) {
		for (Consumer consumer : account.getConsumers()) {
			if (info.equals(consumer.getConsumerInfo())) {
				consumer.setResponsiblePerson(person);
			}
		}
	}

	private void updateCorrections(ConsumerInfo info, RegistryRecord record,
								   EircAccount account, DataSourceDescription sd)
			throws FlexPayException {

		RawConsumerData dataOld = RawConsumersDataUtil.convert(record);
		RawConsumerData data = RawConsumersDataUtil.convert(record);

		List<String> fields = RegistryUtil.parseFIO(newValue);

		// setup fio
		data.addNameValuePair(RawConsumerData.FIELD_FIRST_NAME, fields.get(0));
		data.addNameValuePair(RawConsumerData.FIELD_MIDDLE_NAME, fields.get(1));
		data.addNameValuePair(RawConsumerData.FIELD_LAST_NAME, fields.get(2));

		CorrectionsService service = factory.getCorrectionsService();
		for (Consumer consumer : account.getConsumers()) {
			if (info.equals(consumer.getConsumerInfo())) {

				// update corrections (by internal service id)
				String serviceCode = String.valueOf(consumer.getService().getId());
				DataCorrection c = buildCorrection(dataOld, consumer, serviceCode, sd);
				setServiceCode(data, serviceCode);
				c.setExternalId(data.getFullConsumerId());
				service.save(c);

				// and now by external service code
				serviceCode = consumer.getService().getExternalCode();
				if (StringUtils.isNotBlank(serviceCode)) {
					c = buildCorrection(dataOld, consumer, serviceCode, sd);
					setServiceCode(data, serviceCode);
					c.setExternalId(data.getFullConsumerId());
					service.save(c);
				}
			}
		}
	}

	private DataCorrection buildCorrection(RawConsumerData data, Consumer consumer,
										   String serviceCode, DataSourceDescription sd) {
		CorrectionsService service = factory.getCorrectionsService();
		setServiceCode(data, serviceCode);

		return service.getStub(data.getFullConsumerId(), consumer, sd);
	}

	private void setServiceCode(RawConsumerData data, String serviceCode) {
		data.addNameValuePair(RawConsumerData.FIELD_SERVICE_CODE, serviceCode);
	}
}
