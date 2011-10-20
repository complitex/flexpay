package org.flexpay.payments.service.registry.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
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
import org.flexpay.payments.service.registry.RegistryContainerBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

/**
 * Generate the payments registry in database.
 */
public class PaymentsRegistryDBGeneratorImpl implements PaymentsRegistryDBGenerator {

	private final Logger log = LoggerFactory.getLogger(getClass());

	// required services
	private String moduleName;
	private FPFileService fileService;
	private RegistryService registryService;
	private RegistryRecordService registryRecordService;
	private RegistryTypeService registryTypeService;
	private RegistryStatusService registryStatusService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private RegistryRecordStatusService registryRecordStatusService;
	private PropertiesFactory propertiesFactory;
	private DocumentService documentService;
	private DocumentAdditionTypeService documentAdditionTypeService;
	private OrganizationHistoryGenerator organizationHistoryGenerator;
	private PaymentPointHistoryGenerator paymentPointHistoryGenerator;
	private ServiceHistoryGenerator serviceHistoryGenerator;

	private RegistryContainerBuilder registryContainerBuilder;

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
	@Transactional(readOnly = false)
	@Nullable
	public Registry createRegistry(@NotNull ServiceProvider serviceProvider,
								   @NotNull Organization registerOrganization,
								   @NotNull DateRange range) throws FlexPayException {

		log.info("Searching documents for service provider {} and registered in organization {}",
				new Object[]{serviceProvider.getId(), registerOrganization.getId()});

		List<Document> documents = getDocuments(serviceProvider, registerOrganization, range);

		if (documents.isEmpty()) {
			log.info("No documents were found. No registry will be created.");
			return null;
		}

		log.info("{} documents found", documents.size());

		Registry registry = new Registry();
		fillRegistryData(serviceProvider, registerOrganization, range, registry);
		registryService.create(registry);

		addRegistryRecords(range, documents, registry);
		registryService.update(registry);

		return registry;
	}

