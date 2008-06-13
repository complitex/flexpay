package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.service.ConsumerInfoService;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.flexpay.eirc.dao.ConsumerInfoDao;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ConsumerInfoServiceImpl implements ConsumerInfoService {

	private ConsumerInfoDao consumerInfoDao;
	
	/**
	 * Create or update ConsumerInfo instance
	 *
	 * @param info ConsumerInfo instance to save
	 */
	@Transactional(readOnly = false)
	public void save(ConsumerInfo info) {
		if (info.isNew()) {
			info.setId(null);
			consumerInfoDao.create(info);
		} else {
			consumerInfoDao.update(info);
		}
	}

	/**
	 * Read Consumerinfo details
	 *
	 * @param stub Consumerinfo stub
	 * @return ConsumerInfo if found, or <code>null</code> otherwise
	 */
	public ConsumerInfo readInfo(ConsumerInfo stub) {
		if (!stub.isNew()) {
			return consumerInfoDao.read(stub.getId());
		}

		return new ConsumerInfo(0L);
	}

	public void setConsumerInfoDao(ConsumerInfoDao consumerInfoDao) {
		this.consumerInfoDao = consumerInfoDao;
	}
}
