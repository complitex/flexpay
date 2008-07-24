package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class Organisation extends DomainObjectWithStatus {

	private String individualTaxNumber;
	private String kpp; // code prichiny postanovki na nalogoviy uchet (TODO: translate me)
	private Set<OrganisationDescription> descriptions = Collections.emptySet();
	private Set<OrganisationName> names = Collections.emptySet();
	private Set<ServiceProvider> serviceProviders = Collections.emptySet();
	private Set<Bank> banks = Collections.emptySet();

	private String juridicalAddress;
	private String postalAddress;

	private Set<BankAccount> accounts = Collections.emptySet();
	private Set<Subdivision> childSubdivisions = Collections.emptySet();
	private Set<Subdivision> dependentSubdivisions = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public Organisation() {
	}

	public Organisation(@NotNull Long id) {
		super(id);
	}

	public Organisation(@NotNull Stub<Organisation> stub) {
		super(stub.getId());
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

	public String getJuridicalAddress() {
		return juridicalAddress;
	}

	public void setJuridicalAddress(String juridicalAddress) {
		this.juridicalAddress = juridicalAddress;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public Set<BankAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<BankAccount> accounts) {
		this.accounts = accounts;
	}

	public Set<Subdivision> getChildSubdivisions() {
		return childSubdivisions;
	}

	public void setChildSubdivisions(Set<Subdivision> childSubdivisions) {
		this.childSubdivisions = childSubdivisions;
	}

	public Set<Subdivision> getDependentSubdivisions() {
		return dependentSubdivisions;
	}

	public void setDependentSubdivisions(Set<Subdivision> dependentSubdivisions) {
		this.dependentSubdivisions = dependentSubdivisions;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("KPP", kpp)
				.append("INN", individualTaxNumber)
				.toString();
	}

	public void setName(OrganisationName name) {
		names = TranslationUtil.setTranslation(names, this, name);
	}

	public void setDescription(OrganisationDescription description) {
		descriptions = TranslationUtil.setTranslation(descriptions, this, description);
	}

	public Set<ServiceProvider> getServiceProviders() {
		return serviceProviders;
	}

	public void setServiceProviders(Set<ServiceProvider> serviceProviders) {
		this.serviceProviders = serviceProviders;
	}

	public Set<Bank> getBanks() {
		return banks;
	}

	public void setBanks(Set<Bank> banks) {
		this.banks = banks;
	}
}
