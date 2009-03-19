package org.flexpay.eirc.process.quittance.report;

import org.flexpay.common.util.CollectionUtils;

import java.util.Map;

public class QuittancesStats {

	private int count = 0;
	private Map<String, Integer> stats = CollectionUtils.map();

	/**
	 * Get number of records
	 *
	 * @return number of
	 */

	public int getCount() {
		return count;
	}

	public Map<String, Integer> getStats() {
		return stats;
	}

	public void addAddress(String address) {
		++count;
		Integer addressCount = stats.containsKey(address) ? stats.get(address) : 0;
		stats.put(address, addressCount + 1);
	}

}
