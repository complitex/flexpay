package org.flexpay.orgs.util.config;

import org.flexpay.orgs.service.Security;

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
