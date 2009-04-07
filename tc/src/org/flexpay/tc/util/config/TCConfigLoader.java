package org.flexpay.tc.util.config;

import org.apache.commons.digester.Digester;
import org.flexpay.ab.util.config.ConfigLoader;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class TCConfigLoader extends ConfigLoader {

	public TCConfigLoader(URL[] configFiles) {
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

		d.addCallMethod("flexpay/tcDataRoot", "setTcDataRoot", 0);

		d.addCallMethod("flexpay/tcMaximumFloors", "setMaximumFloors", 0);
		d.addCallMethod("flexpay/tcMaximumPorches", "setMaximumPporches", 0);
		d.addCallMethod("flexpay/tcMaximumApartments", "setMaximumApartments", 0);

	}

}
