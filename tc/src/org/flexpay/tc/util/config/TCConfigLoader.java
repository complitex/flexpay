package org.flexpay.tc.util.config;

import org.flexpay.ab.util.config.AbConfigLoader;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.digester.Digester;

import java.net.URL;

public class TCConfigLoader extends AbConfigLoader {

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

		d.addCallMethod("flexpay/tcMaximumFloors", "setMaximumFloors", 16);
		d.addCallMethod("flexpay/tcMaximumPorches", "setMaximuPporches", 16);

		// setup main service code
		d.addCallMethod("flexpay/tcMaximumAppartments", "setMaximumAppartments", 1000);
	}

}
