package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.ConsumerService;

import java.math.BigDecimal;
import java.util.List;

public class BaseContainerOperation extends ContainerOperation {

	private ServiceOperationsFactory factory;

	private String subserviceId;
	private BigDecimal incomingSaldo;
	private BigDecimal outgoingSaldo;
	private BigDecimal cost;
	private BigDecimal tariff;
	private BigDecimal charges;
	private BigDecimal reappraisal;
	private BigDecimal privileges;
	private BigDecimal subsidy;
	private BigDecimal payments;


	public BaseContainerOperation(ServiceOperationsFactory factory, List<String> datum)
			throws InvalidContainerException {


		super(Integer.valueOf(datum.get(0)));

		if (datum.size() < 11) {
			throw new InvalidContainerException("Invalid base container data");
		}

		int n = 1;
		subserviceId = datum.get(n++);

		String amount = datum.get(n++);
		if (StringUtils.isBlank(amount)) {
			throw new InvalidContainerException("Incoming saldo is empty");
		}
		incomingSaldo = new BigDecimal(amount);

		amount = datum.get(n++);
		if (StringUtils.isBlank(amount)) {
			throw new InvalidContainerException("Outgoing saldo is empty");
		}
		outgoingSaldo = new BigDecimal(amount);

		amount = datum.get(n++);
		if (StringUtils.isNotBlank(amount)) {
			cost = new BigDecimal(amount);
		}

		amount = datum.get(n++);
		if (StringUtils.isNotBlank(amount)) {
			tariff = new BigDecimal(amount);
		}

		amount = datum.get(n++);
		if (StringUtils.isNotBlank(amount)) {
			charges = new BigDecimal(amount);
		}

		amount = datum.get(n++);
		if (StringUtils.isNotBlank(amount)) {
			reappraisal = new BigDecimal(amount);
		}

		amount = datum.get(n++);
		if (StringUtils.isNotBlank(amount)) {
			privileges = new BigDecimal(amount);
		}

		amount = datum.get(n++);
		if (StringUtils.isNotBlank(amount)) {
			subsidy = new BigDecimal(amount);
		}

		amount = datum.get(n);
		if (StringUtils.isNotBlank(amount)) {
			payments = new BigDecimal(amount);
		}

		this.factory = factory;
	}

	private Consumer getConsumer(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {
		ConsumerService consumerService = factory.getConsumerService();

		Consumer consumer;
		if (StringUtils.isNotBlank(subserviceId)) {
			consumer = consumerService.findConsumer(
					registry.getServiceProvider(), record.getPersonalAccountExt(), subserviceId);
			if (consumer == null) {
				throw new FlexPayException("Unnown service code: " + subserviceId + ", cannot find consumer");
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
		throw new RuntimeException("Not implemented");
	}
}
