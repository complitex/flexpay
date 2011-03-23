package org.flexpay.payments.export;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryFPFileType;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryFPFileTypeService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.io.InputStreamCallback;
import org.flexpay.common.util.io.ReaderCallback;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderAttributeService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.process.export.ExportJobParameterNames;
import org.flexpay.payments.process.export.GeneratePaymentsRegistry;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.EircRegistryService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.impl.RSASignatureService;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.payments.util.impl.*;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.calendar.BaseCalendar;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.CronTriggerBean;

import javax.annotation.Resource;
import java.io.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.*;

public class TestGeneratePaymentsRegistry extends PaymentsSpringBeanAwareTestCase {

	private static final String FILE_ENCODING = "cp866";
	private static final int BUFFER_SIZE = 1024;

	private static final long N_SERVICE_ROWS = 2;
	private static final long N_HEAD_ROWS = 8;

	private static final String SERVICE_LINE = StringUtils.repeat("_", 128);

    private ServiceProvider serviceProvider;
    private PaymentPoint paymentPoint;
	private Organization registerOrganization;
    private Organization recipientOrganization;
    private Operation operation;

	@Resource (name = "schedulerFactoryBeanRegistry")
	private StdScheduler tSchedulerFactoryBeanRegistry;
	@Resource (name = "jobTradingDayTrigger")
	private CronTriggerBean tJobTradingDayTrigger;

	@Autowired
	private OperationService operationService;
    @Autowired
	private DocumentService documentService;
	@Autowired
	private FPFileService fpFileService;
	@Autowired
	private RegistryRecordService registryRecordService;
    @Autowired
    private EircRegistryService eircRegistryService;
	@Autowired
	private RegistryService registryService;
    @Autowired
    private ProcessManager processManager;
    @Autowired
    private ServiceProviderService tProviderService;
    @Autowired
    private ServiceProviderAttributeService tProviderAttributeService;
    @Autowired
    private RSASignatureService signatureService;
	@Autowired
	private CorrectionsService correctionsService;
	@Autowired
	private ClassToTypeRegistry classToTypeRegistry;
	@Autowired
	private DataSourceDescription megabankDataSourceDescription;
	@Autowired
	@Qualifier ("registryFPFileTypeService")
	private RegistryFPFileTypeService registryFPFileTypeService;
    @Autowired
    @Qualifier ("paymentsTestPaymentPointUtil")
    private PaymentsTestPaymentPointUtil paymentPointUtil;
    @Autowired
    @Qualifier ("paymentsTestOrganizationUtil")
    private PaymentsTestOrganizationUtil organizationUtil;
    @Autowired
    @Qualifier ("paymentsTestServiceProviderUtil")
    private PaymentsTestServiceProviderUtil serviceProviderUtil;
    @Autowired
    @Qualifier ("paymentsTestCashPaymentOperationUtil")
    private PaymentsTestCashPaymentOperationUtil operationUtil;
    @Autowired
    @Qualifier ("paymentsTestServiceUtil")
    private PaymentsTestServiceUtil paymentsTestServiceUtil;
    @Autowired
    @Qualifier ("paymentsTestRegistryUtil")
    private PaymentsTestRegistryUtil registryUtil;

	@Before
	public void startUp() throws FlexPayExceptionContainer, FlexPayException {

		//create organization
        registerOrganization = organizationUtil.create("112");
        assertNotNull("Did not create register organization", registerOrganization);

        //create organization
        recipientOrganization = organizationUtil.create("111");
        assertNotNull("Did not create recipient organization", recipientOrganization);

		//create service1 provider
		serviceProvider = serviceProviderUtil.create(recipientOrganization);
        assertNotNull("Did not create service provider", serviceProvider);
		DataCorrection serviceProviderCorrection = new DataCorrection("ESPA" + serviceProvider.getId(),
				serviceProvider.getId(), classToTypeRegistry.getType(ServiceProvider.class), megabankDataSourceDescription);
		correctionsService.save(serviceProviderCorrection);

        //create operation payment point
        paymentPoint = paymentPointUtil.create(registerOrganization);
        assertNotNull("Did not create payment point", paymentPoint);

		//create operation
		operation = operationUtil.create(paymentPoint, 100);
        assertNotNull("Did not create operation", operation);

		//change register date operation
		Date creationDate = operation.getRegisterDate();
		creationDate = DateUtil.previous(creationDate);
		operation.setCreationDate(creationDate);
        operationService.update(operation);

        //create service 3
        Service service3 = paymentsTestServiceUtil.create(serviceProvider, 3);
        assertNotNull("Did not create service 3", service3);

        //create service 4
        Service service4 = paymentsTestServiceUtil.create(serviceProvider, 4);
        assertNotNull("Did not create service 3", service4);

        // create document
        Document document = operationUtil.addDocument(operation, service3, 100);
        assertNotNull("Did not create document", document);
        document.setDebtorId("");
        documentService.update(document);

        // create document
        document = operationUtil.addDocument(operation, service4, 203);
        assertNotNull("Did not create document", document);
        document.setDebtorId("");
        documentService.update(document);
	}

