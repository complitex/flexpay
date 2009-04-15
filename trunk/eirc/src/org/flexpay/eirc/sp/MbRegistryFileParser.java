package org.flexpay.eirc.sp;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircRegistryProperties;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.service.*;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

@Transactional (readOnly = true)
public class MbRegistryFileParser implements FileParser {

	private Logger log = LoggerFactory.getLogger(getClass());

	public final static String LAST_FILE_STRING_BEGIN = "999999999";
	public final static String REGISTRY_FILE_ENCODING = "Cp866";
	public final static DateFormat OPERATION_DATE_FORMAT = new SimpleDateFormat("MMyy");

	private RegistryRecordStatus statusLoaded;

	private RegistryService registryService;
	private RegistryRecordService registryRecordService;
	private SpRegistryTypeService registryTypeService;
	private ServiceProviderService serviceProviderService;
	private ConsumerService consumerService;
	private SpRegistryRecordStatusService spRegistryRecordStatusService;
	private SpRegistryStatusService spRegistryStatusService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private PropertiesFactory propertiesFactory;
	private Validator validator;

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	public Registry parse(@NotNull FPFile spFile) throws FlexPayException {

		if (validator != null) {
			log.info("Starting validation MB registry file...");
			validator.validate(spFile);
			log.info("MB registry file validation completed");
		}

		log.info("Starting parsing file: {}", spFile.getOriginalName());

		long beginTime = System.currentTimeMillis();

		File file = spFile.getFile();
		if (file == null) {
			log.debug("Incorrect spFile: can't find file on server (spFile.id = {})", spFile.getId());
			throw new FlexPayException("For FPFile (id = " + spFile.getId() + ") not found temp file: " + spFile.getNameOnServer());
		}

		Registry registry = new Registry();

		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), REGISTRY_FILE_ENCODING), 500);

			statusLoaded = spRegistryRecordStatusService.findByCode(RegistryRecordStatus.LOADED);
			if (statusLoaded == null) {
				throw new FlexPayException("Can't get registry record status \"loaded\" from database");
			}

			registry.setCreationDate(new Date());
			registry.setSpFile(spFile);
			registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_QUITTANCE));
			registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
			registry.setRegistryStatus(spRegistryStatusService.findByCode(RegistryStatus.CREATED));

			ServiceProvider serviceProvider = null;

			for (int lineNum = 0;;lineNum++) {
				String l = reader.readLine();
				if (l == null) {
					log.debug("End of file, lineNum = {}", lineNum);
					break;
				}
				String line = new String(l.getBytes("UTF-8"));
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
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// do nothing
			}
		}

		long endTime = System.currentTimeMillis();
		long time = (endTime - beginTime) / 1000;

		log.info("Parsing file {} finished in {}", spFile.getOriginalName(), (time / 60 + "m " + time % 60 + "s"));

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

		Consumer consumer = consumerService.findConsumer(serviceProvider, fields[4], fields[3]);
		if (consumer == null) {
			throw new FlexPayException("Incorrect record in file (can't parse external account number  " + fields[4] + ")");
		}
		EircRegistryRecordProperties recordProperties = (EircRegistryRecordProperties) propertiesFactory.newRecordProperties();
		recordProperties.setConsumer(consumer);
		record.setProperties(recordProperties);

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
	public void setSpRegistryRecordStatusService(SpRegistryRecordStatusService spRegistryRecordStatusService) {
		this.spRegistryRecordStatusService = spRegistryRecordStatusService;
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

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
