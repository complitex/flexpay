package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateQuittanceDetails;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

public class BaseContainerOperation extends ContainerOperation {

	private ServiceOperationsFactory factory;

	private String serviceId;
	private BigDecimal incomingBalance;
	private BigDecimal outgoingBalance;
	private BigDecimal expence;
	private BigDecimal rate;
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

		expence = isBlank(datum.get(n)) ? null : new BigDecimal(datum.get(n));
		++n;

		rate = isBlank(datum.get(n)) ? null : new BigDecimal(datum.get(n));
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

	private Consumer getConsumer(Registry registry, RegistryRecord record) throws FlexPayException {
		ConsumerService consumerService = factory.getConsumerService();

		Consumer consumer;
		if (serviceId != null && !"#".equals(serviceId)) {
			EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
			ServiceProvider provider = factory.getServiceProviderService().read(props.getServiceProviderStub());
			// todo save read full consumer in properties
			consumer = consumerService.findConsumer(
					new Stub<ServiceProvider>(provider), record.getPersonalAccountExt(), serviceId);
			if (consumer == null) {
				throw new FlexPayException("Cannot find consumer: SP-id=" + props.getServiceProvider().getId() +
										   ", account=" + record.getPersonalAccountExt() + ", code=" + serviceId);
			}
		} else {
			EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
			consumer = props.getConsumer();
			if (consumer == null) {
				throw new FlexPayException("No consumer was set up");
			}
		}

		return consumer;
	}

	/**
	 * ProcessInstance operation
	 *
	 * @param context processing context
	 * @throws FlexPayException if failure occurs
	 */
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException {
		QuittanceService quittanceService = factory.getQuittanceService();

		RegistryRecord record = context.getCurrentRecord();
		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		if (props.getConsumer() == null) {
			throw new FlexPayException("Record consumer not set up");
		}

		if (record.getAmount() == null) {
			record.setAmount(BigDecimal.ZERO);
		}

		QuittanceDetails details = new QuittanceDetails();
		details.setConsumer(getConsumer(context.getRegistry(), record));
		details.setRegistryRecord(record);
		details.setIncomingBalance(incomingBalance);
		details.setOutgoingBalance(outgoingBalance);
		details.setAmount(record.getAmount());
		details.setExpence(expence);
		details.setRate(rate);
		details.setRecalculation(recalculation);
		details.setBenifit(benifit);
		details.setSubsidy(subsidy);
		details.setPayment(payment);
		details.setMonth(DateUtils.truncate(context.getRegistry().getFromDate(), Calendar.MONTH));

		return new DelayedUpdateQuittanceDetails(details, quittanceService);
	}
}
