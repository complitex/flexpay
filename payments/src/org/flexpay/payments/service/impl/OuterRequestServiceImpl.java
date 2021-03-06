package org.flexpay.payments.service.impl;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.BigDecimalUtil;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.StringUtil;
import org.flexpay.orgs.persistence.*;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.action.outerrequest.request.*;
import org.flexpay.payments.action.outerrequest.request.data.ServicePayDetails;
import org.flexpay.payments.action.outerrequest.request.response.*;
import org.flexpay.payments.action.outerrequest.request.response.data.*;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.service.*;
import org.flexpay.payments.service.Roles;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang.StringUtils.*;
import static org.flexpay.ab.util.TranslationUtil.getBuildingNumberWithoutHouseType;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultCountryStub;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultRegionStub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.persistence.registry.RegistryContainer.*;
import static org.flexpay.common.util.BigDecimalUtil.isNotZero;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.TranslationUtil.getTranslation;

public class OuterRequestServiceImpl implements OuterRequestService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private CountryService countryService;
    private BuildingService buildingService;
    private StreetService streetService;
    private TownService townService;
    private RegionService regionService;
    private QuittanceDetailsFinder quittanceDetailsFinder;
    private OperationService operationService;
    private CashboxService cashboxService;
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
    private ApartmentService apartmentService;
    private AddressService addressService;
    private CorrectionsService correctionsService;
    private MasterIndexService masterIndexService;
    private PersonService personService;
    private RegistryService registryService;
    private UserPreferencesService userPreferencesService;
	private TradingDay<Cashbox> cashboxTradingDayService;

    @NotNull
    @Override
    public GetDebtInfoResponse getDeftInfo(GetDebtInfoRequest request) {
        request.copyResponse((GetDebtInfoResponse) quittanceDetailsFinder.findQuittance(request));
        return request.getResponse();
    }

    @NotNull
    @Override
    public GetQuittanceDebtInfoResponse getQuittanceDeftInfo(GetQuittanceDebtInfoRequest request) {
        request.copyResponse((GetQuittanceDebtInfoResponse) quittanceDetailsFinder.findQuittance(request));
        return request.getResponse();
    }

    @NotNull
    @Override
    public PayDebtResponse quittancePay(PayDebtRequest request) throws FlexPayException {

// unused code?
//        Security.authenticateOuterRequest(request.getLogin());

        Cashbox cashbox = new Cashbox(request.getPaymentsUserPreferences().getCashboxId());
        Operation oper = operationService.createBlankOperation(request.getPaymentsUserPreferences().getFullName(), stub(cashbox));
        log.debug("Created blank operation: {}", oper);

        log.debug("Start filling operation");
        PayDebtResponse response;

        try {
            response = fillOperation(request, oper);
        } catch (Exception e) {
            log.error("Error with filling operation", e);
            operationService.delete(stub(oper));
            throw new FlexPayException("Error with filling operation", e);
        }

        log.debug("Finish filling operation");

        if (isNotEmptyOperation(oper)) {
            if (oper.isNew()) {
                operationService.create(oper);
            } else {
                operationService.update(oper);
            }
            log.debug("Operation created");
        } else if (response.getStatus() != null) {
            log.debug("Response: {}", response);
            log.debug("Zero sum for operation or zero documents for operation created. Operation was not created");
            operationService.delete(stub(oper));
            if (response.getStatus() == null || response.getStatus().equals(Status.SUCCESS)) {
                response.setStatus(Status.INCORRECT_PAY_SUM);
            }
            return response;
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
        return !BigDecimalUtil.isZero(operation.getOperationSum()) && operation.getDocuments() != null && !operation.getDocuments().isEmpty();
    }

    @SuppressWarnings({"unchecked"})
    private PayDebtResponse fillOperation(PayDebtRequest request, Operation operation) throws Exception {

        PayDebtResponse response = request.getResponse();

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
        final Long cashboxProcessId = cashbox.getTradingDayProcessInstanceId();

        if (cashboxProcessId == null || cashboxProcessId == 0) {
            log.debug("TradingDaySchedulingJob process id not found for cashbox id = {}", cashbox.getId());
            response.setStatus(Status.REQUEST_IS_NOT_PROCESSED_TRADING_DAY_WAS_CLOSED);
            return response;
        } else {
            log.debug("Found process id {} for cashbox {}", cashboxProcessId, cashbox.getId());

            if (!cashboxTradingDayService.isOpened(cashboxProcessId)) {
                response.setStatus(Status.REQUEST_IS_NOT_PROCESSED_TRADING_DAY_WAS_CLOSED);
                return response;
            }

        }

        Organization organization = paymentPoint.getCollector().getOrganization();
        operation.setOperationSum(request.getTotalPaySum());
        operation.setOperationInputSum(request.getTotalPaySum());
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

        operation.setCreatorUserName(request.getPaymentsUserPreferences().getFullName());
        operation.setRegisterUserName(request.getPaymentsUserPreferences().getFullName());
        operation.setCashierFio(request.getPaymentsUserPreferences().getFullName());

        for (ServicePayDetails spDetails : request.getServicePayDetails()) {

            ServicePayInfo servicePayInfo = new ServicePayInfo();
            servicePayInfo.setServiceStatus(Status.SUCCESS);

            GetQuittanceDebtInfoRequest searchRequest = new GetQuittanceDebtInfoRequest();
            searchRequest.setSearchCriteria(spDetails.getServiceId() + ":" + spDetails.getServiceProviderAccount());
            searchRequest.setSearchType(SearchRequest.TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER);
            searchRequest.setLocale(request.getLocale());
            log.debug("searchRequest = {}", searchRequest);
            getQuittanceDeftInfo(searchRequest);
            GetQuittanceDebtInfoResponse quittanceDetailsResponse = searchRequest.getResponse();
            QuittanceInfo info = null;
            ServiceDetails serviceDetails = null;
            log.debug("getInfos = {}", quittanceDetailsResponse.getInfos());
            if ( quittanceDetailsResponse.getInfos() != null ) {
                log.debug("not null!");
                for(QuittanceInfo aInfo : quittanceDetailsResponse.getInfos()) {
                    for (ServiceDetails sDetails : aInfo.getServiceDetailses() ) {
                        log.debug("sId1 = '{}'", sDetails.getServiceId());
                        log.debug("sId2 = '{}'", spDetails.getServiceId());
                        log.debug("equals = {}", sDetails.getServiceId().equals(spDetails.getServiceId()));
                        log.debug("spId1 = '{}'", sDetails.getServiceProviderAccount());
                        log.debug("spId2 = '{}'", spDetails.getServiceProviderAccount());
                        log.debug("equals = {}", sDetails.getServiceProviderAccount().equals(spDetails.getServiceProviderAccount()));
                        if( sDetails.getServiceId().equals(spDetails.getServiceId()) && sDetails.getServiceProviderAccount().equals(spDetails.getServiceProviderAccount()) ) {
                            info = aInfo;
                            serviceDetails = sDetails;
                            log.debug("found!");
                            break;
                        }
                    }
                    if( info != null ) {
                        log.debug("break!");
                        break;
                    }
                }
            }
            if (info == null) {
                log.info("Cant't find quittances by serviceId and spAccountNumber ({}, {})", spDetails.getServiceId(), spDetails.getServiceProviderAccount());
                servicePayInfo.setServiceStatus(quittanceDetailsResponse.getStatus());
                servicePayInfo.setServiceId(spDetails.getServiceId());
                response.addServicePayInfo(servicePayInfo);
                response.setStatus(Status.REQUEST_IS_NOT_PROCESSED);
                continue;
            }

            Document document = buildDocument(info, serviceDetails, spDetails.getPaySum(), cashbox, request.getLocale());

            if (isEmpty(operation.getAddress())) {
                operation.setAddress(document.getAddress());
                operation.setPayerFIO(stripToEmpty(document.getPayerFIO()));
            }

            if (isNotZero(document.getSum())) {
                operation.addDocument(document);
                servicePayInfo.setServiceId(document.getServiceStub().getId());
                response.addServicePayInfo(servicePayInfo);
            }
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    private Document buildDocument(QuittanceInfo info, ServiceDetails serviceDetails, BigDecimal paySum, Cashbox cashbox, Locale locale) throws Exception {

        Service service = spService.readFull(new Stub<Service>(serviceDetails.getServiceId()));
        ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(service.getServiceProvider().getId()));
        Organization serviceProviderOrganization = serviceProvider.getOrganization();

        Document document = new Document();
        document.setSum(paySum);
        document.setService(service);
        document.setDocumentStatus(documentStatusService.read(DocumentStatus.REGISTERED));
        document.setDocumentType(documentTypeService.read(DocumentType.CASH_PAYMENT));
        document.setDebtorOrganization(cashbox.getPaymentPoint().getCollector().getOrganization());
        document.setCreditorOrganization(serviceProviderOrganization);
        document.setDebt(serviceDetails.getAmount());
        getApartmentAddress(document, info, locale);
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

    public String getErcAccount(List<ServiceDetails.ServiceAttribute> attributes) {

        for (ServiceDetails.ServiceAttribute attribute : attributes) {
            if (attribute.getName().equals(org.flexpay.payments.action.outerrequest.request.response.data.ConsumerAttributes.ATTR_ERC_ACCOUNT)) {
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

    public void getApartmentAddress(Document document, QuittanceInfo quittanceInfo, Locale locale) throws Exception {

        String apartmentMasterIndex = quittanceInfo.getApartmentMasterIndex();
        if (apartmentMasterIndex != null) {
            Stub<Apartment> stub = correctionsService.findCorrection(apartmentMasterIndex,
                    Apartment.class, masterIndexService.getMasterSourceDescriptionStub());
            Apartment apartment = apartmentService.readFullWithHierarchy(stub);
            Street street = streetService.readFull(stub(apartment.getDefaultStreet()));
            Town town = townService.readFull(stub(street.getTown()));
            Region region = regionService.readWithFullHierarhy(town.getRegionStub());

            document.setAddress(addressService.getAddress(stub, locale));
            document.setCountry(getTranslation(region.getCountry().getTranslations(), locale).getName());
            document.setRegion(getTranslation(region.getCurrentName().getTranslations(), locale).getName());
            document.setTownType(getTranslation(town.getCurrentType().getTranslations(), locale).getShortName());
            document.setTownName(getTranslation(town.getCurrentName().getTranslations(), locale).getName());
            document.setStreetType(getTranslation(street.getCurrentType().getTranslations(), locale).getName());
            document.setStreetName(getTranslation(street.getCurrentName().getTranslations(), locale).getName());
            document.setBuildingBulk(apartment.getDefaultBuildings().getBulk());
            document.setBuildingNumber(apartment.getDefaultBuildings().getNumber());
            document.setApartmentNumber(apartment.getNumber());

            return;
        }

        document.setAddress(quittanceInfo.getAddress());
        document.setCountry(quittanceInfo.getCountry());
        document.setRegion(quittanceInfo.getRegion());
        document.setTownType(quittanceInfo.getTownType());
        document.setTownName(quittanceInfo.getTownName());
        document.setStreetType(quittanceInfo.getStreetType());
        document.setStreetName(quittanceInfo.getStreetName());
        document.setBuildingBulk(quittanceInfo.getBuildingBulk());
        document.setBuildingNumber(quittanceInfo.getBuildingNumber());
        document.setApartmentNumber(quittanceInfo.getApartmentNumber());

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
    public ReversalPayResponse reversalPay(ReversalPayRequest request) throws FlexPayException {

        if (!userPreferencesService.isGrantedAuthorities(request.getPaymentsUserPreferences(), Roles.TRADING_DAY_OPERATION_RETURN)) {
            log.debug("User from outer request (username = {}) hasn't rigths for reverse payment", request.getPaymentsUserPreferences().getUsername());
            throw new FlexPayException("User has no rights for reverse payment");
        }

        ReversalPayResponse response = request.getResponse();

        OperationStatus operationStatus = operationStatusService.read(OperationStatus.RETURNED);
        Operation operation = operationService.read(new Stub<Operation>(request.getOperationId()));
        if (operation == null) {
            log.warn("Can't get operation with id {} from DB", request.getOperationId());
            response.setStatus(Status.INCORRECT_OPERATION_ID);
            return response;
        }
        if (!operation.getCanReturn()) {
            throw new FlexPayException("Operation with id " + operation.getId() + " can't return, because has documents for unreturnable services");
        }

        if (!request.getTotalPaySum().equals(operation.getOperationSum())) {
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

        return response;
    }

    @NotNull
    @Override
    public RegistryCommentResponse addRegistryComment(RegistryCommentRequest request) throws FlexPayException {

// unused code?
//        Security.authenticateOuterRequest(request.getLogin());

        RegistryCommentResponse response = request.getResponse();

        Registry registry = registryService.readWithContainers(new Stub<Registry>(request.getRegistryId()));
        if (registry == null) {
            log.warn("Can't get registry with id {} from DB", request.getRegistryId());
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

        if (commentaryContainer == null) {
            commentaryContainer = new RegistryContainer();
            registry.addContainer(commentaryContainer);
        }

        if (isBlank(request.getOrderComment())) {
            commentaryContainer.setData("");
        } else {

            commentaryContainer.setData(COMMENTARY_CONTAINER_TYPE + CONTAINER_DATA_DELIMITER +
                    request.getOrderNumber() + CONTAINER_DATA_DELIMITER +
                    request.getOrderDate() + CONTAINER_DATA_DELIMITER +
                    StringUtil.format(request.getOrderComment(), CONTAINER_DATA_DELIMITER, ESCAPE_SYMBOL));

            if (commentaryContainer.getData().length() > CONTAINER_DATA_MAX_SIZE) {
                log.warn("Commentary length can't be more 256 symbols");
                response.setStatus(Status.INTERNAL_ERROR);
                return response;
            }
        }
        registryService.update(registry);

        return response;
    }

    @NotNull
    @Override
    public GetRegistryListResponse getRegistryList(GetRegistryListRequest request) throws FlexPayException {

// unused code?
//        Security.authenticateOuterRequest(request.getLogin());

        GetRegistryListResponse response = request.getResponse();

        List<Registry> registries;

        if (request.getRegistryType() == null) {
            registries = registryService.findRegistriesInDateInterval(request.getPeriodBeginDateDate(), request.getPeriodEndDateDate());
        } else if (request.getRegistryType() == 1) {
            registries = registryService.findRegistries(RegistryType.TYPE_CASH_PAYMENTS, request.getPeriodBeginDateDate(), request.getPeriodEndDateDate());
        } else if (request.getRegistryType() == 2) {
            registries = registryService.findRegistries(RegistryType.TYPE_BANK_PAYMENTS, request.getPeriodBeginDateDate(), request.getPeriodEndDateDate());
        } else {
            log.warn("Incorrect value in request - {}", request.getRegistryType());
            response.setStatus(Status.REGISTRY_NOT_FOUND);
            return response;
        }

        for (Registry registry : registries) {

            RegistryInfo registryInfo = new RegistryInfo();

            registryInfo.setRegistryId(registry.getId());
            registryInfo.setRegistryDate(registry.getCreationDate());
            registryInfo.setRecordsCount(registry.getRecordsNumber());
            registryInfo.setTotalSum(registry.getAmount());
            registryInfo.setRegistryType(LocalizedTextUtil.findDefaultText(registry.getRegistryType().getI18nName(), request.getLocale()));

            for (RegistryContainer registryContainer : registry.getContainers()) {
                List<String> containerData = StringUtil.splitEscapable(
                        registryContainer.getData(), CONTAINER_DATA_DELIMITER, ESCAPE_SYMBOL);
                if (containerData != null && !containerData.isEmpty() && COMMENTARY_CONTAINER_TYPE.equals(containerData.get(0))) {
                    if (containerData.size() > 3) {
                        registryInfo.setRegistryComment(containerData.get(3));
                    }
                    break;
                }
            }


            response.addRegistryInfo(registryInfo);
        }

        return response;
    }

    @NotNull
    @Override
    public GetServiceListResponse getServiceList(GetServiceListRequest request) throws FlexPayException {

// unused code?
//        Security.authenticateOuterRequest(request.getLogin());

        GetServiceListResponse response = request.getResponse();

        List<Service> services = spService.listAllServices();

        for (Service service : services) {

            ServiceInfo serviceInfo = new ServiceInfo();
            log.debug( "next service: {}", service );

            serviceInfo.setServiceId(service.getId());
            serviceInfo.setServiceName(service.format(request.getLocale()));
            log.debug( "serviceInfo: {}", serviceInfo );
            Language language = LanguageUtil.getLanguage(request.getLocale());
            log.debug( "language: {}", language );
            ServiceProvider serviceProvider = service.getServiceProvider();
            log.debug( "serviceProvider: {}", serviceProvider );
            ServiceProviderDescription description = serviceProvider.getDescription(language);
            log.debug( "description: {}", description );
            String name = description.getName();
            log.debug( "name: {}", name );
            serviceInfo.setServiceProvider(name);

            response.addServiceInfo(serviceInfo);
        }

        return response;
    }

    @NotNull
    @Override
    public GetAddressMasterIndexResponse getAddressMasterIndex(@NotNull GetAddressMasterIndexRequest request) throws FlexPayException {
        GetAddressMasterIndexResponse response = request.getResponse();
        Locale locale = request.getLocale();
        String parentMasterIndex = request.getParentMasterIndex();

        response.setAddressInfoType(request.getParentAddressInfoType() + 1);
        switch (request.getParentAddressInfoType()) {
            case 0:
                if (isNotCorrectInputData(request)) {
                    return response;
                }
                log.debug("Start search country by data: {}", request.getSearchingData());
                List<Country> countries = countryService.findByQuery("%" + request.getSearchingData() + "%");
                for (Country country : countries) {
                    response.addRegistryInfo(new AddressInfo(getTranslation(country.getTranslations(), locale).getName(),
                            masterIndexService.getMasterIndex(country)));
                }
                break;
            case 1:
                if (isNotCorrectInputData(request)) {
                    return response;
                }
                Stub<Country> countryStub;
                if (StringUtils.isEmpty(parentMasterIndex)) {
                    countryStub = new Stub<Country>(getDefaultCountryStub().getId());
                } else {
                    countryStub = correctionsService.findCorrection(parentMasterIndex,
                            Country.class, masterIndexService.getMasterSourceDescriptionStub());
                }
                if (countryStub == null) {
                    log.warn("Parent did not find by master-index: {}", parentMasterIndex);
                    response.setStatus(Status.PARENT_NOT_FOUND);
                    break;
                }
                log.debug("Start search region by data: {}, {}", countryStub, request.getSearchingData());
                List<Region> regions = regionService.findByParentAndQuery(countryStub, "%" + request.getSearchingData() + "%");
                for (Region region : regions) {
                    RegionName name = region.getCurrentName();
                    if (name == null) {
                        log.warn("Found incorrect region: {}", region);
                        continue;
                    }
                    response.addRegistryInfo(new AddressInfo(getTranslation(name.getTranslations(), locale).getName(),
                            masterIndexService.getMasterIndex(region)));
                }
                break;
            case 2:
                if (isNotCorrectInputData(request)) {
                    return response;
                }
                Stub<Region> regionStub;
                if (StringUtils.isEmpty(parentMasterIndex)) {
                    regionStub = new Stub<Region>(getDefaultRegionStub().getId());
                } else {
                    regionStub = correctionsService.findCorrection(parentMasterIndex,
                            Region.class, masterIndexService.getMasterSourceDescriptionStub());
                }
                if (regionStub == null) {
                    response.setStatus(Status.PARENT_NOT_FOUND);
                    log.warn("Parent did not find by master-index: {}", parentMasterIndex);
                    break;
                }
                log.debug("Start search town by data: {}, {}", regionStub, request.getSearchingData());
                List<Town> towns = townService.findByParentAndQuery(regionStub, "%" + request.getSearchingData() + "%");
                for (Town town : towns) {
                    TownName name = town.getCurrentName();
                    if (name == null) {
                        log.warn("Found incorrect town: {}", town);
                        continue;
                    }
                    response.addRegistryInfo(new AddressInfo(getTranslation(name.getTranslations(), locale).getName(),
                            masterIndexService.getMasterIndex(town)));
                }
                break;
            case 3:
                if (isNotCorrectInputData(request)) {
                    return response;
                }
                Stub<Town> townStub;
                townStub = correctionsService.findCorrection(parentMasterIndex,
                        Town.class, masterIndexService.getMasterSourceDescriptionStub());
                if (townStub == null) {
                    response.setStatus(Status.PARENT_NOT_FOUND);
                    log.warn("Parent did not find by master-index: {}", parentMasterIndex);
                    break;
                }
                log.debug("Start search street by data: {}, {}", townStub, request.getSearchingData());
                List<Street> streets = streetService.findByParentAndQuery(townStub, "%" + request.getSearchingData() + "%");
                for (Street street : streets) {
                    StreetType streetType = street.getCurrentType();
			        StreetName streetName = street.getCurrentName();
                    if (streetType == null || streetName == null) {
                        log.warn("Found incorrect street: {}", street);
                        continue;
                    }
                    response.addRegistryInfo(new AddressInfo(getTranslation(streetType.getTranslations(), locale).getShortName() + " " +
                            getTranslation(streetName.getTranslations(), locale).getName(),
                            masterIndexService.getMasterIndex(street)));
                }
                break;
            case 4:
                Stub<Street> streetStub;
                streetStub = correctionsService.findCorrection(parentMasterIndex,
                        Street.class, masterIndexService.getMasterSourceDescriptionStub());
                if (streetStub == null) {
                    response.setStatus(Status.PARENT_NOT_FOUND);
                    log.warn("Parent did not find by master-index: {}", parentMasterIndex);
                    break;
                }
                log.debug("Start search building on street: {}", streetStub);
                List<BuildingAddress> buildingAddresses = buildingService.findAddressesByParent(streetStub);
                if (StringUtils.isEmpty(request.getSearchingData())) {
                    response.setAddressInfos(new ArrayList<AddressInfo>(buildingAddresses.size()));
                    for (BuildingAddress buildingAddress : buildingAddresses) {
                        response.addRegistryInfo(new AddressInfo(
                                getBuildingNumberWithoutHouseType(buildingAddress.getBuildingAttributes(), locale),
                                masterIndexService.getMasterIndex(buildingAddress)));
                    }
                } else {
                    for (BuildingAddress buildingAddress : buildingAddresses) {
                        String foundValue = getBuildingNumberWithoutHouseType(buildingAddress.getBuildingAttributes(), locale);
                        if (StringUtils.startsWithIgnoreCase(foundValue, request.getSearchingData())) {
                            response.addRegistryInfo(new AddressInfo(foundValue, masterIndexService.getMasterIndex(buildingAddress)));
                        }
                    }
                }
                break;
            case 5:
                Stub<BuildingAddress> buildingAddressStub;
                buildingAddressStub = correctionsService.findCorrection(parentMasterIndex,
                        BuildingAddress.class, masterIndexService.getMasterSourceDescriptionStub());
                if (buildingAddressStub == null) {
                    response.setStatus(Status.PARENT_NOT_FOUND);
                    log.warn("Parent did not find by master-index: {}", parentMasterIndex);
                    break;
                }
                log.debug("Start search apartment on building: {}", buildingAddressStub);
                List<Apartment> apartments = apartmentService.findByParent(buildingAddressStub);
                if (StringUtils.isEmpty(request.getSearchingData())) {
                    response.setAddressInfos(new ArrayList<AddressInfo>(apartments.size()));
                    for (Apartment apartment : apartments) {
                        response.addRegistryInfo(new AddressInfo(apartment.getNumber(), masterIndexService.getMasterIndex(apartment)));
                    }
                } else {
                    for (Apartment apartment : apartments) {
                        String foundValue = apartment.getNumber();
                        if (StringUtils.startsWithIgnoreCase(foundValue, request.getSearchingData())) {
                            response.addRegistryInfo(new AddressInfo(foundValue, masterIndexService.getMasterIndex(apartment)));
                        }
                    }
                }
                break;
        }

        return response;
    }

    private boolean isNotCorrectInputData(GetAddressMasterIndexRequest request) {
        String inputData = request.getSearchingData();
        if (StringUtils.isEmpty(inputData) || inputData.length() < 3) {
            log.error("Source searching data '{}' must be longer than 2 chars");
            request.getResponse().setStatus(Status.INCORRECT_MASTER_INDEX_ADDRESS_INPUT_DATA);
            return true;
        }
        return false;
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
    public void setApartmentService(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
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

    @Required
    public void setCashboxTradingDayService(TradingDay<Cashbox> cashboxTradingDayService) {
        this.cashboxTradingDayService = cashboxTradingDayService;
    }

    @Required
    public void setUserPreferencesService(UserPreferencesService userPreferencesService) {
        this.userPreferencesService = userPreferencesService;
    }

    @Required
    public void setStreetService(StreetService streetService) {
        this.streetService = streetService;
    }

    @Required
    public void setTownService(TownService townService) {
        this.townService = townService;
    }

    @Required
    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }

    @Required
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Required
    public void setBuildingService(BuildingService buildingService) {
        this.buildingService = buildingService;
    }
}
