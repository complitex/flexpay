package org.flexpay.eirc.sp;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.persistence.workflow.RegistryWorkflowManager;
import org.flexpay.eirc.service.*;
import org.flexpay.eirc.sp.SpFileReader.Message;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
public class SpFileParser {

	private Logger log = Logger.getLogger(getClass());

	private static final int MAX_CONTAINER_SIZE = 2048;

	public static final DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");

	private SpRegistryService spRegistryService;
	private SpRegistryRecordService spRegistryRecordService;
	private SpRegistryTypeService spRegistryTypeService;
	private SpRegistryArchiveStatusService spRegistryArchiveStatusService;
	private SessionUtils sessionUtils;

	private RegistryWorkflowManager registryWorkflowManager;
	private RegistryRecordWorkflowManager recordWorkflowManager;

	private OrganisationService organisationService;
	private SPService spService;

	private SpRegistry spRegistry;
	private long registryRecordCounter;
	private Message message;

	@Transactional(readOnly = false)
	public void parse(SpFile spFile) throws Exception {
		File file = spFile.getRequestFile();
		if (file == null) {
			throw new FileNotFoundException("For SpFile(id=" + spFile.getId()
					+ ") not found request file: "
					+ spFile.getInternalRequestFileName());
		}

		InputStream is = null;

		try {
			is = new FileInputStream(file);
			SpFileReader reader = new SpFileReader(is);

			while ((message = reader.readMessage()) != null) {
				processMessage(spFile);
			}
			finalizeRegistry();
			sessionUtils.flush();
			sessionUtils.clear();

			if (log.isDebugEnabled()) {
				log.debug("Parsed " + registryRecordCounter + " records");
			}
		} catch (Throwable t) {
			if (spRegistry != null) {
				registryWorkflowManager.setNextErrorStatus(spRegistry);
			}
			log.error("Failed parsing registry file", t);
			throw new Exception("Failed parsing registry file", t);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private void processMessage(SpFile spFile) throws Exception {

		String messageValue = message.getBody();
		Integer messageType = message.getType();
		if (StringUtils.isEmpty(messageValue)) {
			return;
		}

		List<String> messageFieldList = StringUtil.splitEscapable(
				messageValue, Operation.RECORD_DELIMITER, Operation.ESCAPE_SIMBOL);

		if (messageType.equals(Message.MESSAGE_TYPE_HEADER)) {
			processHeader(spFile, messageFieldList);
		} else if (messageType.equals(Message.MESSAGE_TYPE_RECORD)) {
			processRecord(messageFieldList);
		} else if (messageType.equals(Message.MESSAGE_TYPE_FOOTER)) {
			processFooter(messageFieldList);
		}
	}

	private void processHeader(SpFile spFile, List<String> messageFieldList) throws Exception {
		if (messageFieldList.size() < 11) {
			throw new SpFileFormatException(
					"Message header error, invalid number of fields: "
							+ messageFieldList.size() + ", expected 11", message.getPosition());
		}

		registryRecordCounter = 0;
		SpRegistry newRegistry = new SpRegistry();
		newRegistry.setArchiveStatus(spRegistryArchiveStatusService.findByCode(SpRegistryArchiveStatus.NONE));
		registryWorkflowManager.setInitialStatus(newRegistry);
		newRegistry.setSpFile(spFile);
		if (log.isInfoEnabled()) {
			log.info("adding header: " + messageFieldList);
		}
		try {
			int n = 0;
			newRegistry.setRegistryNumber(Long.valueOf(messageFieldList.get(++n)));
			String value = messageFieldList.get(++n);
			SpRegistryType registryType = spRegistryTypeService.read(Long.valueOf(value));
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

			Organisation recipient = organisationService.getOrganisation(newRegistry.getRecipientStub());
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

			spRegistry = spRegistryService.create(newRegistry);
		} catch (NumberFormatException e) {
			log.error("Header parse error", e);
			throw new SpFileFormatException("Header parse error", message.getPosition());
		} catch (ParseException e) {
			log.error("Header parse error", e);
			throw new SpFileFormatException("Header parse error", message.getPosition());
		}
	}

	private void processRecord(List<String> messageFieldList) throws Exception {
		if (spRegistry == null) {
			throw new SpFileFormatException("Error - registry must start before record");
		}

		if (messageFieldList.size() < 10) {
			throw new SpFileFormatException(
					"Message record error, invalid number of fields: "
							+ messageFieldList.size(), message.getPosition());
		}
		registryRecordCounter++;
		if (registryRecordCounter % 100 == 0) {
			sessionUtils.flush();
			sessionUtils.clear();
		}

		SpRegistryRecord record = new SpRegistryRecord();
		record.setSpRegistry(spRegistry);
		try {
			if (log.isInfoEnabled()) {
				log.info("adding record: |"
						+ StringUtils.join(messageFieldList, '-') + "|");
			}

			int n = 1;
			record.setServiceCode(messageFieldList.get(++n));
			record.setPersonalAccountExt(messageFieldList.get(++n));

			// setup consumer address
			String addressStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(addressStr)) {
				List<String> addressFieldList = StringUtil.splitEscapable(
						addressStr, Operation.ADDRESS_DELIMITER, Operation.ESCAPE_SIMBOL);

				if (addressFieldList.size() != 6) {
					throw new SpFileFormatException(
							String.format("Address group '%s' has invalid number of fields %d",
									addressStr, addressFieldList.size()),
							message.getPosition());
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
					throw new SpFileFormatException("Group FIO has error", message.getPosition());
				}
				record.setLastName(fioFieldList.get(0));
				record.setFirstName(fioFieldList.get(1));
				record.setMiddleName(fioFieldList.get(2));
			}

			// setup operation date
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
			throw new SpFileFormatException("Record parse error", message.getPosition());
		} catch (ParseException e) {
			log.error("Record parse error", e);
			throw new SpFileFormatException("Record parse error", message.getPosition());
		}

		spRegistryRecordService.create(record);
	}

	private List<RegistryRecordContainer> parseContainers(SpRegistryRecord record, String containersData)
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
			throw new SpFileFormatException(
					"Message footer error, invalid number of fields", message.getPosition());
		}
	}

	private void finalizeRegistry() throws Exception {
		if (spRegistry == null) {
			return;
		}

		if (spRegistry.getRecordsNumber() != registryRecordCounter) {
			throw new SpFileFormatException("Registry records number error, expected: " +
					spRegistry.getRecordsNumber() + ", found: " + registryRecordCounter);
		}

		registryWorkflowManager.setNextSuccessStatus(spRegistry);
		spRegistry = null;
	}

	/**
	 * @param spRegistryService the spRegistryService to set
	 */
	public void setSpRegistryService(SpRegistryService spRegistryService) {
		this.spRegistryService = spRegistryService;
	}

	/**
	 * @param spRegistryRecordService the spRegistryRecordService to set
	 */
	public void setSpRegistryRecordService(SpRegistryRecordService spRegistryRecordService) {
		this.spRegistryRecordService = spRegistryRecordService;
	}

	/**
	 * @param spRegistryTypeService the spRegistryTypeService to set
	 */
	public void setSpRegistryTypeService(SpRegistryTypeService spRegistryTypeService) {
		this.spRegistryTypeService = spRegistryTypeService;
	}

	/**
	 * @param spRegistryArchiveStatusService the spRegistryArchiveStatusService to set
	 */
	public void setSpRegistryArchiveStatusService(SpRegistryArchiveStatusService spRegistryArchiveStatusService) {
		this.spRegistryArchiveStatusService = spRegistryArchiveStatusService;
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
}
