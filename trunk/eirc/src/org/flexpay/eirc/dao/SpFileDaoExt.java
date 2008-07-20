package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.SpRegistryType;

public interface SpFileDaoExt {

	SpRegistryType getRegistryType(int code);

	/**
	 * Clear current session
	 */
	void clearSession();
}
