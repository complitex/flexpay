package org.flexpay.payments.export.util;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.PropertiesFactory;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.*;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.*;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.process.export.util.GeneratePaymentsDBRegistry;
import org.flexpay.payments.service.*;
import org.flexpay.payments.service.Roles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class TestGeneratePaymentsDBRegistry extends SpringBeanAwareTestCase {
    @Autowired
    private OperationService operationService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OperationTypeService operationTypeService;
    @Autowired
    private OperationLevelService operationLevelService;
    @Autowired
    private OperationStatusService operationStatusService;
    @Autowired
    @Qualifier("documentTypeService")
    private DocumentTypeService documentTypeService;
    @Autowired
    @Qualifier("documentStatusService")
    private DocumentStatusService documentStatusService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    @Qualifier("spService")
    private SPService spService;
    @Autowired
    private ServiceTypeService serviceTypeService;
    @Autowired
    @Qualifier("serviceProviderService")
    private ServiceProviderService serviceProviderService;

    @Autowired
    private FPFileService fpFileService;

    @Autowired
    @Qualifier("registryArchiveStatusService")
    private RegistryArchiveStatusService registryArchiveStatusService;
    @Autowired
    private RegistryRecordService registryRecordService;
    @Autowired
    private RegistryService registryService;
    @Autowired
    private RegistryStatusService registryStatusService;
    @Autowired
    private RegistryTypeService registryTypeService;
    @Autowired
    private PaymentPointService paymentPointService;
    @Autowired
    private PaymentsCollectorService paymentsCollectorService;
    @Autowired
    private RegistryRecordStatusService registryRecordStatusService;
    @Autowired
    private PropertiesFactory propertiesFactory;

    private Organization organization;

    private List<Operation> operations = new ArrayList<Operation>();

    private static final String TEST_USER = "test-user";

    private static final Language LANG = new Language(2L);

    static {
        LANG.setDefault(true);
    }

    private static final List<String> TEST_USER_AUTHORITIES = CollectionUtils.list(
			Roles.BASIC,
            org.flexpay.payments.service.Roles.OPERATION_READ,
            org.flexpay.orgs.service.Roles.ORGANIZATION_READ,
            org.flexpay.orgs.service.Roles.ORGANIZATION_ADD,
            org.flexpay.orgs.service.Roles.ORGANIZATION_DELETE,
            org.flexpay.payments.service.Roles.OPERATION_ADD,
            org.flexpay.payments.service.Roles.OPERATION_CHANGE,
            org.flexpay.payments.service.Roles.OPERATION_DELETE,
            org.flexpay.payments.service.Roles.DOCUMENT_TYPE_READ,
            org.flexpay.payments.service.Roles.DOCUMENT_ADD,
            org.flexpay.payments.service.Roles.DOCUMENT_CHANGE,
            org.flexpay.payments.service.Roles.SERVICE_TYPE_READ,
            org.flexpay.payments.service.Roles.SERVICE_TYPE_ADD,
            org.flexpay.payments.service.Roles.SERVICE_TYPE_CHANGE,
            org.flexpay.payments.service.Roles.SERVICE_ADD,
            org.flexpay.payments.service.Roles.SERVICE_CHANGE,
            org.flexpay.orgs.service.Roles.SERVICE_PROVIDER_ADD,
            org.flexpay.orgs.service.Roles.SERVICE_PROVIDER_CHANGE
	);

    @Before
    public void startUp() throws FlexPayExceptionContainer, FlexPayException {
        authenticatePaymentsRegistryGenerator();

        //create organization
        organization = new Organization();
        organization.setStatus(ObjectWithStatus.STATUS_ACTIVE);
        organization.setIndividualTaxNumber("111");
        organization.setKpp("222");
        organization.setJuridicalAddress("Kharkov");
        organization.setPostalAddress("Kharkov");

        OrganizationName organizationName = new OrganizationName();
		organizationName.setLang(LANG);
        organizationName.setName("test organization name");
        organization.setName(organizationName);

        OrganizationDescription organizationDescription = new OrganizationDescription();
        organizationDescription.setLang(LANG);
        organizationDescription.setName("test organization description");
        organization.setDescription(organizationDescription);

        organizationService.create(organization);

        //create service provider
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setOrganization(organization);
        serviceProvider.setStatus(ServiceProvider.STATUS_ACTIVE);

        ServiceProviderDescription serviceProviderDescription = new ServiceProviderDescription();
        serviceProviderDescription.setLang(LANG);
        serviceProviderDescription.setName("test service provider description");
        serviceProvider.setDescription(serviceProviderDescription);

        serviceProviderService.save(serviceProvider);

        //get operation type
        OperationType operationType = operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT);
        //get operation level
        OperationLevel operationLevel = operationLevelService.read(OperationLevel.LOW);
        //get operation status
        OperationStatus operationStatus = operationStatusService.read(OperationStatus.REGISTERED);
        //get operation payment point
        PaymentPoint paymentPoint = new PaymentPoint();
        paymentPoint.setStatus(PaymentPoint.STATUS_ACTIVE);
        paymentPoint.setAddress("address");

        PaymentPointName paymentPointName = new PaymentPointName();
        paymentPointName.setLang(LANG);
        paymentPointName.setName("payment point name");

        paymentPoint.setName(paymentPointName);

        PaymentsCollector paymentsCollector = new PaymentsCollector();
        paymentsCollector.setOrganization(organization);

        PaymentsCollectorDescription paymentsCollectorDescription = new PaymentsCollectorDescription();
        paymentsCollectorDescription.setLang(LANG);
        paymentsCollectorDescription.setName("payments collector description");
        paymentsCollector.setDescription(paymentsCollectorDescription);

        paymentsCollectorService.create(paymentsCollector);
        paymentPoint.setCollector(paymentsCollector);
        paymentPointService.create(paymentPoint);

        //create operation
        Operation operation = new Operation();
        operation.setOperationSumm(new BigDecimal(100));
        operation.setCreatorUserName(TEST_USER);
        operation.setCreationDate(new Date());
        operation.setOperationType(operationType);
        operation.setCreatorOrganization(organization);
        operation.setOperationLevel(operationLevel);
        operation.setOperationStatus(operationStatus);
        operation.setPaymentPoint(paymentPoint);
        operation.setRegisterOrganization(organization);
        operation.setRegisterDate(new Date());
        operation.setRegisterUserName(TEST_USER);
        operationService.save(operation);

        operations.add(operation);

        //get document type
        DocumentType documentType = documentTypeService.read(DocumentType.CASH_PAYMENT);
        //get document status
        DocumentStatus documentStatus = documentStatusService.read(DocumentStatus.CREATED);
        //get document service
        int code = 1;
        ServiceType serviceType;
        serviceType = serviceTypeService.getServiceType(code);
        if (serviceType == null) {
            serviceType = new ServiceType();
            serviceType.setCode(code);
            serviceType.setStatus(ServiceType.STATUS_ACTIVE);
            ServiceTypeNameTranslation serviceTypeName = new ServiceTypeNameTranslation();
            serviceTypeName.setLang(LANG);
            serviceTypeName.setDescription("type name description");
            serviceTypeName.setName("type name");
            serviceType.setTypeName(serviceTypeName);
            serviceTypeService.create(serviceType);
        }

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setLang(LANG);
        serviceDescription.setName("description");

        Service service = new Service();
        service.setBeginDate(new Date());
        service.setEndDate(new Date(service.getBeginDate().getTime() + 10000));
        service.setServiceType(serviceType);
        service.setDescription(serviceDescription);
        service.setServiceProvider(serviceProvider);
        spService.save(service);

        //Service documentService = spService.listServices();

        //create document
        Document document = new Document();
        document.setSumm(new BigDecimal(100));
        document.setDocumentStatus(documentStatus);
        document.setOperation(operation);
        document.setCreditorOrganization(organization);
        document.setDebtorOrganization(organization);
        document.setDebtorId(organization.getId().toString());
        document.setService(service);
        if (documentType != null) {
            document.setDocumentType(documentType);
        }
        document.setOperation(operation);
        documentService.save(document);

        Set<Document> documents = CollectionUtils.set();
        documents.add(document);

        operation.setDocuments(documents);
        operationService.save(operation);
        
    }

    @After
    public void tearDown() {
        //clearTestData();
        /*if (operation != null) {
            operationService.delete(new Stub<Operation>(operation));
        }*/
    }

    @Test
    public void testCreateDBRegistry() throws FlexPayException {
        assertNotNull(organization);
        assertEquals(1, operations.size());

        FPModule module = fpFileService.getModuleByName("payments");

        FPFile spFile = new FPFile();
        spFile.setNameOnServer("testFile");
        spFile.setOriginalName("testFile");
        spFile.setCreationDate(new Date());
        spFile.setModule(module);
        fpFileService.create(spFile);

        Date currDate = new Date();

        GeneratePaymentsDBRegistry generate = new GeneratePaymentsDBRegistry();

        generate.setOperationService(operationService);
        generate.setRegistryArchiveStatusService(registryArchiveStatusService);
        generate.setRegistryRecordService(registryRecordService);
        generate.setRegistryService(registryService);
        generate.setRegistryStatusService(registryStatusService);
        generate.setRegistryTypeService(registryTypeService);
        generate.setRegistryRecordStatusService(registryRecordStatusService);
        generate.setPropertiesFactory(propertiesFactory);

        Registry registry  = generate.createDBRegestry(spFile, organization, new Date(currDate.getTime() - 100000), new Date(currDate.getTime() + 1000));
        assertNotNull(registry);
        assertEquals(1, registry.getRecordsNumber().intValue());
        assertEquals(100, registry.getAmount().intValue());

        Page<RegistryRecord> page = new Page<RegistryRecord>(20);
        List<RegistryRecord> records = registryRecordService.listRecords(registry,
                new ImportErrorTypeFilter(),
                new RegistryRecordStatusFilter(),
                page);
        assertEquals(1, records.size());
    }

    /*private void clearTestData() {
        for (Operation operation : operations) {
            operationService.delete(Stub.stub(operation));
        }
        organizationService.delete(Stub.stub(organization));
    }*/

    private static void authenticatePaymentsRegistryGenerator() {
		SecurityUtil.authenticate(TEST_USER, TEST_USER_AUTHORITIES);
	}
}
