package org.flexpay.eirc.service.exchange;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.Town;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CRCUtil;
import org.flexpay.eirc.actions.TestSpFileAction;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.sp.RegistryFileParser;
import org.flexpay.eirc.sp.SpFileReader;
import org.flexpay.eirc.test.RandomObjects;
import org.flexpay.eirc.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.test.annotation.NotTransactional;

import java.io.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TestServiceProviderFileProcessor extends TestSpFileAction {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	protected RegistryProcessor registryProcessor;
	@Autowired
	protected ExchangeHelper exchangeHelper;
	protected SPService spService;
	@Autowired
	protected SpRegistryService registryService;

	private RandomObjects randomObjects;


	private static final char RECORD_DELIMITER = Operation.RECORD_DELIMITER;
	private static final char CONTAINER_DATA_DELIMITER = Operation.CONTAINER_DATA_DELIMITER;

	private Random rand = new Random();

	@Autowired
	public void setSpService(@Qualifier ("spService") SPService spService) {
		this.spService = spService;
	}

	@Ignore
	@Test
	@NotTransactional
	public void testSpFileProcessing() throws Throwable {
		SpFile file = uploadFile("org/flexpay/eirc/actions/sp/ree.txt");

		try {
			registryProcessor.processFile(file);
		} finally {
			deleteRecords(file);
			deleteFile(file);
		}
	}

	@Test
	@Ignore
	@NotTransactional
	public void testProcessOpenAccountsRegistry() throws Throwable {
		SpFile file = uploadFile("org/flexpay/eirc/actions/sp/ree_open.txt");

		try {
			registryProcessor.processFile(file);
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				e.printStackTrace();
			}
			throw c;
		} finally {
			deleteRecords(file);
			deleteFile(file);
		}
	}

	@Test
	@NotTransactional
	public void testProcessOpenSubAccountsRegistrySmall() throws Throwable {
		SpFile file = uploadFile("org/flexpay/eirc/actions/sp/ree_open_2_small.txt");

		try {
			registryProcessor.processFile(file);
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				e.printStackTrace();
			}
			throw c;
		} finally {
			deleteRecords(file);
			deleteFile(file);
		}
	}

	@Test
	@Ignore
	@NotTransactional
	public void testProcessOpenSubAccountsRegistry() throws Throwable {
		SpFile file = uploadFile("org/flexpay/eirc/actions/sp/ree_open_2.txt");

		try {
			registryProcessor.processFile(file);
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				e.printStackTrace();
			}
			throw c;
		} finally {
			deleteRecords(file);
			deleteFile(file);
		}
	}

	@Test
	@Ignore
	@NotTransactional
	public void testProcessQuittancesSmallRegistry() throws Throwable {
		SpFile file = uploadFile("org/flexpay/eirc/actions/sp/ree_quittances_small.txt");

		try {
			registryProcessor.processFile(file);
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				e.printStackTrace();
			}
			throw c;
		} finally {
			deleteRecords(file);
			deleteFile(file);
		}
	}

	@Test
	@Ignore
	@NotTransactional
	public void testProcessQuittancesRegistry() throws Throwable {
		SpFile file = uploadFile("org/flexpay/eirc/actions/sp/ree_quittances.txt");

		try {
			registryProcessor.processFile(file);
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				e.printStackTrace();
			}
			throw c;
		} finally {
			deleteRecords(file);
			deleteFile(file);
		}
	}

	@Test
	@Ignore
	@NotTransactional
	public void testProcessOpenSubAccountsRegistryNoClear() throws Throwable {
		SpFile file = new SpFile(265L);

		try {
			registryProcessor.processFile(file);

			checkOpenRegistryRecords(file);
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				e.printStackTrace();
			}
			throw c;
		}
	}

	private void checkOpenRegistryRecords(SpFile file) {
		int nErrorLessRecords = DataAccessUtils.intResult(
				hibernateTemplate.find("select count(*) from RegistryRecord rr " +
									   "where importError is null and rr.spRegistry.spFile.id=?", file.getId()));
		int nConsumerLessRecords = DataAccessUtils.intResult(
				hibernateTemplate.find("select count(*) from RegistryRecord rr " +
									   "where consumer is null and rr.spRegistry.spFile.id=?", file.getId()));
		assertEquals("Invalid number of records without errors", nErrorLessRecords, nConsumerLessRecords);
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private File generatePaymentsFile() throws Throwable {

		int nRecords = 250000;
		File tmpFile = File.createTempFile(String.format("payments_%d.", nRecords), ".bin");

		BigDecimal totalAmount = new BigDecimal("32198000000");

		Organisation recipient = ApplicationConfig.getSelfOrganisation();
		OutputStream os = null;
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			os = new BufferedOutputStream(new FileOutputStream(tmpFile));

			String header = getHeader(nRecords, totalAmount, recipient);
			os.write(header.getBytes(SpFileReader.DEFAULT_CHARSET));

			ServiceProvider provider = randomObjects.getRandomServiceProvider();
			Town town = ApplicationConfig.getDefaultTown();

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
		record.setOrganisation(ApplicationConfig.getSelfOrganisation());
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

		DateFormat dateFormat = new SimpleDateFormat(RegistryFileParser.DATE_FORMAT);

		buf
				// message type
				.append((char) SpFileReader.Message.MESSAGE_TYPE_RECORD)
//				.append('\n')
				.append(RECORD_DELIMITER)

						// registry number
				.append(1)
				.append(RECORD_DELIMITER)

						// service code
				.append(service.getServiceType().getCode())
				.append(RECORD_DELIMITER)

						// account number
				.append(accountRecord.getConsumer().getExternalAccountNumber())
				.append(RECORD_DELIMITER)

						// address
				.append(exchangeHelper.getAddressGroup(apartment))
				.append(RECORD_DELIMITER)

						// FIO
				.append(exchangeHelper.getFIOGroup(stub(person)))
				.append(RECORD_DELIMITER)

						// Operation date
				.append(dateFormat.format(operationDate))
				.append(RECORD_DELIMITER)

						// Unique operation number
				.append(accountRecord.getId())
				.append(RECORD_DELIMITER)

						// Amount
				.append(accountRecord.getAmount())
				.append(RECORD_DELIMITER)

						// Simple payment container
				.append(50)
				.append(CONTAINER_DATA_DELIMITER)

						// Organisation id TODO: uncomment
//				.append(accountRecord.getOrganisation().getUniqueId())
				.append(accountRecord.getOrganisation().getId())
				.append(RECORD_DELIMITER);

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
				.append(RECORD_DELIMITER)

						// registry number
				.append(1)
				.append(RECORD_DELIMITER);

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

		DateFormat dateFormat = new SimpleDateFormat(RegistryFileParser.DATE_FORMAT);

		Date now = DateUtils.truncate(new Date(), Calendar.DATE);
		Date begin = DateUtils.truncate(now, Calendar.MONTH);
		Date end = DateUtils.add(begin, Calendar.MONTH, 1);
		buf
				// message type
				.append((char) SpFileReader.Message.MESSAGE_TYPE_HEADER)
				.append(RECORD_DELIMITER)

						// registry number
				.append(1)
				.append(RECORD_DELIMITER)

						// registry type
				.append(RegistryType.TYPE_CASH_PAYMENTS)
				.append(RECORD_DELIMITER)

						// records number
				.append(nRecords)
				.append(RECORD_DELIMITER)

						// generation date
				.append(dateFormat.format(now))
				.append(RECORD_DELIMITER)

						// begin date
				.append(dateFormat.format(begin))
				.append(RECORD_DELIMITER)

						// begin date
				.append(dateFormat.format(end))
				.append(RECORD_DELIMITER)

				.append(ApplicationConfig.getSelfOrganisation().getId())
				.append(RECORD_DELIMITER)

				.append(recipient.getId())
				.append(RECORD_DELIMITER)

						// registry amount
				.append(amount)
				.append(RECORD_DELIMITER)

						// containers
				.append("")
				.append(RECORD_DELIMITER);

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