	private void addRegistryRecords(DateRange range, List<Document> documents, Registry registry) throws FlexPayException {

		BigDecimal totalSum = new BigDecimal(0);
		Long recordsNumber = 0L;
		Long errorsNumber = 0L;
		Date fromDate = range.getEnd();
		Date tillDate = range.getStart();

		for (Document document : documents) {

			if (document.getRegistryRecord() != null) {
				continue;
			}

			RegistryRecord record = buildRecord(registry, document);

			Date operationDate = record.getOperationDate();
			if (operationDate.before(fromDate)) {
				fromDate = operationDate;
			}

			if (operationDate.after(tillDate)) {
				tillDate = operationDate;
			}

			try {
				registryRecordService.create(record);
				totalSum = totalSum.add(document.getSum());
				recordsNumber++;
			} catch (FlexPayException e) {
				errorsNumber++;
				log.error("Registry record for document " + String.valueOf(document.getId()) + " was not created", e);
			}
		}

		updateRegistry(registry, fromDate, tillDate, totalSum, recordsNumber, errorsNumber);
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATED));

		log.info("Created db registry: id = {}, recordsNumber = {}, amount = {}",
				new Object[]{registry.getId(), recordsNumber, totalSum});
	}

	private void fillRegistryData(@NotNull ServiceProvider provider, @NotNull Organization registerer,
								  @NotNull DateRange dateRange, @NotNull Registry registry) {

		registry.setRecipientCode(provider.getOrganization().getId());
		registry.setSenderCode(registerer.getId());
		registry.setCreationDate(new Date());
		registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_CASH_PAYMENTS));
		registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATING));
		registry.setModule(fileService.getModuleByName(moduleName));
		registry.setFromDate(dateRange.getStart());
		registry.setTillDate(dateRange.getEnd());

		EircRegistryProperties registryProperties = (EircRegistryProperties) propertiesFactory.newRegistryProperties();
		registryProperties.setRecipient(provider.getOrganization());
		registryProperties.setSender(registerer);
		registryProperties.setServiceProvider(provider);
		registryProperties.setRegistry(registry);
		registry.setProperties(registryProperties);

		// TODO: remove this dirty hack
		organizationHistoryGenerator.generateFor(registerer);
		organizationHistoryGenerator.generateFor(provider.getOrganization());

		addContainers(provider, registerer, dateRange, registry);
	}

	private void addContainers(ServiceProvider provider, Organization registerer, DateRange range, Registry registry) {

		// add instance id
		registry.addContainer(registryContainerBuilder.getInstanceIdContainer());

		// add identifiers sync containers
		registry.addContainer(registryContainerBuilder.getRegisterOrganizationSyncContainer(registerer));
		registry.addContainer(registryContainerBuilder.getProviderOrganizationSyncContainer(provider));

		// add identifiers sync containers of used payment points
		List<PaymentPoint> paymentPoints = documentService.listPaymentsPoints(stub(provider), stub(registerer), range);
		log.debug("Payment points is using {} time", paymentPoints.size());
		for (PaymentPoint paymentPoint : paymentPoints) {

			// TODO: remove this dirty hack
			paymentPointHistoryGenerator.generateFor(paymentPoint);

			registry.addContainer(registryContainerBuilder.getPaymentPointSyncContainer(paymentPoint));
		}

		// add identifiers sync containers of used services
		List<Service> services = documentService.listPaymentsServices(stub(provider), stub(registerer), range);
		log.debug("Services is used {} time(s)", services.size());
		for (Service service : services) {

			// TODO: remove this dirty hack
			serviceHistoryGenerator.generateFor(service);

			registry.addContainer(registryContainerBuilder.getServiceSyncContainer(service));
		}
	}

	private void updateRegistry(Registry registry, Date fromDate, Date tillDate, BigDecimal totalSum, Long recordsNumber, Long errorsNumber) {

		registry.setFromDate(fromDate);
		registry.setTillDate(tillDate);
		registry.setAmount(totalSum);
		registry.setRecordsNumber(recordsNumber);
		registry.setErrorsNumber(errorsNumber.intValue());
	}
	
	private RegistryRecord buildRecord(Registry registry, Document document) throws FlexPayException {

		RegistryRecordStatus statusProcessed = registryRecordStatusService.findByCode(RegistryRecordStatus.PROCESSED);

		RegistryRecord record = new RegistryRecord();
		record.setRecordStatus(statusProcessed);
		record.setAmount(document.getSum());
		record.setServiceCode(document.getService().registryCode());
		record.setPersonalAccountExt(document.getCreditorId());
		record.setOperationDate(document.getOperation().getCreationDate());
		record.setRegistry(registry);
		record.setLastName(document.getLastName());
		record.setMiddleName(document.getMiddleName());
		record.setFirstName(document.getFirstName());
		record.setTownName(document.getTownName());
		record.setStreetType(document.getStreetType());
		record.setStreetName(document.getStreetName());
		record.setBuildingNum(document.getBuildingNumber());
		record.setBuildingBulkNum(document.getBuildingBulk());
		record.setApartmentNum(document.getApartmentNumber());
		record.setUniqueOperationNumber(document.getId());
		record.setProperties(propertiesFactory.newRecordProperties());

		addRecordContainers(record, document);

		return record;
	}

	private void addRecordContainers(RegistryRecord record, Document document) throws FlexPayException {

		// add payment container
		record.addContainer(registryContainerBuilder.getSimplePaymentContainer(document));

		// add payment point container
		record.addContainer(registryContainerBuilder.getPaymentPointIdContainer(document));

		// add external organization account number if available
		DocumentAddition ercAccountAddition = getErcAccountAddition(document);
		if (ercAccountAddition != null) {
			record.addContainer(registryContainerBuilder.getExternalOrganizationAccountContainer(ercAccountAddition));
		}
	}
	
	private DocumentAddition getErcAccountAddition(Document document) throws FlexPayException {

		DocumentAdditionType documentErcType = documentAdditionTypeService.findTypeByCode(DocumentAdditionType.CODE_ERC_ACCOUNT);
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

	@NotNull
	private List<Document> getDocuments(ServiceProvider provider, Organization registerer, DateRange range) {

		return documentService.listRegisteredPaymentDocuments(stub(provider), stub(registerer), range);
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

	@Required
	public void setRegistryContainerBuilder(RegistryContainerBuilder registryContainerBuilder) {
		this.registryContainerBuilder = registryContainerBuilder;
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Required
	public void setFileService(FPFileService fileService) {
		this.fileService = fileService;
	}
}
