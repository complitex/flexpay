package org.flexpay.payments.export;

import static junit.framework.Assert.*;
import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.PropertiesFactory;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.service.*;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.io.ReaderCallback;
import org.flexpay.orgs.persistence.*;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.process.export.GeneratePaymentsRegistry;
import org.flexpay.payments.service.*;
import org.flexpay.payments.service.Roles;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.calendar.BaseCalendar;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.CronTriggerBean;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"file:WEB-INF/applicationContext.xml"})
public class TestGeneratePaymentsRegistry extends SpringBeanAwareTestCase {
    private Logger log = Logger.getLogger(TestGeneratePaymentsRegistry.class);

    private static final String FILE_ENCODING = "cp866";

//    @Resource(name = "processManager")
    @Autowired
    private ProcessManager tProcessManager;

    @Resource(name = "serviceProviderService")
    private ServiceProviderService tProviderService;
    @Resource(name = "serviceProviderServiceAttribute")
    private ServiceProviderAttributeService tProviderAttributeService;

    @Resource(name = "schedulerFactoryBeanRegistry")
    private StdScheduler tSchedulerFactoryBeanRegistry;

    @Resource(name = "jobTradingDayTrigger")
    private CronTriggerBean tJobTradingDayTrigger;

    @Resource(name = "languageService")
    private LanguageService tLanguageService;

    @Autowired
    private OperationService operationService;
    @Autowired
    //@Qualifier("organizationService")
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

    private ServiceProvider serviceProvider;
    private Organization registerOrganization;

    private List<Operation> operations = new ArrayList<Operation>();

