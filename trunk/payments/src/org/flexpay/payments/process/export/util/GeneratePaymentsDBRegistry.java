package org.flexpay.payments.process.export.util;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.DocumentService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneratePaymentsDBRegistry {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private RegistryService registryService;
    private RegistryRecordService registryRecordService;
    private RegistryTypeService registryTypeService;
    private RegistryStatusService registryStatusService;
    private RegistryArchiveStatusService registryArchiveStatusService;
    private RegistryRecordStatusService registryRecordStatusService;
    private PropertiesFactory propertiesFactory;
	private OperationService operationService;
    private DocumentService documentService;

    @NotNull
    public Registry createDBRegestry(@NotNull FPFile spFile, @NotNull ServiceProvider serviceProvider, @NotNull Organization registerOrganization,
									 @NotNull Date fromDate, @NotNull Date tillDate) throws FlexPayException {

        log.info("Get document by service provider {}, registered {} organization", new Object[]{serviceProvider.getId(), registerOrganization.getId()});

        List<Document> documents = getDocuments(serviceProvider, registerOrganization, fromDate, tillDate);

        log.info("Count documents {}", documents.size());

        Registry registry = new Registry();

        registry.setCreationDate(new Date());
        registry.setSpFile(spFile);
        registry.setRegistryType(registryTypeService.findByCode(getPaymentsType()));
        registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
        registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATING));
        registry.setFromDate(fromDate);
        registry.setTillDate(tillDate);
        registry.setProperties(propertiesFactory.newRegistryProperties());
        registryService.create(registry);

        BigDecimal summ = new BigDecimal(0);
        Long recordsNumber = 0L;
        for (Document document : documents) {
            if (document.getRegistryRecord() == null) {
                RegistryRecord record = new RegistryRecord();
                RegistryRecordStatus status = registryRecordStatusService.findByCode(RegistryRecordStatus.PROCESSED);
                record.setRecordStatus(status);
                record.setAmount(document.getSumm());
                record.setServiceCode("#" + document.getService().getExternalCode());
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

                List<RegistryRecordContainer> containers = new ArrayList<RegistryRecordContainer>();

                RegistryRecordContainer container = new RegistryRecordContainer();
                container.setOrder(0);
                container.setData("50:" + document.getDebtorId());
                container.setRecord(record);
                containers.add(container);

                record.setContainers(containers);

                try {
                    registryRecordService.create(record);
                } catch (FlexPayException e) {
                    registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATING_CANCELED));
                }

                summ = summ.add(document.getSumm());
                recordsNumber++;
            }
        }
        registry.setAmount(summ);
        registry.setRecordsNumber(recordsNumber);
        registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATED));
        registryService.update(registry);

		log.info("Created db registry: id = {}, recordsNumber = {}, amount = {}", new Object[] {registry.getId(), recordsNumber, summ});

        return registry;

    }
    /*
    @NotNull
    private List<Operation> getOperations(@NotNull Organization organization,
                                          @NotNull Date startDate,
                                          @NotNull Date endDate) {

        return operationService.listReceivedPayments(organization, startDate, endDate);
    }
    */

    @NotNull
    private List<Document> getDocuments(@NotNull ServiceProvider serviceProvider,
                                        @NotNull Organization registerdOrganization,
                                        @NotNull Date startDate,
                                        @NotNull Date endDate) {
        return documentService.listRegisteredPaymentDocuments(serviceProvider, registerdOrganization, startDate, endDate);
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
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
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

}
