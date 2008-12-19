package org.flexpay.eirc.persistence;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.util.TranslationUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServiceProvider extends DomainObjectWithStatus {

	private Set<ServiceProviderDescription> descriptions = Collections.emptySet();
	private Organization organization;
	private DataSourceDescription dataSourceDescription;

	/**
	 * Constructs a new DomainObject.
	 */
	public ServiceProvider() {
	}

	public ServiceProvider(Long id) {
		super(id);
	}

	public Set<ServiceProviderDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<ServiceProviderDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * Getter for property 'dataSourceDescription'.
	 *
	 * @return Value for property 'dataSourceDescription'.
	 */
	public DataSourceDescription getDataSourceDescription() {
		return dataSourceDescription;
	}

	/**
	 * Setter for property 'dataSourceDescription'.
	 *
	 * @param dataSourceDescription Value to set for property 'dataSourceDescription'.
	 */
	public void setDataSourceDescription(DataSourceDescription dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}

	public void setDescription(ServiceProviderDescription description) {
		if (Collections.emptySet().equals(descriptions)) {
			descriptions = new HashSet<ServiceProviderDescription>();
		}

		ServiceProviderDescription candidate = null;
		for (ServiceProviderDescription descr : descriptions) {
			if (descr.isSameLanguage(description)) {
				candidate = descr;
				break;
			}
		}

		if (candidate != null) {
			if (StringUtils.isBlank(description.getName())) {
				descriptions.remove(candidate);
				return;
			}
			candidate.setName(description.getName());
			return;
		}

		if (StringUtils.isBlank(description.getName())) {
			return;
		}

		description.setTranslatable(this);
		descriptions.add(description);
	}

	public String getDefaultDescription() {
		ServiceProviderDescription desc = TranslationUtil.getTranslation(getDescriptions());
		return desc != null ? desc.getName() : "";
	}
}
