package org.flexpay.payments.util.config;

import org.flexpay.payments.service.Security;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.common.persistence.Stub;

public class ApplicationConfig extends org.flexpay.ab.util.config.ApplicationConfig {

	private Stub<Organization> mbOrganizationStub;

	static {
		// ensure Security fields are initialised
		Security.touch();
	}

	protected static ApplicationConfig getInstance() {
		return (ApplicationConfig) org.flexpay.common.util.config.ApplicationConfig.getInstance();
	}

	/**
	 * TODO: perform lookup by individual tax number, not id
	 *
	 * @param organizationId Organization id
	 */
	public void setMbOrganizationId(String organizationId) {
		mbOrganizationStub = new Stub<Organization>(Long.valueOf(organizationId));
	}

	public static Stub<Organization> getMbOrganizationStub() {
		return getInstance().mbOrganizationStub;
	}
}
