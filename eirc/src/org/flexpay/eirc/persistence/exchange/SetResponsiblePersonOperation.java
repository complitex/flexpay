package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataUtil;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.exchange.conditions.AccountPersonChangeCondition;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.importexport.ImportUtil;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.EircRegistryProperties;

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
	public void process(Registry registry, RegistryRecord record) throws FlexPayException {

		Consumer consumer = ContainerProcessHelper.getConsumer(record, factory);

		// find consumer and set FIO here
		ConsumerInfo info = consumer.getConsumerInfo();

//		List<String> fields = RegistryUtil.parseFIO(newValue);
		String lName = newValue;
		String fName = "";
		String mName = "";

		info.setFirstName(fName);
		info.setMiddleName(mName);
		info.setLastName(lName);

		factory.getConsumerInfoService().save(info);

		log.debug("Updated consumer info first-middle-last names");

		EircAccountService accountService = factory.getAccountService();
		EircAccount eircAccount = accountService.readFull(consumer.getEircAccountStub());
		if (eircAccount == null) {
			throw new IllegalStateException("EIRC account not found for consumer #" + consumer.getId());
		}

		// setup consumers responsible person
		Person person = findResponsiblePerson(record, fName, mName, lName);
		setupResponsiblePerson(info, person, eircAccount);
		saveConsumers(eircAccount, info);

		log.debug("Set responsible person: {}", person);

		// check if need to setup eirc account responsible person
		AccountPersonChangeCondition condition = factory.getConditionsFactory().getAccountPersonChangeCondition();
		if (condition.needUpdatePerson(eircAccount, consumer)) {
			eircAccount.setPerson(person);
			saveAccount(eircAccount);
		}

		// update corrections
		EircRegistryProperties registryProperties = (EircRegistryProperties) registry.getProperties();
		ServiceProvider provider = factory.getServiceProviderService().read(registryProperties.getServiceProviderStub());
		DataSourceDescription sd = provider.getDataSourceDescription();
		updateCorrections(info, record, eircAccount, sd);
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
			if (account.isNew()) {
				factory.getAccountService().create(account);
			} else {
				factory.getAccountService().update(account);
			}
		} catch (FlexPayExceptionContainer e) {
			throw e.getFirstException();
		}
	}

	private Person findResponsiblePerson(RegistryRecord record, String fName, String mName, String lName) throws FlexPayException {
		ImportUtil importUtil = factory.getImportUtil();
		ImportError error = new ImportError();
		Consumer consumer = ContainerProcessHelper.getConsumer(record, factory);
		Person person = importUtil.findPersonByFIO(consumer.getApartmentStub(),
				fName, mName, lName, error);
		if (error.getErrorId() != null) {
			log.warn("Responsible person not found {}", error.getErrorId());
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

//		List<String> fields = RegistryUtil.parseFIO(newValue);

		// setup fio
		data.addNameValuePair(RawConsumerData.FIELD_FIRST_NAME, "");
		data.addNameValuePair(RawConsumerData.FIELD_MIDDLE_NAME, "");
		data.addNameValuePair(RawConsumerData.FIELD_LAST_NAME, newValue);

		CorrectionsService service = factory.getCorrectionsService();
		for (Consumer consumer : account.getConsumers()) {
			if (info.equals(consumer.getConsumerInfo())) {

				// need to update corrections with a Full consumer info only as
				// FIO is used only there

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
