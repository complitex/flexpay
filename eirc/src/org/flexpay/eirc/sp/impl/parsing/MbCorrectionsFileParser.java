package org.flexpay.eirc.sp.impl.parsing;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.sp.impl.MbFileParser;
import org.flexpay.eirc.sp.impl.MbFileValidator;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class MbCorrectionsFileParser extends MbFileParser {

	private final String MODIFICATIONS_START_DATE_FORMAT = "ddMMyy";

	@Transactional (propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	protected List<Registry> parseFile(@NotNull FPFile spFile) throws FlexPayException {

		Registry infoRegistry = new Registry();
		infoRegistry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_QUITTANCE));
		List<Registry> registries = CollectionUtils.list(infoRegistry);

		BufferedReader reader = null;

		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			reader = new BufferedReader(new InputStreamReader(spFile.getInputStream(), REGISTRY_FILE_ENCODING));
			initRegistry(spFile, infoRegistry);

			Logger plog = ProcessLogger.getLogger(getClass());
			StopWatch watch = new StopWatch();
			watch.start();

			long recordsNum = 0;
			int lineNum = 0;
			List<RegistryRecord> recordStack = CollectionUtils.list();
			for (; ; lineNum++) {
				String line = reader.readLine();
				if (line == null) {
					log.debug("End of file, lineNum = {}", lineNum);
					break;
				}
				if (lineNum == 0) {
				} else if (lineNum == 1) {
					parseHeader(line, registries);
				} else if (line.startsWith(FOOTER_MARKER)) {
					infoRegistry.setRecordsNumber(recordsNum);
					log.info("Total {} records created", recordsNum);
					break;
				} else {
					recordsNum += parseRecord(line, infoRegistry, recordStack);
					if (recordsNum % 1000 == 0) {
						log.info("{} records created, {} lines processed", recordsNum, lineNum - 1);
					}
				}

				if (recordStack.size() >= 50) {
					flushRecordStack(recordStack);
				}

				if (lineNum % 100 == 0) {
					plog.info("Parsed {} records, time spent {}", lineNum, watch);
				}
			}

			flushRecordStack(recordStack);

			List<Registry> result = CollectionUtils.list();
			for (Registry registry : registries) {
				registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADED));
				registryService.update(registry);
				result.add(registry);
			}

			watch.stop();
			if (plog.isInfoEnabled()) {
				plog.info("Registry parse completed, total lines {}, total records {}, total time {}",
						new Object[]{lineNum, recordsNum, watch});
			}

		} catch (IOException e) {
			log.error("Error reading file " + spFile, e);
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return registries;
	}

	private void flushRecordStack(List<RegistryRecord> records) {
		registryRecordService.create(records);
		records.clear();
	}

	private void initRegistry(FPFile spFile, Registry registry) {
		registry.setCreationDate(new Date());
		registry.setSpFile(spFile);
		registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADING));
	}

	private void parseHeader(String line, List<Registry> registries) throws FlexPayException {
		String[] fields = line.split("=");
		for (Registry registry : registries) {
			parseHeader(fields, registry);
			registryService.create(registry);
		}
	}

	private void parseHeader(String[] fields, Registry registry) throws FlexPayException {
		log.debug("Getting service provider with id = {} from DB", fields[1]);
		Stub<ServiceProvider> providerStub = correctionsService.findCorrection(
				fields[1], ServiceProvider.class, megabankSD);
		if (providerStub == null) {
			throw new FlexPayException("No service provider correction with id " + fields[1]);
		}
		ServiceProvider serviceProvider = serviceProviderService.read(providerStub);
		if (serviceProvider == null) {
			throw new FlexPayException("Incorrect header line (can't find service provider with id " + fields[1] + ")");
		}

		EircRegistryProperties registryProperties = (EircRegistryProperties) propertiesFactory.newRegistryProperties();
		registry.setProperties(registryProperties);
		registryProperties.setServiceProvider(serviceProvider);
		registry.setSenderCode(serviceProvider.getOrganizationStub().getId());
		registry.setRecipientCode(ApplicationConfig.getSelfOrganization().getId());

		try {
			Date period = new SimpleDateFormat(FILE_CREATION_DATE_FORMAT).parse(fields[2]);
			registry.setFromDate(period);
			registry.setTillDate(period);
		} catch (ParseException e) {
			// do nothing
		}
	}

	private long parseRecord(String line, Registry registry, List<RegistryRecord> recordStack) throws FlexPayException {
		String[] fields = line.split("=");

		String[] serviceCodes = fields[20].split(";");

		long count = 0;
		for (String serviceCode : serviceCodes) {
			if (StringUtils.isEmpty(serviceCode) || "0".equals(serviceCode)) {
				return 0;
			}
			RegistryRecord record = newRecord(registry, fields, serviceCode);
			recordStack.add(record);

			// check if consumer already exists and does not create account
			EircRegistryRecordProperties recProps = (EircRegistryRecordProperties) record.getProperties();
			Consumer consumer = consumerService.findConsumer(record.getPersonalAccountExt(), recProps.getServiceStub());
			if (consumer == null) {
				log.debug("No consumer found, adding creation record {}", record.getPersonalAccountExt());
				createAccountRecord(record, fields);
				++count;
				record = newRecord(registry, fields, serviceCode);
				recordStack.add(record);
			} else {
				recProps.setConsumer(consumer);
			}

			createRecord(record, fields);
			++count;
		}

		return count;
	}

	private void setBuildingAddress(RegistryRecord record, String addr) throws FlexPayException {
		String[] parts = MbFileValidator.parseBuildingAddress(addr);
		record.setBuildingNum(parts[0]);
		if (parts.length > 1) {
			record.setBuildingBulkNum(parts[1]);
		}
	}

	private String getModificationDate(String field) {

		try {
			return new SimpleDateFormat("ddMMyyyy").format(
					new SimpleDateFormat(MODIFICATIONS_START_DATE_FORMAT).parse(field));
		} catch (ParseException e) {
			return "";
		}
	}

	private long createAccountRecord(RegistryRecord record, String[] fields) throws FlexPayException {

		List<RegistryRecordContainer> containers = CollectionUtils.list();

		String modificationStartDate = getModificationDate(fields[19]);
		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setOrder(1);
		container.setData("1:" + modificationStartDate + "::");
		container.setRecord(record);
		containers.add(container);

		record.setContainers(containers);

		registryRecordService.create(record);

		return 1;
	}

	private RegistryRecord createRecord(RegistryRecord record, String[] fields) {

		List<RegistryRecordContainer> containers = CollectionUtils.list();
		String modificationStartDate = getModificationDate(fields[19]);

		// ФИО
		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setOrder(3);
		container.setData("3:" + modificationStartDate + "::" + fields[2]);
		container.setRecord(record);
		containers.add(container);

		// Количество проживающих
		RegistryRecordContainer container1 = new RegistryRecordContainer();
		container1.setOrder(4);
		container1.setData("4:" + modificationStartDate + "::" + fields[15]);
		container1.setRecord(record);
		containers.add(container1);

		// Площадь общая
		RegistryRecordContainer container2 = new RegistryRecordContainer();
		container2.setOrder(5);
		container2.setData("5:" + modificationStartDate + "::" + fields[10]);
		container2.setRecord(record);
		containers.add(container2);

		// Площадь жилая
		RegistryRecordContainer container3 = new RegistryRecordContainer();
		container3.setOrder(6);
		container3.setData("6:" + modificationStartDate + "::" + fields[11]);
		container3.setRecord(record);
		containers.add(container3);

		// Тип льготы
		RegistryRecordContainer container4 = new RegistryRecordContainer();
		container4.setOrder(8);
		container4.setData("8:" + modificationStartDate + "::" + fields[17]);
		container4.setRecord(record);
		containers.add(container4);

		// ФИО носителя льготы
		RegistryRecordContainer container5 = new RegistryRecordContainer();
		container5.setOrder(9);
		container5.setData("9:" + modificationStartDate + "::" + fields[26]);
		container5.setRecord(record);
		containers.add(container5);

		// Количество пользующихся льготой
		RegistryRecordContainer container6 = new RegistryRecordContainer();
		container6.setOrder(12);
		container6.setData("12:" + modificationStartDate + "::" + fields[16]);
		container6.setRecord(record);
		containers.add(container6);

		record.setContainers(containers);

		return record;
	}

	private RegistryRecord newRecord(Registry registry, String[] fields, String serviceCode) throws FlexPayException {

		RegistryRecord record = new RegistryRecord();
		record.setRegistry(registry);
		record.setProperties(propertiesFactory.newRecordProperties());

		setServiceCode(record, serviceCode);
		record.setPersonalAccountExt(fields[1]);
		record.setOperationDate(new Date());

		record.setLastName(fields[2]);
		record.setMiddleName("");
		record.setFirstName("");
		record.setCity("ХАРЬКОВ");
		record.setStreetType(fields[6]);
		record.setStreetName(fields[7]);
		setBuildingAddress(record, fields[8]);
		record.setApartmentNum(fields[9]);
		record.setRecordStatus(statusLoaded);

		return record;
	}

	private void setServiceCode(RegistryRecord record, String mbServiceTypeCode) throws FlexPayException {

		EircRegistryProperties properties = (EircRegistryProperties) record.getRegistry().getProperties();
		List<Service> services = findInternalServices(properties.getServiceProviderStub(),
				mbServiceTypeCode, record.getRegistry().getFromDate());
		if (services.size() > 1) {
			throw new FlexPayException("Cannot map type to service: " +
									   serviceTypesMapper.getInternalType(mbServiceTypeCode));
		}

		Service service = services.get(0);
		record.setServiceCode(String.valueOf(service.getId()));
		EircRegistryRecordProperties recProps = (EircRegistryRecordProperties) record.getProperties();
		recProps.setService(service);
	}

	private List<Service> findInternalServices(Stub<ServiceProvider> providerStub, String mbCode, Date date)
			throws FlexPayException {

		Stub<ServiceType> typeStub = serviceTypesMapper.getInternalType(mbCode);
		List<Service> services = spService.findServices(
				providerStub, typeStub, date);
		if (services.isEmpty()) {
			throw new FlexPayException("No service found by internal type " + typeStub + ", mb code=" + mbCode);
		}

		return services;
	}
}
