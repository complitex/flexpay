package org.flexpay.eirc.util.config;

import org.flexpay.eirc.persistence.Organisation;

public class ApplicationConfig extends org.flexpay.ab.util.config.ApplicationConfig {

	private Organisation selfOrganisation;

	/**
	 * TODO: perform lookup by individual tax number, not id
	 *
	 * @param organisationId Organisation id
	 */
	public void setSelfOrganisationId(String organisationId) {
		selfOrganisation = new Organisation(Long.valueOf(organisationId));
	}

	public static ApplicationConfig getInstance() {
		return (ApplicationConfig) org.flexpay.common.util.config.ApplicationConfig.getInstance();
	}

	public Organisation getSelfOrganisation() {
		return selfOrganisation;
	}
}
