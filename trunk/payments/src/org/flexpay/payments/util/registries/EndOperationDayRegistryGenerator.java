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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EndOperationDayRegistryGenerator {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private PaymentPointService paymentPointService;
	private OrganizationService organizationService;
	private DocumentService documentService;
	private OperationService operationService;
	private RegistryRecordService registryRecordService;
	private RegistryRecordStatusService registryRecordStatusService;
	private RegistryService registryService;
	private RegistryTypeService registryTypeService;
	private RegistryStatusService registryStatusService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private PropertiesFactory propertiesFactory;

	public Registry generate(@NotNull Stub<PaymentPoint> pointStub, @NotNull Stub<Organization> orgStub,
							 @NotNull Date beginDate, @NotNull Date endDate) throws FlexPayException {

		PaymentPoint paymentPoint = paymentPointService.read(pointStub);
		if (paymentPoint == null) {
			log.error("Payment point with id - {} does not exist", pointStub.getId());
			return null;
		}
		Organization organization = organizationService.readFull(orgStub);
		if (organization == null) {
			log.error("Organization with id - {} does not exist", orgStub.getId());
			return null;
		}

		List<Operation> operations = operationService.listPaymentOperations(beginDate, endDate);

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

			for (Document document : operation.getDocuments()) {

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

				recordsNum++;
			}

		}

		registry.setRecordsNumber(recordsNum);
		registry.setAmount(totalSumm);
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATED));
		registry = registryService.update(registry);

		return registry;

	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
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
