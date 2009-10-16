package org.flexpay.payments.util.config;

import org.flexpay.payments.service.Security;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.common.persistence.Stub;
import org.springframework.beans.factory.annotation.Required;

public class ApplicationConfig {

	private Stub<Organization> mbOrganizationStub;

	private static final ApplicationConfig INSTANCE = new ApplicationConfig();

	static {
		// ensure Security fields are initialised
		Security.touch();
	}

	public static ApplicationConfig getInstance() {
		return INSTANCE;
	}

	@Required
	public void setMbOrganizationId(Long organizationId) {
		mbOrganizationStub = new Stub<Organization>(organizationId);
	}

	public static Stub<Organization> getMbOrganizationStub() {
		return getInstance().mbOrganizationStub;
	}
}
