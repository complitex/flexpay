package org.flexpay.eirc.persistence.exchange;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.service.importexport.CorrectionsService;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import org.flexpay.eirc.dao.importexport.RawConsumersDataUtil;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.persistence.exchange.delayed.*;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Open new service provider personal account
 */
public class OpenAccountOperation extends AbstractChangePersonalAccountOperation {

	private static final Logger log = LoggerFactory.getLogger(OpenAccountOperation.class);
	private ServiceOperationsFactory factory;

	public OpenAccountOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(datum);

		this.factory = factory;
	}

	/**
	 * Process operation
	 *
	 * @param context ProcessingContext
	 * @throws FlexPayException if failure occurs
	 */
	public DelayedUpdate process(@NotNull ProcessingContext context)
			throws FlexPayException, FlexPayExceptionContainer {

		RegistryRecord record = context.getCurrentRecord();
		Registry registry = context.getRegistry();

		if (!validate(registry, record)) {
			return DelayedUpdateNope.INSTANCE;
		}
		DelayedUpdatesContainer container = new DelayedUpdatesContainer();

		ConsumerInfo info = saveConsumerInfo(record);
		DelayedUpdate update = new DelayedUpdateConsumerInfo(info, factory.getConsumerInfoService());
		update.doUpdate();
//		container.addUpdate(update);

		EircAccount account = getEircAccount(record, info);

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		Consumer consumer = new Consumer();
		consumer.setApartment(props.getApartment());
		consumer.setResponsiblePerson(props.getPerson());
		consumer.setExternalAccountNumber(record.getPersonalAccountExt());
		consumer.setBeginDate(changeApplyingDate);
		consumer.setEndDate(getFutureInfinite());
		consumer.setService(props.getService());
		consumer.setEircAccount(account);
		consumer.setConsumerInfo(info);

		container.addUpdate(new DelayedUpdateConsumer(consumer, factory.getConsumerService()));

		createCorrections(registry, record, consumer, container);
		props.setFullConsumer(consumer);

		container.doUpdate();

		return DelayedUpdateNope.INSTANCE;
	}

	private ConsumerInfo saveConsumerInfo(RegistryRecord record) {

		ConsumerInfo info = new ConsumerInfo();

		info.setFirstName(record.getFirstName());
		info.setMiddleName(record.getMiddleName());
		info.setLastName(record.getLastName());

		info.setCityName(record.getCity());
		info.setStreetTypeName(record.getStreetType());
		info.setStreetName(record.getStreetName());
		info.setBuildingNumber(record.getBuildingNum());
		info.setBuildingBulk(record.getBuildingBulkNum());
		info.setApartmentNumber(record.getApartmentNum());

		return info;
	}

	private void createCorrections(Registry registry, RegistryRecord record, Consumer consumer, DelayedUpdatesContainer container) {

		CorrectionsService correctionsService = factory.getCorrectionsService();

		// add short consumer correction
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		RawConsumerData data = RawConsumersDataUtil.convert(registry, record);
		Organization sender = factory.getOrganizationService().readFull(props.getSenderStub());
		Stub<DataSourceDescription> sd = sender.sourceDescriptionStub();

		container.addUpdate(new DelayedUpdateCorrection(correctionsService, consumer, data.getShortConsumerId(), sd));

		// add full consumer correction
		container.addUpdate(new DelayedUpdateCorrection(correctionsService, consumer, data.getFullConsumerId(), sd));
	}

	/**
	 * Check if EIRC account exists, and create a new one if necessary
	 *
	 * @param record RegistryRecord
	 * @param info   ConsumerInfo
	 * @return EircAccount instance
	 * @throws FlexPayException if failure occurs
	 */
	private EircAccount getEircAccount(RegistryRecord record, ConsumerInfo info)
			throws FlexPayException, FlexPayExceptionContainer {

		EircAccountService accountService = factory.getAccountService();

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		Stub<Person> personStub = props.getPersonStub();
		Stub<Apartment> apartmentStub = props.getApartmentStub();
		if (personStub != null && apartmentStub != null) {
			EircAccount account = accountService.findAccount(personStub, apartmentStub);
			if (account != null) {
				return account;
			}
		}

		EircAccount account = new EircAccount();
		account.setPerson(props.getPerson());
		account.setApartment(props.getApartment());
		account.setConsumerInfo(info);

		DelayedUpdate update = new DelayedUpdateEircAccount(account, accountService);
		update.doUpdate();
//		container.addUpdate(update);

		return account;
	}

	/**
	 * Validate registry record for processing, validation is not allowed if consumer already exists
	 *
	 * @param registry Registry
	 * @param record   Record
	 * @return <code>true</true> if processing allowed, or <code>false</code> otherwise
	 * @throws FlexPayException if processing cannot be done at all
	 */
	private boolean validate(Registry registry, RegistryRecord record) throws FlexPayException {

		log.debug("validating record: {}", record);

		if (registry.getRegistryType().getCode() != RegistryType.TYPE_INFO) {
			throw new FlexPayException("Create consumer operation only allowed in Information registry type");
		}

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		if (props.getConsumer() != null) {
			log.info("Already existing consumer");
			return false;
		}

		if (props.getApartment() == null) {
			throw new FlexPayException("Cannot create consumer without apartment set");
		}

		if (props.getPerson() == null) {
			log.warn("Creating account without person set");
		}

		if (props.getService() == null) {
			throw new FlexPayException("Cannot create consumer without service set");
		}

		log.debug("Creating consumer: {}", record);

		return true;
	}
}
