package org.flexpay.sz.util.config;

import org.apache.commons.digester.Digester;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class ConfigLoader extends org.flexpay.eirc.util.config.ConfigLoader {

	public ConfigLoader(URL[] configFiles) {
		super(configFiles);
	}

	/**
	 * Get ApplicationConfig, dependent nodes should replace config instance
	 *
	 * @return ApplicationConfig
	 */
	@NotNull
	@Override
	protected ApplicationConfig getNewConfig() {
		return new ApplicationConfig();
	}

	/**
	 * Add config loading rules
	 *
	 * @param d Digester
	 */
	@Override
	protected void addRules(Digester d) {
		super.addRules(d);

		d.addCallMethod("flexpay/szDataRoot", "setSzDataRoot", 0);
		d.addCallMethod("flexpay/szDefaultDbfFileEncoding", "setSzDefaultDbfFileEncoding", 0);

	}

}
