package org.flexpay.payments.export;

import static junit.framework.Assert.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.PropertiesFactory;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryFPFileType;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.service.*;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.io.ReaderCallback;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.*;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.process.export.GeneratePaymentsRegistry;
import org.flexpay.payments.process.export.job.ExportJobParameterNames;
import org.flexpay.payments.service.*;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.ab.persistence.filters.ImportErrorTypeFilter;
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
import org.jetbrains.annotations.Nullable;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class TestGeneratePaymentsRegistry extends PaymentsSpringBeanAwareTestCase {

	private static final String FILE_ENCODING = "cp866";
    private static final int BUFFER_SIZE = 1024;

	//    @Resource(name = "processManager")
	@Autowired
	private ProcessManager tProcessManager;

	@Resource (name = "serviceProviderService")
	private ServiceProviderService tProviderService;
	@Resource (name = "serviceProviderServiceAttribute")
	private ServiceProviderAttributeService tProviderAttributeService;

	@Resource (name = "schedulerFactoryBeanRegistry")
	private StdScheduler tSchedulerFactoryBeanRegistry;

	@Resource (name = "jobTradingDayTrigger")
	private CronTriggerBean tJobTradingDayTrigger;

	@Resource (name = "languageService")
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
	@Qualifier ("documentTypeService")
	private DocumentTypeService documentTypeService;
	@Autowired
	@Qualifier ("documentStatusService")
	private DocumentStatusService documentStatusService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	@Qualifier ("spService")
	private SPService spService;
	@Autowired
	private ServiceTypeService serviceTypeService;

	@Autowired
	private FPFileService fpFileService;

	@Autowired
	@Qualifier ("registryArchiveStatusService")
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
    @Autowired
    @Qualifier("registryFPFileTypeService")
    private RegistryFPFileTypeService registryFPFileTypeService;

	private ServiceProvider serviceProvider;
	private Organization registerOrganization;

	private List<Operation> operations = new ArrayList<Operation>();

	private static final String TEST_USER = "test-user";

	@Before
	public void startUp() throws FlexPayExceptionContainer, FlexPayException {

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
		Organization recipientOrganization = new Organization();
		recipientOrganization.setStatus(ObjectWithStatus.STATUS_ACTIVE);
		recipientOrganization.setIndividualTaxNumber("111");
		recipientOrganization.setKpp("222");
		recipientOrganization.setJuridicalAddress("Kharkov");
		recipientOrganization.setPostalAddress("Kharkov");

		organizationName = new OrganizationName();
		organizationName.setLang(lang);
		organizationName.setName("test organization name");
		recipientOrganization.setName(organizationName);

		organizationDescription = new OrganizationDescription();
		organizationDescription.setLang(lang);
		organizationDescription.setName("test organization description");
		recipientOrganization.setDescription(organizationDescription);

		organizationService.create(recipientOrganization);

		//create service1 provider
		serviceProvider = new ServiceProvider();
		serviceProvider.setOrganization(recipientOrganization);
		serviceProvider.setStatus(ServiceProvider.STATUS_ACTIVE);
		serviceProvider.setEmail("test@test.ru");

		ServiceProviderDescription serviceProviderDescription = new ServiceProviderDescription();
		serviceProviderDescription.setLang(lang);
		serviceProviderDescription.setName("test service1 provider description");
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
		operationService.create(operation);

		//change register date operation
		Date creationDate = operation.getRegisterDate();
		creationDate = DateUtil.previous(creationDate);
		operation.setCreationDate(creationDate);

		operations.add(operation);

		//get document type
		DocumentType documentType = documentTypeService.read(DocumentType.CASH_PAYMENT);
		//get document status
		DocumentStatus documentStatus = documentStatusService.read(DocumentStatus.REGISTERED);
		//get document service1
		int code = 3;
		ServiceType serviceType2;
		serviceType2 = serviceTypeService.getServiceType(code);
		assertNotNull("Service Type with code " + code + " does not exist in database", serviceType2);

		code = 4;
		ServiceType serviceType4;
		serviceType4 = serviceTypeService.getServiceType(code);
		assertNotNull("Service Type with code " + code + " does not exist in database", serviceType4);

		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setLang(lang);
		serviceDescription.setName("description");

		Service service1 = new Service();
		service1.setBeginDate(new Date());
		service1.setEndDate(new Date(service1.getBeginDate().getTime() + 100000));
		service1.setServiceType(serviceType2);
		service1.setExternalCode("3");
		service1.setDescription(serviceDescription);
		service1.setServiceProvider(serviceProvider);
		spService.create(service1);

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
		document.setService(service1);
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
		documentService.create(document);

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
		documentService.create(document);
		documents.add(document);

		operation.setDocuments(documents);
		operationService.update(operation);

	}

	@Test
	public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException, SchedulerException, IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		GeneratePaymentsRegistry jobScheduler = new GeneratePaymentsRegistry();
		jobScheduler.setProcessManager(tProcessManager);
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

        List<Long> registries = (List<Long>)schedulerContext.getMergedJobDataMap().get(ExportJobParameterNames.REGISTRIES);
        assertNotNull(registries);

        assertTrue("Parameters map must content 1 registry", registries.size() > 0);

        RegistryFPFileType mbFormat = registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT);
        assertNotNull("Registry file MB format not found", mbFormat);

        RegistryFPFileType fpFormat = registryFPFileTypeService.findByCode(RegistryFPFileType.FP_FORMAT);
        assertNotNull("Registry file FP format not found", fpFormat);

        Registry registry = null;
        int i = 0;
        for (Long registryId : registries) {
            Registry tmpRegistry = registryService.read(new Stub<Registry>(registryId));
            assertNotNull("Registry with id=" + registryId + " not found", tmpRegistry);
            if (tmpRegistry.getRecipientCode().equals(serviceProvider.getOrganization().getId()) && tmpRegistry.getSenderCode().equals(registerOrganization.getId())) {
                registry = tmpRegistry;
                i++;
            }
        }
        assertEquals("Need one tested registry", 1, i);

        // assert registry data
        assertEquals(2, registry.getRecordsNumber().intValue());
        assertEquals(303, registry.getAmount().intValue());
        List<RegistryRecord> records = registryRecordService.listRecords(registry,
                new ImportErrorTypeFilter(),
                new RegistryRecordStatusFilter(),
                new Page<RegistryRecord>(20));
        assertEquals("Registry does not content 2 records", 2, records.size());

        // assert count files
        assertEquals("Registry does not content 2 files", 2, registry.getFiles().size());

        // assert MB File
        FPFile registryMBFile = registry.getFiles().get(mbFormat);
        assertNotNull("MB file not found for registry id=" + registry.getId(), registryMBFile);
        registryMBFile = fpFileService.read(Stub.stub(registryMBFile));

        assertDigitalSignature(registryMBFile, "WEB-INF/payments/configs/keys/public.key");
        assertTotalCountLine(registryMBFile, 14);
        assertRecordCountLine(registryMBFile, 2);
        fpFileService.deleteFromFileSystem(registryMBFile);

        // assert FP File
        FPFile registryFPFile = registry.getFiles().get(fpFormat);
        assertNotNull("FP file not found for registry id=" + registry.getId(), registryFPFile);
        registryFPFile = fpFileService.read(Stub.stub(registryFPFile));
        assertTrue("FP file nullable", registryFPFile.getSize() > 0);
        fpFileService.deleteFromFileSystem(registryFPFile);

