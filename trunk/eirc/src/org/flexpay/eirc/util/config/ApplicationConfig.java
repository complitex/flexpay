package org.flexpay.eirc.util.config;

import org.flexpay.orgs.persistence.Organization;
import org.flexpay.eirc.service.Security;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ApplicationConfig extends org.flexpay.payments.util.config.ApplicationConfig {

	private Organization selfOrganization;
	private String eircDataRoot;
	private String eircId;

	private int eircMainServiceCode;

	static {
		// ensure Security fields are initialised
		Security.touch();
	}


	/**
	 * TODO: perform lookup by individual tax number, not id
	 *
	 * @param organizationId Organization id
	 */
	public void setSelfOrganizationId(String organizationId) {
		selfOrganization = new Organization(Long.valueOf(organizationId));
	}

	protected static ApplicationConfig getInstance() {
		return (ApplicationConfig) org.flexpay.common.util.config.ApplicationConfig.getInstance();
	}

	@NotNull
	public static Organization getSelfOrganization() {
		return getInstance().selfOrganization;
	}

	public static String getEircId() {
		return getInstance().eircId;
	}

	public static int getEircMainServiceCode() {
		return getInstance().eircMainServiceCode;
	}

	public void setEircMainServiceCode(String eircMainServiceCode) {
		this.eircMainServiceCode = Integer.valueOf(eircMainServiceCode);
	}

	public void setEircId(String eircId) {
		this.eircId = eircId;
	}

	public static File getEircDataRoot() {
		return getInstance().getEircDataRootInternal();
	}

	private File getEircDataRootInternal() {
		return new File(getDataRootInternal(), eircDataRoot);
	}

	public void setEircDataRoot(String eircDataRoot) {
		this.eircDataRoot = eircDataRoot;
		File eircRoot = getEircDataRootInternal();
		if (!eircRoot.exists()) {
			//noinspection ResultOfMethodCallIgnored
			eircRoot.mkdirs();
		}
	}
}
