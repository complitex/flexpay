package org.flexpay.eirc.sp.parsing;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.*;
import org.flexpay.eirc.sp.MbFileParser;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public class MbCorrectionsFileParser extends MbFileParser<Registry> {

	public static final String ACCOUNT_CLOSED = "ЛИЦЕВОЙ ЗАКРЫТ";
	public static final DateFormat MODIFICATIONS_START_DATE_FORMAT = new SimpleDateFormat("ddMMyy");

	private RegistryService registryService;
	private RegistryRecordService registryRecordService;
	private SpRegistryTypeService registryTypeService;
	private ServiceProviderService serviceProviderService;
	private ConsumerService consumerService;
	private SpRegistryStatusService spRegistryStatusService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private PropertiesFactory propertiesFactory;

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	public Registry parseFile(@NotNull FPFile spFile) throws FlexPayException {

		Registry registry = new Registry();

		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(spFile.getFile()), REGISTRY_FILE_ENCODING), 500);

			registry.setCreationDate(new Date());
			registry.setSpFile(spFile);
			registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_QUITTANCE));
			registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
			registry.setRegistryStatus(spRegistryStatusService.findByCode(RegistryStatus.CREATED));

			ServiceProvider serviceProvider = null;

			long recordsNum = 0;

			for (int lineNum = 0;;lineNum++) {
				String line = reader.readLine();
				if (line == null) {
					log.debug("End of file, lineNum = {}", lineNum);
					break;
				}
				if (lineNum == 0) {
				} else if (lineNum == 1) {
					serviceProvider = parseHeader(line);
					EircRegistryProperties registryProperties = (EircRegistryProperties) propertiesFactory.newRegistryProperties();
					registryProperties.setServiceProvider(serviceProvider);
					registry.setProperties(registryProperties);
					registry = registryService.create(registry);
				} else if (line.startsWith(LAST_FILE_STRING_BEGIN)) {
					registry.setRecordsNumber(recordsNum);
					break;
				} else {
					recordsNum += parseRecord(line, serviceProvider, registry);
				}

			}

			registry = registryService.update(registry);

		} catch (IOException e) {
			log.error("Error with reading file", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// do nothing
			}
		}

		return registry;

	}

	private ServiceProvider parseHeader(String line) throws FlexPayException {
		String[] fields = line.split("=");
		ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(Long.parseLong(fields[1])));
		if (serviceProvider == null) {
			throw new FlexPayException("Incorrect header line (can't find service provider with id " + fields[1] + ")");
		}
		return serviceProvider;
	}

	private long parseRecord(String line, ServiceProvider serviceProvider, Registry registry) throws FlexPayException {
		String[] fields = line.split("=");

		String modificationStartDate = "";
		try {
			modificationStartDate = new SimpleDateFormat("ddMMyyyy").format(MODIFICATIONS_START_DATE_FORMAT.parse(fields[19]));
		} catch (ParseException e) {
			// do nothing
		}

		if (ACCOUNT_CLOSED.equals(fields[2])) {
			return 0;
		}

		String[] serviceCodes = fields[20].split(";");

		for (String serviceCode : serviceCodes) {
			createRecord(serviceProvider, registry, fields, serviceCode);
		}

		return serviceCodes.length;
	}

	private RegistryRecord createRecord(ServiceProvider serviceProvider, Registry registry, String[] fields, String serviceCode) throws FlexPayException {

		Consumer consumer = consumerService.findConsumer(serviceProvider, fields[1], serviceCode);
		if (consumer == null) {
			log.debug("Can't find consumer for serviceProvider.id = {}, account = {}, service.code = {}",
					new Object[] {serviceProvider.getId(), fields[1], serviceCode});
			throw new FlexPayException("Incorrect record in file (can't find consumer)");
		}

		String modificationStartDate = "";
		try {
			modificationStartDate = new SimpleDateFormat("ddMMyyyy").format(MODIFICATIONS_START_DATE_FORMAT.parse(fields[19]));
		} catch (ParseException e) {
			// do nothing
		}

		ConsumerInfo consumerInfo = consumer.getConsumerInfo();
		consumerInfo.setLastName(fields[2]);
		consumerInfo.setStreetTypeName(fields[6]);
		consumerInfo.setStreetName(fields[7]);
		consumerInfo.setBuildingNumber(fields[8]);
		consumerInfo.setApartmentNumber(fields[9]);

		List<RegistryRecordContainer> containers = new ArrayList<RegistryRecordContainer>();

		// ФИО
		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setOrder(3);
		container.setData("3:" + modificationStartDate + "::" + fields[2]);
		containers.add(container);

		// Количество проживающих
		RegistryRecordContainer container1 = new RegistryRecordContainer();
		container1.setOrder(4);
		container1.setData("4:" + modificationStartDate + "::" + fields[15]);
		containers.add(container1);

		// Площадь общая
		RegistryRecordContainer container2 = new RegistryRecordContainer();
		container2.setOrder(5);
		container2.setData("5:" + modificationStartDate + "::" + fields[10]);
		containers.add(container2);

		// Площадь жилая
		RegistryRecordContainer container3 = new RegistryRecordContainer();
		container3.setOrder(6);
		container3.setData("6:" + modificationStartDate + "::" + fields[11]);
		containers.add(container3);

		// Тип льготы
		RegistryRecordContainer container4 = new RegistryRecordContainer();
		container4.setOrder(8);
		container4.setData("8:" + modificationStartDate + "::" + fields[17]);
		containers.add(container4);

		// ФИО носителя льготы
		RegistryRecordContainer container5 = new RegistryRecordContainer();
		container5.setOrder(9);
		container5.setData("9:" + modificationStartDate + "::" + fields[26]);
		containers.add(container5);

		// Количество пользующихся льготой
		RegistryRecordContainer container6 = new RegistryRecordContainer();
		container6.setOrder(12);
		container6.setData("12:" + modificationStartDate + "::" + fields[16]);
		containers.add(container6);

		RegistryRecord record = new RegistryRecord();
		record.setRecordStatus(statusLoaded);
		record.setContainers(containers);
		record.setPersonalAccountExt(fields[1]);

		EircRegistryRecordProperties recordProperties = (EircRegistryRecordProperties) propertiesFactory.newRecordProperties();
		recordProperties.setConsumer(consumer);
		record.setProperties(recordProperties);

		record.setRegistry(registry);
		registryRecordService.create(record);

		return record;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	@Required
	public void setRegistryTypeService(SpRegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

	@Required
	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	@Required
	public void setSpRegistryStatusService(SpRegistryStatusService spRegistryStatusService) {
		this.spRegistryStatusService = spRegistryStatusService;
	}

	@Required
	public void setRegistryArchiveStatusService(RegistryArchiveStatusService registryArchiveStatusService) {
		this.registryArchiveStatusService = registryArchiveStatusService;
	}

	@Required
	public void setPropertiesFactory(PropertiesFactory propertiesFactory) {
		this.propertiesFactory = propertiesFactory;
	}

}
