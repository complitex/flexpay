package org.flexpay.eirc.sp;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.FileSource;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.persistence.workflow.RegistryWorkflowManager;
import org.flexpay.eirc.service.*;
import org.flexpay.eirc.sp.SpFileReader.Message;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Transactional (readOnly = true)
public class RegistryFileParser {

	private Logger log = Logger.getLogger(getClass());

	public static final String DATE_FORMAT = "ddMMyyyyHHmmss";

	private static final int MAX_CONTAINER_SIZE = 2048;

	private SpRegistryService registryService;
	private SpRegistryRecordService spRegistryRecordService;
	private SpRegistryTypeService registryTypeService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private SessionUtils sessionUtils;

	private RegistryWorkflowManager registryWorkflowManager;
	private RegistryRecordWorkflowManager recordWorkflowManager;

	private OrganisationService organisationService;
	private SPService spService;
	private ConsumerService consumerService;

	@SuppressWarnings ({"ConstantConditions"})
	@Transactional (propagation = Propagation.NOT_SUPPORTED)
	public void parse(SpFile spFile) throws Exception {

		FileSource fileSource = null;
		InputStream is = null;

		SpRegistry registry = null;
		Logger processLog = ProcessLogger.getLogger(getClass());

		if (processLog.isInfoEnabled()) {
			processLog.info("Starting parsing file: " + spFile);
		}

		Long[] recordCounter = {0L};
		try {
			fileSource = openRegistryFile(spFile);
			is = fileSource.openStream();
			SpFileReader reader = new SpFileReader(is);

			Message message;
			while ((message = reader.readMessage()) != null) {
				registry = processMessage(message, spFile, registry, recordCounter);
			}
			finalizeRegistry(registry, recordCounter);
			sessionUtils.flush();
			sessionUtils.clear();

			if (processLog.isInfoEnabled()) {
				if (log.isTraceEnabled()) {
					processLog.trace("Parsed " + recordCounter[0] + " records");
				} else if (recordCounter[0] % 100 == 0) {
					processLog.info("Parsed " + recordCounter[0] + " records");
				}
			}
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

		if (processLog.isInfoEnabled()) {
			processLog.info("File successfully parsed, total records: " + recordCounter[0]);
		}
	}

	/**
	 * Open source registry file
	 *
	 * @param spFile Registry file
	 * @return FileSource
	 * @throws Exception if failure occurs
	 */
	private FileSource openRegistryFile(SpFile spFile) throws Exception {
		File file = spFile.getRequestFile();
		if (file == null) {
			throw new FileNotFoundException("For SpFile(id=" + spFile.getId()
											+ ") not found request file: "
											+ spFile.getInternalRequestFileName());
		}

		log.debug("Opening registry file: " + spFile);

		String type = "";
		if (spFile.getRequestFileName().endsWith(".zip")) {
			log.debug("zip file");
			type = "zip";
		} else if (spFile.getRequestFileName().endsWith(".gz")) {
			log.debug("gzip file");
			type = "gzip";
		}
		return new FileSource(file, type);
	}

	@Transactional (propagation = Propagation.NOT_SUPPORTED)
	private SpRegistry processMessage(Message message, SpFile spFile, SpRegistry registry, Long[] recordCounter) throws Exception {

		String messageValue = message.getBody();
		Integer messageType = message.getType();
		if (StringUtils.isEmpty(messageValue)) {
			return registry;
		}

		List<String> messageFieldList = StringUtil.splitEscapable(
				messageValue, Operation.RECORD_DELIMITER, Operation.ESCAPE_SIMBOL);

		if (log.isDebugEnabled()) {
			log.debug("Message fields: " + messageFieldList);
		}

		if (messageType.equals(Message.MESSAGE_TYPE_HEADER)) {
			if (registry != null) {
				throw new SpFileFormatException("Not unique registry header present");
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
	private SpRegistry processHeader(SpFile spFile, List<String> messageFieldList) throws Exception {
		if (messageFieldList.size() < 11) {
			throw new SpFileFormatException(
					"Message header error, invalid number of fields: "
					+ messageFieldList.size() + ", expected 11");
		}

		if (log.isInfoEnabled()) {
			log.info("adding header: " + messageFieldList);
		}
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

		SpRegistry newRegistry = new SpRegistry();
		newRegistry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registryWorkflowManager.setInitialStatus(newRegistry);
		newRegistry.setSpFile(spFile);
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
			newRegistry.setContainers(parseContainers(newRegistry, messageFieldList.get(++n)));

			if (log.isInfoEnabled()) {
				log.info("Creating new registry: " + newRegistry);
			}

			Organisation recipient;
			if (newRegistry.getRecipientCode() == 0) {
				log.debug("Recipient is EIRC, code=0");
				recipient = ApplicationConfig.getSelfOrganisation();
			} else {
				log.debug("Recipient is fetched via code=" + newRegistry.getRecipientCode());
				recipient = organisationService.getOrganisation(newRegistry.getRecipientStub());
			}
			newRegistry.setRecipient(recipient);
			if (recipient == null) {
				log.error("Failed processing registry header, recipient not found: #" + newRegistry.getRecipientCode());
				throw new FlexPayException("Cannot find recipient organisation " + newRegistry.getRecipientCode());
			}
			Organisation sender = organisationService.getOrganisation(newRegistry.getSenderStub());
			newRegistry.setSender(sender);
			if (sender == null) {
				log.error("Failed processing registry header, sender not found: #" + newRegistry.getSenderCode());
				throw new FlexPayException("Cannot find sender organisation " + newRegistry.getSenderCode());
			}
			if (log.isInfoEnabled()) {
				log.info("Recipient: " + recipient + "\n sender: " + sender);
			}

			ServiceProvider provider = spService.getProvider(newRegistry.getSenderCode());
			if (provider == null) {
				log.error("Failed processing registry header, provider not found: #" + newRegistry.getSenderCode());
				throw new FlexPayException("Cannot find service provider " + newRegistry.getSenderCode());
			}
			newRegistry.setServiceProvider(provider);

			validateRegistry(newRegistry);

			return registryService.create(newRegistry);
		} catch (NumberFormatException e) {
			log.error("Header parse error", e);
			throw new SpFileFormatException("Header parse error");
		} catch (ParseException e) {
			log.error("Header parse error", e);
			throw new SpFileFormatException("Header parse error");
		}
	}

	/**
	 * Check if registry header is valid
	 *
	 * @param registry Registry to validate
	 * @throws FlexPayException if registry header validation fails
	 */
	@Transactional (readOnly = true)
	private void validateRegistry(SpRegistry registry) throws FlexPayException {
		SpRegistry persistent = registryService.getRegistryByNumber(registry.getRegistryNumber(), registry.getSenderStub());
		if (persistent != null) {
			throw new FlexPayException("Registry number duplicate");
		}
	}

	@Transactional (readOnly = true, propagation = Propagation.REQUIRED)
	private void processRecord(List<String> messageFieldList, SpRegistry registry, Long[] recordCounter) throws Exception {
		if (registry == null) {
			throw new SpFileFormatException("Error - registry header should go before record");
		}

		if (messageFieldList.size() < 10) {
			throw new SpFileFormatException(
					"Message record error, invalid number of fields: "
					+ messageFieldList.size());
		}

		recordCounter[0] = recordCounter[0] + 1;
		if (recordCounter[0] % 100 == 0) {
			sessionUtils.flush();
			sessionUtils.clear();
		}

		RegistryRecord record = new RegistryRecord();
		record.setSpRegistry(registry);
		try {
			if (log.isInfoEnabled()) {
				log.info("adding record: |"
						 + StringUtils.join(messageFieldList, '-') + "|");
			}

			int n = 1;
			record.setServiceCode(messageFieldList.get(++n));
			record.setPersonalAccountExt(messageFieldList.get(++n));

			Service service = consumerService.findService(
					registry.getServiceProvider(), record.getServiceCode());
			if (service == null) {
				log.warn("Unknown service code: " + record.getServiceCode());
			}
			record.setService(service);

			// setup consumer address
			String addressStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(addressStr)) {
				List<String> addressFieldList = StringUtil.splitEscapable(
						addressStr, Operation.ADDRESS_DELIMITER, Operation.ESCAPE_SIMBOL);

				if (addressFieldList.size() != 6) {
					throw new SpFileFormatException(
							String.format("Address group '%s' has invalid number of fields %d",
									addressStr, addressFieldList.size()));
				}
				record.setCity(addressFieldList.get(0));
				record.setStreetType(addressFieldList.get(1));
				record.setStreetName(addressFieldList.get(2));
				record.setBuildingNum(addressFieldList.get(3));
				record.setBuildingBulkNum(addressFieldList.get(4));
				record.setApartmentNum(addressFieldList.get(5));
			}

			// setup person first, middle, last names
			String fioStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(fioStr)) {
				List<String> fioFieldList = StringUtil.splitEscapable(
						fioStr, Operation.FIO_DELIMITER, Operation.ESCAPE_SIMBOL);
				if (fioFieldList.size() != 3) {
					throw new SpFileFormatException(
							String.format("FIO group '%s' has invalid number of fields %d",
									fioStr, fioFieldList.size()));
				}
				record.setLastName(fioFieldList.get(0));
				record.setFirstName(fioFieldList.get(1));
				record.setMiddleName(fioFieldList.get(2));
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
			throw new SpFileFormatException("Record parse error");
		} catch (ParseException e) {
			log.error("Record parse error", e);
			throw new SpFileFormatException("Record parse error");
		}

		spRegistryRecordService.create(record);
	}

	private List<RegistryRecordContainer> parseContainers(RegistryRecord record, String containersData)
			throws SpFileFormatException {

		List<String> containers = StringUtil.splitEscapable(
				containersData, Operation.CONTAINER_DELIMITER, Operation.ESCAPE_SIMBOL);
		List<RegistryRecordContainer> result = new ArrayList<RegistryRecordContainer>(containers.size());
		int n = 0;
		for (String data : containers) {
			if (StringUtils.isBlank(data)) {
				continue;
			}
			if (data.length() > MAX_CONTAINER_SIZE) {
				throw new SpFileFormatException("Too long container found: " + data);
			}
			RegistryRecordContainer container = new RegistryRecordContainer();
			container.setOrder(n++);
			container.setRecord(record);
			container.setData(data);
			result.add(container);
		}

		return result;
	}

	private List<RegistryContainer> parseContainers(SpRegistry registry, String containersData)
			throws SpFileFormatException {

		List<String> containers = StringUtil.splitEscapable(
				containersData, Operation.CONTAINER_DELIMITER, Operation.ESCAPE_SIMBOL);
		List<RegistryContainer> result = new ArrayList<RegistryContainer>(containers.size());
		int n = 0;
		for (String data : containers) {
			if (StringUtils.isBlank(data)) {
				continue;
			}
			if (data.length() > MAX_CONTAINER_SIZE) {
				throw new SpFileFormatException("Too long container found: " + data);
			}
			RegistryContainer container = new RegistryContainer();
			container.setOrder(n++);
			container.setRegistry(registry);
			container.setData(data);
			result.add(container);
		}

		return result;
	}

	private void processFooter(List<String> messageFieldList)
			throws SpFileFormatException {
		if (messageFieldList.size() < 2) {
			throw new SpFileFormatException("Message footer error, invalid number of fields");
		}
	}

	private void finalizeRegistry(SpRegistry registry, Long[] recordCounter) throws Exception {
		if (registry == null) {
			return;
		}

		if (!registry.getRecordsNumber().equals(recordCounter[0])) {
			throw new SpFileFormatException("Registry records number error, expected: " +
											registry.getRecordsNumber() + ", found: " + recordCounter[0]);
		}

		registryWorkflowManager.setNextSuccessStatus(registry);
	}

	/**
	 * @param registryService the spRegistryService to set
	 */
	public void setRegistryService(SpRegistryService registryService) {
		this.registryService = registryService;
	}

	/**
	 * @param spRegistryRecordService the spRegistryRecordService to set
	 */
	public void setSpRegistryRecordService(SpRegistryRecordService spRegistryRecordService) {
		this.spRegistryRecordService = spRegistryRecordService;
	}

	/**
	 * @param registryTypeService the spRegistryTypeService to set
	 */
	public void setRegistryTypeService(SpRegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

	/**
	 * @param registryArchiveStatusService the spRegistryArchiveStatusService to set
	 */
	public void setRegistryArchiveStatusService(RegistryArchiveStatusService registryArchiveStatusService) {
		this.registryArchiveStatusService = registryArchiveStatusService;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}

	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}

	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}
}
