package org.flexpay.payments.export.util;

import static junit.framework.Assert.*;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.PropertiesFactory;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.*;
import org.flexpay.common.service.importexport.ImportErrorService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.*;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.process.export.util.GeneratePaymentsDBRegistry;
import org.flexpay.payments.service.*;
import org.flexpay.payments.service.importexport.imp.ClassToTypeRegistryPayments;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class TestGeneratePaymentsDBRegistry extends PaymentsSpringBeanAwareTestCase {
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
	@Qualifier ("serviceProviderService")
	private ServiceProviderService serviceProviderService;

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
	private DocumentAdditionTypeService documentAdditionTypeService;

	@Resource (name = "classToTypeRegistryPayments")
	//@Qualifier("classToTypeRegistryPayments")
	private ClassToTypeRegistryPayments classToTypeRegistryPayments;

	@Autowired
	private ImportErrorService importErrorService;

	private ServiceProvider serviceProvider;
	private Organization registerOrganization;

	private List<Operation> operations = new ArrayList<Operation>();

	private static final String TEST_USER = "test-user";

	private static final Language LANG = new Language(2L);

	static {
		LANG.setDefault(true);
	}

	@Before
	public void startUp() throws FlexPayExceptionContainer, FlexPayException {

		registerOrganization = new Organization();
		registerOrganization.setStatus(ObjectWithStatus.STATUS_ACTIVE);
		registerOrganization.setIndividualTaxNumber("112");
		registerOrganization.setKpp("224");
		registerOrganization.setJuridicalAddress("Kharkov");
		registerOrganization.setPostalAddress("Kharkov");

		OrganizationName organizationName = new OrganizationName();
		organizationName.setLang(LANG);
		organizationName.setName("test register organization name");
		registerOrganization.setName(organizationName);

		OrganizationDescription organizationDescription = new OrganizationDescription();
		organizationDescription.setLang(LANG);
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
		organizationName.setLang(LANG);
		organizationName.setName("test organization name");
		organization.setName(organizationName);

		organizationDescription = new OrganizationDescription();
		organizationDescription.setLang(LANG);
		organizationDescription.setName("test organization description");
		organization.setDescription(organizationDescription);

		organizationService.create(organization);

		//create service provider
		serviceProvider = new ServiceProvider();
		serviceProvider.setOrganization(organization);
		serviceProvider.setStatus(ServiceProvider.STATUS_ACTIVE);

		ServiceProviderDescription serviceProviderDescription = new ServiceProviderDescription();
		serviceProviderDescription.setLang(LANG);
		serviceProviderDescription.setName("test service provider description");
		serviceProvider.setDescription(serviceProviderDescription);

		serviceProviderService.create(serviceProvider);

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
		paymentsCollector.setOrganization(registerOrganization);

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
		operation.setCreatorOrganization(registerOrganization);
		operation.setOperationLevel(operationLevel);
		operation.setOperationStatus(operationStatus);
		operation.setPaymentPoint(paymentPoint);
		operation.setRegisterOrganization(registerOrganization);
		operation.setRegisterDate(new Date());
		operation.setRegisterUserName(TEST_USER);
		operationService.create(operation);

		operations.add(operation);

		//get document type
		DocumentType documentType = documentTypeService.read(DocumentType.CASH_PAYMENT);
		//get document status
		DocumentStatus documentStatus = documentStatusService.read(DocumentStatus.REGISTERED);
		//get document service
		int code = 3;
		ServiceType serviceType;
		serviceType = serviceTypeService.getServiceType(code);
		/*
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
				*/

		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setLang(LANG);
		serviceDescription.setName("description");

		Service service = new Service();
		service.setBeginDate(new Date());
		service.setEndDate(new Date(service.getBeginDate().getTime() + 10000));
		service.setServiceType(serviceType);
		service.setDescription(serviceDescription);
		service.setServiceProvider(serviceProvider);
		spService.create(service);

		//Service documentService = spService.listServices();

		//create document
		Document document = new Document();
		document.setSumm(new BigDecimal(100));
		document.setDocumentStatus(documentStatus);
		document.setOperation(operation);
		document.setCreditorOrganization(registerOrganization);
		document.setDebtorOrganization(registerOrganization);
		document.setDebtorId(registerOrganization.getId().toString());
		document.setService(service);
		if (documentType != null) {
			document.setDocumentType(documentType);
		}
		document.setOperation(operation);
		documentService.create(document);

		Set<Document> documents = CollectionUtils.set();
		documents.add(document);

		operation.setDocuments(documents);
		operationService.update(operation);

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
		assertNotNull(serviceProvider);
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
		generate.setDocumentService(documentService);
		generate.setRegistryArchiveStatusService(registryArchiveStatusService);
		generate.setRegistryRecordService(registryRecordService);
		generate.setRegistryService(registryService);
		generate.setRegistryStatusService(registryStatusService);
		generate.setRegistryTypeService(registryTypeService);
		generate.setRegistryRecordStatusService(registryRecordStatusService);
		generate.setPropertiesFactory(propertiesFactory);
		generate.setDocumentAdditionTypeService(documentAdditionTypeService);

		Registry registry = generate.createDBRegestry(spFile, serviceProvider, registerOrganization,
				new Date(currDate.getTime() + 1000), new Date(currDate.getTime() + 10000));
		assertNull(registry);
		//assertEquals(0, registry.getRecordsNumber().intValue());

		registry = generate.createDBRegestry(spFile, serviceProvider, registerOrganization,
				new Date(currDate.getTime() - 100000), new Date(currDate.getTime() + 1000));
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
}
