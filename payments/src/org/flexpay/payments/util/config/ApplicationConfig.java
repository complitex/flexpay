package org.flexpay.payments.util.config;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.service.Security;
import org.springframework.beans.factory.annotation.Required;

public class ApplicationConfig {

	private Stub<Organization> mbOrganizationStub;
	private String keystorePath;
	private String keystorePassword;

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

	public static String getKeystorePath() {
		return getInstance().keystorePath;
	}

	@Required
	public void setKeystorePath(String keystorePath) {
		this.keystorePath = keystorePath;
	}

	public static String getKeystorePassword() {
		return getInstance().keystorePassword;
	}

	@Required
	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}
}
