package org.flexpay.${module_name}.util.config;

import org.flexpay.${module_name}.service.Security;

public class ApplicationConfig {

	private static final ApplicationConfig INSTANCE = new ApplicationConfig();

	static {
		// ensure Security fields are initialised
		Security.touch();
	}

	public static ApplicationConfig getInstance() {
		return INSTANCE;
	}
}
