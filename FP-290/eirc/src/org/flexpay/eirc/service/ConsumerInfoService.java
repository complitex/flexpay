package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.ConsumerInfo;

public interface ConsumerInfoService {

	/**
	 * Read Consumerinfo details
	 *
	 * @param stub ConsumerInfo stub
	 * @return ConsumerInfo if found, or new instance otherwise
	 */
	ConsumerInfo readInfo(ConsumerInfo stub);

	/**
	 * Create or update ConsumerInfo instance
	 *
	 * @param info ConsumerInfo instance to save
	 */
	void save(ConsumerInfo info);
}