    @After
    public void tearDown() {
		/*
        authenticateTestUser();
        //delete generation registry
        OrganizationFilter senderOrganizationFilter = new SenderOrganizationFilter();
        senderOrganizationFilter.setOrganizations(CollectionUtils.list(registerOrganization));
		senderOrganizationFilter.setSelected(Stub.stub(registerOrganization));
        List<ObjectFilter> filters = CollectionUtils.<ObjectFilter>list(senderOrganizationFilter);
        for (Registry registry : eircRegistryService.findObjects(null, filters, new Page<Registry>(1000))) {
            System.out.println("Delete registry " + registry.getId());
			log.debug("Delete registry {}", registry.getId());
            registryUtil.delete(registry);
        }
        //delete operation with own documents
        operationUtil.delete(operation);
        //delete service provider with services
        serviceProviderUtil.delete(serviceProvider);
        //delete payment point
        paymentPointUtil.delete(paymentPoint);
        //delete organization
        organizationUtil.delete(registerOrganization);
        organizationUtil.delete(recipientOrganization);
        */
    }

    @Test
	public void testStartTradingDay() throws Exception {

		GeneratePaymentsRegistry jobScheduler = new GeneratePaymentsRegistry();
		jobScheduler.setProcessManager(processManager);
		jobScheduler.setProviderService(tProviderService);
		jobScheduler.setServiceProviderAttributeService(tProviderAttributeService);
		jobScheduler.setPrivateKey("WEB-INF/payments/configs/keys/secret.key");

		BaseCalendar calendar = new BaseCalendar();

		TriggerFiredBundle fireBundle = new TriggerFiredBundle(
				tJobTradingDayTrigger.getJobDetail(), tJobTradingDayTrigger, calendar,
				false, null, null, null, null);
		JobExecutionContext schedulerContext = new JobExecutionContext(
				tSchedulerFactoryBeanRegistry, fireBundle, jobScheduler);
		log.debug("start jobScheduler");
		jobScheduler.execute(schedulerContext);
		log.debug("finish jobScheduler");

		@SuppressWarnings ({"unchecked"})
		List<Long> registries = (List<Long>) schedulerContext
				.getMergedJobDataMap().get(ExportJobParameterNames.REGISTRIES);
		assertNotNull(registries);
		assertFalse("Parameters should contain at least 1 registry", registries.isEmpty());

		RegistryFPFileType mbFormat = registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT);
		assertNotNull("Registry file MB format not found", mbFormat);

		RegistryFPFileType fpFormat = registryFPFileTypeService.findByCode(RegistryFPFileType.FP_FORMAT);
		assertNotNull("Registry file FP format not found", fpFormat);

		Registry registry = null;
		int i = 0;
		for (Long registryId : registries) {
			Registry tmpRegistry = registryService.read(new Stub<Registry>(registryId));
			assertNotNull("Registry with id=" + registryId + " not found", tmpRegistry);
			if (tmpRegistry.getRecipientCode().equals(serviceProvider.getOrganization().getId())
				&& tmpRegistry.getSenderCode().equals(registerOrganization.getId())) {
				registry = tmpRegistry;
				i++;
			}
		}
		assertEquals("Need one tested registry", 1, i);
		assertNotNull("No registry", registry);

		// assert registry data
		assertEquals(2, registry.getRecordsNumber().intValue());
		assertEquals(303, registry.getAmount().intValue());
		List<RegistryRecord> records = registryRecordService.listRecords(registry,
				new ImportErrorTypeFilter(),
				new RegistryRecordStatusFilter(),
				new Page<RegistryRecord>(20));
		assertEquals("Registry does not contain 2 records", 2, records.size());

		// assert count files
		assertEquals("Registry does not contain 2 files", 2, registry.getFiles().size());

