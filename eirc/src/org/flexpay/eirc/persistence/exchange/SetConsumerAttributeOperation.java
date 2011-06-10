package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateConsumer;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Set Consumer attribute
 */
public abstract class SetConsumerAttributeOperation extends ContainerOperation {

	protected ServiceOperationsFactory factory;
	protected Date applyingDate;
	protected String value;

	public SetConsumerAttributeOperation(ServiceOperationsFactory factory, List<String> datum)
		throws InvalidContainerException {

		super(Integer.valueOf(datum.get(0)));

		if (datum.size() < 2) {
			throw new InvalidContainerException("Invalid change personal account operation data");
		}

		try {
			String dateStr = datum.get(1);
			if (StringUtils.isBlank(dateStr)) {
				applyingDate = DateUtil.now();
			} else if (dateStr.length() == "ddMMyyyy".length()) {
				applyingDate = new SimpleDateFormat("ddMMyyyy").parse(dateStr);
			} else if (dateStr.length() == "ddMMyyyyHHmmss".length()) {
				applyingDate = new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateStr);
			} else {
				applyingDate = DateUtil.now();
			}
			if (DateUtil.now().before(applyingDate)) {
				throw new InvalidContainerException("Someone invented time machine? Specified date is in a future: " + datum.get(1));
			}
		} catch (ParseException e) {
			throw new InvalidContainerException("Cannot parse date: " + datum.get(1));
		}

		value = datum.get(2);

		this.factory = factory;

	}

	/**
	 * ProcessInstance operation
	 *
	 * @param context ProcessingContext
	 * @throws FlexPayException if failure occurs
	 */
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer {
		Consumer consumer = ContainerProcessHelper.getConsumer(context.getCurrentRecord(), factory);

		ConsumerAttributeTypeBase type = factory.getConsumerAttributeTypeService()
				.readByCode(getConsumerAttributeCode());
		if (type == null) {
			throw new FlexPayException("Cannot find attribute " + getConsumerAttributeCode());
		}

		ConsumerAttribute oldAttr = consumer.getAttributeForDate(type, applyingDate);

		if (oldAttr != null && oldAttr.isNew()) {
			setAttributeValue(oldAttr);
		} else if (oldAttr == null || changed(oldAttr)) {
			ConsumerAttribute attribute = new ConsumerAttribute();
			setAttributeValue(attribute);
			attribute.setType(type);
			consumer.setTmpAttributeForDate(attribute, applyingDate);
			return new DelayedUpdateConsumer(consumer, factory.getConsumerService());
		}
		return DelayedUpdateNope.INSTANCE;
	}

	/**
	 * Compare exist attribute value with new data
	 *
	 * @param oldAttribute Old attribute
	 * @return <code>true</code> if old attribute was not changed, or <code>false</code> otherwise
	 */
	protected abstract boolean changed(ConsumerAttribute oldAttribute);

	/**
	 * Set new  consumer attribute value
	 *
	 * @param attribute Consumer attribute
	 */
	protected abstract void setAttributeValue(ConsumerAttribute attribute);

	protected abstract String getConsumerAttributeCode();

}