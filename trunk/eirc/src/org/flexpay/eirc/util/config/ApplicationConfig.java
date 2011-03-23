package org.flexpay.eirc.util.config;

import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.service.Security;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;

import static org.flexpay.common.util.config.ApplicationConfig.getDataRoot;

public class ApplicationConfig {

	private Organization selfOrganization;
	private String eircDataRoot;
	private String eircId;

	private int eircMainServiceCode;

	private static final ApplicationConfig INSTANCE = new ApplicationConfig();

	static {
		// ensure Security fields are initialised
		Security.touch();
	}


	@NotNull
	public static ApplicationConfig getInstance() {
		return INSTANCE;
	}

	@NotNull
	public static Organization getSelfOrganization() {
		return getInstance().selfOrganization;
	}

	@NotNull
	public static Stub<Organization> getSelfOrganizationStub() {
		return Stub.stub(getSelfOrganization());
	}

	public static String getEircId() {
		return getInstance().eircId;
	}

	public static int getEircMainServiceCode() {
		return getInstance().eircMainServiceCode;
	}

	@Required
	public void setEircMainServiceCode(Integer eircMainServiceCode) {
		this.eircMainServiceCode = eircMainServiceCode;
	}

	@Required
	public void setEircId(String eircId) {
		this.eircId = eircId;
	}

	public void setSelfOrganizationId(Long organizationId) {
		selfOrganization = new Organization(organizationId);
	}

	@Required
	public void setEircDataRoot(String eircDataRoot) {
		this.eircDataRoot = eircDataRoot;
		File eircRoot = getEircDataRootInternal();
		if (!eircRoot.exists()) {
			//noinspection ResultOfMethodCallIgnored
			eircRoot.mkdirs();
		}
	}

	private File getEircDataRootInternal() {
		return new File(getDataRoot(), eircDataRoot);
	}
}
