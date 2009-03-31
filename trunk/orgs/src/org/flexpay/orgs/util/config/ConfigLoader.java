package org.flexpay.orgs.util.config;

import org.apache.commons.digester.Digester;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class ConfigLoader extends org.flexpay.common.util.config.ConfigLoader {

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
	}
}
