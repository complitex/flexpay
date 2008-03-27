package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.UpdateConfig;

public interface UpdateConfigDao {

	/**
	 * Read update config
	 *
	 * @return UpdateConfig instance
	 */
	UpdateConfig getConfig();

	/**
	 * Save update config
	 *
	 * @param config UpdateConfig to store
	 */
	void saveConfig(UpdateConfig config);
}
