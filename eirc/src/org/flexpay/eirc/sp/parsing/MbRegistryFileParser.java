package org.flexpay.eirc.sp.parsing;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.eirc.persistence.EircRegistryProperties;
import org.flexpay.eirc.service.*;
import org.flexpay.eirc.sp.MbFileParser;
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
import java.util.Date;

@Transactional (readOnly = true)
public class MbRegistryFileParser extends MbFileParser<Registry> {

	private RegistryService registryService;
	private RegistryRecordService registryRecordService;
	private SpRegistryTypeService registryTypeService;
	private ServiceProviderService serviceProviderService;
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
					registry.setRecordsNumber((long) lineNum - 2);
					break;
				} else {
					RegistryRecord record = parseRecord(line, serviceProvider);
					record.setRegistry(registry);
					registryRecordService.create(record);
				}

			}

			registry = registryService.update(registry);

		} catch (IOException e) {
			log.error("Error with reading file", e);
		} finally {
			IOUtils.closeQuietly(reader);
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

	private RegistryRecord parseRecord(String line, ServiceProvider serviceProvider) throws FlexPayException {
		String[] fields = line.split("=");
		Long income = Long.parseLong(fields[1]);
		Long saldo = Long.parseLong(fields[2]);

		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setOrder(0);
		container.setData("100::0:" + saldo +":::" + income + ":::");

		RegistryRecord record = new RegistryRecord();
		record.setRecordStatus(statusLoaded);
		record.setAmount(BigDecimal.valueOf(saldo));
		record.getContainers().add(container);
		record.setServiceCode(fields[3]);
		record.setPersonalAccountExt(fields[4]);
		try {
			record.setOperationDate(OPERATION_DATE_FORMAT.parse(fields[5]));
		} catch (ParseException e) {
			// do nothing
		}

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
