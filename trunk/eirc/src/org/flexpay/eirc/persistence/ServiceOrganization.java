package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * Service organization
 */
public class ServiceOrganization extends DomainObjectWithStatus {

	private Organization organization;
	private Set<ServedBuilding> servedBuildings = Collections.emptySet();
	private Set<ServiceOrganizationDescription> descriptions = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public ServiceOrganization() {
	}

	public ServiceOrganization(Long id) {
		super(id);
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Set<ServedBuilding> getBuildings() {
		return servedBuildings;
	}

	public void setBuildings(Set<ServedBuilding> servedBuildings) {
		this.servedBuildings = servedBuildings;
	}

    @NotNull
	public Set<ServiceOrganizationDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(@NotNull Set<ServiceOrganizationDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public String getName() throws Exception {
		return getName(ApplicationConfig.getDefaultLocale());
	}

	public String getName(Locale locale) throws Exception {
		return TranslationUtil.getTranslation(organization.getNames(), locale).getName();
	}

    public void setDescription(ServiceOrganizationDescription description) {
        descriptions = TranslationUtil.setTranslation(descriptions, this, description);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ServiceOrganization");
        sb.append("{id=").append(getId());
        sb.append('}');
        return sb.toString();
    }

}
