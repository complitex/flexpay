package org.flexpay.payments.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.BigDecimalUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.quittance.*;
import org.flexpay.payments.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.stripToEmpty;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.BigDecimalUtil.isNotZero;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.SecurityUtil.getUserName;
import static org.flexpay.payments.actions.request.data.DebtsRequest.SEARCH_QUITTANCE_DEBT_REQUEST;
import static org.flexpay.payments.persistence.quittance.InfoRequest.serviceProviderAccountNumberRequest;

public class QuittancePayerImpl implements QuittancePayer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private QuittanceDetailsFinder quittanceDetailsFinder;
    private OperationService operationService;
    private CashboxService cashboxService;
    private DocumentTypeService documentTypeService;
    private DocumentStatusService documentStatusService;
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

    @Override
    public PayInfoResponse quittancePay(PayRequest payRequest) throws FlexPayException {

        Security.authenticatePayer();

        //TODO: Откуда брать айдишник кассы?
        Cashbox cashbox = new Cashbox();
        cashbox.setId(1L);
        Operation oper = operationService.createBlankOperation(SecurityUtil.getUserName(), stub(cashbox));
        log.debug("Created blank operation: {}", oper);

        log.debug("Start filling operation");

        try {
            fillOperation(payRequest, oper);
        } catch (Exception e) {
            log.error("Error with filling operation", e);
            throw new FlexPayException("Error with filling operation", e);
        }

        log.debug("Finish filling operation");

        if (isNotEmptyOperation(oper)) {
            if (oper.isNew()) {
                operationService.create(oper);
            } else {
                operationService.update(oper);
            }
        } else {
            log.debug("Zero summ for operation or zero documents for operation created. Operation was not created");
        }

        log.debug("Operation created");

        PayInfoResponse response = new PayInfoResponse();

        response.setOperationId(oper.getId());
        response.setStatusCode(PayInfoResponse.STATUS_SUCCESS);

        for (Document document : oper.getDocuments()) {

            ServicePayInfo servicePayInfo = new ServicePayInfo();
            servicePayInfo.setServiceId(document.getServiceStub().getId());
            servicePayInfo.setDocumentId(document.getId());
            servicePayInfo.setServiceStatusCode(ServicePayInfo.STATUS_SUCCESS);

            response.addServicePayInfo(servicePayInfo);

        }

        return response;
    }

    private boolean isNotEmptyOperation(Operation operation) {
        return !BigDecimalUtil.isZero(operation.getOperationSumm()) && operation.getDocuments() != null && !operation.getDocuments().isEmpty();
    }

    @SuppressWarnings({"unchecked"})
    private PayInfoResponse fillOperation(PayRequest payRequest, Operation operation) throws Exception {

        PayInfoResponse response = new PayInfoResponse();

        Cashbox cashbox = cashboxService.read(operation.getCashboxStub());

        Organization organization = cashbox.getPaymentPoint().getCollector().getOrganization();
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
            InfoRequest infoRequest = serviceProviderAccountNumberRequest(spDetails.getServiceId() + ":" + spDetails.getServiceProviderAccount(), SEARCH_QUITTANCE_DEBT_REQUEST);

            QuittanceDetailsResponse quittanceDetailsResponse = quittanceDetailsFinder.findQuittance(infoRequest);
            if (quittanceDetailsResponse.getInfos().length == 0) {
                log.info("Cant't find quittances by serviceId and spAccountNumber ({}, {})", spDetails.getServiceId(), spDetails.getServiceProviderAccount());
                servicePayInfo.setServiceStatusCode(11);
                continue;
            }

            Document document = buildDocument(quittanceDetailsResponse.getInfos()[0], spDetails.getPaySum(), cashbox);

            if (isEmpty(operation.getAddress())) {
                operation.setAddress(document.getAddress());
                operation.setPayerFIO(stripToEmpty(document.getPayerFIO()));
            }

            if (isNotZero(document.getSumm())) {
                operation.addDocument(document);
            }
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    private Document buildDocument(QuittanceInfo info, BigDecimal paySum, Cashbox cashbox) throws Exception {

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
        document.setAddress(getApartmentAddress(info));
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

    public String getApartmentAddress(QuittanceInfo quittanceInfo) throws Exception {

        String apartmentMasterIndex = quittanceInfo.getApartmentMasterIndex();
        if (apartmentMasterIndex != null) {
            Stub<Apartment> stub = correctionsService.findCorrection(apartmentMasterIndex,
                    Apartment.class, masterIndexService.getMasterSourceDescriptionStub());
            return addressService.getAddress(stub, new Locale("ru"));
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
