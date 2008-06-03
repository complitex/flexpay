package org.flexpay.eirc.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Organisation extends DomainObjectWithStatus {

	private String individualTaxNumber;
	private String kpp; // code prichiny postanovki na nalogoviy uchet (TODO: translate me)
	private Set<OrganisationDescription> descriptions = Collections.emptySet();
	private Set<OrganisationName> names = Collections.emptySet();
	private Set<ServiceProvider> serviceProviders = Collections.emptySet();
	private String uniqueId;

	/**
	 * Constructs a new DomainObject.
	 */
	public Organisation() {
	}

	public Organisation(Long id) {
		super(id);
	}

	public String getIndividualTaxNumber() {
		return individualTaxNumber;
	}

	public void setIndividualTaxNumber(String individualTaxNumber) {
		this.individualTaxNumber = individualTaxNumber;
	}

	public String getKpp() {
		return kpp;
	}

	public void setKpp(String kpp) {
		this.kpp = kpp;
	}

	public Set<OrganisationDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<OrganisationDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public Set<OrganisationName> getNames() {
		return names;
	}

	public void setNames(Set<OrganisationName> names) {
		this.names = names;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("KPP", kpp)
				.append("INN", individualTaxNumber)
				.append("names", getNames())
				.append("descriptions", getDescriptions())
				.toString();
	}

	public void setName(OrganisationName organisationName) {
		if (names == Collections.EMPTY_SET) {
			names = new HashSet<OrganisationName>();
		}

		OrganisationName candidate = null;
		for (OrganisationName name : names) {
			if (name.getLang().getId().equals(organisationName.getLang().getId())) {
				candidate = name;
				break;
			}
		}

		if (candidate != null) {
			if (StringUtils.isBlank(organisationName.getName())) {
				names.remove(candidate);
				return;
			}
			candidate.setName(organisationName.getName());
			return;
		}

		if (StringUtils.isBlank(organisationName.getName())) {
			return;
		}

		organisationName.setTranslatable(this);
		names.add(organisationName);
	}

	public void setDescription(OrganisationDescription organisationDescription) {
		if (descriptions == Collections.EMPTY_SET) {
			descriptions = new HashSet<OrganisationDescription>();
		}

		OrganisationDescription candidate = null;
		for (OrganisationDescription description : descriptions) {
			if (description.getLang().getId().equals(organisationDescription.getLang().getId())) {
				candidate = description;
				break;
			}
		}

		if (candidate != null) {
			if (StringUtils.isBlank(organisationDescription.getName())) {
				descriptions.remove(candidate);
				return;
			}
			candidate.setName(organisationDescription.getName());
			return;
		}

		if (StringUtils.isBlank(organisationDescription.getName())) {
			return;
		}

		organisationDescription.setTranslatable(this);
		descriptions.add(organisationDescription);
	}

	public Set<ServiceProvider> getServiceProviders() {
		return serviceProviders;
	}

	public void setServiceProviders(Set<ServiceProvider> serviceProviders) {
		this.serviceProviders = serviceProviders;
	}
}
