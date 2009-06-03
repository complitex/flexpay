package org.flexpay.eirc.sp.impl.parsing;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.sp.impl.MbFileParser;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public class MbCorrectionsFileParser extends MbFileParser {

	public static final String ACCOUNT_CLOSED = "ЛИЦЕВОЙ ЗАКРЫТ";
	public static final String MODIFICATIONS_START_DATE_FORMAT = "ddMMyy";

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	protected Registry parseFile(@NotNull FPFile spFile) throws FlexPayException {

		Registry registry = new Registry();

		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(FPFileUtil.getFileOnServer(spFile)), REGISTRY_FILE_ENCODING), 500);

			registry.setCreationDate(new Date());
			registry.setSpFile(spFile);
			registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_QUITTANCE));
			registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
			registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADING));

			long recordsNum = 0;

			for (int lineNum = 0;;lineNum++) {
				String line = reader.readLine();
				if (line == null) {
					log.debug("End of file, lineNum = {}", lineNum);
					break;
				}
				if (lineNum == 0) {
				} else if (lineNum == 1) {
					registry = registryService.create(parseHeader(line, registry));
				} else if (line.startsWith(LAST_FILE_STRING_BEGIN)) {
					registry.setRecordsNumber(recordsNum);
					log.info("Total {} records created", recordsNum);
					break;
/*
				} else if (lineNum == 19340 || lineNum == 19439
						|| lineNum == 19450 || lineNum == 19492
						|| lineNum == 25492 || lineNum == 25495
						|| lineNum == 25563 || lineNum == 25581
						|| lineNum == 31679 || lineNum == 31809
						|| lineNum == 38016 || lineNum == 38056
						|| lineNum == 38188 || lineNum == 43834
						|| lineNum == 43837 || lineNum == 43848
						|| lineNum == 43891 || lineNum == 44041
						|| lineNum == 44044 || lineNum == 49607
						|| lineNum == 49609 || lineNum == 49664
						|| lineNum == 49680 || lineNum == 49759
						|| lineNum == 59193 || lineNum == 75116
						|| lineNum == 75298 || lineNum == 75324
						|| lineNum == 105690 || lineNum == 108907
						|| lineNum == 136493 || lineNum == 136695
						|| lineNum == 167513 || lineNum == 167564
						|| lineNum == 167696 || lineNum == 167702
						|| lineNum == 167705 || lineNum == 167769
						|| lineNum == 167781 || lineNum == 167784
						|| lineNum == 167833 || lineNum == 198577
						|| lineNum == 198642 || lineNum == 198648
						|| lineNum == 198669 || lineNum == 229311
						|| lineNum == 229380 || lineNum == 229539
						|| lineNum == 229688 || lineNum == 259886
						|| lineNum == 289980 || lineNum == 290106
						|| lineNum == 290163 || lineNum == 290174
						|| lineNum == 290177 || lineNum == 290281
						|| lineNum == 290302 || lineNum == 290322
						|| lineNum == 290389 || lineNum == 297778
						|| lineNum == 320443 || lineNum == 320518
						|| lineNum == 320529 || lineNum == 320549
						|| lineNum == 320595 || lineNum == 320612
						|| lineNum == 320622 || lineNum == 320710
						|| lineNum == 320727 || lineNum == 355126) {
*/
				} else {
					recordsNum += parseRecord(line, registry);
					if (recordsNum % 1000 == 0) {
						log.info("{} records created, {} lines processed", recordsNum, lineNum - 1);
					}
				}

			}

			registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADED));
			registry = registryService.update(registry);

		} catch (IOException e) {
			log.error("Error with reading file", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return registry;

	}

	private Registry parseHeader(String line, Registry registry) throws FlexPayException {
		String[] fields = line.split("=");
		log.debug("Getting service provider with id = {} from DB", fields[1]);
		ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(Long.parseLong(fields[1])));
		if (serviceProvider == null) {
			throw new FlexPayException("Incorrect header line (can't find service provider with id " + fields[1] + ")");
		}

		EircRegistryProperties registryProperties = (EircRegistryProperties) propertiesFactory.newRegistryProperties();
		registryProperties.setServiceProvider(serviceProvider);
		registryProperties.setRegistry(registry);
		registry.setSenderCode(serviceProvider.getOrganization().getId());
		registry.setRecipientCode(ApplicationConfig.getSelfOrganization().getId());
		registry.setProperties(registryProperties);

		try {
			registry.setFromDate(new SimpleDateFormat(FILE_CREATION_DATE_FORMAT).parse(fields[2]));
			registry.setTillDate(new SimpleDateFormat(FILE_CREATION_DATE_FORMAT).parse(fields[2]));
		} catch (Exception e) {
			// do nothing
		}

		return registry;
	}

	private long parseRecord(String line, Registry registry) throws FlexPayException {
		String[] fields = line.split("=");

/*
		if (ACCOUNT_CLOSED.equals(fields[2])) {
			log.debug("Account closed. Skipping line");
			return 0;
		}
*/

		String[] serviceCodes = fields[20].split(";");

		for (String serviceCode : serviceCodes) {
			if (serviceCode == null || serviceCode.length() == 0 || serviceCode.equals("0")) {
				return 0;
			}
			createRecord(registry, fields, serviceCode);
//			createAccountRecord(registry, fields, serviceCode);
		}

		return serviceCodes.length;
	}

	private RegistryRecord createAccountRecord(Registry registry, String[] fields, String serviceCode) throws FlexPayException {

		String modificationStartDate = "";
		try {
			modificationStartDate = new SimpleDateFormat("ddMMyyyy").format(new SimpleDateFormat(MODIFICATIONS_START_DATE_FORMAT).parse(fields[19]));
		} catch (ParseException e) {
			// do nothing
		}

		RegistryRecord record = new RegistryRecord();
		record.setServiceCode("#" + serviceCode);
		record.setPersonalAccountExt(fields[1]);
		record.setOperationDate(new Date());
		record.setRegistry(registry);

		record.setLastName(fields[2]);
		record.setMiddleName("");
		record.setFirstName("");
		record.setCity("ХАРЬКОВ");
		record.setBuildingBulkNum("");
		record.setStreetType(fields[6]);
		record.setStreetName(fields[7]);
		record.setBuildingNum(fields[8]);
		record.setApartmentNum(fields[9]);
		record.setRecordStatus(statusLoaded);

		List<RegistryRecordContainer> containers = new ArrayList<RegistryRecordContainer>();

		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setOrder(1);
		container.setData("1:" + modificationStartDate + "::");
		container.setRecord(record);
		containers.add(container);

		record.setContainers(containers);

		EircRegistryRecordProperties registryProperties = (EircRegistryRecordProperties) propertiesFactory.newRecordProperties();
		registryProperties.setRecord(record);
		record.setProperties(registryProperties);
		record = registryRecordService.create(record);

		return record;

	}

	private RegistryRecord createRecord(Registry registry, String[] fields, String serviceCode) throws FlexPayException {

		String modificationStartDate = "";
		try {
			modificationStartDate = new SimpleDateFormat("ddMMyyyy").format(new SimpleDateFormat(MODIFICATIONS_START_DATE_FORMAT).parse(fields[19]));
		} catch (ParseException e) {
			// do nothing
		}

		RegistryRecord record = new RegistryRecord();
		record.setServiceCode("#" + serviceCode);
		record.setPersonalAccountExt(fields[1]);
		record.setOperationDate(new Date());
		record.setRegistry(registry);

		record.setLastName(fields[2]);
		record.setMiddleName("");
		record.setFirstName("");
		record.setCity("ХАРЬКОВ");
		record.setBuildingBulkNum("");
		record.setStreetType(fields[6]);
		record.setStreetName(fields[7]);
		record.setBuildingNum(fields[8]);
		record.setApartmentNum(fields[9]);
		record.setRecordStatus(statusLoaded);

		List<RegistryRecordContainer> containers = new ArrayList<RegistryRecordContainer>();

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

		EircRegistryRecordProperties registryProperties = (EircRegistryRecordProperties) propertiesFactory.newRecordProperties();
		registryProperties.setRecord(record);
		record.setProperties(registryProperties);
		record = registryRecordService.create(record);

		return record;
	}

}
