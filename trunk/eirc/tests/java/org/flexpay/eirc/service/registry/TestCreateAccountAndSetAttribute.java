package org.flexpay.eirc.service.registry;

import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessor;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import static org.flexpay.ab.persistence.TestData.*;
import static org.flexpay.orgs.test.TestData.*;
import static org.flexpay.payments.test.TestData.*;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.service.SPService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.collection.PersistentList;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collections;

public class TestCreateAccountAndSetAttribute extends EircSpringBeanAwareTestCase {

	@Autowired
	private ServiceProviderFileProcessor serviceProviderFileProcessor;
	@Autowired
	private RegistryService registryService;
	@Autowired
	private RegistryRecordService recordService;
	@Autowired
	private RegistryTypeService typeService;
	@Autowired
	private RegistryStatusService statusService;
	@Autowired
	private RegistryArchiveStatusService archiveStatusService;
	@Autowired
	private RegistryRecordStatusService recordStatusService;
	@Autowired
	private PropertiesFactory propertiesFactory;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private ServiceProviderService providerService;
	@Autowired
	private SPService spService;
	@Autowired
	private ApartmentService apartmentService;

	@Test
	public void testProcessing() throws Exception {

		Registry registry = createRegistry();
		RegistryRecord record = createRecord(registry);

		// add create account and change ERC account number containers
		PersistentList list = (PersistentList) record.getContainers();
		record.addContainer(new RegistryRecordContainer("1:01062009::"));
		record.addContainer(new RegistryRecordContainer("15:01062009::ERC0808080:" + getMBOrganizationStub().getId()));
		recordService.update(record);

		serviceProviderFileProcessor.processRegistry(registry);
	}

	private RegistryRecord createRecord(Registry registry) throws ParseException, FlexPayException {
		RegistryRecord record = new RegistryRecord();
		record.setRegistry(registry);

		record.setServiceCode(SRV_KVARPLATA.getId().toString());
		// лиц. счет поставщика услуг
		record.setPersonalAccountExt("080080080");
		record.setCity("Н-ск");
		record.setStreetType("ул");
		record.setStreetName("Ивнова");
		record.setBuildingNum("27");
		record.setBuildingBulkNum("");
		record.setApartmentNum("2");
		record.setFirstName("Василий");
		record.setMiddleName("Иванович");
		record.setLastName("Иванов");
		record.setOperationDate(DateUtil.parseDate("2009/06/01"));
		record.setUniqueOperationNumber(1L);
		record.setAmount(BigDecimal.ZERO);

		record.setRecordStatus(recordStatusService.findByCode(RegistryRecordStatus.LOADED));

		EircRegistryRecordProperties properties = (EircRegistryRecordProperties) propertiesFactory.newRecordProperties();
		properties.setApartment(apartmentService.readFull(IVANOVA_27_1));
		properties.setPerson(null);
		properties.setService(spService.readFull(SRV_KVARPLATA));

		record.setProperties(properties);

		record.setImportError(null);
		recordService.create(record);
		return record;
	}

	private Stub<Organization> getMBOrganizationStub() {
		return ApplicationConfig.getMbOrganizationStub();
	}

	private Registry createRegistry() throws ParseException, FlexPayException {
		Registry registry = new Registry();
		registry.setRegistryNumber(10L);
		registry.setRecordsNumber(1L);
		registry.setCreationDate(DateUtil.parseDate("2009/07/29"));
		registry.setFromDate(DateUtil.parseDate("2009/06/01"));
		registry.setTillDate(DateUtil.parseDate("2009/06/30"));
		registry.setSenderCode(ORG_CN.getId());
		registry.setRecipientCode(ApplicationConfig.getSelfOrganization().getId());
		registry.setAmount(BigDecimal.ZERO);

		registry.setSpFile(null);
		registry.setRegistryType(typeService.findByCode(RegistryType.TYPE_INFO));
		registry.setRegistryStatus(statusService.findByCode(RegistryStatus.LOADED));
		registry.setArchiveStatus(archiveStatusService.findByCode(RegistryArchiveStatus.NONE));

		EircRegistryProperties properties = (EircRegistryProperties) propertiesFactory.newRegistryProperties();
		properties.setSender(organizationService.readFull(new Stub<Organization>(registry.getSenderCode())));
		properties.setRecipient(organizationService.readFull(new Stub<Organization>(registry.getRecipientCode())));
		properties.setServiceProvider(providerService.getProvider(new Stub<Organization>(registry.getSenderCode())));
		registry.setProperties(properties);

		registryService.create(registry);
		return registry;
	}
}
