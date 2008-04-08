package org.flexpay.eirc.util.config;

import java.io.File;

import org.flexpay.eirc.persistence.Organisation;

public class ApplicationConfig extends
		org.flexpay.ab.util.config.ApplicationConfig {

	private Organisation selfOrganisation;
	private File eircDataRoot;
	private String eircId;

	/**
	 * TODO: perform lookup by individual tax number, not id
	 * 
	 * @param organisationId
	 *            Organisation id
	 */
	public void setSelfOrganisationId(String organisationId) {
		selfOrganisation = new Organisation(Long.valueOf(organisationId));
	}

	public static ApplicationConfig getInstance() {
		return (ApplicationConfig) org.flexpay.common.util.config.ApplicationConfig
				.getInstance();
	}

	public Organisation getSelfOrganisation() {
		return selfOrganisation;
	}

	public String getEircId() {
		return eircId;
	}

	public void setEircId(String eircId) {
		this.eircId = eircId;
	}

	public File getEircDataRoot() {
		return eircDataRoot;
	}

	public void setEircDataRoot(String eircDataRoot) {
		this.eircDataRoot = new File(getDataRoot(), eircDataRoot);
		if (!this.eircDataRoot.exists()) {
			this.eircDataRoot.mkdirs();
		}
	}

}
