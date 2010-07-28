package org.flexpay.eirc.sp.impl.parsing;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryFPFileTypeService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.sp.impl.MbFileParser;
import org.flexpay.eirc.sp.impl.MbParsingConstants;
import org.flexpay.eirc.sp.impl.validation.CorrectionsRecordValidator;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import static org.flexpay.payments.util.config.ApplicationConfig.getMbOrganizationStub;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional (readOnly = true)
public class MbCorrectionsFileParser extends MbFileParser {

	private static final String MODIFICATIONS_START_DATE_FORMAT = "ddMMyy";

	private String moduleName;
	private FPFileService fileService;
	private RegistryFPFileTypeService registryFPFileTypeService;

	@Transactional (propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	@Override
	protected List<Registry> parseFile(@NotNull FPFile spFile) throws FlexPayException {
		Logger plog = ProcessLogger.getLogger(getClass());

		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(spFile.toFileSource().openStream(), MbParsingConstants.REGISTRY_FILE_ENCODING));
		} catch (IOException e) {
			throw new FlexPayException("Error open file " + spFile, e);
		}

		Registry infoRegistry = new Registry();
		initRegistry(spFile, infoRegistry);

		List<Registry> registries = CollectionUtils.list(infoRegistry);

		Long totalLineNum = 0L;
		Long totalRecordsNum = 0L;

		Map<String, Object> parameters = CollectionUtils.map();
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

