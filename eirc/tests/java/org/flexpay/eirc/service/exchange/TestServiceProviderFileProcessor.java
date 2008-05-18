package org.flexpay.eirc.service.exchange;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.Town;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.CRCUtil;
import org.flexpay.eirc.actions.TestSpFileAction;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.SpRegistryTypeService;
import org.flexpay.eirc.sp.SpFileParser;
import org.flexpay.eirc.sp.SpFileReader;
import org.flexpay.eirc.test.RandomObjects;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TestServiceProviderFileProcessor extends TestSpFileAction {

	private static Logger log = Logger.getLogger(TestServiceProviderFileProcessor.class);

	@Autowired
	private ServiceProviderFileProcessor fileProcessor;
	@Autowired
	private ExchangeHelper exchangeHelper;
	@Autowired
	private SPService spService;

	private RandomObjects randomObjects;


	private static final char DELIMITER_RECORDS = SpFileParser.RECORD_DELIMITER.charAt(0);
	private static final char DELIMITER_CONTAINER = ':';

	private Random rand = new Random();

	@Ignore
	@Test
	public void testSpFileProcessing() throws Throwable {
//		File generatedFile = generatePaymentsFile();
//		SpFile file = uploadFile(generatedFile.getAbsolutePath());
//		SpFile file = uploadFile("org/flexpay/eirc/actions/sp/payments_100.bin");
		SpFile file = uploadFile("org/flexpay/eirc/actions/sp/ree.txt");

		try {
			fileProcessor.processFile(file);
		} finally {
			deleteRecords(file);
			deleteFile(file);
		}
	}

	private File generatePaymentsFile() throws Throwable {

		int nRecords = 250000;
		File tmpFile = File.createTempFile(String.format("payments_%d.", nRecords), ".bin");

		BigDecimal totalAmount = new BigDecimal("32198000000");

		Organisation recipient = ApplicationConfig.getInstance().getSelfOrganisation();
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(tmpFile));

			String header = getHeader(nRecords, totalAmount, recipient);
			os.write(header.getBytes(SpFileReader.DEFAULT_CHARSET));

			ServiceProvider provider = randomObjects.getRandomServiceProvider();
			Town town = ApplicationConfig.getInstance().getDefaultTown();

			long start = System.currentTimeMillis();
			for (int n = 0; n < nRecords; ++n) {
				log.info(String.format("====================================================\nCreating record #%d", n));
				BigDecimal amount = totalAmount.divide(new BigDecimal(nRecords));
				AccountRecord accountRecord = getRandomRecord(town, provider, amount);
				log.info(String.format("Random record %d", System.currentTimeMillis() - start));
				String record = getPaymentRecord(accountRecord);
				os.write(record.getBytes(SpFileReader.DEFAULT_CHARSET));
				log.info(String.format("Record extract %d", System.currentTimeMillis() - start));
				start = System.currentTimeMillis();
			}

			String footer = getFooter();
			os.write(footer.getBytes(SpFileReader.DEFAULT_CHARSET));

			return tmpFile;
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

	private void logTime(long start, int label) {
		if (log.isInfoEnabled()) {
			log.info("Time spent " + label + ": " + (System.currentTimeMillis() - start) + " ms. ");
		}
	}

	private AccountRecord getRandomRecord(Town town, ServiceProvider serviceProvider, BigDecimal amount) {
		AccountRecord record = new AccountRecord();

		long start = System.currentTimeMillis();

		Consumer consumer = new Consumer();
		consumer.setExternalAccountNumber("#" + String.valueOf(Math.abs(rand.nextLong())));
		logTime(start, 0);
		consumer.setResponsiblePerson(randomObjects.getRandomPerson());
		logTime(start, 1);
		consumer.setService(randomObjects.getRandomService(serviceProvider));
		logTime(start, 2);
		consumer.setApartment(randomObjects.getRandomApartment(town));
		logTime(start, 3);

		record.setConsumer(consumer);

		Date now = new Date();
		Date begin = DateUtils.truncate(now, Calendar.MONTH);
		long diff = now.getTime() - begin.getTime();
		record.setOperationDate(new Date(begin.getTime() + (rand.nextLong() % diff)));

		record.setId(Math.abs(rand.nextLong()));
		record.setRecordType(spService.getRecordType(AccountRecordType.TYPE_PAYMENT));
		record.setOrganisation(ApplicationConfig.getInstance().getSelfOrganisation());
		record.setAmount(amount);

		logTime(start, 4);
		return record;
	}

	private String getPaymentRecord(AccountRecord accountRecord) throws IOException, FlexPayException {
		StringBuilder buf = new StringBuilder();

		Apartment apartment = ((Consumer) accountRecord.getConsumer()).getApartment();
		Person person = accountRecord.getConsumer().getResponsiblePerson();
		Service service = accountRecord.getConsumer().getService();
		Date operationDate = accountRecord.getOperationDate();

		buf
				// message type
				.append((char) SpFileReader.Message.MESSAGE_TYPE_RECORD)
//				.append('\n')
				.append(DELIMITER_RECORDS)

						// registry number
				.append(1)
				.append(DELIMITER_RECORDS)

						// service code
				.append(service.getServiceType().getCode())
				.append(DELIMITER_RECORDS)

						// account number
				.append(accountRecord.getConsumer().getExternalAccountNumber())
				.append(DELIMITER_RECORDS)

						// address
				.append(exchangeHelper.getAddressGroup(apartment))
				.append(DELIMITER_RECORDS)

						// FIO
				.append(exchangeHelper.getFIOGroup(person))
				.append(DELIMITER_RECORDS)

						// Operation date
				.append(SpFileParser.dateFormat.format(operationDate))
				.append(DELIMITER_RECORDS)

						// Unique operation number
				.append(accountRecord.getId())
				.append(DELIMITER_RECORDS)

						// Amount
				.append(accountRecord.getAmount())
				.append(DELIMITER_RECORDS)

						// Simple payment container
				.append(50)
				.append(DELIMITER_CONTAINER)

						// Organisation id TODO: uncomment
//				.append(accountRecord.getOrganisation().getUniqueId())
				.append(accountRecord.getOrganisation().getId())
				.append(DELIMITER_RECORDS);

		// CRC16
		String str = buf.toString();
		int crc16 = CRCUtil.crc16(str.getBytes(SpFileReader.DEFAULT_CHARSET));
		buf.append(String.format("%x", crc16));

		if (log.isInfoEnabled()) {
			log.info(String.format("'%s', crc: %x", str, crc16));
		}

		return buf.toString();
	}

	private String getFooter() throws IOException {
		StringBuilder buf = new StringBuilder();

		buf
				// message type
				.append((char) SpFileReader.Message.MESSAGE_TYPE_FOOTER)
//				.append('\n')
				.append(DELIMITER_RECORDS)

						// registry number
				.append(1)
				.append(DELIMITER_RECORDS);

		// CRC16
		String str = buf.toString();
		int crc16 = CRCUtil.crc16(str.getBytes(SpFileReader.DEFAULT_CHARSET));
		buf.append(String.format("%x", crc16));

		if (log.isInfoEnabled()) {
			log.info(String.format("Footer '%s', crc: %x", str, crc16));
		}

		return buf.toString();
	}

	private String getHeader(int nRecords, BigDecimal amount, Organisation recipient) throws IOException {
		StringBuilder buf = new StringBuilder();

		Date now = DateUtils.truncate(new Date(), Calendar.DATE);
		Date begin = DateUtils.truncate(now, Calendar.MONTH);
		Date end = DateUtils.add(begin, Calendar.MONTH, 1);
		buf
				// message type
				.append((char) SpFileReader.Message.MESSAGE_TYPE_HEADER)
				.append(DELIMITER_RECORDS)

						// registry number
				.append(1)
				.append(DELIMITER_RECORDS)

						// registry type
				.append(SpRegistryTypeService.NALICHNIE_OPLATI.shortValue())
				.append(DELIMITER_RECORDS)

						// records number
				.append(nRecords)
				.append(DELIMITER_RECORDS)

						// generation date
				.append(SpFileParser.dateFormat.format(now))
				.append(DELIMITER_RECORDS)

						// begin date
				.append(SpFileParser.dateFormat.format(begin))
				.append(DELIMITER_RECORDS)

						// begin date
				.append(SpFileParser.dateFormat.format(end))
				.append(DELIMITER_RECORDS)

						// sender TODO: uncomment
//				.append(ApplicationConfig.getInstance().getSelfOrganisation().getUniqueId())
				.append(ApplicationConfig.getInstance().getSelfOrganisation().getId())
				.append(DELIMITER_RECORDS)

						// recipient TODO: uncomment
//				.append(recipient.getUniqueId())
				.append(recipient.getId())
				.append(DELIMITER_RECORDS)

						// registry amount
				.append(amount)
				.append(DELIMITER_RECORDS)

						// containers
				.append("")
				.append(DELIMITER_RECORDS);

		// CRC16
		String str = buf.toString();
		int crc16 = CRCUtil.crc16(str.getBytes(SpFileReader.DEFAULT_CHARSET));
		buf.append(String.format("%x", crc16));

		if (log.isInfoEnabled()) {
			log.info(String.format("Header '%s', crc: %x", str, crc16));
		}

		return buf.toString();
	}

	@Before
	public void prepareTestInstance() throws Exception {

		randomObjects = new RandomObjects(rand, applicationContext);
	}
}
