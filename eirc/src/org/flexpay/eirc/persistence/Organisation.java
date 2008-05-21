package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Set;

public class Organisation extends DomainObjectWithStatus {

	private String individualTaxNumber;
	private String kpp; // code prichiny postanovki na nalogoviy uchet (TODO: translate me)
	private Set<OrganisationDescription> descriptions = Collections.emptySet();
	private Set<OrganisationName> names = Collections.emptySet();
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
}
