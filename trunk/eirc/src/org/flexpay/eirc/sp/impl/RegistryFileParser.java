package org.flexpay.eirc.sp.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.RegistryWorkflowManager;
import org.flexpay.common.service.*;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.FileSource;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.sp.FileParser;
import org.flexpay.eirc.sp.RegistryFormatException;
import org.flexpay.eirc.sp.RegistryUtil;
import org.flexpay.eirc.sp.SpFileReader;
import org.flexpay.eirc.sp.SpFileReader.Message;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.EircRegistryService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional (readOnly = true)
public class RegistryFileParser implements FileParser {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String DATE_FORMAT = "ddMMyyyyHHmmss";

	private static final int MAX_CONTAINER_SIZE = 2048;

	private String moduleName;
	private EircRegistryService eircRegistryService;
	private RegistryService registryService;
	private RegistryRecordService registryRecordService;
	private RegistryTypeService registryTypeService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private SessionUtils sessionUtils;

	private RegistryWorkflowManager registryWorkflowManager;
	private RegistryRecordWorkflowManager recordWorkflowManager;

	private OrganizationService organizationService;
	private ServiceProviderService providerService;
	private ConsumerService consumerService;

	private MasterIndexService masterIndexService;
	private CorrectionsService correctionsService;
	private ClassToTypeRegistry typeRegistry;

	private PropertiesFactory propertiesFactory;
	private FPFileService fileService;
    private RegistryFPFileTypeService registryFPFileTypeService;

    @SuppressWarnings ({"ConstantConditions"})
	@Transactional (propagation = Propagation.NOT_SUPPORTED)
	@Override
    public List<Registry> parse(FPFile spFile, Logger processLog) throws Exception {

        FileSource fileSource = null;
		InputStream is = null;

		Registry registry = null;
		processLog.info("Starting parsing file: {}", spFile);

		Long[] recordCounter = {0L};
		try {
			fileSource = openRegistryFile(spFile);
			is = fileSource.openStream();
			SpFileReader reader = new SpFileReader(is);

			Message message;
			while ((message = reader.readMessage()) != null) {
				registry = processMessage(message, spFile, registry, recordCounter);

				// add some process log
				processLog.trace("Parsed {} records", recordCounter[0]);
				if (recordCounter[0] > 0 && recordCounter[0] % 100 == 0) {
					processLog.info("Parsed {} records", recordCounter[0]);
				}
			}
			finalizeRegistry(registry, recordCounter);
			sessionUtils.flush();
			sessionUtils.clear();

		} catch (Throwable t) {
			if (registry != null) {
				registryWorkflowManager.setNextErrorStatus(registry);
			}
			processLog.error("Failed parsing registry file", t);
			throw new Exception("Failed parsing registry file", t);
		} finally {
			IOUtils.closeQuietly(is);
			if (fileSource != null) {
				fileSource.close();
			}
		}

		processLog.info("File successfully parsed, total records: {}", recordCounter[0]);

		return CollectionUtils.list(registry);
    }

	@SuppressWarnings ({"ConstantConditions"})
	@Transactional (propagation = Propagation.NOT_SUPPORTED)
	@Override
	public List<Registry> parse(FPFile spFile) throws Exception {
		return parse(spFile, log);
	}

	@Override
	public int iterateParseFile(@NotNull BufferedReader reader, @NotNull Map<String, Object> properties) throws FlexPayException {
		throw new FlexPayException("Do not implement");
	}

    /**
	 * Open source registry file
	 *
	 * @param spFile Registry file
	 * @return FileSource
	 * @throws Exception if failure occurs
	 */
	private FileSource openRegistryFile(FPFile spFile) throws Exception {
		return spFile.toFileSource();
	}

	@Transactional (propagation = Propagation.NOT_SUPPORTED)
	private Registry processMessage(Message message, FPFile spFile, Registry registry, Long[] recordCounter) throws Exception {

		String messageValue = message.getBody();
		if (StringUtils.isEmpty(messageValue)) {
			return registry;
		}

		Integer messageType = message.getType();

		List<String> messageFieldList = StringUtil.splitEscapable(
				messageValue, Operation.RECORD_DELIMITER, Operation.ESCAPE_SYMBOL);

		log.debug("Message fields: {}", messageFieldList);

		if (messageType.equals(Message.MESSAGE_TYPE_HEADER)) {
			if (registry != null) {
				throw new RegistryFormatException("Not unique registry header present");
			}
			registry = processHeader(spFile, messageFieldList);
		} else if (messageType.equals(Message.MESSAGE_TYPE_RECORD)) {
			processRecord(messageFieldList, registry, recordCounter);
		} else if (messageType.equals(Message.MESSAGE_TYPE_FOOTER)) {
			processFooter(messageFieldList);
		}

		return registry;
	}

