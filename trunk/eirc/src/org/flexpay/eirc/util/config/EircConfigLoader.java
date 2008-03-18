package org.flexpay.eirc.util.config;

import org.flexpay.common.util.config.CommonConfigLoader;
import org.apache.commons.digester.Digester;

import java.net.URL;

public class EircConfigLoader extends CommonConfigLoader {

	public EircConfigLoader(URL[] configFiles) {
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

//		d.addCallMethod("flexpay/defaultCountry", "setDefaultCountry", 0);
//		d.addCallMethod("flexpay/defaultRegion", "setDefaultRegion", 0);
//		d.addCallMethod("flexpay/defaultTown", "setDefaultTown", 0);
		d.addSetProperties("flexpay/defaultCountry", "id", "defaultCountryId");
		d.addSetProperties("flexpay/defaultRegion", "id", "defaultRegionId");
		d.addSetProperties("flexpay/defaultTown", "id", "defaultTownId");
		d.addSetProperties("flexpay/organisation", "id", "selfOrganisationId");
	}
}
