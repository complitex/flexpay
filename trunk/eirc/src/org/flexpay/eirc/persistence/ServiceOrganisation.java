package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.eirc.util.config.ApplicationConfig;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * Service organisation
 */
public class ServiceOrganisation extends DomainObjectWithStatus {

	private Organisation organisation;
	private Set<ServedBuilding> servedBuildings = Collections.emptySet();
	private Set<ServiceOrganisationDescription> descriptions = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public ServiceOrganisation() {
	}

	public ServiceOrganisation(Long id) {
		super(id);
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public Set<ServedBuilding> getBuildings() {
		return servedBuildings;
	}

	public void setBuildings(Set<ServedBuilding> servedBuildings) {
		this.servedBuildings = servedBuildings;
	}

	public Set<ServiceOrganisationDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<ServiceOrganisationDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public String getName() throws Exception {
		return getName(ApplicationConfig.getDefaultLocale());
	}

	public String getName(Locale locale) throws Exception {
		return TranslationUtil.getTranslation(organisation.getNames(), locale).getName();
	}
}
