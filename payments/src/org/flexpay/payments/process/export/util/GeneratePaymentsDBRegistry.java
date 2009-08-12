package org.flexpay.payments.process.export.util;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.DocumentAdditionType;
import org.flexpay.payments.persistence.DocumentAddition;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.DocumentAdditionTypeService;
import org.flexpay.payments.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class        GeneratePaymentsDBRegistry {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final SimpleDateFormat registryDateFormat = new SimpleDateFormat("ddMMyyy");

    private RegistryService registryService;
    private RegistryRecordService registryRecordService;
    private RegistryTypeService registryTypeService;
    private RegistryStatusService registryStatusService;
    private RegistryArchiveStatusService registryArchiveStatusService;
    private RegistryRecordStatusService registryRecordStatusService;
    private PropertiesFactory propertiesFactory;
	private OperationService operationService;
    private DocumentService documentService;
    private DocumentAdditionTypeService documentAdditionTypeService;

    @Nullable
    public Registry createDBRegestry(@NotNull FPFile spFile, @NotNull ServiceProvider serviceProvider, @NotNull Organization registerOrganization,
									 @NotNull Date fromDate, @NotNull Date tillDate) throws FlexPayException {

        log.info("Get document by service provider {}, registered {} organization", new Object[]{serviceProvider.getId(), registerOrganization.getId()});

        List<Document> documents = getDocuments(serviceProvider, registerOrganization, fromDate, tillDate);

        log.info("Count documents {}", documents.size());

        if (documents.size() == 0) {
            return null;
        }

        Registry registry = new Registry();

        registry.setRecipientCode(serviceProvider.getOrganization().getId());
        registry.setSenderCode(registerOrganization.getId());
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
        Long errorsNumber = 0L;
        RegistryRecordStatus statusProcessed = registryRecordStatusService.findByCode(RegistryRecordStatus.PROCESSED);
        DocumentAdditionType documentErcType = documentAdditionTypeService.findTypeByCode(DocumentAdditionType.CODE_ERC_ACCOUNT);
        for (Document document : documents) {
            if (document.getRegistryRecord() == null) {
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

                DocumentAddition ercAccountAddition = null;
                if (documentErcType != null) {
                    for (DocumentAddition documentAddition : document.getAdditions()) {
                        if (documentErcType.equals(documentAddition.getAdditionType())) {
                            ercAccountAddition = documentAddition;
                            break;
                        }
                    }
                }

                List<RegistryRecordContainer> containers = new ArrayList<RegistryRecordContainer>();

                RegistryRecordContainer container = new RegistryRecordContainer();
                container.setOrder(0);
                container.setData("50:" + document.getDebtorId());
                container.setRecord(record);
                containers.add(container);

                if (ercAccountAddition != null) {
                    container = new RegistryRecordContainer();
                    container.setOrder(1);
                    container.setData("15:" + registryDateFormat.format(new Date()) + ":" + ercAccountAddition.getStringValue() + ApplicationConfig.getMbOrganizationStub().getId());
                    container.setRecord(record);
                    containers.add(container);
                }

                record.setContainers(containers);

                try {
                    registryRecordService.create(record);
                    summ = summ.add(document.getSumm());
                    recordsNumber++;
                } catch (FlexPayException e) {
                    errorsNumber++;
                    log.error("Registry record for document {} did not create", String.valueOf(document.getId()), e);
                }
            }
        }
        registry.setAmount(summ);
        registry.setRecordsNumber(recordsNumber);
        registry.setErrorsNumber(errorsNumber.intValue());
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

    @Required
    public void setDocumentAdditionTypeService(DocumentAdditionTypeService documentAdditionTypeService) {
        this.documentAdditionTypeService = documentAdditionTypeService;
    }
}
