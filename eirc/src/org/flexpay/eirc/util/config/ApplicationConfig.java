package org.flexpay.eirc.util.config;

import org.flexpay.eirc.persistence.Organisation;

import java.io.File;

public class ApplicationConfig extends
		org.flexpay.ab.util.config.ApplicationConfig {

	private Organisation selfOrganisation;
	private String eircDataRoot;
	private String eircId;

	/**
	 * TODO: perform lookup by individual tax number, not id
	 *
	 * @param organisationId Organisation id
	 */
	public void setSelfOrganisationId(String organisationId) {
		selfOrganisation = new Organisation(Long.valueOf(organisationId));
	}

	protected static ApplicationConfig getInstance() {
		return (ApplicationConfig) org.flexpay.common.util.config.ApplicationConfig.getInstance();
	}

	public static Organisation getSelfOrganisation() {
		return getInstance().selfOrganisation;
	}

	public static String getEircId() {
		return getInstance().eircId;
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
			eircRoot.mkdirs();
		}
	}
}
