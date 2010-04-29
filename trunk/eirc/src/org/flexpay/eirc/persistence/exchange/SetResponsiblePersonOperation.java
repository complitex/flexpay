package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataUtil;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.persistence.exchange.conditions.AccountPersonChangeCondition;
import org.flexpay.eirc.persistence.exchange.delayed.*;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.importexport.ImportUtil;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Setup account responsible person first middle last names (FIO)
 */
public class SetResponsiblePersonOperation extends AbstractChangePersonalAccountOperation {

	private static final Logger log = LoggerFactory.getLogger(SetResponsiblePersonOperation.class);
	private ServiceOperationsFactory factory;

	public SetResponsiblePersonOperation(ServiceOperationsFactory factory, List<String> datum)
			throws InvalidContainerException {

		super(datum);
		this.factory = factory;
	}

	/**
	 * Process operation
	 *
	 * @param context ProcessingContext
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException {

		DelayedUpdatesContainer container = new DelayedUpdatesContainer();

		RegistryRecord record = context.getCurrentRecord();
		Consumer consumer = ContainerProcessHelper.getConsumer(record, factory);
		boolean consumerWasProcessed = false;
		for (RegistryRecord processedRegistryRecord : context.getOperationRecords()) {
			if (processedRegistryRecord.getProperties() != null &&
					processedRegistryRecord.getProperties() instanceof EircRegistryRecordProperties) {

				EircRegistryRecordProperties registryRecordProperties = (EircRegistryRecordProperties) processedRegistryRecord.getProperties();

				if (!processedRegistryRecord.equals(record) && registryRecordProperties.hasFullConsumer() &&
						consumer.equals(registryRecordProperties.getConsumer())) {
					consumer = registryRecordProperties.getConsumer();
					((EircRegistryRecordProperties) record.getProperties()).setFullConsumer(consumer);
					consumerWasProcessed = true;
					log.debug("Get consumer from processed registry");
					break;
				}
			}
		}

		// find consumer and set FIO here
		ConsumerInfo info = consumer.getConsumerInfo();

//		List<String> fields = RegistryUtil.parseFIO(newValue);
		String lName = newValue;
		String fName = "";
		String mName = "";

		info.setFirstName(fName);
		info.setMiddleName(mName);
		info.setLastName(lName);

		if (!consumerWasProcessed) {
			container.addUpdate(new DelayedUpdateConsumerInfo(info, factory.getConsumerInfoService()));
		}

		log.debug("Updated consumer info first-middle-last names");

		EircAccountService accountService = factory.getAccountService();
		EircAccount eircAccount = consumer.isEircAccountNew() ? consumer.getEircAccount() :
								  accountService.readFull(consumer.getEircAccountStub());
		if (eircAccount == null) {
			throw new IllegalStateException("EIRC account not found for consumer #" + consumer.getId());
		}

		// setup consumers responsible person
		Person person = findResponsiblePerson(record, fName, mName, lName);
		setupResponsiblePerson(info, person, eircAccount);
		saveConsumers(eircAccount, info, container);

		log.debug("Set responsible person: {}", person);

		// check if need to setup eirc account responsible person
		AccountPersonChangeCondition condition = factory.getConditionsFactory().getAccountPersonChangeCondition();
		if (condition.needUpdatePerson(eircAccount, consumer)) {
			eircAccount.setPerson(person);
			container.addUpdate(new DelayedUpdateEircAccount(eircAccount, factory.getAccountService()));
		}

		// update corrections
		Registry registry = context.getRegistry();
		EircRegistryProperties registryProperties = (EircRegistryProperties) registry.getProperties();
		Organization sender = factory.getOrganizationService().readFull(registryProperties.getSenderStub());
		Stub<DataSourceDescription> sd = sender.sourceDescriptionStub();
		updateCorrections(info, record, eircAccount, sd, container);

		return container;
	}

	/**
	 * Save updated consumers
	 *
	 * @param account   EircAccount that consumers to be saved
	 * @param info	  ConsumerInfo used to find consumers to save
	 * @param container Delayed o[erations container
	 */
	private void saveConsumers(EircAccount account, ConsumerInfo info, DelayedUpdatesContainer container) {

		ConsumerService service = factory.getConsumerService();
		for (Consumer consumer : account.getConsumers()) {
			if (info.equals(consumer.getConsumerInfo())) {
				container.addUpdate(new DelayedUpdateConsumer(consumer, service));
			}
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

	private void updateCorrections(ConsumerInfo info, RegistryRecord record, EircAccount account,
								   Stub<DataSourceDescription> sd, DelayedUpdatesContainer container)
			throws FlexPayException {

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
				setServiceCode(data, serviceCode);
				container.addUpdate(new DelayedUpdateCorrection(service, consumer, data.getFullConsumerId(), sd));

				// and now by external service code
				serviceCode = consumer.getService().getExternalCode();
				if (StringUtils.isNotBlank(serviceCode)) {
					setServiceCode(data, serviceCode);
					container.addUpdate(new DelayedUpdateCorrection(service, consumer, data.getFullConsumerId(), sd));
				}
			}
		}
	}

	private void setServiceCode(RawConsumerData data, String serviceCode) {
		data.addNameValuePair(RawConsumerData.FIELD_SERVICE_CODE, serviceCode);
	}

}
