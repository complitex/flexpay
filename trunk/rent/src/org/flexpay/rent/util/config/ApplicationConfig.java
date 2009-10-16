package org.flexpay.rent.util.config;

import org.flexpay.rent.service.Security;

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
