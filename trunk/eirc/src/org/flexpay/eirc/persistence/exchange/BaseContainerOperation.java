package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Calendar;

public class BaseContainerOperation extends ContainerOperation {

	private static Logger log = Logger.getLogger(BaseContainerOperation.class);

	private ServiceOperationsFactory factory;

	private String serviceId;
	private BigDecimal incomingBalance;
	private BigDecimal outgoingBalance;
	private BigDecimal amount;
	private BigDecimal expence;
	private String rate;
	private BigDecimal recalculation;
	private BigDecimal benifit;
	private BigDecimal subsidy;
	private BigDecimal payment;

	public BaseContainerOperation(ServiceOperationsFactory factory, List<String> datum)
			throws InvalidContainerException {

		super(Integer.valueOf(datum.get(0)));

		if (datum.size() < 11) {
			throw new InvalidContainerException("Not enough data for quittance container");
		}

		int n = 1;

		serviceId = isBlank(datum.get(n)) ? null : datum.get(n);
		++n;

		incomingBalance = isBlank(datum.get(n)) ? null : new BigDecimal(datum.get(n));
		++n;

		outgoingBalance = isBlank(datum.get(n)) ? null : new BigDecimal(datum.get(n));
		++n;

		amount = isBlank(datum.get(n)) ? null : new BigDecimal(datum.get(n));
		++n;

		expence = isBlank(datum.get(n)) ? null : new BigDecimal(datum.get(n));
		++n;

		rate = isBlank(datum.get(n)) ? null : datum.get(n);
		++n;

		recalculation = isBlank(datum.get(n)) ? null : new BigDecimal(datum.get(n));
		++n;

		benifit = isBlank(datum.get(n)) ? null : new BigDecimal(datum.get(n));
		++n;

		subsidy = isBlank(datum.get(n)) ? null : new BigDecimal(datum.get(n));
		++n;

		payment = isBlank(datum.get(n)) ? null : new BigDecimal(datum.get(n));
		++n;

		this.factory = factory;
	}

	private boolean isBlank(String str) {
		return StringUtils.isBlank(str);
	}

	private Consumer getConsumer(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {
		ConsumerService consumerService = factory.getConsumerService();

		Consumer consumer;
		if (serviceId != null) {
			consumer = consumerService.findConsumer(
					registry.getServiceProvider(), record.getPersonalAccountExt(), serviceId);
			if (consumer == null) {
				throw new FlexPayException("Unnown service code: " + serviceId + ", cannot find consumer");
			}
		} else {
			consumer = (Consumer) record.getConsumer();
			if (consumer == null) {
				throw new FlexPayException("No consumer was set up");
			}
		}

		return consumer;
	}

	/**
	 * Process operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws FlexPayException if failure occurs
	 */
	public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {
		QuittanceService quittanceService = factory.getQuittanceService();

		if (record.getConsumer() == null) {
			throw new FlexPayException("Record consumer not set up");
		}

		if (record.getAmount() == null) {
			record.setAmount(BigDecimal.ZERO);
		}

		QuittanceDetails details = new QuittanceDetails();
		details.setConsumer(getConsumer(registry, record));
		details.setRegistryRecord(record);
		details.setIncomingBalance(incomingBalance);
		details.setOutgoingBalance(outgoingBalance);
		details.setAmount(amount);
		details.setExpence(expence);
		details.setRate(rate);
		details.setRecalculation(recalculation);
		details.setBenifit(benifit);
		details.setSubsidy(subsidy);
		details.setPayment(payment);
		details.setMonth(DateUtils.truncate(registry.getFromDate(), Calendar.MONTH));

		try {
			quittanceService.save(details);
		} catch (FlexPayExceptionContainer container) {
			FlexPayException e = container.getExceptions().iterator().next();
			log.error("Failed saving quittance details, first error is ", e);
			throw e;
		}
	}
}
