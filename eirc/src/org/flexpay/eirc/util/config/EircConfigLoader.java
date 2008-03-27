package org.flexpay.eirc.util.config;

import org.flexpay.common.util.config.CommonConfigLoader;
import org.flexpay.ab.util.config.AbConfigLoader;
import org.apache.commons.digester.Digester;

import java.net.URL;

public class EircConfigLoader extends AbConfigLoader {

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

		d.addSetProperties("flexpay/organisation", "id", "selfOrganisationId");
	}
}
