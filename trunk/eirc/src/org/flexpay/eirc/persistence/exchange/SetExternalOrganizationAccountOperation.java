package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateConsumer;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.orgs.persistence.Organization;

import static org.flexpay.payments.util.config.ApplicationConfig.getMbOrganizationStub;

import org.flexpay.payments.actions.outerrequest.request.response.data.ConsumerAttributes;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SetExternalOrganizationAccountOperation extends AbstractChangePersonalAccountOperation {

	private static final Logger log = LoggerFactory.getLogger(SetExternalOrganizationAccountOperation.class);

	private ServiceOperationsFactory factory;
	private Long organizationId;

	static private final StopWatch getConsumerAttributeTypeWatch = new StopWatch();
	static private final StopWatch setMbAccountNumberWatch = new StopWatch();
	static private final StopWatch visitUpdatesWatch = new StopWatch();
	static private final StopWatch getConsumerWatch = new StopWatch();
	static private final StopWatch consumerGetAttributeForDate = new StopWatch();
	static private final StopWatch consumerSetTmpAttributeForDate = new StopWatch();

	static {

		getConsumerAttributeTypeWatch.start();
		getConsumerAttributeTypeWatch.suspend();

		setMbAccountNumberWatch.start();
		setMbAccountNumberWatch.suspend();

		visitUpdatesWatch.start();
		visitUpdatesWatch.suspend();

		getConsumerWatch.start();
		getConsumerWatch.suspend();

		consumerGetAttributeForDate.start();
		consumerGetAttributeForDate.suspend();

		consumerSetTmpAttributeForDate.start();
		consumerSetTmpAttributeForDate.suspend();
	}

	public SetExternalOrganizationAccountOperation(ServiceOperationsFactory factory, List<String> datum)
			throws InvalidContainerException {

		super(datum);
		this.factory = factory;

		if (datum.size() != 5) {
			throw new InvalidContainerException("Expected 5 fields");
		}
		try {
			organizationId = Long.parseLong(datum.get(4));
			if (organizationId == null || organizationId <= 0) {
				throw new InvalidContainerException("Illegal organization id specified: " + datum.get(4));
			}
		} catch (NumberFormatException ex) {
			throw new InvalidContainerException("Invalid organization id specified: " + datum.get(4));
		}
	}

	/**
	 * Process operation
	 *
	 * @param context ProcessingContext
	 * @throws FlexPayException if failure occurs
	 */
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer {

		// check if needs only to setup document addition
//		visitUpdatesWatch.resume();
		try {
			if (context.getRegistry().getRegistryType().isPayments()) {
				return visitUpdates(context);
			}
		} finally {
//			visitUpdatesWatch.suspend();
		}

//		setMbAccountNumberWatch.resume();
		try {
			if (getMbOrganizationStub().getId().equals(organizationId)) {
				return setMbAccountNumber(context.getCurrentRecord());
			}
		} finally {
//			setMbAccountNumberWatch.suspend();
		}

		return DelayedUpdateNope.INSTANCE;
	}

	private DelayedUpdate setMbAccountNumber(RegistryRecord record) throws FlexPayException, FlexPayExceptionContainer {

//		getConsumerWatch.resume();
		Consumer consumer = ContainerProcessHelper.getConsumer(record, factory);
//		getConsumerWatch.suspend();

//		getConsumerAttributeTypeWatch.resume();
		ConsumerAttributeTypeBase type = factory.getConsumerAttributeTypeService()
				.readByCode(org.flexpay.payments.actions.outerrequest.request.response.data.ConsumerAttributes.ATTR_ERC_ACCOUNT);
//		getConsumerAttributeTypeWatch.suspend();
		if (type == null) {
			throw new FlexPayException("Cannot find attribute " + ConsumerAttributes.ATTR_ERC_ACCOUNT);
		}

//		consumerGetAttributeForDate.resume();
		ConsumerAttribute oldAttr = consumer.getAttributeForDate(type, changeApplyingDate);
//		consumerGetAttributeForDate.suspend();

		if (oldAttr == null || !StringUtils.equals(oldAttr.getStringValue(), newValue)) {
			ConsumerAttribute attribute = new ConsumerAttribute();
			attribute.setType(type);
			attribute.setStringValue(newValue);
//			consumerSetTmpAttributeForDate.resume();
			consumer.setTmpAttributeForDate(attribute, changeApplyingDate);
//			consumerSetTmpAttributeForDate.suspend();
			return new DelayedUpdateConsumer(consumer, factory.getConsumerService());
		}

		return DelayedUpdateNope.INSTANCE;
	}

	private DelayedUpdate visitUpdates(ProcessingContext context) throws FlexPayException {

		context.visitCurrentRecordUpdates(new DelayedUpdateVisitor() {
			@Override
			public void apply(DelayedUpdate update) {
				if (update instanceof ExternalOrganizationAccountAwareUpdate) {
					((ExternalOrganizationAccountAwareUpdate) update).setExternalAccount(newValue, new Stub<Organization>(organizationId));
				}
			}
		});

		return DelayedUpdateNope.INSTANCE;
	}

	public static void printWatch() {
		log.debug("Time get consumer attribute type: {}, set Mb account number: {}, visit updates: {}\n" +
				"get consumer: {}, consumer get attribute for date: {}, consumer set tmp attribute for date: {}" ,
				new Object[]{getConsumerAttributeTypeWatch, setMbAccountNumberWatch, visitUpdatesWatch,
				getConsumerWatch, consumerGetAttributeForDate, consumerSetTmpAttributeForDate});
	}
}
