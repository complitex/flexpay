package org.flexpay.ab.util.config;

import org.apache.commons.digester.Digester;
import org.flexpay.common.util.config.CommonConfigLoader;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

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
	@NotNull
	protected ApplicationConfig getNewConfig() {
		return new ApplicationConfig();
	}

	/**
	 * Add config loading rules
	 *
	 * @param d Digester
	 */
	protected void addRules(@NonNls Digester d) {
		super.addRules(d);

		d.addSetProperties("flexpay/defaultCountry", "id", "defaultCountryId");
		d.addSetProperties("flexpay/defaultRegion", "id", "defaultRegionId");
		d.addSetProperties("flexpay/defaultTown", "id", "defaultTownId");

		d.addSetProperties("flexpay/buildingAttributeTypeNumber", "id", "buildingAttributeTypeNumberId");
		d.addSetProperties("flexpay/buildingAttributeTypeBulk", "id", "buildingAttributeTypeBulkId");
	}
}
