package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.SpRegistryType;

public interface SpFileDaoExt {

	SpRegistryType getRegistryType(int type);

	/**
	 * Clear current session
	 */
	void clearSession();
}
