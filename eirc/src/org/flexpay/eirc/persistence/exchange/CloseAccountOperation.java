package org.flexpay.eirc.persistence.exchange;

import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;

import java.util.List;

/**
 * Close service provider personal account
 */
public class CloseAccountOperation extends AbstractChangePersonalAccountOperation {

	private ServiceOperationsFactory factory;

	public CloseAccountOperation(ServiceOperationsFactory factory, List<String> datum)
			throws InvalidContainerException {

		super(datum);
		this.factory = factory;
	}

	/**
	 * Process operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public void process(SpRegistry registry, RegistryRecord record) throws FlexPayException {

		Consumer consumer = (Consumer) record.getConsumer();
		if (consumer == null) {
			throw new FlexPayException("Cannot close account without consumer set");
		}

		// disable consumer
		consumer.disable();
		try {
			factory.getConsumerService().save(consumer);
		} catch (FlexPayExceptionContainer container) {
			throw container.getFirstException();
		}

		// todo remove corrections to consumer

		EircAccount account = factory.getAccountService().readFull(consumer.getEircAccountStub());
		if (account == null) {
			throw new IllegalStateException("EIRC account not found for consumer: " + consumer);
		}
	}
}
