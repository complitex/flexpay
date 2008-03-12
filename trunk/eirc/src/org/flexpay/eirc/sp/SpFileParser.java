package org.flexpay.eirc.sp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistryType;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.SpRegistryTypeService;
import org.flexpay.eirc.sp.SpFileReader.Message;

public class SpFileParser {
	private static final String RECORD_DELIMETER = ";";
	private static final String ADDRESS_DELIMETER = ",";
	private static final String FIO_DELIMETER = ",";

	private static final DateFormat dateFormat = new SimpleDateFormat(
			"ddMMyyyyHHmmss");

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

	public void parse() throws IOException, SpFileFormatException,
			FlexPayException {
		File file = spFile.getRequestFile();
		if (file == null) {
			throw new FileNotFoundException("For SpFile(id=" + spFile.getId()
					+ ") not found request file: " + file.getAbsolutePath());
		}

		InputStream is = new FileInputStream(file);
		SpFileReader reader = new SpFileReader(is);

		try {

			// Message message = null;
			while ((message = reader.readMessage()) != null) {
				processMessage();
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// ignore
			}
		}

	}

	private void processMessage() throws SpFileFormatException,
			FlexPayException {
		String messageValue = message.getBody();
		Integer messageType = message.getType();
		if ("".equals(messageValue)) {
			return;
		}
		List<String> messageFieldList = StringUtil.tokenize(messageValue,
				RECORD_DELIMETER);
		String crc16 = messageFieldList.get(messageFieldList.size() - 1);
		validateCrc16(messageValue, crc16);
		if (messageType.equals(Message.MESSAGE_TYPE_HEADER)) {
			processHeader(messageFieldList);
		} else if (messageType.equals(Message.MESSAGE_TYPE_RECORD)) {
			processRecord(messageFieldList);
		} else if (messageType.equals(Message.MESSAGE_TYPE_FOOTER)) {
			processFooter(messageFieldList);
		}
	}

	private void processHeader(List<String> messageFieldList)
			throws FlexPayException, SpFileFormatException {
		if (messageFieldList.size() != 11) {
			throw new SpFileFormatException("Message header error", message
					.getPosition());
		}
		registryRecordCounter = 0;
		spRegistry = new SpRegistry();
		spRegistry.setSpFile(spFile);
		try {

			spRegistry.setRegistryNumber(Long.valueOf(messageFieldList.get(0)));
			SpRegistryType spRegistryType = spRegistryTypeService.read(Long
					.valueOf(messageFieldList.get(1)));
			spRegistry.setRegistryType(spRegistryType);
			spRegistry.setRecordsNumber(Long.valueOf(messageFieldList.get(2)));
			spRegistry.setCreationDate(dateFormat
					.parse(messageFieldList.get(3)));
			spRegistry.setFromDate(dateFormat.parse(messageFieldList.get(4)));
			spRegistry.setTillDate(dateFormat.parse(messageFieldList.get(5)));
			spRegistry.setSenderCode(Long.valueOf(messageFieldList.get(6)));
			spRegistry.setRecipientCode(Long.valueOf(messageFieldList.get(7)));
			String amountStr = messageFieldList.get(8);
			if (amountStr != null && !"".equals(amountStr)) {
				spRegistry.setAmount(BigDecimal.valueOf(Long
						.parseLong(amountStr)));
			}
			spRegistry.setContainers(messageFieldList.get(9));
		} catch (NumberFormatException e) {
			throw new SpFileFormatException("Header parse error", message
					.getPosition());
		} catch (ParseException e) {
			throw new SpFileFormatException("Header parse error", message
					.getPosition());
		}

		spRegistryService.create(spRegistry);
	}

	private void processRecord(List<String> messageFieldList)
			throws FlexPayException, SpFileFormatException {
		if (messageFieldList.size() != 10) {
			throw new SpFileFormatException("Message record error", message
					.getPosition());
		}
		registryRecordCounter++;

		SpRegistryRecord record = new SpRegistryRecord();
		record.setSpRegistry(spRegistry);
		try {
			record.setServiceCode(Long.valueOf(messageFieldList.get(1)));
			record.setPersonalAccountExt(messageFieldList.get(2));

			String addressStr = messageFieldList.get(3);
			if (addressStr != null && !"".equals(addressStr)) {
				List<String> addressFieldList = StringUtil.tokenize(addressStr,
						ADDRESS_DELIMETER);
				if (addressFieldList.size() != 6) {
					throw new SpFileFormatException("Group address has error",
							message.getPosition());
				}
				record.setCity(addressFieldList.get(0));
				record.setStreetType(addressFieldList.get(1));
				record.setStreetName(addressFieldList.get(2));
				record.setBuildingNum(addressFieldList.get(3));
				record.setBuildingBulkNum(addressFieldList.get(4));
				record.setApartmentNum(addressFieldList.get(5));
			}

			String fioStr = messageFieldList.get(4);
			if (fioStr != null && !"".equals(fioStr)) {
				List<String> fioFieldList = StringUtil.tokenize(fioStr,
						FIO_DELIMETER);
				if (fioFieldList.size() != 3) {
					throw new SpFileFormatException("Group FIO has error",
							message.getPosition());
				}
				record.setLastName(fioFieldList.get(0));
				record.setFirstName(fioFieldList.get(1) + " "
						+ fioFieldList.get(2));
			}

			record.setOperationDate(dateFormat.parse(messageFieldList.get(5)));

			String uniqueOperationNumberStr = messageFieldList.get(6);
			if (uniqueOperationNumberStr != null
					&& !"".equals(uniqueOperationNumberStr)) {
				record.setUniqueOperationNumber(Long
						.valueOf(uniqueOperationNumberStr));
			}

			String amountStr = messageFieldList.get(7);
			if (amountStr != null && !"".equals(amountStr)) {
				record.setAmount(BigDecimal.valueOf(Long.parseLong(amountStr)));
			}
		} catch (NumberFormatException e) {
			throw new SpFileFormatException("Record parse error", message
					.getPosition());
		} catch (ParseException e) {
			throw new SpFileFormatException("Record parse error", message
					.getPosition());
		}

		String containersStr = messageFieldList.get(8);
		if (containersStr != null && !"".equals(containersStr)) {
			record.setContainers(containersStr);
		}

		spRegistryRecordService.create(record);
	}

	private void processFooter(List<String> messageFieldList)
			throws SpFileFormatException {
		if (messageFieldList.size() != 2) {
			throw new SpFileFormatException("Message footer error", message
					.getPosition());
		}
		// do nothing
	}

	private boolean validateCrc16(String record, String crc16)
			throws SpFileFormatException {
		return true; // TODO code it
	}

	/**
	 * @param spRegistryService
	 *            the spRegistryService to set
	 */
	public void setSpRegistryService(SpRegistryService spRegistryService) {
		this.spRegistryService = spRegistryService;
	}

	/**
	 * @param spRegistryRecordService
	 *            the spRegistryRecordService to set
	 */
	public void setSpRegistryRecordService(
			SpRegistryRecordService spRegistryRecordService) {
		this.spRegistryRecordService = spRegistryRecordService;
	}

	/**
	 * @param spRegistryTypeService
	 *            the spRegistryTypeService to set
	 */
	public void setSpRegistryTypeService(
			SpRegistryTypeService spRegistryTypeService) {
		this.spRegistryTypeService = spRegistryTypeService;
	}

}
