package org.flexpay.eirc.sp.impl.parsing;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.sp.impl.MbFileParser;
import org.flexpay.eirc.sp.impl.ParseContext;
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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class MbChargesFileParser extends MbFileParser {

	public static final String OPERATION_DATE_FORMAT = "MMyy";
	public static final String INCOME_PERIOD_DATE_FORMAT = "MMyy";

	@Transactional (propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	protected List<Registry> parseFile(@NotNull FPFile spFile) throws FlexPayException {

		Registry registry = new Registry();

		BufferedReader reader = null;

		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			reader = new BufferedReader(new InputStreamReader(spFile.getInputStream(), REGISTRY_FILE_ENCODING));
			registry.setCreationDate(new Date());
			registry.setSpFile(spFile);
			registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_QUITTANCE));
			registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
			registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADING));

			Logger plog = ProcessLogger.getLogger(getClass());
			StopWatch watch = new StopWatch();
			watch.start();

			long recordsNum = 0;
			int lineNum = 0;
			ParseContext parseContext = new ParseContext();
			parseContext.setRegistry(registry);
			parseContext.setPlog(plog);
			for (; ; lineNum++) {
				String line = reader.readLine();
				if (line == null) {
					log.debug("End of file, lineNum = {}", lineNum);
					break;
				}
				if (lineNum == 0) {
					continue;
				} else if (lineNum == 1) {
					registry = registryService.create(parseHeader(line, registry));
				} else if (line.startsWith(FOOTER_MARKER)) {
					registry.setRecordsNumber(recordsNum);
					plog.info("Total {} records created", recordsNum);
					break;
				} else {
					recordsNum += parseRecord(line, parseContext);
				}

				if (parseContext.getRecords().size() >= 50) {
					flushRecordStack(parseContext.getRecords());
				}

				if (lineNum % 100 == 0) {
					plog.info("Parsed {} records, time spent {}", lineNum, watch);
				}
			}

			registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADED));
			registry = registryService.update(registry);

			parseContext.flushLastAccountRecords();
			flushRecordStack(parseContext.getRecords());

			watch.stop();
			if (plog.isInfoEnabled()) {
				plog.info("Registry parse completed, total lines {}, total records {}, total time {}",
						new Object[]{lineNum, recordsNum, watch});
			}
		} catch (IOException e) {
			log.error("Error with reading file", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return CollectionUtils.list(registry);
	}

	private void flushRecordStack(List<RegistryRecord> records) {
		registryRecordService.create(records);
		records.clear();
	}

	private Registry parseHeader(String line, Registry registry) throws FlexPayException {
		String[] fields = line.split("=");
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
		registryProperties.setServiceProvider(serviceProvider);
		registry.setSenderCode(serviceProvider.getOrganizationStub().getId());
		registry.setRecipientCode(ApplicationConfig.getSelfOrganization().getId());
		registry.setProperties(registryProperties);

		try {
			Date dateFrom = new SimpleDateFormat(INCOME_PERIOD_DATE_FORMAT).parse(fields[2]);
			registry.setFromDate(dateFrom);
			registry.setTillDate(DateUtil.previous(DateUtil.nextMonth(dateFrom)));
		} catch (Exception e) {
			// do nothing
		}

		return registry;
	}

	private long parseRecord(String line, ParseContext context) throws FlexPayException {
		String[] fields = line.split("=");

		Date operationDate = null;
		try {
			operationDate = new SimpleDateFormat(OPERATION_DATE_FORMAT).parse(fields[5]);
		} catch (ParseException e) {
			// do nothing
		}

		if (ObjectUtils.equals(operationDate, context.getRegistry().getFromDate())) {
			RegistryRecord record = newChargesRecord(context, fields, operationDate);
			context.addLastRecord(record);
			return 1;
		}

		context.getPlog().info("Found recalculation line {}", line);
		// dates change, record is a recalculation info, update previous record's container
		List<Service> services = findInternalServices(
				context.getServiceProviderStub(),
				mbServiceTypeCode(fields), operationDate);
		RegistryRecord record = context.findLastRecordByService(accountNumberExt(fields), services.get(0));
		if (record == null) {
			throw new FlexPayException("Invalid correction line '" + line + "', previous charges record not found.");
		}
		// update recalculation field in base container
		RegistryRecordContainer container = getFirstContainer(record);
		String[] parts = container.getData().split(":");
		BigDecimal recalculation = (StringUtils.isBlank(parts[7]) ?
									BigDecimal.ZERO : new BigDecimal(parts[7]).divide(new BigDecimal("100")))
				.add(charges(fields));
		container.setData("100:" + // container type (BASE container) (1)
						  ":" + // subservice code (2)
						  parts[2] + ":" + // incoming balance (3)
						  new BigDecimal(parts[3]).add(recalculation) + ":" + // outgoing balance (4)
						  parts[4] + ":" + // expense (5)
						  parts[5] + ":" + // tariff (6)
						  parts[6] + ":" + // charges (7)
						  recalculation.toString() + ":" + // recalculation  (8)
						  ":" + // privileges sum (9)
						  ":" + // subsidy sum (10)
						  "" // payments sum (11)
		);

		return 0;
	}

	private RegistryRecordContainer getFirstContainer(RegistryRecord record) {
		return record.getContainers().get(0);
	}

	private BigDecimal charges(String[] fields) {
		return new BigDecimal(fields[1]).divide(new BigDecimal("100"));
	}

	private BigDecimal incomingBalance(String[] fields) {
		return new BigDecimal(fields[2]).divide(new BigDecimal("100"));
	}

	private String mbServiceTypeCode(String[] fields) {
		return fields[3];
	}

	private String accountNumberExt(String[] fields) {
		return fields[4];
	}

	private RegistryRecord newChargesRecord(ParseContext context, String[] fields, Date operationDate) throws FlexPayException {

		RegistryRecord record = new RegistryRecord();
		record.setRegistry(context.getRegistry());
		record.setOperationDate(operationDate);
		EircRegistryRecordProperties props = (EircRegistryRecordProperties) propertiesFactory.newRecordProperties();
		record.setProperties(props);

		record.setRecordStatus(statusLoaded);
		BigDecimal charges = charges(fields);
		BigDecimal incomingBalance = incomingBalance(fields);
		record.setAmount(incomingBalance);
		setServiceCode(record, mbServiceTypeCode(fields));
		record.setPersonalAccountExt(accountNumberExt(fields));

		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setData("100:" + // container type (BASE container) (1)
						  ":" + // subservice code (2)
						  incomingBalance.toString() + ":" + // incoming balance (3)
						  incomingBalance.add(charges).toString() + ":" + // outgoing balance (4)
						  ":" + // expense (5)
						  ":" + // tariff (6)
						  charges.toString() + ":" + // charges (7)
						  ":" + // recalculation  (8)
						  ":" + // privileges sum (9)
						  ":" + // subsidy sum (10)
						  "" // payments sum (11)
		);
		record.addContainer(container);
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
