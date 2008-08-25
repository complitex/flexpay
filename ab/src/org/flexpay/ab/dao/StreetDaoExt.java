package org.flexpay.ab.dao;

import java.util.Date;

public interface StreetDaoExt {

	void invalidateTypeTemporals(Long streetId, Date futureInfinity, Date invalidDate);

	/**
	 * Check if street is in a town
	 *
	 * @param townId   Town key to check in
	 * @param streetId Street key to check
	 * @return <code>true</code> if requested street is in a town
	 */
	boolean isStreetInTown(Long townId, Long streetId);
}