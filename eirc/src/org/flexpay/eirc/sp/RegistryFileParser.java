package org.flexpay.eirc.sp;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
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
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
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

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String DATE_FORMAT = "ddMMyyyyHHmmss";

	private static final int MAX_CONTAINER_SIZE = 2048;

	private RegistryService registryService;
	private RegistryRecordService registryRecordService;
	private SpRegistryTypeService registryTypeService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private SessionUtils sessionUtils;

	private RegistryWorkflowManager registryWorkflowManager;
	private RegistryRecordWorkflowManager recordWorkflowManager;

	private OrganizationService organizationService;
	private ServiceProviderService providerService;
	private ConsumerService consumerService;

	@SuppressWarnings ({"ConstantConditions"})
	@Transactional (propagation = Propagation.NOT_SUPPORTED)
	public void parse(FPFile spFile) throws Exception {

		FileSource fileSource = null;
		InputStream is = null;

		SpRegistry registry = null;
		Logger processLog = ProcessLogger.getLogger(getClass());

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
	}

	/**
	 * Open source registry file
	 *
	 * @param spFile Registry file
	 * @return FileSource
	 * @throws Exception if failure occurs
	 */
	private FileSource openRegistryFile(FPFile spFile) throws Exception {
		File file = spFile.getFile();
		if (file == null) {
			throw new FileNotFoundException("For FPFile(id=" + spFile.getId()
											+ ") not found temp file: " + spFile.getNameOnServer());
		}

		log.debug("Opening registry file: {}", spFile);

		String type = "";
		if (spFile.getOriginalName().endsWith(".zip")) {
			log.debug("zip file");
			type = "zip";
		} else if (spFile.getOriginalName().endsWith(".gz")) {
			log.debug("gzip file");
			type = "gzip";
		}
		return new FileSource(file, type);
	}

	@Transactional (propagation = Propagation.NOT_SUPPORTED)
	private SpRegistry processMessage(Message message, FPFile spFile, SpRegistry registry, Long[] recordCounter) throws Exception {

		String messageValue = message.getBody();
		Integer messageType = message.getType();
		if (StringUtils.isEmpty(messageValue)) {
			return registry;
		}

		List<String> messageFieldList = StringUtil.splitEscapable(
				messageValue, Operation.RECORD_DELIMITER, Operation.ESCAPE_SIMBOL);

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
	private SpRegistry processHeader(FPFile spFile, List<String> messageFieldList) throws Exception {
		if (messageFieldList.size() < 11) {
			throw new RegistryFormatException(
					"Message header error, invalid number of fields: "
					+ messageFieldList.size() + ", expected 11");
		}

		log.info("adding header: {}", messageFieldList);

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

			log.info("Creating new registry: {}", newRegistry);

			Organization recipient;
			if (newRegistry.getRecipientCode() == 0) {
				log.debug("Recipient is EIRC, code=0");
				recipient = ApplicationConfig.getSelfOrganization();
			} else {
				log.debug("Recipient is fetched via code={}", newRegistry.getRecipientCode());
				recipient = organizationService.readFull(newRegistry.getRecipientStub());
			}
			newRegistry.setRecipient(recipient);
			if (recipient == null) {
				log.error("Failed processing registry header, recipient not found: #{}", newRegistry.getRecipientCode());
				throw new FlexPayException("Cannot find recipient organization " + newRegistry.getRecipientCode());
			}
			Organization sender = organizationService.readFull(newRegistry.getSenderStub());
			newRegistry.setSender(sender);
			if (sender == null) {
				log.error("Failed processing registry header, sender not found: #{}", newRegistry.getSenderCode());
				throw new FlexPayException("Cannot find sender organization " + newRegistry.getSenderCode());
			}
			log.info("Recipient: {}\n sender: {}", recipient, sender);

			ServiceProvider provider = providerService.getProvider(newRegistry.getSenderCode());
			if (provider == null) {
				log.error("Failed processing registry header, provider not found: #{}", newRegistry.getSenderCode());
				throw new FlexPayException("Cannot find service provider " + newRegistry.getSenderCode());
			}
			newRegistry.setServiceProvider(provider);

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

	/**
	 * Check if registry header is valid
	 *
	 * @param registry Registry to validate
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if registry header validation fails
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
		record.setSpRegistry(registry);
		try {
			log.info("adding record: '{}'", StringUtils.join(messageFieldList, '-'));
			int n = 1;
			record.setServiceCode(messageFieldList.get(++n));
			record.setPersonalAccountExt(messageFieldList.get(++n));

			Service service = consumerService.findService(
					registry.getServiceProvider(), record.getServiceCode());
			if (service == null) {
				log.warn("Unknown service code: {}", record.getServiceCode());
			}
			record.setService(service);

			// setup consumer address
			String addressStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(addressStr)) {
				List<String> addressFieldList = StringUtil.splitEscapable(
						addressStr, Operation.ADDRESS_DELIMITER, Operation.ESCAPE_SIMBOL);

				if (addressFieldList.size() != 6) {
					throw new RegistryFormatException(
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
				containersData, Operation.CONTAINER_DELIMITER, Operation.ESCAPE_SIMBOL);
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

	private List<RegistryContainer> parseContainers(SpRegistry registry, String containersData)
			throws RegistryFormatException {

		List<String> containers = StringUtil.splitEscapable(
				containersData, Operation.CONTAINER_DELIMITER, Operation.ESCAPE_SIMBOL);
		List<RegistryContainer> result = new ArrayList<RegistryContainer>(containers.size());
		int n = 0;
		for (String data : containers) {
			if (StringUtils.isBlank(data)) {
				continue;
			}
			if (data.length() > MAX_CONTAINER_SIZE) {
				throw new RegistryFormatException("Too long container found: " + data);
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
			throws RegistryFormatException {
		if (messageFieldList.size() < 2) {
			throw new RegistryFormatException("Message footer error, invalid number of fields");
		}
	}

	private void finalizeRegistry(SpRegistry registry, Long[] recordCounter) throws Exception {
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
	public void setSpRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	@Required
	public void setRegistryTypeService(SpRegistryTypeService registryTypeService) {
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
}
