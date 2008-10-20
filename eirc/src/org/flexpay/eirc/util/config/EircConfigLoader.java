package org.flexpay.eirc.util.config;

import org.apache.commons.digester.Digester;
import org.flexpay.ab.util.config.AbConfigLoader;
import org.jetbrains.annotations.NotNull;

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
	@NotNull
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

		d.addCallMethod("flexpay/eircDataRoot", "setEircDataRoot", 0);
		d.addCallMethod("flexpay/eircId", "setEircId", 0);

		// setup main service code
		d.addCallMethod("flexpay/eirc-main-service-code", "setEircMainServiceCode", 0);
	}
}
