package org.flexpay.sz.util.config;

import org.apache.commons.digester.Digester;
import org.flexpay.ab.util.config.AbConfigLoader;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class SZConfigLoader extends AbConfigLoader {

	public SZConfigLoader(URL[] configFiles) {
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

		d.addCallMethod("flexpay/szDataRoot", "setSzDataRoot", 0);
		d.addCallMethod("flexpay/szDefaultDbfFileEncoding", "setSzDefaultDbfFileEncoding", 0);

	}

}