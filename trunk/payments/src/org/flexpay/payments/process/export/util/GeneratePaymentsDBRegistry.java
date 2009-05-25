package org.flexpay.payments.process.export.util;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @NotNull
    public Registry createDBRegestry(@NotNull FPFile spFile, @NotNull Organization organization, @NotNull Date fromDate, @NotNull Date tillDate) throws FlexPayException {
        log.info("Get operation by organization " + organization.getId());
        List<Operation> operations = getOperations(organization, fromDate, tillDate);
        log.info("Count operations " + operations.size());

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
        for (Operation operation : operations) {
            for (Document document : operation.getDocuments()) {
                if (document.getRegistryRecord() == null) {
                    RegistryRecord record = new RegistryRecord();
                    RegistryRecordStatus status = registryRecordStatusService.findByCode(RegistryRecordStatus.PROCESSED);
                    record.setRecordStatus(status);
                    record.setAmount(document.getSumm());
                    record.setServiceCode("#" + document.getService().getExternalCode());
                    record.setPersonalAccountExt(document.getDebtorId());
                    record.setOperationDate(operation.getCreationDate());
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
        }
        registry.setAmount(summ);
        registry.setRecordsNumber(recordsNumber);
        registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATED));
        registryService.update(registry);
        log.info("Created db registry: id =" + registry.getId() + ", recordsNumber=" + recordsNumber + ", amount=" + summ);

        return registry;

    }

    @NotNull
    private List<Operation> getOperations(@NotNull Organization organization,
                                          @NotNull Date startDate,
                                          @NotNull Date endDate) {
        return operationService.listReceivedPayments(organization, startDate, endDate);
    }

    @NotNull
    private Integer getPaymentsType() {
        return RegistryType.TYPE_CASH_PAYMENTS;
    }

    private OperationService operationService;

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    public void setRegistryRecordService(RegistryRecordService registryRecordService) {
        this.registryRecordService = registryRecordService;
    }

    public void setRegistryTypeService(RegistryTypeService registryTypeService) {
        this.registryTypeService = registryTypeService;
    }

    public void setRegistryStatusService(RegistryStatusService registryStatusService) {
        this.registryStatusService = registryStatusService;
    }

    public void setRegistryArchiveStatusService(RegistryArchiveStatusService registryArchiveStatusService) {
        this.registryArchiveStatusService = registryArchiveStatusService;
    }

    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    public void setRegistryRecordStatusService(RegistryRecordStatusService registryRecordStatusService) {
        this.registryRecordStatusService = registryRecordStatusService;
    }

    public void setPropertiesFactory(PropertiesFactory propertiesFactory) {
        this.propertiesFactory = propertiesFactory;
    }
}
