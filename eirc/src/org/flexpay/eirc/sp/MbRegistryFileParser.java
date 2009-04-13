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
import java.util.Date;

@Transactional (readOnly = true)
public class MbRegistryFileParser {

	private Logger log = LoggerFactory.getLogger(getClass());

	public final static String FIRST_FILE_STRING =
			"                                                                                                    "
			+ "                                                                                                    "
			+ "                                                                                                    ";

	public final static String LAST_FILE_STRING_BEGIN = "999999999";

	public final static String OPERATION_DATE_FORMAT = "MMyy";
	public final static String REGISTRY_FILE_ENCODING = "Cp866";

	private Long totalSaldoSumm = 0L;
	private Long totalIncomeSumm = 0L;
	private int lineNum = 0;

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

	@SuppressWarnings ({"ConstantConditions"})
	@Transactional(propagation = Propagation.REQUIRED)
	public Registry parse(@NotNull FPFile spFile) throws Exception {

		log.info("Starting parsing file: {}", spFile.getOriginalName());

		long beginTime = System.currentTimeMillis();
		File file = spFile.getFile();
		if (file == null) {
			log.debug("Incorrect spFile: can't find file on server (spFile.id = {})", spFile.getId());
			throw new FileNotFoundException("For FPFile (id = " + spFile.getId() + ") not found temp file: " + spFile.getNameOnServer());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), REGISTRY_FILE_ENCODING), 500);

		Registry registry = new Registry();

		try {

			statusLoaded = spRegistryRecordStatusService.findByCode(RegistryRecordStatus.LOADED);
			if (statusLoaded == null) {
				throw new Exception("Can't get registry record status \"loaded\" from database");
			}

			ServiceProvider serviceProvider = parseHeader(reader);

			EircRegistryProperties registryProperties = (EircRegistryProperties) propertiesFactory.newRegistryProperties();
			registryProperties.setServiceProvider(serviceProvider);

			registry.setCreationDate(new Date());
			registry.setSpFile(spFile);
			registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_QUITTANCE));
			registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
			registry.setRegistryStatus(spRegistryStatusService.findByCode(RegistryStatus.CREATED));
			registry.setProperties(registryProperties);

			registry = registryService.create(registry);

			for (;;lineNum++) {
				String l = reader.readLine();
				if (l == null) {
					log.debug("Error: end of file, lineNum = {}", lineNum);
					break;
				}
				String line = new String(l.getBytes("UTF-8"));
				if (line.startsWith(LAST_FILE_STRING_BEGIN)) {
					int recordsNumber = parseFooter(line);
					if (lineNum - 1 != recordsNumber) {
						throw new FlexPayException("Invalid data in file (incorrect records number in file - " + recordsNumber + ")");
					}
					break;
				}
				RegistryRecord record = parseRecord(line, serviceProvider);
				record.setRegistry(registry);

				registryRecordService.create(record);

			}

			registry.setRecordsNumber((long) lineNum - 1);

			registryService.update(registry);

		} catch (IOException e) {
			log.error("Error with reading file", e);
			return null;
		} catch (FlexPayException e) {
			log.warn("Incorrect file (line number {}): {}", (lineNum + 1), e);
			return null;
		} finally {
			reader.close();
		}

		long endTime = System.currentTimeMillis();
		long time = (endTime - beginTime) / 1000;

		log.info("Parsing file {} finished in {}", spFile.getOriginalName(), (time / 60 + "m " + time % 60 + "s"));

		return registry;

	}

	private ServiceProvider parseHeader(BufferedReader reader) throws IOException, FlexPayException {
		ServiceProvider serviceProvider = null;
		for (;;lineNum++) {
			String l = reader.readLine();
			if (l == null) {
				throw new FlexPayException("Can't read file line");
			}
			String line = new String(l.getBytes("UTF-8"));
			if (lineNum == 0) {
				if (line.length() != 300) {
					throw new FlexPayException("First line must be equals 300 spaces");
				}
			} else if (lineNum == 1) {
				String[] fields = line.split("=");
				if (fields.length != 4) {
					throw new FlexPayException("Incorrect header line (not 4 fields)");
				}
				try {
					serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(Long.parseLong(fields[1])));
				} catch (NumberFormatException e) {
					throw new FlexPayException("Incorrect header line (can't parse service provider id " + fields[1] + ")");
				}
				if (serviceProvider == null) {
					throw new FlexPayException("Incorrect header line (can't find service provider with id " + fields[1] + ")");
				}
				break;
			}
		}
		return serviceProvider;
	}

	private RegistryRecord parseRecord(String line, ServiceProvider serviceProvider) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 6) {
			throw new FlexPayException("Incorrect record in file (not 6 fields)");
		}
		Long income;
		try {
			income = Long.parseLong(fields[1]);
			totalIncomeSumm += income;
		} catch (Exception e) {
			throw new FlexPayException("Incorrect record in file (can't parse summ " + fields[1] + ")");
		}
		Long saldo;
		try {
			saldo = Long.parseLong(fields[2]);
			totalSaldoSumm += saldo;
		} catch (Exception e) {
			throw new FlexPayException("Incorrect record in file (can't parse saldo summ " + fields[2] + ")");
		}

		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setOrder(0);
		container.setData("100::0:" + saldo +":::" + income + ":::");

		RegistryRecord record = new RegistryRecord();
		record.setAmount(BigDecimal.valueOf(saldo));
		record.getContainers().add(container);
		record.setServiceCode(fields[3]);
		record.setPersonalAccountExt(fields[4]);

		Consumer consumer = consumerService.findConsumer(serviceProvider, fields[4], fields[3]);
		if (consumer == null) {
			throw new FlexPayException("Incorrect record in file (can't parse external account number  " + fields[4] + ")");
		}
		EircRegistryRecordProperties recordProperties = (EircRegistryRecordProperties) propertiesFactory.newRecordProperties();
		recordProperties.setConsumer(consumer);
		record.setRecordStatus(statusLoaded);
		record.setProperties(recordProperties);

		try {
			record.setOperationDate(new SimpleDateFormat(OPERATION_DATE_FORMAT).parse(fields[5]));
		} catch (ParseException e) {
			throw new FlexPayException("Incorrect record in file (can't parse operation date " + fields[5] + ")");
		}

		return record;
	}

	private int parseFooter(String line) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 4) {
			throw new FlexPayException("Incorrect footer line (not 4 fields)");
		}
		if (!fields[0].equals(LAST_FILE_STRING_BEGIN)) {
			throw new FlexPayException("Incorrect footer line (first field must be equals 999999999)");
		}
		try {
			if (!totalIncomeSumm.equals(Long.parseLong(fields[1]))) {
				throw new FlexPayException("Invalid data in file (total income summ in footer not equals with summ of incomes in all lines - " + fields[1] + ")");
			}
		} catch (NumberFormatException e) {
			throw new FlexPayException("Incorrect footer line (can't parse total income summ " + fields[1] + ")");
		}
		try {
			if (!totalSaldoSumm.equals(Long.parseLong(fields[2]))) {
				throw new FlexPayException("Invalid data in file (total saldo summ in footer not equals with summ of saldos in all lines - " + fields[2] + ")");
			}
		} catch (NumberFormatException e) {
			throw new FlexPayException("Incorrect footer line (can't parse total saldo summ " + fields[2] + ")");
		}
		int totalLines;
		try {
			totalLines = Integer.parseInt(fields[3]);
		} catch (NumberFormatException e) {
			throw new FlexPayException("Incorrect footer line (can't parse total amount of lines in file - " + fields[3] + ")");
		}

		return totalLines;
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

}
