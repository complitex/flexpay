package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateConsumer;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.quittance.ConsumerAttributes;

import java.util.List;

public class SetExternalOrganizationAccountOperation extends AbstractChangePersonalAccountOperation {

	private ServiceOperationsFactory factory;
	private Long organizationId;

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
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Override
	public DelayedUpdate process(Registry registry, RegistryRecord record) throws FlexPayException, FlexPayExceptionContainer {

		Organization organization = factory.getOrganizationService().readFull(new Stub<Organization>(organizationId));
		if (organization == null) {
			throw new FlexPayException("Invalid organization id " + organizationId);
		}

		if (ApplicationConfig.getMbOrganizationStub().sameId(organization)) {
			return setMbAccountNumber(record);
		}

		return DelayedUpdateNope.INSTANCE;
	}

	private DelayedUpdate setMbAccountNumber(RegistryRecord record) throws FlexPayException, FlexPayExceptionContainer {

		Consumer consumer = ContainerProcessHelper.getConsumer(record, factory);
		ConsumerAttributeTypeBase type = factory.getConsumerAttributeTypeService()
				.readByCode(ConsumerAttributes.ATTR_ERC_ACCOUNT);
		if (type == null) {
			throw new FlexPayException("Cannot find attribute " + ConsumerAttributes.ATTR_ERC_ACCOUNT);
		}

		ConsumerAttribute oldAttr = consumer.getAttributeForDate(type, changeApplyingDate);
		if (oldAttr == null || !StringUtils.equals(oldAttr.getStringValue(), newValue)) {
			ConsumerAttribute attribute = new ConsumerAttribute();
			attribute.setType(type);
			attribute.setStringValue(newValue);
			consumer.setTmpAttributeForDate(attribute, changeApplyingDate);
			return new DelayedUpdateConsumer(consumer, factory.getConsumerService());
		}

		return DelayedUpdateNope.INSTANCE;
	}
}