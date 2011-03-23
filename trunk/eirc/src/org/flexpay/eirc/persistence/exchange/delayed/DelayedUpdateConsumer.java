package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;
import org.flexpay.eirc.service.ConsumerService;

public class DelayedUpdateConsumer implements DelayedUpdate {

	private Consumer consumer;
	private ConsumerService service;

	public DelayedUpdateConsumer(Consumer consumer, ConsumerService service) {
		this.consumer = consumer;
		this.service = service;
	}

	/**
	 * Perform storage update
	 */
	@Override
	public void doUpdate() throws FlexPayExceptionContainer {
		service.save(consumer);
	}

	@Override
	public int hashCode() {
		return consumer.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof DelayedUpdateConsumer
			   && consumer.equals(((DelayedUpdateConsumer) obj).consumer);
	}
}
