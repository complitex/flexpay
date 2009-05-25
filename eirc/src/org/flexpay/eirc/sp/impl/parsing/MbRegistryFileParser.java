package org.flexpay.eirc.sp.impl.parsing;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.eirc.persistence.EircRegistryProperties;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.sp.impl.MbFileParser;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class MbRegistryFileParser extends MbFileParser {

	public static final String OPERATION_DATE_FORMAT = "MMyy";
	public static final String INCOME_PERIOD_DATE_FORMAT = "MMyy";

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	protected Registry parseFile(@NotNull FPFile spFile) throws FlexPayException {

		Registry registry = new Registry();

		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(spFile.getFile()), REGISTRY_FILE_ENCODING), 500);

			registry.setCreationDate(new Date());
			registry.setSpFile(spFile);
			registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_INCOME));
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
			Date dateFrom = new SimpleDateFormat(INCOME_PERIOD_DATE_FORMAT).parse(fields[2]);
			registry.setFromDate(dateFrom);
			Calendar c = Calendar.getInstance();
			c.setTime(dateFrom);
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
			c.setTime(new Date(c.getTime().getTime() - 24*60*60*1000));
			registry.setTillDate(c.getTime());
		} catch (Exception e) {
			// do nothing
		}

		return registry;
	}

	private long parseRecord(String line, Registry registry) throws FlexPayException {
		String[] fields = line.split("=");

		RegistryRecord record = new RegistryRecord();
		record.setRecordStatus(statusLoaded);
		record.setAmount(new BigDecimal(fields[2]));
		record.setServiceCode("#" + fields[3]);
		record.setPersonalAccountExt(fields[4]);
		record.setOperationDate(new Date());
		record.setRegistry(registry);

		record.setLastName("");
		record.setMiddleName("");
		record.setFirstName("");
		record.setCity("");
		record.setStreetType("");
		record.setStreetName("");
		record.setBuildingNum("");
		record.setBuildingBulkNum("");
		record.setApartmentNum("");

		try {
			record.setOperationDate(new SimpleDateFormat(OPERATION_DATE_FORMAT).parse(fields[5]));
		} catch (ParseException e) {
			// do nothing
		}

		List<RegistryRecordContainer> containers = new ArrayList<RegistryRecordContainer>();

		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setOrder(0);
		container.setData("100::0:" + fields[2] +":::" + fields[1] + "::::");
		container.setRecord(record);
		containers.add(container);

		record.setContainers(containers);

		EircRegistryRecordProperties registryProperties = (EircRegistryRecordProperties) propertiesFactory.newRecordProperties();
		registryProperties.setRecord(record);
		record.setProperties(registryProperties);
		registryRecordService.create(record);

		return 1;
	}

}
