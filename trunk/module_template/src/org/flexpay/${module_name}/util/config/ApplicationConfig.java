package org.flexpay.${module_name}.util.config;

import org.flexpay.${module_name}.service.Security;

public class ApplicationConfig extends org.flexpay.${parent_module_name}.util.config.ApplicationConfig {

	static {
		// ensure Security fields are initialised
		Security.touch();
	}
}
