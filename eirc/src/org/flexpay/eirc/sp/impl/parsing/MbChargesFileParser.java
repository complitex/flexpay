package org.flexpay.eirc.sp.impl.parsing;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryFPFileTypeService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.sp.impl.MbFileParser;
import org.flexpay.eirc.sp.impl.MbParsingConstants;
import org.flexpay.eirc.sp.impl.ParseContext;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
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
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;

@Transactional (readOnly = true)
public class MbChargesFileParser extends MbFileParser {

	public static final String OPERATION_DATE_FORMAT = "MMyy";
	public static final String INCOME_PERIOD_DATE_FORMAT = "MMyy";

	private String moduleName;
	private FPFileService fileService;
    private RegistryFPFileTypeService registryFPFileTypeService;

	@Transactional (propagation = Propagation.SUPPORTS, readOnly = false)
    @Override
	protected List<Registry> parseFile(@NotNull FPFile spFile) throws FlexPayException {

		Logger plog = ProcessLogger.getLogger(getClass());

		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(spFile.toFileSource().openStream(), MbParsingConstants.REGISTRY_FILE_ENCODING));
		} catch (IOException e) {
            log.error("Can't open file {}", spFile);
			throw new FlexPayException("Error open file " + spFile, e);
		}

		Registry registry = new Registry();
		initRegistry(spFile, registry);

		List<Registry> registries = CollectionUtils.list(registry);

		Long totalLineNum = 0L;
		Long totalRecordsNum = 0L;

		Map<String, Object> parameters = map();
		parameters.put(ParserParameterConstants.PARAM_REGISTRIES, registries);
		parameters.put(ParserParameterConstants.PARAM_TOTAL_LINE_NUM, totalLineNum);
		parameters.put(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM, totalRecordsNum);
		parameters.put(ParserParameterConstants.PARAM_FLUSH_NUMBER_REGISTRY_RECORD, 50L);
		try {
			while(iterateParseFile(reader, parameters) > 0) {
				plog.info("Parsed {} lines", parameters.get(ParserParameterConstants.PARAM_TOTAL_LINE_NUM));
			}
		} finally {
			IOUtils.closeQuietly(reader);
		}

		List<Registry> result = list();
		for (Registry reg : registries) {
			reg.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADED));
			registryService.update(reg);
			result.add(reg);
		}

		if (plog.isInfoEnabled()) {
			plog.info("Registry parse completed, total lines {}, total records {}",
					new Object[]{parameters.get(ParserParameterConstants.PARAM_TOTAL_LINE_NUM),
								parameters.get(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM)});
		}

		return registries;
	}

	@SuppressWarnings ({"unchecked"})
	@Transactional (propagation = Propagation.MANDATORY, readOnly = false)
    @Override
	public int iterateParseFile(@NotNull BufferedReader reader, @NotNull Map<String, Object> properties) throws FlexPayException {
		List<Registry> registries = (List<Registry>)properties.get(ParserParameterConstants.PARAM_REGISTRIES);
		Long totalLineNum = (Long)properties.get(ParserParameterConstants.PARAM_TOTAL_LINE_NUM);
		Long totalRecordNum = (Long)properties.get(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM);
		Long flushNumberRegistryRecord = (Long)properties.get(ParserParameterConstants.PARAM_FLUSH_NUMBER_REGISTRY_RECORD);

		Registry registry = registries.get(0);
		ParseContext parseContext = new ParseContext();
		parseContext.setRegistry(registry);

		int countChar = 0;

		String line = null;
		try {
			if (totalLineNum == 0) {
				reader.skip(MbParsingConstants.FIRST_FILE_STRING_SIZE + 2);
				totalLineNum++;
				countChar += MbParsingConstants.FIRST_FILE_STRING_SIZE + 2;
			}
			do {
				line = reader.readLine();
				log.debug("totalLineNum={}, line: {}", new Object[]{totalLineNum, line});
				if (line == null) {
					log.debug("End of file, lineNum = {}", totalLineNum);
					countChar = -1;
					break;
				}
				countChar += line.length() + 2;
				if (totalLineNum == 1) {
					parseHeader(line, registry);
				} else if (line.startsWith(MbParsingConstants.LAST_FILE_STRING_BEGIN)) {
					registry.setRecordsNumber(totalRecordNum);
					log.info("Total {} records created", totalRecordNum);
					countChar = -1;
					break;
				} else {
					totalRecordNum += parseRecord(line, parseContext);
				}
				totalLineNum++;
			} while(parseContext.getRecords().size() < flushNumberRegistryRecord);

			parseContext.flushLastAccountRecords();
			flushRecordStack(parseContext.getRecords());

		} catch (IOException e) {
			throw new FlexPayException("Error reading file ", e);
		} catch (Throwable e) {
			log.error("Exception in line {}: {}", totalLineNum, line);
			throw new FlexPayException(e);
		}

		properties.put(ParserParameterConstants.PARAM_TOTAL_LINE_NUM, totalLineNum);
		properties.put(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM, totalRecordNum);
		return countChar;
	}

	private void initRegistry(FPFile spFile, Registry registry) {
		registry.setCreationDate(new Date());
		registry.getFiles().put(registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT), spFile);
		registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_QUITTANCE));
		registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADING));
		registry.setModule(fileService.getModuleByName(moduleName));
	}

	private void flushRecordStack(List<RegistryRecord> records) {
		registryRecordService.create(records);
		records.clear();
	}

	private Registry parseHeader(String line, Registry registry) throws FlexPayException {
		String[] fields = lineParser.parse(line);
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

		return registryService.create(registry);
	}

	private long parseRecord(String line, ParseContext context) throws FlexPayException {
		String[] fields = lineParser.parse(line);

		Date operationDate = null;
		if (fields.length >= 6) {
			try {
				operationDate = new SimpleDateFormat(OPERATION_DATE_FORMAT).parse(fields[5]);
			} catch (ParseException e) {
				// do nothing
			}
		}

		if (operationDate == null || ObjectUtils.equals(operationDate, context.getRegistry().getFromDate())) {
			RegistryRecord record = newChargesRecord(context, fields, context.getRegistry().getFromDate());
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
						  parts[3] + ":" + // outgoing balance (4)
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
		return (StringUtils.isNotEmpty(fields[1]))? new BigDecimal(fields[1]).divide(new BigDecimal("100")):
				new BigDecimal(0).divide(new BigDecimal("100"));
	}

	private BigDecimal outgoingBalance(String[] fields) {
		// Doc and protocol mismatch, in doc - incoming balance, actually - outgoing
		return (StringUtils.isNotEmpty(fields[1]))? new BigDecimal(fields[2]).divide(new BigDecimal("100")): 
				new BigDecimal(0).divide(new BigDecimal("100"));
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

		if (statusLoaded == null) {
			statusLoaded = registryRecordStatusService.findByCode(RegistryRecordStatus.LOADED);
			if (statusLoaded == null) {
				throw new FlexPayException("Can't get registry record status \"loaded\" from database");
			}
		}
		record.setRecordStatus(statusLoaded);
		BigDecimal charges = charges(fields);
		BigDecimal outgoingBalance = outgoingBalance(fields);
		record.setAmount(outgoingBalance);
		setServiceCode(record, mbServiceTypeCode(fields));
		record.setPersonalAccountExt(accountNumberExt(fields));

		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setData("100:" + // container type (BASE container) (1)
						  ":" + // subservice code (2)
						  "0.00" + ":" + // incoming balance (3)
						  outgoingBalance.toString() + ":" + // outgoing balance (4)
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

    @Required
    public void setRegistryFPFileTypeService(RegistryFPFileTypeService registryFPFileTypeService) {
        this.registryFPFileTypeService = registryFPFileTypeService;
    }

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Required
	public void setFileService(FPFileService fileService) {
		this.fileService = fileService;
	}
}
