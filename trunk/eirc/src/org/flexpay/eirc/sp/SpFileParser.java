package org.flexpay.eirc.sp;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.CRCUtil;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistryType;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.SpRegistryTypeService;
import org.flexpay.eirc.sp.SpFileReader.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class SpFileParser {

	private static Logger log = Logger.getLogger(SpFileParser.class);

	public static final String RECORD_DELIMITER = ";";
	public static final String ADDRESS_DELIMITER = ",";
	public static final String FIO_DELIMITER = ",";

	public static final DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");

	private SpRegistryService spRegistryService;
	private SpRegistryRecordService spRegistryRecordService;
	private SpRegistryTypeService spRegistryTypeService;

	private SpFile spFile;

	private SpRegistry spRegistry;
	private long registryRecordCounter;
	private Message message;

	public SpFileParser(SpFile spFile) {
		this.spFile = spFile;
	}

	public void parse() throws IOException, SpFileFormatException, FlexPayException {
		File file = spFile.getRequestFile();
		if (file == null) {
			throw new FileNotFoundException("For SpFile(id=" + spFile.getId()
					+ ") not found request file: " + spFile.getInternalRequestFileName());
		}

		InputStream is = new FileInputStream(file);
		SpFileReader reader = new SpFileReader(is);

		try {
			// Message message = null;
			while ((message = reader.readMessage()) != null) {
				processMessage();
			}
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private void processMessage() throws SpFileFormatException, FlexPayException {

		String messageValue = message.getBody();
		Integer messageType = message.getType();
		if (StringUtils.isEmpty(messageValue)) {
			return;
		}

		List<String> messageFieldList = StringUtil.tokenize(messageValue, RECORD_DELIMITER);
		String crc16 = messageFieldList.get(messageFieldList.size() - 1);

		validateCrc16(joinFields(messageType, messageFieldList), crc16);

		if (messageType.equals(Message.MESSAGE_TYPE_HEADER)) {
			processHeader(messageFieldList);
		} else if (messageType.equals(Message.MESSAGE_TYPE_RECORD)) {
			processRecord(messageFieldList);
		} else if (messageType.equals(Message.MESSAGE_TYPE_FOOTER)) {
			processFooter(messageFieldList);
		}
	}

	// get original string crc calculated on
	private String joinFields(int type, List<String> messageFieldList) {
		StringBuilder buf = new StringBuilder();
		buf.append((char)type);
		for (int i=0; i < messageFieldList.size() - 1; ++i) {
			buf.append(messageFieldList.get(i)).append(RECORD_DELIMITER);
		}

		return buf.toString();
	}

	private void processHeader(List<String> messageFieldList)
			throws FlexPayException, SpFileFormatException {
		if (messageFieldList.size() != 12) {
			throw new SpFileFormatException("Message header error, invalid number of fields: " +
											messageFieldList.size(), message.getPosition());
		}
		registryRecordCounter = 0;
		spRegistry = new SpRegistry();
		spRegistry.setSpFile(spFile);
		try {
			if (log.isInfoEnabled()) {
				log.info("adding header: " + messageFieldList);
			}

			int n = 0;
			spRegistry.setRegistryNumber(Long.valueOf(messageFieldList.get(++n)));
			SpRegistryType spRegistryType = spRegistryTypeService.read(Long.valueOf(messageFieldList.get(++n)));
			spRegistry.setRegistryType(spRegistryType);
			spRegistry.setRecordsNumber(Long.valueOf(messageFieldList.get(++n)));
			spRegistry.setCreationDate(dateFormat.parse(messageFieldList.get(++n)));
			spRegistry.setFromDate(dateFormat.parse(messageFieldList.get(++n)));
			spRegistry.setTillDate(dateFormat.parse(messageFieldList.get(++n)));
			spRegistry.setSenderCode(Long.valueOf(messageFieldList.get(++n)));
			spRegistry.setRecipientCode(Long.valueOf(messageFieldList.get(++n)));
			String amountStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(amountStr)) {
				spRegistry.setAmount(new BigDecimal(amountStr));
			}
			spRegistry.setContainers(messageFieldList.get(++n));
		} catch (NumberFormatException e) {
			throw new SpFileFormatException("Header parse error", message.getPosition());
		} catch (ParseException e) {
			throw new SpFileFormatException("Header parse error", message.getPosition());
		}

		spRegistryService.create(spRegistry);
	}

	private void processRecord(List<String> messageFieldList)
			throws FlexPayException, SpFileFormatException {
		if (messageFieldList.size() != 11) {
			throw new SpFileFormatException("Message record error", message.getPosition());
		}
		registryRecordCounter++;

		SpRegistryRecord record = new SpRegistryRecord();
		record.setSpRegistry(spRegistry);
		try {
			if (log.isInfoEnabled()) {
				log.info("adding record: |" + StringUtils.join(messageFieldList, '-') + "|");
			}

			int n = 1;
			record.setServiceCode(Long.valueOf(messageFieldList.get(++n)));
			record.setPersonalAccountExt(messageFieldList.get(++n));

			String addressStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(addressStr)) {
				List<String> addressFieldList = StringUtil.tokenize(addressStr, ADDRESS_DELIMITER);
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

			String fioStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(fioStr)) {
				List<String> fioFieldList = StringUtil.tokenize(fioStr, FIO_DELIMITER);
				if (fioFieldList.size() != 3) {
					throw new SpFileFormatException("Group FIO has error", message.getPosition());
				}
				record.setLastName(fioFieldList.get(0));
				record.setFirstName(fioFieldList.get(1));
				record.setMiddleName(fioFieldList.get(2));
			}

			record.setOperationDate(dateFormat.parse(messageFieldList.get(++n)));

			String uniqueOperationNumberStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(uniqueOperationNumberStr)) {
				record.setUniqueOperationNumber(Long.valueOf(uniqueOperationNumberStr));
			}

			String amountStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(amountStr)) {
				record.setAmount(new BigDecimal(amountStr));
			}

			String containersStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(containersStr)) {
				record.setContainers(containersStr);
			}

		} catch (NumberFormatException e) {
			throw new SpFileFormatException("Record parse error", message.getPosition());
		} catch (ParseException e) {
			throw new SpFileFormatException("Record parse error", message.getPosition());
		}

		spRegistryRecordService.create(record);
	}

	private void processFooter(List<String> messageFieldList) throws SpFileFormatException {
		if (messageFieldList.size() != 3) {
			throw new SpFileFormatException("Message footer error, invalid number of fields", message.getPosition());
		}
		// do nothing
	}

	private void validateCrc16(String record, String crc16) throws SpFileFormatException {
		try {
			char crc = (char) Integer.parseInt(crc16, 16);
			char crcCalc = CRCUtil.crc16(record.getBytes(SpFileReader.DEFAULT_CHARSET));
			if (crcCalc != crc) {
				throw new CRC16Exception(String.format(
						"crc16 failure for record: %s, calculated %x", record, (int)crcCalc));
			}
		} catch (UnsupportedEncodingException e) {
			// should not reach!
			throw new RuntimeException(e);
		}
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
	public void setSpRegistryRecordService(
			SpRegistryRecordService spRegistryRecordService) {
		this.spRegistryRecordService = spRegistryRecordService;
	}

	/**
	 * @param spRegistryTypeService the spRegistryTypeService to set
	 */
	public void setSpRegistryTypeService(
			SpRegistryTypeService spRegistryTypeService) {
		this.spRegistryTypeService = spRegistryTypeService;
	}
}