	@Transactional (readOnly = false, propagation = Propagation.REQUIRED)
	private Registry processHeader(FPFile spFile, List<String> messageFieldList) throws Exception {
		if (messageFieldList.size() < 10) {
			throw new RegistryFormatException(
					"Message header error, invalid number of fields: "
					+ messageFieldList.size() + ", expected at least 10");
		}

		log.info("Adding header: {}", messageFieldList);

		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

		Registry newRegistry = new Registry();
		newRegistry.setModule(fileService.getModuleByName(moduleName));
		newRegistry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registryWorkflowManager.setInitialStatus(newRegistry);
		newRegistry.getFiles().put(registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT), spFile);
		try {
			int n = 0;
			newRegistry.setRegistryNumber(Long.valueOf(messageFieldList.get(++n)));
			String value = messageFieldList.get(++n);
			RegistryType registryType = registryTypeService.read(Long.valueOf(value));
			if (registryType == null) {
				throw new FlexPayException("Unknown registry type field: " + value);
			}
			newRegistry.setRegistryType(registryType);
			newRegistry.setRecordsNumber(Long.valueOf(messageFieldList.get(++n)));
			newRegistry.setCreationDate(dateFormat.parse(messageFieldList.get(++n)));
			newRegistry.setFromDate(dateFormat.parse(messageFieldList.get(++n)));
			newRegistry.setTillDate(dateFormat.parse(messageFieldList.get(++n)));
			newRegistry.setSenderCode(Long.valueOf(messageFieldList.get(++n)));
			newRegistry.setRecipientCode(Long.valueOf(messageFieldList.get(++n)));
			String amountStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(amountStr)) {
				newRegistry.setAmount(new BigDecimal(amountStr));
			}
			if (messageFieldList.size() > n) {
				parseContainers(newRegistry, messageFieldList.get(++n));
			}

			log.info("Creating new registry: {}", newRegistry);

			EircRegistryProperties props = (EircRegistryProperties) propertiesFactory.newRegistryProperties();
			newRegistry.setProperties(props);

			Organization recipient = setRecipient(newRegistry, props);
			Organization sender = setSender(newRegistry, props);
			log.info("Recipient: {}\n sender: {}", recipient, sender);

			ServiceProvider provider = getProvider(newRegistry);
			if (provider == null) {
				log.error("Failed processing registry header, provider not found: #{}", newRegistry.getSenderCode());
				throw new FlexPayException("Cannot find service provider " + newRegistry.getSenderCode());
			}
			props.setServiceProvider(provider);

			validateRegistry(newRegistry);

