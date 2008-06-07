package org.flexpay.eirc.service.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.dao.ConsumerDao;
import org.flexpay.eirc.dao.ConsumerDaoExt;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.service.ConsumerService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ConsumerServiceImpl implements ConsumerService {

	private ConsumerDao consumerDao;
	private ConsumerDaoExt consumerDaoExt;

	/**
	 * Try to find persistent consumer by example
	 *
	 * @param example Consumer
	 * @return Persistent consumer if found, or <code>null</code> otherwise
	 */
	public Consumer findConsumer(Consumer example) {
		List<Consumer> consumers = consumerDaoExt.findConsumers(
				new Page(1, 1), // request the only record
				example.getResponsiblePerson().getId(),
				example.getService().getId(),
				example.getExternalAccountNumber(),
				example.getApartment().getId()
		);
		return consumers.isEmpty() ? null : consumers.get(0);
	}

	/**
	 * Create or update Consumer object
	 *
	 * @param consumer Consumer to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation failure occurs
	 */
	@Transactional(readOnly = false)
	public void save(Consumer consumer) throws FlexPayExceptionContainer {
		if (consumer.isNew()) {
			consumer.setId(null);
			consumerDao.create(consumer);
		} else {
			consumerDao.update(consumer);
		}
	}

	public void setConsumerDao(ConsumerDao consumerDao) {
		this.consumerDao = consumerDao;
	}

	public void setConsumerDaoExt(ConsumerDaoExt consumerDaoExt) {
		this.consumerDaoExt = consumerDaoExt;
	}
}