//        registryService.delete(registry);
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
					if (line.startsWith("+--------+")) {
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
					if (line.startsWith("______")) {
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

    private static void assertDigitalSignature(FPFile file, String key) throws IOException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {
        final Signature sign = readPublicSignature(key);
		file.withReader(FILE_ENCODING, new ReaderCallback() {
			public void read(Reader r) throws IOException {
				@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
				BufferedReader reader = new BufferedReader(r);

				String line;
				int startSignature = -1;
                String signature = "";
                int i = 0;
                int fileSize = 0;
                try {
                    while ((line = reader.readLine()) != null) {
                        System.out.print(++i + ":");
                        if (line.startsWith("_____") && startSignature <= 0) {
                            startSignature++;
                            System.out.println("startSignature=" + startSignature);
                        } else if (startSignature == 0 && line.length() > 0) {
                            if (signature.length() > 0) {
                                line = "\n" + line;
                            }
                            signature += line;
                            System.out.println();
                        } else if (startSignature > 0) {
                            line = line + "\n";
                            fileSize += line.getBytes(FILE_ENCODING).length;
                            sign.update(line.getBytes(FILE_ENCODING));
                            System.out.print(line);
                        }
                    }
                    System.out.println("signature:" + signature);
                    System.out.println("signature length:" + signature.getBytes(FILE_ENCODING).length);
                    System.out.println("file size:" + fileSize);
                    assertTrue("Assert verify file", sign.verify(signature.getBytes(FILE_ENCODING)));
                } catch (SignatureException ex) {
                    assertTrue(ex.getMessage(), false);
                }
			}
		});
	}

    private static Signature readPublicSignature(String key) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        byte[] pubKeyBytes = getDataFile(key);

        // decode public key
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyBytes);
        PublicKey pubKey = keyFactory.generatePublic(pubSpec);

        Signature instance = Signature.getInstance("SHA1withRSA");
        instance.initVerify(pubKey);
        return instance;
    }

    @Nullable
    private static byte[] getDataFile(String file) throws IOException {
        InputStream is = ApplicationConfig.getResourceAsStream(file);
        if (is == null) {
            return null;
        }
        InputStream dis = new DataInputStream(is);
        byte[] data = new byte[BUFFER_SIZE];
        int off = 0;
        int countRead;
        while ((countRead = dis.read(data, off, BUFFER_SIZE)) > 0) {
            off += countRead;
            data = Arrays.copyOf(data, off + BUFFER_SIZE);
        }
        dis.close();
        is.close();
        return data;
    }

    private static byte[] getDataFile(InputStream is, int off, int len) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        int curLen = BUFFER_SIZE;
        if (len > 0 && curLen > len) {
            curLen = len;
        }
        byte[] privKeyBytes = new byte[curLen];
        int countRead;
        while ((off < len || len < 0) && (countRead = dis.read(privKeyBytes, off, curLen)) > 0) {
            off += countRead;
            if (len > 0 && off + BUFFER_SIZE > len) {
                curLen = len - off;
            }
            privKeyBytes = Arrays.copyOf(privKeyBytes, off + curLen);
        }
        return privKeyBytes;
    }
}
