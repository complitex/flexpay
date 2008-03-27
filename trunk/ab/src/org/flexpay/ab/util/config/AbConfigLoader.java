package org.flexpay.ab.util.config;

import org.flexpay.common.util.config.CommonConfigLoader;
import org.apache.commons.digester.Digester;

import java.net.URL;

public class AbConfigLoader extends CommonConfigLoader {

	public AbConfigLoader(URL[] configFiles) {
		super(configFiles);
	}

	/**
	 * Get ApplicationConfig, dependent nodes should replace config instance
	 *
	 * @return ApplicationConfig
	 */
	protected ApplicationConfig getNewConfig() {
		return new ApplicationConfig();
	}

	/**
	 * Add config loading rules
	 *
	 * @param d Digester
	 */
	protected void addRules(Digester d) {
		super.addRules(d);

		d.addSetProperties("flexpay/defaultCountry", "id", "defaultCountryId");
		d.addSetProperties("flexpay/defaultRegion", "id", "defaultRegionId");
		d.addSetProperties("flexpay/defaultTown", "id", "defaultTownId");
	}
}