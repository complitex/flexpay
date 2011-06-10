package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;
import org.flexpay.eirc.service.ConsumerInfoService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class DelayedUpdateConsumerInfo implements DelayedUpdate {

	private ConsumerInfo info;
	private ConsumerInfoService service;

	public DelayedUpdateConsumerInfo(ConsumerInfo info, ConsumerInfoService service) {
		this.info = info;
		this.service = service;
	}

	/**
	 * Perform storage update
	 *
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if operation fails
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if operation fails
	 */
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	@Override
	public void doUpdate() throws FlexPayException, FlexPayExceptionContainer {
		service.save(info);
	}

	@Override
	public int hashCode() {
		return info.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof DelayedUpdateConsumerInfo &&
				((DelayedUpdateConsumerInfo) obj).info.equals(info);
	}
}
