package org.flexpay.payments.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.BigDecimalUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.StringUtil;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.actions.request.data.request.*;
import org.flexpay.payments.actions.request.data.response.PayInfoResponse;
import org.flexpay.payments.actions.request.data.response.QuittanceDetailsResponse;
import org.flexpay.payments.actions.request.data.response.SimpleResponse;
import org.flexpay.payments.actions.request.data.response.Status;
import org.flexpay.payments.actions.request.data.response.data.*;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.process.export.TradingDay;
import org.flexpay.payments.service.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang.StringUtils.*;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.persistence.registry.RegistryContainer.*;
import static org.flexpay.common.util.BigDecimalUtil.isNotZero;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.SecurityUtil.getUserName;
import static org.flexpay.payments.actions.request.data.request.InfoRequest.serviceProviderAccountNumberRequest;

public class OuterRequestServiceImpl implements OuterRequestService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private QuittanceDetailsFinder quittanceDetailsFinder;
    private OperationService operationService;
    private CashboxService cashboxService;
    private ProcessManager processManager;
    private PaymentPointService paymentPointService;
    private DocumentTypeService documentTypeService;
    private DocumentStatusService documentStatusService;
    private DocumentService documentService;
    private DocumentAdditionTypeService documentAdditionTypeService;
    private OperationLevelService operationLevelService;
    private OperationStatusService operationStatusService;
    private OperationTypeService operationTypeService;
    private SPService spService;
    private ServiceProviderService serviceProviderService;
    private AddressService addressService;
    private CorrectionsService correctionsService;
    private MasterIndexService masterIndexService;
    private PersonService personService;
    private RegistryService registryService;

    @NotNull
    @Override
    public QuittanceDetailsResponse findQuittance(InfoRequest request) {
        return quittanceDetailsFinder.findQuittance(request);
    }

    @NotNull
    @Override
    public PayInfoResponse quittancePay(PayRequest payRequest) throws FlexPayException {
        Security.authenticateOuterRequest();

        //TODO: Откуда брать айдишник кассы?
        Cashbox cashbox = new Cashbox();
        cashbox.setId(1L);
        Operation oper = operationService.createBlankOperation(SecurityUtil.getUserName(), stub(cashbox));
        log.debug("Created blank operation: {}", oper);

        log.debug("Start filling operation");
        PayInfoResponse response;

        try {
            response = fillOperation(payRequest, oper);
        } catch (Exception e) {
            log.error("Error with filling operation", e);
            throw new FlexPayException("Error with filling operation", e);
        }

        log.debug("Finish filling operation");

        response.setRequestId(payRequest.getRequestId());

        if (isNotEmptyOperation(oper)) {
            if (oper.isNew()) {
                operationService.create(oper);
            } else {
                operationService.update(oper);
            }
            log.debug("Operation created");
        } else {
            log.debug("Zero sum for operation or zero documents for operation created. Operation was not created");
        }

        response.setOperationId(oper.getId());

        for (Document document : oper.getDocuments()) {
            for (ServicePayInfo info : response.getServicePayInfos()) {
                if (info.getServiceId().equals(document.getService().getId())) {
                    info.setDocumentId(document.getId());
                    break;
                }
            }
        }

        return response;
    }

    private boolean isNotEmptyOperation(Operation operation) {
        return !BigDecimalUtil.isZero(operation.getOperationSumm()) && operation.getDocuments() != null && !operation.getDocuments().isEmpty();
    }

    @SuppressWarnings({"unchecked"})
    private PayInfoResponse fillOperation(PayRequest payRequest, Operation operation) throws Exception {

        PayInfoResponse response = new PayInfoResponse();
        response.setStatus(Status.SUCCESS);

        Cashbox cashbox = cashboxService.read(operation.getCashboxStub());
        if (cashbox == null) {
            log.warn("Can't get cashbox with id {} from DB", operation.getCashboxStub().getId());
            throw new FlexPayException("Can't get cashbox with id " + operation.getCashboxStub().getId() + " from DB");
        }

        PaymentPoint paymentPoint = paymentPointService.read(cashbox.getPaymentPointStub());
        if (paymentPoint == null) {
            log.warn("Can't get payment point with id {} from DB", cashbox.getPaymentPointStub().getId());
            throw new FlexPayException("Can't get payment point with id " + cashbox.getPaymentPointStub().getId() + " from DB");
        }
        final Long paymentProcessId = paymentPoint.getTradingDayProcessInstanceId();

        if (paymentProcessId == null || paymentProcessId == 0) {
            log.debug("TradingDay process id not found for Payment point id = {}", paymentPoint.getId());
            response.setStatus(Status.REQUEST_IS_NOT_PROCESSED_TRADING_DAY_WAS_CLOSED);
            return response;
        } else {
            log.debug("Found process id {} for cashbox {}", paymentProcessId, cashbox.getId());

            if (!TradingDay.isOpened(processManager, paymentProcessId, log)) {
                response.setStatus(Status.REQUEST_IS_NOT_PROCESSED_TRADING_DAY_WAS_CLOSED);
                return response;
            }

        }

        Organization organization = paymentPoint.getCollector().getOrganization();
        operation.setOperationSumm(payRequest.getTotalToPay());
        operation.setOperationInputSumm(payRequest.getTotalToPay());
        operation.setChange(BigDecimal.ZERO);
        operation.setCreationDate(new Date());
        operation.setRegisterDate(new Date());
        operation.setCreatorOrganization(organization);
        operation.setPaymentPoint(cashbox.getPaymentPoint());
        operation.setCashbox(cashbox);
        operation.setRegisterOrganization(organization);
        operation.setOperationStatus(operationStatusService.read(OperationStatus.REGISTERED));
        operation.setOperationLevel(operationLevelService.read(OperationLevel.AVERAGE));
        operation.setOperationType(operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT));

        operation.setCreatorUserName(getUserName());
        operation.setRegisterUserName(getUserName());
        operation.setCashierFio(getUserName());

        for (ServicePayDetails spDetails : payRequest.getServicePayDetails()) {

            ServicePayInfo servicePayInfo = new ServicePayInfo();
            servicePayInfo.setServiceStatus(Status.SUCCESS);

            InfoRequest infoRequest = serviceProviderAccountNumberRequest(spDetails.getServiceId() + ":" + spDetails.getServiceProviderAccount(), RequestType.SEARCH_QUITTANCE_DEBT_REQUEST, payRequest.getLocale());
            log.debug("infoRequest = {}", infoRequest);
            QuittanceDetailsResponse quittanceDetailsResponse = findQuittance(infoRequest);
            if (quittanceDetailsResponse.getInfos() == null || quittanceDetailsResponse.getInfos().length == 0) {
                log.info("Cant't find quittances by serviceId and spAccountNumber ({}, {})", spDetails.getServiceId(), spDetails.getServiceProviderAccount());
                servicePayInfo.setServiceStatus(quittanceDetailsResponse.getStatus());
                servicePayInfo.setServiceId(spDetails.getServiceId());
                response.addServicePayInfo(servicePayInfo);
                response.setStatus(Status.REQUEST_IS_NOT_PROCESSED);
                continue;
            }

            Document document = buildDocument(quittanceDetailsResponse.getInfos()[0], spDetails.getPaySum(), cashbox, payRequest.getLocale());

            if (isEmpty(operation.getAddress())) {
                operation.setAddress(document.getAddress());
                operation.setPayerFIO(stripToEmpty(document.getPayerFIO()));
            }

            if (isNotZero(document.getSumm())) {
                operation.addDocument(document);
                servicePayInfo.setServiceId(document.getServiceStub().getId());
                response.addServicePayInfo(servicePayInfo);
            }
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    private Document buildDocument(QuittanceInfo info, BigDecimal paySum, Cashbox cashbox, Locale locale) throws Exception {

        ServiceDetails serviceDetails = info.getDetailses()[0];
        Service service = spService.readFull(new Stub<Service>(serviceDetails.getServiceId()));
        ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(service.getServiceProvider().getId()));
        Organization serviceProviderOrganization = serviceProvider.getOrganization();

        Document document = new Document();
        document.setSumm(paySum);
        document.setService(service);
        document.setDocumentStatus(documentStatusService.read(DocumentStatus.REGISTERED));
        document.setDocumentType(documentTypeService.read(DocumentType.CASH_PAYMENT));
        document.setDebtorOrganization(cashbox.getPaymentPoint().getCollector().getOrganization());
        document.setCreditorOrganization(serviceProviderOrganization);
        document.setDebt(serviceDetails.getAmount());
        document.setAddress(getApartmentAddress(info, locale));
        document.setPayerFIO(stripToEmpty(getPersonFio(info)));
        document.setDebtorId(info.getAccountNumber());
        document.setCreditorId(serviceDetails.getServiceProviderAccount());

        DocumentAddition ercAccountAddition = new DocumentAddition();
        ercAccountAddition.setAdditionType(documentAdditionTypeService.findTypeByCode(DocumentAdditionType.CODE_ERC_ACCOUNT));
        ercAccountAddition.setDocument(document);
        ercAccountAddition.setStringValue(getErcAccount(serviceDetails.getAttributes()));
        document.setAdditions(set(ercAccountAddition));

        setPayerName(info, document);

        return document;
    }

    public String getErcAccount(ServiceDetails.ServiceAttribute[] attributes) {

        for (ServiceDetails.ServiceAttribute attribute : attributes) {
            if (attribute.getName().equals(ConsumerAttributes.ATTR_ERC_ACCOUNT)) {
                return attribute.getValue();
            }
        }

        return null;
    }

    public String getPersonFio(QuittanceInfo quittanceInfo) {

        String personMasterIndex = quittanceInfo.getPersonMasterIndex();
        if (personMasterIndex != null) {
            Stub<Person> stub = correctionsService.findCorrection(personMasterIndex,
                    Person.class, masterIndexService.getMasterSourceDescriptionStub());
            Person person = personService.readFull(stub);
            return person.getFIO();
        } else {
            return quittanceInfo.getPersonFio();
        }
    }

    public String getApartmentAddress(QuittanceInfo quittanceInfo, Locale locale) throws Exception {

        String apartmentMasterIndex = quittanceInfo.getApartmentMasterIndex();
        if (apartmentMasterIndex != null) {
            Stub<Apartment> stub = correctionsService.findCorrection(apartmentMasterIndex,
                    Apartment.class, masterIndexService.getMasterSourceDescriptionStub());
            return addressService.getAddress(stub, locale);
        } else {
            return quittanceInfo.getAddress();
        }
    }

    private void setPayerName(QuittanceInfo info, Document document) {

        String[] pieces = getPersonFio(info).split(" ");
        List<String> tokens = list();
        int tSize = 0;
        for (String p : pieces) {
            if (StringUtils.isNotBlank(p)) {
                tokens.add(p);
                tSize++;
            }
        }

        if (tSize > 0) {
            document.setLastName(tokens.get(0));
            if (tSize > 1) {
                document.setFirstName(tokens.get(1));
                if (tSize > 2) {
                    StringBuilder middleNameBuilder = new StringBuilder();
                    for (int i = 2; i < tSize; i++) {
                        middleNameBuilder.append(tokens.get(i));
                        if (i < tSize - 1) {
                            middleNameBuilder.append(" ");
                        }
                    }
                    document.setMiddleName(middleNameBuilder.toString());
                }
            }
        }
    }

    @NotNull
    @Override
    public SimpleResponse refund(ReversalPayRequest request) throws FlexPayException {

        Security.authenticateOuterRequest();

        SimpleResponse response = new SimpleResponse();
        response.setRequestId(request.getRequestId());

        OperationStatus operationStatus = operationStatusService.read(OperationStatus.RETURNED);
        Stub<Operation> stub = new Stub<Operation>(request.getOperationId());
        Operation operation = operationService.read(stub);
        if (operation == null) {
            log.warn("Can't get operation with id {} from DB", stub.getId());
            response.setStatus(Status.INCORRECT_OPERATION_ID);
            return response;
        }

        if (!request.getTotalPaySum().equals(operation.getOperationSumm())) {
            log.warn("Request total pay sum not equals operation sum!");
            response.setStatus(Status.INCORRECT_PAY_SUM);
            return response;
        }

        operation.setOperationStatus(operationStatus);

        DocumentStatus documentStatus = documentStatusService.read(DocumentStatus.RETURNED);

        for (Document document : operation.getDocuments()) {
            document.setDocumentStatus(documentStatus);
            if (document.isNew()) {
                documentService.create(document);
            } else {
                documentService.update(document);
            }
        }

        operationService.update(operation);

        response.setStatus(Status.SUCCESS);

        return response;
    }

    @NotNull
    @Override
    public SimpleResponse addRegistryComment(RegistryCommentRequest request) throws FlexPayException {

        Security.authenticateOuterRequest();

        SimpleResponse response = new SimpleResponse();
        response.setRequestId(request.getRequestId());

        Stub<Registry> stub = new Stub<Registry>(request.getRegistryId());
        Registry registry = registryService.readWithContainers(stub);
        if (registry == null) {
            log.warn("Can't get registry with id {} from DB", stub.getId());
            response.setStatus(Status.REGISTRY_NOT_FOUND);
            return response;
        }

        List<RegistryContainer> containers = registry.getContainers();
        RegistryContainer commentaryContainer = null;
        for (RegistryContainer registryContainer : containers) {
            List<String> containerData = StringUtil.splitEscapable(
                    registryContainer.getData(), CONTAINER_DATA_DELIMITER, ESCAPE_SYMBOL);
            if (containerData != null && !containerData.isEmpty() && COMMENTARY_CONTAINER_TYPE.equals(containerData.get(0))) {
                commentaryContainer = registryContainer;
                break;
            }
        }

        SimpleDateFormat format = new SimpleDateFormat(RegistryContainer.COMMENTARY_PAYMENT_DATE_FORMAT);

        if (commentaryContainer == null) {
            commentaryContainer = new RegistryContainer();
            commentaryContainer.setRegistry(registry);
            containers.add(commentaryContainer);
        }

        if (isBlank(request.getOrderComment())) {
            commentaryContainer.setData("");
        } else {

            commentaryContainer.setData(COMMENTARY_CONTAINER_TYPE + CONTAINER_DATA_DELIMITER +
                    request.getOrderNumber() + CONTAINER_DATA_DELIMITER +
                    format.format(request.getOrderDate()) + CONTAINER_DATA_DELIMITER +
                    request.getOrderComment());

            if (commentaryContainer.getData().length() > CONTAINER_DATA_MAX_SIZE) {
                log.warn("Commentary length can't be more 256 symbols", stub.getId());
                response.setStatus(Status.INTERNAL_ERROR);
                return response;
            }
        }
        registryService.update(registry);

        response.setStatus(Status.SUCCESS);

        return response;
    }

    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Required
    public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
    }

    @Required
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Required
    public void setQuittanceDetailsFinder(QuittanceDetailsFinder quittanceDetailsFinder) {
        this.quittanceDetailsFinder = quittanceDetailsFinder;
    }

    @Required
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    @Required
    public void setCashboxService(CashboxService cashboxService) {
        this.cashboxService = cashboxService;
    }

    @Required
    public void setDocumentTypeService(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @Required
    public void setDocumentStatusService(DocumentStatusService documentStatusService) {
        this.documentStatusService = documentStatusService;
    }

    @Required
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Required
    public void setDocumentAdditionTypeService(DocumentAdditionTypeService documentAdditionTypeService) {
        this.documentAdditionTypeService = documentAdditionTypeService;
    }

    @Required
    public void setOperationLevelService(OperationLevelService operationLevelService) {
        this.operationLevelService = operationLevelService;
    }

    @Required
    public void setOperationStatusService(OperationStatusService operationStatusService) {
        this.operationStatusService = operationStatusService;
    }

    @Required
    public void setOperationTypeService(OperationTypeService operationTypeService) {
        this.operationTypeService = operationTypeService;
    }

    @Required
    public void setSpService(SPService spService) {
        this.spService = spService;
    }

    @Required
    public void setServiceProviderService(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @Required
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @Required
    public void setCorrectionsService(CorrectionsService correctionsService) {
        this.correctionsService = correctionsService;
    }

    @Required
    public void setMasterIndexService(MasterIndexService masterIndexService) {
        this.masterIndexService = masterIndexService;
    }

    @Required
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

}
