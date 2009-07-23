package org.flexpay.payments.util.registries;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.common.util.RegistryUtil;
import org.flexpay.common.util.StringUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class EndOperationDayRegistryGeneratorImpl implements EndOperationDayRegistryGenerator {

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

	@Transactional (propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	@Nullable
	public Registry generate(@NotNull PaymentPoint paymentPoint, @NotNull Organization organization,
							 @NotNull Date beginDate, @NotNull Date endDate) throws FlexPayException {

		log.info("Start generating end operation day registry...");

		List<Operation> operations = operationService.listReceivedPayments(paymentPoint, beginDate, endDate);
		log.debug("Found {} operations", operations.size());

		Registry registry = new Registry();

		registry.setCreationDate(new Date());
		registry.setSenderCode(paymentPoint.getCollector().getOrganizationStub().getId());
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

		Date minDate = null;
		Date maxDate = null;

		for (Operation operation : operations) {

			log.debug("Operation with id = {} processing...", operation.getId());

			if (minDate == null || operation.getCreationDate().getTime() < minDate.getTime()) {
				minDate = operation.getCreationDate();
			}
			if (maxDate == null || operation.getCreationDate().getTime() > maxDate.getTime()) {
				maxDate = operation.getCreationDate();
			}

			for (Document document : operation.getDocuments()) {

				log.debug("Document with id = {} processing...", document.getId());

				RegistryRecord record = new RegistryRecord();
				record.setRegistry(registry);
				record.setOperationDate(operation.getCreationDate());
				record.setRecordStatus(recordStatus);

				record.setServiceCode(String.valueOf(document.getService().getServiceType().getCode()));
				record.setPersonalAccountExt(document.getCreditorId());
				record.setUniqueOperationNumber(document.getId());

//					record.setLastName(StringUtils.stripToEmpty(document.getPayerFIO()));
				//todo: parse last, middle and first name
				record.setLastName(StringUtils.stripToEmpty(document.getLastName()));
				record.setMiddleName(StringUtils.stripToEmpty(document.getMiddleName()));
				record.setFirstName(StringUtils.stripToEmpty(document.getFirstName()));
				record.setCity(StringUtils.stripToEmpty(document.getTown()));
				record.setBuildingBulkNum(StringUtils.stripToEmpty(document.getBuildingBulk()));
				record.setStreetType(StringUtils.stripToEmpty(document.getStreetType()));
				record.setStreetName(StringUtils.stripToEmpty(document.getStreetName()));
				record.setBuildingNum(StringUtils.stripToEmpty(document.getBuildingNumber()));
				record.setApartmentNum(StringUtils.stripToEmpty(document.getApartmentNumber()));

				List<RegistryRecordContainer> containers = new ArrayList<RegistryRecordContainer>();

				RegistryRecordContainer container = new RegistryRecordContainer();
				BigDecimal summ = document.getSumm().setScale(2, BigDecimal.ROUND_HALF_UP);
				record.setAmount(summ);

				totalSumm = totalSumm.add(summ);
				container.setOrder(0);
				container.setData(RegistryUtil.BANK_PAYMENT_CONTAINER_CODE + RegistryUtil.CONTAINER_BODY_SEPARATOR +
								  StringUtil.getString(paymentPoint.getId()) + RegistryUtil.CONTAINER_BODY_SEPARATOR +
								  StringUtil.getString(operation.getId()) + RegistryUtil.CONTAINER_BODY_SEPARATOR +
								  StringUtil.getString(operation.getOperationSumm()));
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

		if (recordsNum == 0) {
			log.info("Finish generating end operation day registry...");
			log.info("0 records created. No Registry created.");
			return null;
		} else {
			registry.setFromDate(minDate == null ? new Date() : minDate);
			registry.setTillDate(maxDate == null ? new Date() : maxDate);
			registry.setRecordsNumber(recordsNum);
			registry.setAmount(totalSumm);
			registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATED));
			registry = registryService.update(registry);

			log.info("Finish generating end operation day registry...");
			log.info("Registry = {}", registry);

			return registry;
		}
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