		List<Registry> result = CollectionUtils.list();
		for (Registry registry : registries) {
			registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADED));
			registryService.update(registry);
			result.add(registry);
		}

		if (plog.isInfoEnabled()) {
			plog.info("Registry parse completed, total lines {}, total records {}",
					new Object[]{parameters.get(ParserParameterConstants.PARAM_TOTAL_LINE_NUM),
								parameters.get(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM)});
		}

		return registries;
	}

	@SuppressWarnings ({"unchecked"})
	@Override
	@Transactional (propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	public int iterateParseFile(@NotNull BufferedReader reader, @NotNull Map<String, Object> properties) throws FlexPayException {
		List<Registry> registries = (List<Registry>)properties.get(ParserParameterConstants.PARAM_REGISTRIES);
		Long totalLineNum = (Long)properties.get(ParserParameterConstants.PARAM_TOTAL_LINE_NUM);
		Long totalRecordNum = (Long)properties.get(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM);
		Long flushNumberRegistryRecord = (Long)properties.get(ParserParameterConstants.PARAM_FLUSH_NUMBER_REGISTRY_RECORD);

		Registry infoRegistry = registries.get(0);

		int countChar = 0;

		try {
			if (totalLineNum == 0) {
				reader.skip(MbParsingConstants.FIRST_FILE_STRING_SIZE + 2);
				totalLineNum++;
				countChar += MbParsingConstants.FIRST_FILE_STRING_SIZE + 2;
			}

			List<RegistryRecord> recordStack = CollectionUtils.list();
			long iterRecordNum = 0;
			do {
				String line = reader.readLine();
				//log.debug("totalLineNum={}, line: {}", new Object[]{totalLineNum, line});
				if (line == null) {
					log.debug("End of file, lineNum = {}", totalLineNum);
					countChar = -1;
					break;
				}
				countChar += line.length() + 2;
				if (totalLineNum == 1) {
					parseHeader(line, registries);
				} else if (line.startsWith(MbParsingConstants.LAST_FILE_STRING_BEGIN)) {
					infoRegistry.setRecordsNumber(totalRecordNum);
					log.info("Total {} records created", totalRecordNum);
					countChar = -1;
					break;
				} else {
					long recordNum = parseRecord(line, infoRegistry, recordStack);
					iterRecordNum += recordNum;
					totalRecordNum += recordNum;
				}
				totalLineNum++;
			} while(iterRecordNum < flushNumberRegistryRecord);

			flushRecordStack(recordStack);

		} catch (IOException e) {
			throw new FlexPayException("Error reading file ", e);
		}

		properties.put(ParserParameterConstants.PARAM_TOTAL_LINE_NUM, totalLineNum);
		properties.put(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM, totalRecordNum);
		return countChar;
	}

	private void flushRecordStack(List<RegistryRecord> records) {
		registryRecordService.create(records);
		records.clear();
	}

	private void initRegistry(FPFile spFile, Registry registry) throws FlexPayException {
		registry.setCreationDate(new Date());
		registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_INFO));
		registry.getFiles().put(registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT), spFile);
		registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADING));
		registry.setModule(fileService.getModuleByName(moduleName));
	}

	private void parseHeader(String line, List<Registry> registries) throws FlexPayException {
		String[] fields = lineParser.parse(line);
		for (Registry registry : registries) {
			parseHeader(fields, registry);
			registryService.create(registry);
		}
	}

	private void parseHeader(String[] fields, Registry registry) throws FlexPayException {
		log.debug("fields: {}", fields);
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
			Date period = new SimpleDateFormat(MbParsingConstants.FILE_CREATION_DATE_FORMAT).parse(fields[2]);
			registry.setFromDate(period);
			registry.setTillDate(period);
		} catch (ParseException e) {
			// do nothing
		}
	}

	/*
	private String[] parseRecordLine(String line) {
		return StringUtils.splitByWholeSeparator(line, "=", 28);
	}
	*/

	private long parseRecord(String line, Registry registry, List<RegistryRecord> recordStack) throws FlexPayException {
		log.debug("Parse line: {}", line);

		String[] fields = lineParser.parse(line);

		if (fields.length == CorrectionsRecordValidator.FIELDS_LENGTH_SKIP_RECORD) {
			log.debug("Skip record: {}", line);
			return 0;
		}

		// remove duplicates in service codes
		Set<String> serviceCodes = CollectionUtils.set(fields[20].split(";"));

		long count = 0;
		for (String serviceCode : serviceCodes) {
			if (StringUtils.isEmpty(serviceCode) || "0".equals(serviceCode)) {
				return 0;
			}
			RegistryRecord record = newRecord(registry, fields, serviceCode);

			//In processing operation check if consumer already exists and does not create account
			addCreateAccountContainer(record, fields);

			setInfoContainers(record, fields);
			if (!record.getContainers().isEmpty()) {
				++count;
				recordStack.add(record);
			}
		}

		return count;
	}

	private void setBuildingAddress(RegistryRecord record, String addr) throws FlexPayException {
		String[] parts = parseBuildingAddress(addr);
		record.setBuildingNum(parts[0]);
		if (parts.length > 1) {
			record.setBuildingBulkNum(parts[1]);
		}
	}

	protected String[] parseBuildingAddress(String mbBuidingAddress) throws FlexPayException {
		String[] parts = StringUtils.split(mbBuidingAddress, ' ');
		if (parts.length > 1 && parts[1].startsWith(MbParsingConstants.BUILDING_BULK_PREFIX)) {
			parts[1] = parts[1].substring(MbParsingConstants.BUILDING_BULK_PREFIX.length());
		}
		return parts;
	}

	private String getModificationDate(String field) {

		try {
			return new SimpleDateFormat("ddMMyyyy").format(
					new SimpleDateFormat(MODIFICATIONS_START_DATE_FORMAT).parse(field));
		} catch (ParseException e) {
			return "";
		}
	}

	private long addCreateAccountContainer(RegistryRecord record, String[] fields) {

		String modificationStartDate = getModificationDate(fields[19]);
		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setData("1:" + modificationStartDate + "::");
		record.addContainer(container);

		return 1;
	}

	private String getErcAccount(String[] fields) {
		return fields[0];
	}

	private RegistryRecord setInfoContainers(RegistryRecord record, String[] fields) {

		String modificationStartDate = getModificationDate(fields[19]);

		RegistryRecordContainer container = new RegistryRecordContainer();
		container.setData("15:" + modificationStartDate + "::" + getErcAccount(fields) +
						  ":" + getMbOrganizationStub().getId());
		record.addContainer(container);

		// ФИО
		container = new RegistryRecordContainer();
		container.setData("3:" + modificationStartDate + "::" + fields[2]);
		record.addContainer(container);

		// Количество проживающих
		if (StringUtils.isNotEmpty(fields[15]) && !"0".equals(fields[15])) {
			container = new RegistryRecordContainer();
			container.setData("4:" + modificationStartDate + "::" + fields[15]);
			record.addContainer(container);
		}

		// Площадь общая
		if (StringUtils.isNotEmpty(fields[10]) && !"0.00".equals(fields[10])) {
			container = new RegistryRecordContainer();
			container.setData("5:" + modificationStartDate + "::" + fields[10]);
			record.addContainer(container);
		}

		// Площадь жилая
		if (StringUtils.isNotEmpty(fields[11]) && !"0.00".equals(fields[11])) {
			container = new RegistryRecordContainer();
			container.setData("6:" + modificationStartDate + "::" + fields[11]);
			record.addContainer(container);
		}

		// Тип льготы
		if (StringUtils.isNotEmpty(fields[17]) && !"0".equals(fields[17])) {
			container = new RegistryRecordContainer();
			container.setData("8:" + modificationStartDate + "::" + fields[17]);
			record.addContainer(container);
		}

		// ФИО носителя льготы
		if (fields.length != CorrectionsRecordValidator.FIELDS_LENGTH_EMPTY_FOOTER &&
				StringUtils.isNotEmpty(fields[26]) && !"0".equals(fields[26])) {
			container = new RegistryRecordContainer();
			container.setData("9:" + modificationStartDate + "::" + fields[26]);
			record.addContainer(container);
		}

		// Количество пользующихся льготой
		if (StringUtils.isNotEmpty(fields[16]) && !"0".equals(fields[16])) {
			container = new RegistryRecordContainer();
			container.setData("12:" + modificationStartDate + "::" + fields[16]);
			record.addContainer(container);
		}

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
		record.setTownName("ХАРЬКОВ");
		record.setStreetType(fields[6]);
		record.setStreetName(fields[7]);
		setBuildingAddress(record, fields[8]);
		record.setApartmentNum(fields[9]);
		if (statusLoaded == null) {
			statusLoaded = registryRecordStatusService.findByCode(RegistryRecordStatus.LOADED);
			if (statusLoaded == null) {
				throw new FlexPayException("Can't get registry record status \"loaded\" from database");
			}
		}
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
		List<Service> services = spService.findServices(providerStub, typeStub);
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
