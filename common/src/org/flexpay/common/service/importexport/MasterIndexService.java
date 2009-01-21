package org.flexpay.common.service.importexport;

public interface MasterIndexService {

	/**
	 * Get next master index value
	 *
	 * @param type Objects type to get next index for
	 * @return next index value
	 */
	Long next(int type);

	/**
	 * Get batch of master index values
	 *
	 * @param type Objects type to get next indexes for
	 * @param number Number of required index values
	 * @return next index values, minimum-maximum pair
	 */
	Long[] nextBatch(int type, int number);
}
