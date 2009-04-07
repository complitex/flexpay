package org.flexpay.payments.util.config;

import org.flexpay.payments.service.Security;

public class ApplicationConfig extends org.flexpay.ab.util.config.ApplicationConfig {

	static {
		// ensure Security fields are initialised
		Security.touch();
	}
}
