package org.flexpay.rent.util.config;

import org.flexpay.rent.service.Security;

public class ApplicationConfig extends org.flexpay.payments.util.config.ApplicationConfig {

	static {
		// ensure Security fields are initialised
		Security.touch();
	}
}
