package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.dao.ConsumerDao;
import org.flexpay.common.dao.paging.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class ConsumerServiceImpl implements ConsumerService {

	private ConsumerDao consumerDao;

	/**
	 * Try to find persistent consumer by example
	 *
	 * @param example Consumer
	 * @return Persistent consumer if found, or <code>null</code> otherwise
	 */
	public Consumer findConsumer(Consumer example) {
		List<Consumer> consumers = consumerDao.findConsumers(
				new Page(1, 1), // request the only record
				example.getResponsiblePerson().getId(),
				example.getService().getId(),
				example.getExternalAccountNumber(),
				example.getApartment().getId()
		);
		return consumers.isEmpty() ? null : consumers.get(0);
	}

	/**
	 * Setter for property 'consumerDao'.
	 *
	 * @param consumerDao Value to set for property 'consumerDao'.
	 */
	public void setConsumerDao(ConsumerDao consumerDao) {
		this.consumerDao = consumerDao;
	}
}
