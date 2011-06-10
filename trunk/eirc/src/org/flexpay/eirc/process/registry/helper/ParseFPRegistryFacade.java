package org.flexpay.eirc.process.registry.helper;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.RegistryWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.service.*;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.FileSource;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.process.registry.ProcessHeaderActionHandler;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.sp.RegistryFormatException;
import org.flexpay.eirc.sp.RegistryUtil;
import org.flexpay.eirc.sp.SpFileReader;
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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.process.handler.TaskHandler.getProcessInstanceId;
import static org.flexpay.common.util.CollectionUtils.list;

@Transactional(readOnly = true)
public class ParseFPRegistryFacade implements ParseRegistryFacade {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private FPFileService fpFileService;
	private RegistryRecordService registryRecordService;

	private RegistryService registryService;
	private EircRegistryService eircRegistryService;
	private RegistryTypeService registryTypeService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private RegistryFPFileTypeService registryFPFileTypeService;
	private RegistryWorkflowManager registryWorkflowManager;
	private RegistryRecordWorkflowManager recordWorkflowManager;

	private PropertiesFactory propertiesFactory;

	private OrganizationService organizationService;
	private ServiceProviderService providerService;
	private ConsumerService consumerService;

	private MasterIndexService masterIndexService;
	private CorrectionsService correctionsService;
	private ClassToTypeRegistry typeRegistry;

	private String moduleName;

	private static final Long MIN_READ_CHARS = 32000L;
	private static final Long FLUSH_NUMBER_REGISTRY_RECORDS = 50L;