		// assert MB File
		FPFile registryMBFile = registry.getFiles().get(mbFormat);
		assertNotNull("MB file not found for registry id=" + registry.getId(), registryMBFile);
		registryMBFile = fpFileService.read(Stub.stub(registryMBFile));

		System.out.println("MBFile: " + registryMBFile);
		assertDigitalSignature(registryMBFile, "WEB-INF/payments/configs/keys/public.key");
		assertTotalCountLine(registryMBFile, N_SERVICE_ROWS + N_HEAD_ROWS + registry.getRecordsNumber());
		assertRecordCountLine(registryMBFile, 2);
//		fpFileService.deleteFromFileSystem(registryMBFile);

		// assert FP File
		FPFile registryFPFile = registry.getFiles().get(fpFormat);
		assertNotNull("FP file not found for registry id=" + registry.getId(), registryFPFile);
		registryFPFile = fpFileService.read(Stub.stub(registryFPFile));
		assertTrue("FP file nullable", registryFPFile.getSize() > 0);
//		fpFileService.deleteFromFileSystem(registryFPFile);
	}

	private void assertTotalCountLine(FPFile file, final long n) throws IOException {

		log.debug("assertTotalCountLine");

		file.withReader(FILE_ENCODING, new ReaderCallback() {
            @Override
			public void read(Reader r) throws IOException {
				@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
				BufferedReader reader = new BufferedReader(r);

				assertEquals("Service line expected", SERVICE_LINE, reader.readLine());
				String line;
				boolean secondServiceLineFound = false;
				while ((line = reader.readLine()) != null) {
					if (SERVICE_LINE.equals(line)) {
						secondServiceLineFound = true;
						break;
					}
				}
				assertTrue("Second service line not found", secondServiceLineFound);

				long i = N_SERVICE_ROWS;
				while (reader.readLine() != null) {
					i++;
				}
				assertEquals("Invalid lines number", n, i);
			}
		});
	}

	private void assertRecordCountLine(FPFile file, final int n) throws IOException {

		log.debug("assertRecordCountLine");

		file.withReader(FILE_ENCODING, new ReaderCallback() {
            @Override
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
				assertEquals("Invalid records lines number", n, i);
			}
		});
	}

	private void assertDigitalSignature(FPFile file, String key) throws Exception {

		log.debug("assertDigitalSignature");

		final Signature sign = signatureService.readPublicSignature(key);

		file.withInputStream(new InputStreamCallback() {
            @Override
			public void read(InputStream is) throws IOException {

				byte[] lineFeed = "\n".getBytes(FILE_ENCODING);
                String serviceLine = new String(TestGeneratePaymentsRegistry.read(is, SERVICE_LINE.length()));
                assertEquals("Incorrect service line", SERVICE_LINE, serviceLine);
				TestGeneratePaymentsRegistry.read(is, lineFeed.length);
				// 128 - is a size of SHA-1 signature
				byte[] signature = TestGeneratePaymentsRegistry.read(is, 128);

				byte[] readBytes;
				do {
					readBytes = TestGeneratePaymentsRegistry.read(is, lineFeed.length);
				} while ("\n".equals(new String(readBytes, FILE_ENCODING)));

				assertEquals("Incorrect start second service line", "_", new String(readBytes, FILE_ENCODING));

                serviceLine = "_" + new String(TestGeneratePaymentsRegistry.read(is, SERVICE_LINE.length() - 1));
                assertEquals("Incorrect second service line", SERVICE_LINE, serviceLine);

                TestGeneratePaymentsRegistry.read(is, lineFeed.length);

				try {
					byte[] buffer = new byte[1024];
					int nRead;
					while ((nRead = is.read(buffer)) != -1) {
						sign.update(buffer, 0, nRead);
					}
					assertTrue("Digital signature failed", sign.verify(signature));
				} catch (SignatureException ex) {
					throw new RuntimeException(ex);
				}
			}

		});
	}

	private static byte[] read(InputStream is, int n) throws IOException {

		byte[] bytes = new byte[n];

		for (int i = 0; i < n; ++i) {
			bytes[i] = (byte) is.read();
		}
		return bytes;
	}

	private static Signature readPublicSignature(String key) throws Exception {

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

		ByteArrayOutputStream os = new ByteArrayOutputStream(BUFFER_SIZE);
		IOUtils.copy(is, os);
		return os.toByteArray();
	}
}
