package org.flexpay.payments.util.registries;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public class EndOperationDayRegistryGenerator {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DocumentService documentService;
	private OperationService operationService;
	private RegistryRecordService registryRecordService;
	private RegistryRecordStatusService registryRecordStatusService;
	private RegistryService registryService;
	private RegistryTypeService registryTypeService;
	private RegistryStatusService registryStatusService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private PropertiesFactory propertiesFactory;

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	public Registry generate(@NotNull PaymentPoint paymentPoint, @NotNull Organization organization,
							 @NotNull Date beginDate, @NotNull Date endDate) throws FlexPayException {

		log.info("Start generating end operation day registry...");

		List<Operation> operations = operationService.listPaymentOperations(beginDate, endDate);
		log.debug("Found {} operations", operations.size());

		Registry registry = new Registry();

		registry.setFromDate(beginDate);
		registry.setTillDate(endDate);
		registry.setCreationDate(new Date());
		registry.setSenderCode(paymentPoint.getId());
		registry.setRecipientCode(organization.getId());
		registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_BANK_PAYMENTS));
		registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATING));

		RegistryProperties registryProperties = propertiesFactory.newRegistryProperties();
		registryProperties.setRegistry(registry);
		registry.setProperties(registryProperties);

		registry = registryService.create(registry);

		BigDecimal totalSumm = BigDecimal.ZERO;

		RegistryRecordStatus recordStatus = registryRecordStatusService.findByCode(RegistryRecordStatus.PROCESSED);

		long recordsNum = 0;

		for (Operation operation : operations) {

			log.debug("Operation with id = {} processing...", operation.getId());

			for (Document document : operation.getDocuments()) {

				log.debug("Document with id = {} processing...", document.getId());

				RegistryRecord record = new RegistryRecord();
				record.setRegistry(registry);
				record.setOperationDate(operation.getCreationDate());
				record.setRecordStatus(recordStatus);

				record.setServiceCode("#" + document.getService().getExternalCode());
				record.setPersonalAccountExt(document.getDebtorId());
				record.setUniqueOperationNumber(operation.getId());

				record.setLastName(document.getLastName());
				record.setMiddleName(document.getMiddleName());
				record.setFirstName(document.getFirstName());
				record.setCity(document.getTown());
				record.setBuildingBulkNum(document.getBuildingBulk());
				record.setStreetType(document.getStreetType());
				record.setStreetName(document.getStreetName());
				record.setBuildingNum(document.getBuildingNumber());
				record.setApartmentNum(document.getApartmentNumber());

				List<RegistryRecordContainer> containers = new ArrayList<RegistryRecordContainer>();

				RegistryRecordContainer container = new RegistryRecordContainer();
				BigDecimal summ = document.getSumm().setScale(2, BigDecimal.ROUND_HALF_UP);
				totalSumm.add(summ);
				container.setOrder(0);
				container.setData("52:" + operation.getCreatorOrganization().getId() + ":" + operation.getId() + ":" + operation.getOperationSumm());
				container.setRecord(record);
				containers.add(container);

				record.setContainers(containers);

				RegistryRecordProperties recordProperties = propertiesFactory.newRecordProperties();
				recordProperties.setRecord(record);
				record.setProperties(recordProperties);
				record = registryRecordService.create(record);

				log.debug("Record created ({})", record);

				recordsNum++;
			}

		}

		registry.setRecordsNumber(recordsNum);
		registry.setAmount(totalSumm);
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATED));
		registry = registryService.update(registry);

		log.info("Finish generating end operation day registry...");
		log.info("Registry = {}", registry);

		return registry;

	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	@Required
	public void setRegistryRecordStatusService(RegistryRecordStatusService registryRecordStatusService) {
		this.registryRecordStatusService = registryRecordStatusService;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
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
	public void setPropertiesFactory(PropertiesFactory propertiesFactory) {
		this.propertiesFactory = propertiesFactory;
	}

}