    private static final String TEST_USER = "test-user";

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
            org.flexpay.orgs.service.Roles.SERVICE_PROVIDER_CHANGE,
            org.flexpay.orgs.service.Roles.PAYMENTS_COLLECTOR_ADD,
            org.flexpay.orgs.service.Roles.PAYMENT_POINT_ADD
	);

    @Before
    public void startUp() throws FlexPayExceptionContainer, FlexPayException {
        authenticatePaymentsRegistryGenerator();

        List<Language> langList = tLanguageService.getLanguages();
        assertNotSame(0, langList.size());
        Language lang = null;
        for (Language language : langList) {
            if ("ru".equals(language.getLocale().getLanguage())) {
                lang = language;
            }
            log.debug("Lang: " + language.getLocale().getLanguage());
        }
        assertNotNull(lang);

        //create organization
        registerOrganization = new Organization();
        registerOrganization.setStatus(ObjectWithStatus.STATUS_ACTIVE);
        registerOrganization.setIndividualTaxNumber("112");
        registerOrganization.setKpp("224");
        registerOrganization.setJuridicalAddress("Kharkov");
        registerOrganization.setPostalAddress("Kharkov");

        OrganizationName organizationName = new OrganizationName();
		organizationName.setLang(lang);
        organizationName.setName("test register organization name");
        registerOrganization.setName(organizationName);

        OrganizationDescription organizationDescription = new OrganizationDescription();
        organizationDescription.setLang(lang);
        organizationDescription.setName("test register organization description");
        registerOrganization.setDescription(organizationDescription);

        organizationService.create(registerOrganization);

        //create organization
        Organization organization = new Organization();
        organization.setStatus(ObjectWithStatus.STATUS_ACTIVE);
        organization.setIndividualTaxNumber("111");
        organization.setKpp("222");
        organization.setJuridicalAddress("Kharkov");
        organization.setPostalAddress("Kharkov");

        organizationName = new OrganizationName();
		organizationName.setLang(lang);
        organizationName.setName("test organization name");
        organization.setName(organizationName);

        organizationDescription = new OrganizationDescription();
        organizationDescription.setLang(lang);
        organizationDescription.setName("test organization description");
        organization.setDescription(organizationDescription);

        organizationService.create(organization);

        //create service2 provider
        serviceProvider = new ServiceProvider();
        serviceProvider.setOrganization(organization);
        serviceProvider.setStatus(ServiceProvider.STATUS_ACTIVE);

        ServiceProviderDescription serviceProviderDescription = new ServiceProviderDescription();
        serviceProviderDescription.setLang(lang);
        serviceProviderDescription.setName("test service2 provider description");
        serviceProvider.setDescription(serviceProviderDescription);

        tProviderService.create(serviceProvider);

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
        paymentPointName.setLang(lang);
        paymentPointName.setName("payment point name");

        paymentPoint.setName(paymentPointName);

        PaymentsCollector paymentsCollector = new PaymentsCollector();
        paymentsCollector.setOrganization(registerOrganization);

        PaymentsCollectorDescription paymentsCollectorDescription = new PaymentsCollectorDescription();
        paymentsCollectorDescription.setLang(lang);
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
        operation.setCreatorOrganization(registerOrganization);
        operation.setOperationLevel(operationLevel);
        operation.setOperationStatus(operationStatus);
        operation.setPaymentPoint(paymentPoint);
        operation.setRegisterOrganization(registerOrganization);
        operation.setRegisterDate(new Date());
        operation.setRegisterUserName(TEST_USER);
        operationService.save(operation);

        //change register date operation
        Date creationDate = operation.getRegisterDate();
        creationDate = DateUtil.previous(creationDate);
        operation.setCreationDate(creationDate);

        operations.add(operation);

        //get document type
        DocumentType documentType = documentTypeService.read(DocumentType.CASH_PAYMENT);
        //get document status
        DocumentStatus documentStatus = documentStatusService.read(DocumentStatus.REGISTERED);
        //get document service2
        int code = 3;
        ServiceType serviceType2;
        serviceType2 = serviceTypeService.getServiceType(code);
        if (serviceType2 == null) {
            serviceType2 = new ServiceType();
            serviceType2.setCode(code);
            serviceType2.setStatus(ServiceType.STATUS_ACTIVE);
            ServiceTypeNameTranslation serviceTypeName = new ServiceTypeNameTranslation();
            serviceTypeName.setLang(lang);
            serviceTypeName.setDescription("type name description");
            serviceTypeName.setName("type name");
            serviceType2.setTypeName(serviceTypeName);
            serviceTypeService.create(serviceType2);
        }

        code = 4;
        ServiceType serviceType4;
        serviceType4 = serviceTypeService.getServiceType(code);
        if (serviceType4 == null) {
            serviceType4 = new ServiceType();
            serviceType4.setCode(code);
            serviceType4.setStatus(ServiceType.STATUS_ACTIVE);
            ServiceTypeNameTranslation serviceTypeName = new ServiceTypeNameTranslation();
            serviceTypeName.setLang(lang);
            serviceTypeName.setDescription("type name description");
            serviceTypeName.setName("type name");
            serviceType4.setTypeName(serviceTypeName);
            serviceTypeService.create(serviceType4);
        }

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setLang(lang);
        serviceDescription.setName("description");

        Service service2 = new Service();
        service2.setBeginDate(new Date());
        service2.setEndDate(new Date(service2.getBeginDate().getTime() + 100000));
        service2.setServiceType(serviceType2);
        service2.setExternalCode("2");
        service2.setDescription(serviceDescription);
        service2.setServiceProvider(serviceProvider);
        spService.create(service2);

        Service service4 = new Service();
        service4.setBeginDate(new Date());
        service4.setEndDate(new Date(service4.getBeginDate().getTime() + 100000));
        service4.setServiceType(serviceType4);
        service4.setExternalCode("4");
        service4.setDescription(serviceDescription);
        service4.setServiceProvider(serviceProvider);
        spService.create(service4);

        //Service documentService = spService.listServices();

        //create document
        Document document = new Document();
        document.setSumm(new BigDecimal(100));
        document.setDocumentStatus(documentStatus);
        document.setOperation(operation);
        document.setCreditorOrganization(registerOrganization);
        document.setDebtorOrganization(registerOrganization);
        document.setDebtorId(registerOrganization.getId().toString());
        document.setService(service2);
        document.setAddress("Test address");
        document.setLastName("Test Last Name");
        document.setMiddleName("Test Middle Name");
        document.setFirstName("Test First Name");
        document.setTown("Test Town");
        document.setStreetType("Test Street Type");
        document.setStreetName("Test Street Name");
        document.setBuildingNumber("00");
        document.setBuildingBulk("1234567890");
        document.setApartmentNumber("1");

        if (documentType != null) {
            document.setDocumentType(documentType);
        }
        document.setOperation(operation);
        documentService.save(document);

        Set<Document> documents = CollectionUtils.set();
        documents.add(document);

        document = new Document();
        document.setSumm(new BigDecimal(203));
        document.setDocumentStatus(documentStatus);
        document.setOperation(operation);
        document.setCreditorOrganization(registerOrganization);
        document.setDebtorOrganization(registerOrganization);
        document.setDebtorId(registerOrganization.getId().toString());
        document.setService(service4);
        document.setAddress("Test address");
        document.setLastName("Test Last Name");
        document.setMiddleName("Test Middle Name");
        document.setFirstName("Test First Name");
        document.setTown("Test Town");
        document.setStreetType("Test Street Type");
        document.setStreetName("Test Street Name");
        document.setBuildingNumber("00");
        document.setBuildingBulk("1234567890");
        document.setApartmentNumber("1");

        if (documentType != null) {
            document.setDocumentType(documentType);
        }
        document.setOperation(operation);
        documentService.save(document);
        documents.add(document);

        operation.setDocuments(documents);
        operationService.save(operation);

    }

    @Test
    public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException, SchedulerException, IOException {
        GeneratePaymentsRegistry jobScheduler = new GeneratePaymentsRegistry();
        jobScheduler.setProcessManager(tProcessManager);
        jobScheduler.setProviderId(serviceProvider.getId());
        jobScheduler.setRegisteredOrganizationId(registerOrganization.getId());
        jobScheduler.setEmail("test@test.ru");
        jobScheduler.setProviderService(tProviderService);
        jobScheduler.setServiceProviderAttributeService(tProviderAttributeService);
        jobScheduler.setPrivateKey("WEB-INF/payments/configs/keys/secret.key");

        BaseCalendar calendar = new BaseCalendar();

        TriggerFiredBundle fireBundle = new TriggerFiredBundle(tJobTradingDayTrigger.getJobDetail(), tJobTradingDayTrigger, calendar,
                false, null, null, null, null);
        JobExecutionContext schedulerContext = new JobExecutionContext(tSchedulerFactoryBeanRegistry, fireBundle, jobScheduler);
        log.debug("start jobScheduler");
        jobScheduler.execute(schedulerContext);
        log.debug("finish jobScheduler");

        Map parameters = schedulerContext.getMergedJobDataMap();

        //check registry
        Registry registry = null;
        if (parameters.containsKey("Registry")) {
			Object o = parameters.get("Registry");
			if (o instanceof Registry) {
				registry = (Registry) o;
			} else {
				log.warn("Invalid registry`s instance class");
			}
		} else if (parameters.containsKey("RegistryId")) {
			Long registryId = (Long) parameters.get("RegistryId");
			registry = registryService.read(new Stub<Registry>(registryId));
		}
        assertNotNull(registry);
        assertEquals(2, registry.getRecordsNumber().intValue());
        assertEquals(303, registry.getAmount().intValue());

        Page<RegistryRecord> page = new Page<RegistryRecord>(20);
        List<RegistryRecord> records = registryRecordService.listRecords(registry,
                new ImportErrorTypeFilter(),
                new RegistryRecordStatusFilter(),
                page);
        assertEquals(2, records.size());

        //check last processed date
        String newLastProcessedDateSt = (String) parameters.get("lastProcessedDate");
        assertNotNull(newLastProcessedDateSt);
        Date newLastProcessedDate = new Date(Long.parseLong(newLastProcessedDateSt));
        assertEquals(DateUtil.truncateDay(new Date()), newLastProcessedDate);

        //check MB file
        FPFile spFile = null;
		if (parameters.containsKey("File")) {
			Object o = parameters.get("File");
			if (o instanceof FPFile && ((FPFile) o).getId() != null) {
				spFile = (FPFile) o;
				spFile = fpFileService.read(stub(spFile));
			} else {
				log.warn("Invalid file`s instance class");
			}
		} else if (parameters.containsKey("FileId")) {
			Long fileId = (Long) parameters.get("FileId");
			spFile = fpFileService.read(new Stub<FPFile>(fileId));
		}
        assertNotNull(spFile);
        assertDigitalSignatureCountLine(spFile, 3);
        //assertTotalCountLine(spFile, 15);
        assertRecordCountLine(spFile, 2);
        fpFileService.deleteFromFileSystem(spFile);
    }

    private static void assertTotalCountLine(FPFile file, final int n) throws IOException {
		file.withReader(FILE_ENCODING, new ReaderCallback() {
			public void read(Reader r) throws IOException {
				@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
				BufferedReader reader = new BufferedReader(r);
				int i = 0;
				while (reader.readLine() != null) {
					i++;
				}
				assertEquals(n, i);
			}
		});
	}

    private static void assertRecordCountLine(FPFile file, final int n) throws IOException {
		file.withReader(FILE_ENCODING, new ReaderCallback() {
			public void read(Reader r) throws IOException {
				@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
				BufferedReader reader = new BufferedReader(r);
				int i = 0;
                String line;
                boolean startRecords = false;
				while ((line = reader.readLine()) != null) {
                    if (line.startsWith("--------+")) {
                        startRecords = true;
                    }
                    if (line.contains("|Test") && startRecords) {
					    i++;
                    }
				}
				assertEquals(n, i);
			}
		});
	}


    private static void assertDigitalSignatureCountLine(FPFile file, final int n) throws IOException {
		file.withReader(FILE_ENCODING, new ReaderCallback() {
			public void read(Reader r) throws IOException {
				@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
				BufferedReader reader = new BufferedReader(r);
				int i = 0;
                String line;
                boolean startSignature = false;
				while ((line = reader.readLine()) != null) {
                    if (line.startsWith("-----------------------------------------------")) {
                        if (startSignature) {
                            assertEquals(n, i);
                            return;
                        } else {
                            startSignature = true;
                        }
                    } else if (startSignature) {
                        i++;
                    }
				}
			}
		});
	}

    private static void authenticatePaymentsRegistryGenerator() {
		SecurityUtil.authenticate(TEST_USER, TEST_USER_AUTHORITIES);
	}
}
