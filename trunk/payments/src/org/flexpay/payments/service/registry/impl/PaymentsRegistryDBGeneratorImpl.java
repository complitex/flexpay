package org.flexpay.payments.service.registry.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.DocumentAddition;
import org.flexpay.payments.persistence.DocumentAdditionType;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.service.DocumentAdditionTypeService;
import org.flexpay.payments.service.DocumentService;
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

	@Nullable
	public Registry createDBRegistry(@NotNull FPFile spFile, @NotNull ServiceProvider serviceProvider, @NotNull Organization registerOrganization,
									 @NotNull Date fromDate, @NotNull Date tillDate) throws FlexPayException {

		log.info("Searching documents for service provider {} and registered in organization {}",
				new Object[]{serviceProvider.getId(), registerOrganization.getId()});

		List<Document> documents = getDocuments(serviceProvider, registerOrganization, fromDate, tillDate);

		if (documents.size() == 0) {
			log.info("No documents were found. No registry will be created.");
			return null;
		}

		log.info("{} documents found", documents.size());

		Registry registry = new Registry();
		fillRegistryData(spFile, serviceProvider, registerOrganization, fromDate, tillDate, registry);
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

	private void fillRegistryData(FPFile spFile, ServiceProvider serviceProvider, Organization registerOrganization,
								  Date fromDate, Date tillDate, Registry registry) {

		registry.setRecipientCode(serviceProvider.getOrganization().getId());
		registry.setSenderCode(registerOrganization.getId());
		registry.setCreationDate(new Date());
		registry.setSpFile(spFile);
		registry.setRegistryType(registryTypeService.findByCode(getPaymentsType()));
		registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATING));
		registry.setFromDate(fromDate);
		registry.setTillDate(tillDate);

		EircRegistryProperties registryProperties = (EircRegistryProperties) propertiesFactory.newRegistryProperties();
		registryProperties.setRecipient(serviceProvider.getOrganization());
		registryProperties.setSender(registerOrganization);
		registryProperties.setServiceProvider(serviceProvider);
		registry.setProperties(registryProperties);
	}

	private RegistryRecord buildRecord(Registry registry, Document document) throws FlexPayException {

		RegistryRecordStatus statusProcessed = getRecordStatusProcessed();

		RegistryRecord record = new RegistryRecord();
		record.setRecordStatus(statusProcessed);
		record.setAmount(document.getSumm());
		record.setServiceCode("#" + document.getService().getServiceType().getCode());
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
		container.setData("52:" + document.getCreditorOrganization().getId() +
						  ":" + document.getOperation().getId() +
						  ":" + document.getOperation().getOperationSumm());
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

		for (DocumentAddition documentAddition : document.getAdditions()) {
			if (documentErcType.equals(documentAddition.getAdditionType())) {
				return documentAddition;
			}
		}

		return null;
	}

	private DocumentAdditionType getErcAccountDocAdditionType() throws FlexPayException {

		return documentAdditionTypeService.findTypeByCode(DocumentAdditionType.CODE_ERC_ACCOUNT);
	}

	@NotNull
	private List<Document> getDocuments(@NotNull ServiceProvider serviceProvider,
										@NotNull Organization registeredOrganization,
										@NotNull Date startDate,
										@NotNull Date endDate) {

		return documentService.listRegisteredPaymentDocuments(serviceProvider, registeredOrganization, startDate, endDate);
	}

	@NotNull
	private Integer getPaymentsType() {
		return RegistryType.TYPE_CASH_PAYMENTS;
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
}