	@SuppressWarnings ({"unchecked"})
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	@Override
	public String parse(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");

		Long minReadChars = MIN_READ_CHARS;
		Long flushNumberRegistryRecords = FLUSH_NUMBER_REGISTRY_RECORDS;

		// prepare process logger
		ProcessLogger.setThreadProcessId(getProcessInstanceId(parameters));

		Logger processLog = ProcessLogger.getLogger(getClass());

		List<RegistryRecord> records = list();

		Long spFileId = (Long) parameters.get(ParseRegistryConstants.PARAM_FILE_ID);
		FPFile spFile = fpFileService.read(new Stub<FPFile>(spFileId));
		if (spFile == null) {
			processLog.error("Inner error");
			log.error("Can't get spFile from DB (id = {})", spFileId);
			return TaskHandler.RESULT_ERROR;
		}
		if (parameters.containsKey(ParseRegistryConstants.PARAM_MIN_READ_CHARS)) {
			minReadChars = (Long)parameters.get(ParseRegistryConstants.PARAM_MIN_READ_CHARS);
		}

		if (parameters.containsKey(ParseRegistryConstants.PARAM_FLUSH_NUMBER_REGISTRY_RECORDS)) {
			flushNumberRegistryRecords = (Long)parameters.get(ParseRegistryConstants.PARAM_FLUSH_NUMBER_REGISTRY_RECORDS);
		}

		List<SpFileReader.Message> listMessage = (List<SpFileReader.Message>)parameters.get(ParseRegistryConstants.PARAM_MESSAGES);

		SpFileReader reader = (SpFileReader)parameters.get(ParseRegistryConstants.PARAM_READER);
		InputStream is;
		try {
			FileSource fileSource = openRegistryFile(spFile);
			is = fileSource.openStream();
			if (reader == null) {
                reader = new SpFileReader(is);
            }
			reader.setInputStream(is);
		} catch (IOException e) {
			processLog.error("Inner error");
			log.error("Failed open stream");
			return TaskHandler.RESULT_ERROR;
		}

		try {
			boolean nextIterate;
			do {
				listMessage = getMessages(reader, listMessage, minReadChars);

				int i = 0;
				for (SpFileReader.Message message : listMessage) {
					if (message == null) {
						finalizeRegistry(parameters, records, flushNumberRegistryRecords, processLog);
						reader.setInputStream(null);
						return ParseRegistryConstants.RESULT_END;
					}
					i++;
					String messageValue = message.getBody();
					if (StringUtils.isEmpty(messageValue)) {
						continue;
					}
					List<String> messageFieldList = StringUtil.splitEscapable(
							messageValue, Operation.RECORD_DELIMITER, Operation.ESCAPE_SYMBOL);

					Integer messageType = message.getType();

					if (messageType.equals(SpFileReader.Message.MESSAGE_TYPE_HEADER)) {
						Registry registry = processHeader(spFile, messageFieldList, processLog);
						if (registry == null) {
							reader.setInputStream(null);
							return TaskHandler.RESULT_ERROR;
						}
						parameters.put(ParseRegistryConstants.PARAM_REGISTRY_ID, registry.getId());
						parameters.put(ParseRegistryConstants.PARAM_SERVICE_PROVIDER_ID, ((EircRegistryProperties)registry.getProperties()).getServiceProvider().getId());
						log.debug("Create registry {}. Add it to process parameters", registry.getId());
					} else if (messageType.equals(SpFileReader.Message.MESSAGE_TYPE_RECORD)) {
						RegistryRecord record = processRecord(parameters, messageFieldList, processLog);
						if (record == null) {
							reader.setInputStream(null);
							return TaskHandler.RESULT_ERROR;
						}
						records.add(record);
						if (flushRecordStack(parameters, records, flushNumberRegistryRecords, processLog)) {
							List<SpFileReader.Message> outgoingMessages = listMessage.subList(i, listMessage.size());
							parameters.put(ParseRegistryConstants.PARAM_MESSAGES, CollectionUtils.list(outgoingMessages));
							reader.setInputStream(null);
							parameters.put(ParseRegistryConstants.PARAM_READER, reader);
							return TaskHandler.RESULT_NEXT;
						}
					} else if (messageType.equals(SpFileReader.Message.MESSAGE_TYPE_FOOTER)) {
						processFooter(messageFieldList, processLog);
					}
				}
				nextIterate = !listMessage.isEmpty();
				listMessage.clear();

			} while(nextIterate);
			log.error("Failed registry file");
		} catch(Exception e) {
			log.error("Processing error", e);
			processLog.error("Inner error");
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				log.error("Failed reader", e);
				processLog.error("Inner error");
			}
		}
		try {
			reader.setInputStream(null);
		} catch (IOException e) {
			log.error("Inner error", e);
			processLog.error("Inner error");
		}
		return TaskHandler.RESULT_ERROR;
	}

	@SuppressWarnings ({"unchecked"})
	private List<SpFileReader.Message> getMessages(SpFileReader reader, List<SpFileReader.Message> listMessage, Long minReadChars) throws FlexPayException {
		if (listMessage == null) {
			listMessage = list();
		} else if (!listMessage.isEmpty()) {
			return listMessage;
		}

        try {
			Long startPoint = reader.getPosition();
			SpFileReader.Message message;

			do {
				message = reader.readMessage();
				listMessage.add(message);
			} while (message != null && (reader.getPosition() - startPoint) < minReadChars);
			log.debug("read {} number record", listMessage.size());

            return listMessage;
        } catch (IOException e) {
            throw new FlexPayException("Failed open stream", e);
        }
	}

	/**
	 * Open source registry file
	 *
	 * @param spFile Registry file
	 * @return FileSource
	 * @throws IOException if failure occurs
	 */
	private FileSource openRegistryFile(FPFile spFile) throws IOException {
		return spFile.toFileSource();
	}

	private boolean flushRecordStack(@NotNull Map<String, Object> parameters, @Nullable List<RegistryRecord> records,
									 @NotNull Long flushNumberRegistryRecords, @NotNull Logger processLog) throws FlexPayException {
		return flushRecordStack(parameters, records, null, false, flushNumberRegistryRecords, processLog);
	}

	@SuppressWarnings ({"unchecked"})
	private boolean flushRecordStack(@NotNull Map<String, Object> parameters,
								  @Nullable List<RegistryRecord> records, @Nullable Registry registry, boolean finalize,
								  @NotNull Long flushNumberRegistryRecords, @NotNull Logger processLog) throws FlexPayException {
		if (records != null && (records.size() >= flushNumberRegistryRecords || finalize)) {
			if (registry == null) {
				registry = getRegistry(parameters, processLog);
				if (registry == null) {
					throw new FlexPayException("Registry not found");
				}
			}

			Long recordCounter = (Long)parameters.get(ParseRegistryConstants.PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS);
			if (recordCounter == null) {
				recordCounter = 0L;
			}
			recordCounter += records.size();
			parameters.put(ParseRegistryConstants.PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS, recordCounter);

			for (RegistryRecord record : records) {
				record.setRegistry(registry);
			}
			registryRecordService.create(records);

			records.clear();
			log.debug("Flushing data. Total count: {}", recordCounter);
			return true;
		}
		return false;
	}

	private void finalizeRegistry(Map<String, Object> parameters, @Nullable List<RegistryRecord> records,
								  @NotNull Long flushNumberRegistryRecords, @NotNull Logger processLog) throws FlexPayException {
		Registry registry = getRegistry(parameters, processLog);
		if (registry == null) {
			throw new FlexPayException("Registry not found");
		}

		flushRecordStack(parameters, records, registry, true, flushNumberRegistryRecords, processLog);

		log.debug("Finalize registry");

		Long recordCounter = (Long)parameters.get(ParseRegistryConstants.PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS);

		if (!registry.getRecordsNumber().equals(recordCounter)) {
			processLog.error("Registry records number error, expected: {}, found: {}",
					new Object[]{registry.getRecordsNumber(), recordCounter});
			throw new FlexPayException("Registry records number error, expected: " +
											  registry.getRecordsNumber() + ", found: " + recordCounter);
		}

		try {
			registryWorkflowManager.setNextSuccessStatus(registry);
		} catch (TransitionNotAllowed transitionNotAllowed) {
			throw new FlexPayException("Does not finalize registry", transitionNotAllowed);
		}
	}

	@Nullable
	private Registry getRegistry(Map<String, Object> parameters, @NotNull Logger processLog) {
		Long registryId = (Long)parameters.get(ParseRegistryConstants.PARAM_REGISTRY_ID);
		if (registryId == null) {
			processLog.error("Can`t get {} from parameters", ProcessHeaderActionHandler.PARAM_REGISTRY_ID);
			log.error("Can`t get {} from parameters", ProcessHeaderActionHandler.PARAM_REGISTRY_ID);
			return null;
		}
		return registryService.read(new Stub<Registry>(registryId));
	}

	private Registry processHeader(FPFile spFile, List<String> messageFieldList, @NotNull Logger processLog) {
		if (messageFieldList.size() < 10) {
			processLog.error("Message header error, invalid number of fields: {}, expected at least 10", messageFieldList.size());
			return null;
		}

		processLog.info("Adding header: {}", messageFieldList);

		DateFormat dateFormat = new SimpleDateFormat(org.flexpay.eirc.process.registry.ParseRegistryConstants.DATE_FORMAT);

		Registry newRegistry = new Registry();
		newRegistry.setModule(fpFileService.getModuleByName(moduleName));
		newRegistry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		try {
			registryWorkflowManager.setInitialStatus(newRegistry);
			newRegistry.getFiles().put(registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT), spFile);
			int n = 0;
			newRegistry.setRegistryNumber(Long.valueOf(messageFieldList.get(++n)));
			String value = messageFieldList.get(++n);
			RegistryType registryType = registryTypeService.read(Long.valueOf(value));
			if (registryType == null) {
				processLog.error("Unknown registry type field: {}", value);
				return null;
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
				if (!parseContainers(newRegistry, messageFieldList.get(++n), processLog)) {
					return null;
				}
			}

			processLog.info("Creating new registry: {}", newRegistry);

			newRegistry.setProperties(propertiesFactory.newRegistryProperties());
			EircRegistryProperties props = (EircRegistryProperties) newRegistry.getProperties();
			props.setRegistry(newRegistry);

			Organization recipient = setRecipient(newRegistry, processLog);
			if (recipient == null) {
				processLog.error("Failed processing registry header, recipient not found: #{}", newRegistry.getRecipientCode());
				return null;
			}
			Organization sender = setSender(newRegistry, processLog);
			if (sender == null) {
				processLog.error("Failed processing registry header, sender not found: #{}", newRegistry.getSenderCode());
				return null;
			}
			processLog.info("Recipient: {}\n sender: {}", recipient, sender);

			if (!validateProvider(newRegistry, processLog)) {
				return null;
			}
			ServiceProvider provider = getProvider(newRegistry);
			if (provider == null) {
				processLog.error("Failed processing registry header, provider not found: #{}", newRegistry.getSenderCode());
				return null;
			}
			props.setServiceProvider(provider);

			if (!validateRegistry(newRegistry, processLog)) {
				return null;
			}

			return registryService.create(newRegistry);
		} catch (NumberFormatException e) {
			processLog.error("Header parse error", e);
		} catch (ParseException e) {
			processLog.error("Header parse error", e);
		} catch (TransitionNotAllowed e) {
			processLog.error("Header parse error", e);
		} catch (FlexPayException e) {
			processLog.error("Header parse error", e);
		}
		return null;
	}

	private boolean validateProvider(Registry registry, @NotNull Logger processLog) {
		if (registry.getRegistryType().isPayments()) {
			Stub<Organization> recipient = new Stub<Organization>(registry.getRecipientCode());
			if (recipient.sameId(ApplicationConfig.getSelfOrganization())) {
				processLog.error("Expected service provider recipient, but recieved eirc code");
				return false;
			}
		}
		return true;
	}

	private ServiceProvider getProvider(Registry registry) {
		// for payments registry assume recipient is a service provider
		if (registry.getRegistryType().isPayments()) {
			return providerService.getProvider(new Stub<Organization>(registry.getRecipientCode()));
		}
		return providerService.getProvider(new Stub<Organization>(registry.getSenderCode()));
	}

	private Organization setSender(Registry registry, @NotNull Logger processLog) {

		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();

		processLog.debug("Fetching sender via code={}", registry.getSenderCode());
		Organization sender = findOrgByRegistryCorrections(registry, registry.getSenderCode(), processLog);
		if (sender == null) {
			sender = organizationService.readFull(props.getSenderStub());
		}
		props.setSender(sender);
		return sender;
	}

	private Organization setRecipient(Registry registry, @NotNull Logger processLog) {
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();

		Organization recipient;
		if (registry.getRecipientCode() == 0) {
			processLog.debug("Recipient is EIRC, code=0");
			recipient = organizationService.readFull(ApplicationConfig.getSelfOrganizationStub());
		} else {
			processLog.debug("Fetching recipient via code={}", registry.getRecipientCode());
			recipient = findOrgByRegistryCorrections(registry, registry.getRecipientCode(), processLog);
			if (recipient == null) {
				recipient = organizationService.readFull(props.getRecipientStub());
			}
		}
		props.setRecipient(recipient);
		return recipient;
	}

	@Nullable
	private Organization findOrgByRegistryCorrections(Registry registry, Long code, @NotNull Logger processLog) {

		for (RegistryContainer container : registry.getContainers()) {
			String data = container.getData();
			processLog.debug("Candidate: {}", data);
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
					processLog.debug("Found organization by master correction: {}", datum.get(4));
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

	private boolean parseContainers(Registry registry, String containersData, @NotNull Logger processLog) {

		List<String> containers = StringUtil.splitEscapable(
				containersData, Operation.CONTAINER_DELIMITER, Operation.ESCAPE_SYMBOL);
		for (String data : containers) {
			if (StringUtils.isBlank(data)) {
				continue;
			}
			if (data.length() > org.flexpay.eirc.process.registry.ParseRegistryConstants.MAX_CONTAINER_SIZE) {
				processLog.error("Too long container found: {}", data);
				return false;
			}
			registry.addContainer(new RegistryContainer(data));
		}
		return true;
	}

	/**
	 * Check if registry header is valid
	 *
	 * @param registry Registry to validate
	 * @param  processLog Process logger
	 * @return false if registry header validation fails
	 */
	private boolean validateRegistry(Registry registry, @NotNull Logger processLog) {
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		Registry persistent = eircRegistryService.getRegistryByNumber(registry.getRegistryNumber(), props.getSenderStub());
		if (persistent != null) {
			processLog.error("Registry was already uploaded");
			return false;
		}
		return true;
	}

	private RegistryRecord processRecord(Map<String, Object> parameters, List<String> messageFieldList, @NotNull Logger processLog) {
		if (messageFieldList.size() < 10) {
			processLog.error("Message record error, invalid number of fields: {}", messageFieldList.size());
			return null;
		}
		Long serviceProviderId = (Long)parameters.get(ProcessHeaderActionHandler.PARAM_SERVICE_PROVIDER_ID);
		if (serviceProviderId == null) {
			processLog.error("Inner error");
			return null;
		}
		Stub<ServiceProvider> serviceProviderStub = new Stub<ServiceProvider>(serviceProviderId);

		RegistryRecord record = new RegistryRecord();
		record.setProperties(propertiesFactory.newRecordProperties());
		try {
			log.info("adding record: '{}'", StringUtils.join(messageFieldList, '-'));
			int n = 1;
			record.setServiceCode(messageFieldList.get(++n));
			record.setPersonalAccountExt(messageFieldList.get(++n));

			EircRegistryRecordProperties recordProps = (EircRegistryRecordProperties) record.getProperties();
			Service service = consumerService.findService(serviceProviderStub, record.getServiceCode());
			if (service == null) {
				processLog.warn("Unknown service code: {}", record.getServiceCode());
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
			DateFormat dateFormat = new SimpleDateFormat(org.flexpay.eirc.process.registry.ParseRegistryConstants.DATE_FORMAT);
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

			return record;
		} catch (NumberFormatException e) {
			log.error("Record number parse error", e);
		} catch (ParseException e) {
			log.error("Record parse error", e);
		} catch (RegistryFormatException e) {
			log.error("Record number parse error", e);
		} catch (TransitionNotAllowed transitionNotAllowed) {
			log.error("Record number parse error", transitionNotAllowed);
		} catch (FlexPayException e) {
			log.error("Record number parse error", e);
		}
		processLog.error("Record number parse error");
		return null;
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
			if (data.length() > org.flexpay.eirc.process.registry.ParseRegistryConstants.MAX_CONTAINER_SIZE) {
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

	public void processFooter(List<String> messageFieldList, @NotNull Logger processLog) throws FlexPayException {
		if (messageFieldList.size() < 2) {
			processLog.error("Message footer error, invalid number of fields");
			throw new FlexPayException("Message footer error, invalid number of fields");
		}
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
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
	public void setRegistryTypeService(RegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

	@Required
	public void setRegistryArchiveStatusService(RegistryArchiveStatusService registryArchiveStatusService) {
		this.registryArchiveStatusService = registryArchiveStatusService;
	}

	@Required
	public void setRegistryFPFileTypeService(RegistryFPFileTypeService registryFPFileTypeService) {
		this.registryFPFileTypeService = registryFPFileTypeService;
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
	public void setPropertiesFactory(PropertiesFactory propertiesFactory) {
		this.propertiesFactory = propertiesFactory;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setProviderService(ServiceProviderService providerService) {
		this.providerService = providerService;
	}

	@Required
	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
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
}
