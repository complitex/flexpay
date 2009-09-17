package org.flexpay.payments.service.registry.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DateRange;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.history.OrganizationHistoryGenerator;
import org.flexpay.orgs.service.history.PaymentPointHistoryGenerator;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.service.DocumentAdditionTypeService;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.history.ServiceHistoryGenerator;
import org.flexpay.payments.service.registry.PaymentsRegistryDBGenerator;
import org.flexpay.payments.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Generate the payments registry in database.
 */
public class PaymentsRegistryDBGeneratorImpl implements PaymentsRegistryDBGenerator {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private RegistryService registryService;
	private RegistryRecordService registryRecordService;
	private RegistryTypeService registryTypeService;
	private RegistryStatusService registryStatusService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private RegistryRecordStatusService registryRecordStatusService;
	private PropertiesFactory propertiesFactory;
	private DocumentService documentService;
	private DocumentAdditionTypeService documentAdditionTypeService;
	private ClassToTypeRegistry typeRegistry;
	private MasterIndexService masterIndexService;
	private OrganizationHistoryGenerator organizationHistoryGenerator;
	private PaymentPointHistoryGenerator paymentPointHistoryGenerator;
	private ServiceHistoryGenerator serviceHistoryGenerator;

	/**
	 * Create the new payments registry in database from registered payment documents.<br/> One document is the one record
	 * in registry.
	 *
	 * @param serviceProvider	  Document was generated for service provider.
	 * @param registerOrganization Registered organization generated document.
	 * @param range				Created payments in date range.
	 * @return Payments registry
	 * @throws FlexPayException
	 */
	@Nullable
	public Registry createRegistry(@NotNull ServiceProvider serviceProvider,
								   @NotNull Organization registerOrganization,
								   @NotNull DateRange range) throws FlexPayException {

		log.info("Searching documents for service provider {} and registered in organization {}",
				new Object[]{serviceProvider.getId(), registerOrganization.getId()});

		List<Document> documents = getDocuments(serviceProvider, registerOrganization, range);

		if (documents.size() == 0) {
			log.info("No documents were found. No registry will be created.");
			return null;
		}

		log.info("{} documents found", documents.size());

		Registry registry = new Registry();
		fillRegistryData(serviceProvider, registerOrganization, range, registry);
		registryService.create(registry);

		BigDecimal totalSumm = new BigDecimal(0);
		Long recordsNumber = 0L;
		Long errorsNumber = 0L;
		for (Document document : documents) {
			if (document.getRegistryRecord() == null) {
				RegistryRecord record = buildRecord(registry, document);
				try {
					registryRecordService.create(record);
					totalSumm = totalSumm.add(document.getSumm());
					recordsNumber++;
				} catch (FlexPayException e) {
					errorsNumber++;
					log.error("Registry record for document " + String.valueOf(document.getId()) + " was not created", e);
				}
			}
		}

		setRegistryTotals(registry, totalSumm, recordsNumber, errorsNumber);
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATED));
		registryService.update(registry);

		log.info("Created db registry: id = {}, recordsNumber = {}, amount = {}",
				new Object[]{registry.getId(), recordsNumber, totalSumm});

		return registry;
	}

	private void setRegistryTotals(Registry registry, BigDecimal totalSumm, Long recordsNumber, Long errorsNumber) {
		registry.setAmount(totalSumm);
		registry.setRecordsNumber(recordsNumber);
		registry.setErrorsNumber(errorsNumber.intValue());
	}

	private void fillRegistryData(@NotNull ServiceProvider provider, @NotNull Organization registerer,
								  @NotNull DateRange range, @NotNull Registry registry) {

		registry.setRecipientCode(provider.getOrganization().getId());
		registry.setSenderCode(registerer.getId());
		registry.setCreationDate(new Date());
		registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_CASH_PAYMENTS));
		registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATING));
		registry.setFromDate(range.getStart());
		registry.setTillDate(range.getEnd());

		EircRegistryProperties registryProperties = (EircRegistryProperties) propertiesFactory.newRegistryProperties();
		registryProperties.setRecipient(provider.getOrganization());
		registryProperties.setSender(registerer);
		registryProperties.setServiceProvider(provider);
		registry.setProperties(registryProperties);

		// TODO: remove this dirty hack
		organizationHistoryGenerator.generateFor(registerer);
		organizationHistoryGenerator.generateFor(provider.getOrganization());

		// add instance id
		registry.addContainer(new RegistryContainer(
				"503" +
				":" + ApplicationConfig.getInstanceId()
		));

		// add identifiers sync containers
		registry.addContainer(new RegistryContainer(
				"502" +
				":" + typeRegistry.getType(Organization.class) +
				":" + registerer.getId() +
				":" +
				":" + masterIndexService.getMasterIndex(registerer) +
				":1"
		));
		registry.addContainer(new RegistryContainer(
				"502" +
				":" + typeRegistry.getType(Organization.class) +
				":" + provider.getOrganization().getId() +
				":" +
				":" + masterIndexService.getMasterIndex(provider.getOrganization()) +
				":1"
		));

		// add identifiers sync containers of used payment points
		List<PaymentPoint> paymentPoints = documentService.listPaymentsPoints(stub(provider), stub(registerer), range);
		log.debug("Payment points is using {} time", paymentPoints.size());
		for (PaymentPoint paymentPoint : paymentPoints) {

			// TODO: remove this dirty hack
			paymentPointHistoryGenerator.generateFor(paymentPoint);

			registry.addContainer(new RegistryContainer(
					"502" +
					":" + typeRegistry.getType(PaymentPoint.class) +
					":" + paymentPoint.getId() +
					":" +
					":" + masterIndexService.getMasterIndex(paymentPoint) +
					":1"
			));
		}

		// add identifiers sync containers of used services
		List<Service> services = documentService.listPaymentsServices(stub(provider), stub(registerer), range);
		log.debug("Services is used {} time(s)", services.size());
		for (Service service : services) {

			// TODO: remove this dirty hack
			serviceHistoryGenerator.generateFor(service);

			registry.addContainer(new RegistryContainer(
					"502" +
					":" + typeRegistry.getType(Service.class) +
					":" + service.getId() +
					":" +
					":" + masterIndexService.getMasterIndex(service) +
					":1"
			));
		}
	}

	private RegistryRecord buildRecord(Registry registry, Document document) throws FlexPayException {

		RegistryRecordStatus statusProcessed = getRecordStatusProcessed();

		RegistryRecord record = new RegistryRecord();
		record.setRecordStatus(statusProcessed);
		record.setAmount(document.getSumm());
		record.setServiceCode(document.getService().registryCode());
		record.setPersonalAccountExt(document.getDebtorId());
		record.setOperationDate(document.getOperation().getCreationDate());
		record.setRegistry(registry);
		record.setLastName(document.getLastName());
		record.setMiddleName(document.getMiddleName());
		record.setFirstName(document.getFirstName());
		record.setCity(document.getTown());
		record.setStreetType(document.getStreetType());
		record.setStreetName(document.getStreetName());
		record.setBuildingNum(document.getBuildingNumber());
		record.setBuildingBulkNum(document.getBuildingBulk());
		record.setApartmentNum(document.getApartmentNumber());
		record.setUniqueOperationNumber(document.getOperation().getId());
		record.setProperties(propertiesFactory.newRecordProperties());
		buildRecordContainers(document, record);

		return record;
	}

	@NotNull
	private RegistryRecordStatus getRecordStatusProcessed() {

		return registryRecordStatusService.findByCode(RegistryRecordStatus.PROCESSED);
	}

	private void buildRecordContainers(Document document, RegistryRecord record)
			throws FlexPayException {

		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setData("50:" + document.getCreditorOrganization().getId());
		record.addContainer(container);

		// add payment point container
		record.addContainer(new RegistryRecordContainer(
				"500:" + document.getOperation().getPaymentPoint().getId()));

		// add external organization account number if available
		DocumentAddition ercAccountAddition = getErcAccountAddition(document);
		if (ercAccountAddition != null) {
			record.addContainer(new RegistryRecordContainer(
					"15:" +
					"01011900:" +
					":" + ercAccountAddition.getStringValue() +
					":" + ApplicationConfig.getMbOrganizationStub().getId()));
		}
	}

	private DocumentAddition getErcAccountAddition(Document document) throws FlexPayException {

		DocumentAdditionType documentErcType = getErcAccountDocAdditionType();
		if (documentErcType == null) {
			log.warn("No ERC account document addition type found");
			return null;
		}

		for (DocumentAddition addition : document.getAdditions()) {
			if (documentErcType.equals(addition.getAdditionType())) {
				return addition;
			}
		}

		return null;
	}

	private DocumentAdditionType getErcAccountDocAdditionType() throws FlexPayException {

		return documentAdditionTypeService.findTypeByCode(DocumentAdditionType.CODE_ERC_ACCOUNT);
	}

	@NotNull
	private List<Document> getDocuments(ServiceProvider provider, Organization registerer, DateRange range) {

		return documentService.listRegisteredPaymentDocuments(
				stub(provider), stub(registerer), range);
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	@Required
	public void setRegistryTypeService(RegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

	@Required
	public void setRegistryStatusService(RegistryStatusService registryStatusService) {
		this.registryStatusService = registryStatusService;
	}

	@Required
	public void setRegistryArchiveStatusService(RegistryArchiveStatusService registryArchiveStatusService) {
		this.registryArchiveStatusService = registryArchiveStatusService;
	}

	@Required
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	@Required
	public void setRegistryRecordStatusService(RegistryRecordStatusService registryRecordStatusService) {
		this.registryRecordStatusService = registryRecordStatusService;
	}

	@Required
	public void setPropertiesFactory(PropertiesFactory propertiesFactory) {
		this.propertiesFactory = propertiesFactory;
	}

	@Required
	public void setDocumentAdditionTypeService(DocumentAdditionTypeService documentAdditionTypeService) {
		this.documentAdditionTypeService = documentAdditionTypeService;
	}

	@Required
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}

	@Required
	public void setOrganizationHistoryGenerator(OrganizationHistoryGenerator organizationHistoryGenerator) {
		this.organizationHistoryGenerator = organizationHistoryGenerator;
	}

	@Required
	public void setPaymentPointHistoryGenerator(PaymentPointHistoryGenerator paymentPointHistoryGenerator) {
		this.paymentPointHistoryGenerator = paymentPointHistoryGenerator;
	}

	@Required
	public void setServiceHistoryGenerator(ServiceHistoryGenerator serviceHistoryGenerator) {
		this.serviceHistoryGenerator = serviceHistoryGenerator;
	}
}
