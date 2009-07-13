package org.flexpay.eirc.util.config;

import org.apache.commons.digester.Digester;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class ConfigLoader extends org.flexpay.payments.util.config.ConfigLoader {

	public ConfigLoader(URL[] configFiles) {
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

		d.addSetProperties("flexpay/organization", "id", "selfOrganizationId");

		d.addCallMethod("flexpay/eircDataRoot", "setEircDataRoot", 0);
		d.addCallMethod("flexpay/eircId", "setEircId", 0);

		// setup main service code
		d.addCallMethod("flexpay/eirc-main-service-code", "setEircMainServiceCode", 0);
	}

}
