package org.flexpay.orgs.util.config;

import org.flexpay.orgs.service.Security;

public class ApplicationConfig extends org.flexpay.common.util.config.ApplicationConfig {

	static {
		// ensure Security fields are initialised
		Security.touch();
	}
}