			return registryService.create(newRegistry);
		} catch (NumberFormatException e) {
			log.error("Header parse error", e);
			throw new RegistryFormatException("Header parse error");
		} catch (ParseException e) {
			log.error("Header parse error", e);
			throw new RegistryFormatException("Header parse error");
		}
	}

	private Organization setSender(Registry registry, EircRegistryProperties props) throws FlexPayException {

		log.debug("Fetching sender via code={}", registry.getSenderCode());
		Organization sender = findOrgByRegistryCorrections(registry, registry.getSenderCode());
		if (sender == null) {
			sender = organizationService.readFull(props.getSenderStub());
		}
		props.setSender(sender);
		if (sender == null) {
			log.error("Failed processing registry header, sender not found: #{}", registry.getSenderCode());
			throw new FlexPayException("Cannot find sender organization " + registry.getSenderCode());
		}
		return sender;
	}

	private Organization setRecipient(Registry registry, EircRegistryProperties props) throws FlexPayException {
		Organization recipient;
		if (registry.getRecipientCode() == 0) {
			log.debug("Recipient is EIRC, code=0");
			recipient = organizationService.readFull(ApplicationConfig.getSelfOrganizationStub());
		} else {
			log.debug("Fetching recipient via code={}", registry.getRecipientCode());
			recipient = findOrgByRegistryCorrections(registry, registry.getRecipientCode());
			if (recipient == null) {
				recipient = organizationService.readFull(props.getRecipientStub());
			}
		}
		props.setRecipient(recipient);
		if (recipient == null) {
			log.error("Failed processing registry header, recipient not found: #{}", registry.getRecipientCode());
			throw new FlexPayException("Cannot find recipient organization " + registry.getRecipientCode());
		}
		return recipient;
	}

	@Nullable
	private Organization findOrgByRegistryCorrections(Registry registry, Long code) {

		for (RegistryContainer container : registry.getContainers()) {
			String data = container.getData();
			log.debug("Candidate: {}", data);
			if (data.startsWith("502"+Operation.CONTAINER_DATA_DELIMITER)) {
				List<String> datum = StringUtil.splitEscapable(
								data, Operation.CONTAINER_DATA_DELIMITER, Operation.ESCAPE_SYMBOL);
				// skip if correction is not for Organization type
				if (Integer.parseInt(datum.get(1)) != typeRegistry.getType(Organization.class)) {
					continue;
				}
				// skip if correction is not for the object with requested code
				if (Long.parseLong(datum.get(2)) != code) {
					continue;
				}

				if (StringUtils.isNotBlank(datum.get(4)) && "1".equals(datum.get(5))) {
					Stub<Organization> stub = correctionsService.findCorrection(
							datum.get(4), Organization.class, masterIndexService.getMasterSourceDescriptionStub());
					if (stub == null) {
						throw new IllegalStateException("Expected master correction for organization, " +
														"but not found: " + data);
					}
					log.debug("Found organization by master correction: {}", datum.get(4));
					Organization org = organizationService.readFull(stub);
					if (org == null) {
						throw new IllegalStateException("Existing master correction for organization " +
														"references nowhere: " + data);
					}
					return org;
				}
			}
		}

		return null;
	}

	private ServiceProvider getProvider(Registry registry) throws FlexPayException {
		// for payments registry assume recipient is a service provider
		if (registry.getRegistryType().isPayments()) {
			Stub<Organization> recipient = new Stub<Organization>(registry.getRecipientCode());
			if (recipient.sameId(ApplicationConfig.getSelfOrganization())) {
				throw new FlexPayException("Expected service provider recipient, but recieved eirc code");
			}
			return providerService.getProvider(recipient);
		}
		return providerService.getProvider(new Stub<Organization>(registry.getSenderCode()));
	}

	/**
	 * Check if registry header is valid
	 *
	 * @param registry Registry to validate
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if registry header validation fails
	 */
	@Transactional (readOnly = true)
	private void validateRegistry(Registry registry) throws FlexPayException {
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		Registry persistent = eircRegistryService.getRegistryByNumber(registry.getRegistryNumber(), props.getSenderStub());
		if (persistent != null) {
			throw new FlexPayException("Registry number duplicate");
		}
	}

	@Transactional (readOnly = true, propagation = Propagation.REQUIRED)
	private void processRecord(List<String> messageFieldList, Registry registry, Long[] recordCounter) throws Exception {
		if (registry == null) {
			throw new RegistryFormatException("Error - registry header should go before record");
		}

		if (messageFieldList.size() < 10) {
			throw new RegistryFormatException(
					"Message record error, invalid number of fields: "
					+ messageFieldList.size());
		}

		recordCounter[0] = recordCounter[0] + 1;
		if (recordCounter[0] % 100 == 0) {
			sessionUtils.flush();
			sessionUtils.clear();
		}

		RegistryRecord record = new RegistryRecord();
		record.setProperties(propertiesFactory.newRecordProperties());
		record.setRegistry(registry);
		try {
			log.info("adding record: '{}'", StringUtils.join(messageFieldList, '-'));
			int n = 1;
			record.setServiceCode(messageFieldList.get(++n));
			record.setPersonalAccountExt(messageFieldList.get(++n));

			EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
			EircRegistryRecordProperties recordProps = (EircRegistryRecordProperties) record.getProperties();
			Service service = consumerService.findService(
					new Stub<ServiceProvider>(props.getServiceProvider()), record.getServiceCode());
			if (service == null) {
				log.warn("Unknown service code: {}", record.getServiceCode());
			}
			recordProps.setService(service);

			// setup consumer address
			String addressStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(addressStr)) {
				List<String> addressFieldList = StringUtil.splitEscapable(
						addressStr, Operation.ADDRESS_DELIMITER, Operation.ESCAPE_SYMBOL);

				if (addressFieldList.size() != 6) {
					throw new RegistryFormatException(
							String.format("Address group '%s' has invalid number of fields %d",
									addressStr, addressFieldList.size()));
				}
				record.setTownName(addressFieldList.get(0));
				record.setStreetType(addressFieldList.get(1));
				record.setStreetName(addressFieldList.get(2));
				record.setBuildingNum(addressFieldList.get(3));
				record.setBuildingBulkNum(addressFieldList.get(4));
				record.setApartmentNum(addressFieldList.get(5));
			}

			// setup person first, middle, last names
			String fioStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(fioStr)) {
				List<String> fields = RegistryUtil.parseFIO(fioStr);
				record.setLastName(fields.get(0));
				record.setFirstName(fields.get(1));
				record.setMiddleName(fields.get(2));
			}

			// setup operation date
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			record.setOperationDate(dateFormat.parse(messageFieldList.get(++n)));

			// setup unique operation number
			String uniqueOperationNumberStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(uniqueOperationNumberStr)) {
				record.setUniqueOperationNumber(Long.valueOf(uniqueOperationNumberStr));
			}

			// setup amount
			String amountStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(amountStr)) {
				record.setAmount(new BigDecimal(amountStr));
			}

			// setup containers
			String containersStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(containersStr)) {
				record.setContainers(parseContainers(record, containersStr));
			}

			// setup record status
			recordWorkflowManager.setInitialStatus(record);
		} catch (NumberFormatException e) {
			log.error("Record number parse error", e);
			throw new RegistryFormatException("Record parse error");
		} catch (ParseException e) {
			log.error("Record parse error", e);
			throw new RegistryFormatException("Record parse error");
		}

		registryRecordService.create(record);
	}

	private List<RegistryRecordContainer> parseContainers(RegistryRecord record, String containersData)
			throws RegistryFormatException {

		List<String> containers = StringUtil.splitEscapable(
				containersData, Operation.CONTAINER_DELIMITER, Operation.ESCAPE_SYMBOL);
		List<RegistryRecordContainer> result = new ArrayList<RegistryRecordContainer>(containers.size());
		int n = 0;
		for (String data : containers) {
			if (StringUtils.isBlank(data)) {
				continue;
			}
			if (data.length() > MAX_CONTAINER_SIZE) {
				throw new RegistryFormatException("Too long container found: " + data);
			}
			RegistryRecordContainer container = new RegistryRecordContainer();
			container.setOrder(n++);
			container.setRecord(record);
			container.setData(data);
			result.add(container);
		}

		return result;
	}

	private void parseContainers(Registry registry, String containersData)
			throws RegistryFormatException {

		List<String> containers = StringUtil.splitEscapable(
				containersData, Operation.CONTAINER_DELIMITER, Operation.ESCAPE_SYMBOL);
		for (String data : containers) {
			if (StringUtils.isBlank(data)) {
				continue;
			}
			if (data.length() > MAX_CONTAINER_SIZE) {
				throw new RegistryFormatException("Too long container found: " + data);
			}
			registry.addContainer(new RegistryContainer(data));
		}
	}

	private void processFooter(List<String> messageFieldList)
			throws RegistryFormatException {
		if (messageFieldList.size() < 2) {
			throw new RegistryFormatException("Message footer error, invalid number of fields");
		}
	}

	private void finalizeRegistry(Registry registry, Long[] recordCounter) throws Exception {
		if (registry == null) {
			return;
		}

		if (!registry.getRecordsNumber().equals(recordCounter[0])) {
			throw new RegistryFormatException("Registry records number error, expected: " +
											  registry.getRecordsNumber() + ", found: " + recordCounter[0]);
		}

		registryWorkflowManager.setNextSuccessStatus(registry);
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setEircRegistryService(EircRegistryService eircRegistryService) {
		this.eircRegistryService = eircRegistryService;
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	@Required
	public void setRegistryTypeService(RegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

	@Required
	public void setRegistryArchiveStatusService(RegistryArchiveStatusService registryArchiveStatusService) {
		this.registryArchiveStatusService = registryArchiveStatusService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}

	@Required
	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	@Required
	public void setProviderService(ServiceProviderService providerService) {
		this.providerService = providerService;
	}

	@Required
	public void setPropertiesFactory(PropertiesFactory propertiesFactory) {
		this.propertiesFactory = propertiesFactory;
	}

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
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
