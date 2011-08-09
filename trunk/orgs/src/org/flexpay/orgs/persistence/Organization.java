package org.flexpay.orgs.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;

public class Organization extends DomainObjectWithStatus {

	private String individualTaxNumber;
	private String kpp; // code prichiny postanovki na nalogoviy uchet (TODO: translate me)
	private String juridicalAddress;
	private String postalAddress;

	private Set<OrganizationDescription> descriptions = set();
	private Set<OrganizationName> names = set();

	// instances
	private Set<ServiceProvider> serviceProviders = set();
	private Set<Bank> banks = set();
    private Set<ServiceOrganization> serviceOrganizations = set();
	private Set<PaymentCollector> paymentCollectors = set();

	private Set<BankAccount> accounts = set();
	private Set<Subdivision> childSubdivisions = set();
	private Set<Subdivision> dependentSubdivisions = set();

	private DataSourceDescription dataSourceDescription;

	public Organization() {
	}

	public Organization(@NotNull Long id) {
		super(id);
	}

	public Organization(@NotNull Stub<Organization> stub) {
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

	public Set<OrganizationDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<OrganizationDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public Set<OrganizationName> getNames() {
		return names;
	}

	public void setNames(Set<OrganizationName> names) {
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

	@Nullable
	public String getName(@NotNull Locale locale) {
		OrganizationName name = TranslationUtil.getTranslation(getNames(), locale);
		return name != null ? name.getName() : null;
	}

	@Nullable
	public String getName() {
		OrganizationName name = TranslationUtil.getTranslation(getNames());
		return name != null ? name.getName() : null;
	}

	public void setName(OrganizationName name) {
		names = TranslationUtil.setTranslation(names, this, name);
	}

	public void setDescription(OrganizationDescription description) {
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

    public Set<ServiceOrganization> getServiceOrganizations() {
        return serviceOrganizations;
    }

    public void setServiceOrganizations(Set<ServiceOrganization> serviceOrganizations) {
        this.serviceOrganizations = serviceOrganizations;
    }

	public Set<PaymentCollector> getPaymentCollectors() {
		return paymentCollectors;
	}

	public void setPaymentCollectors(Set<PaymentCollector> paymentCollectors) {
		this.paymentCollectors = paymentCollectors;
	}

	public DataSourceDescription getDataSourceDescription() {
		return dataSourceDescription;
	}

	public void setDataSourceDescription(DataSourceDescription dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}

	public Stub<DataSourceDescription> sourceDescriptionStub() {
		return stub(dataSourceDescription);
	}

	/**
	 * Get name translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public OrganizationName getNameTranslation(@NotNull Language lang) {

		for (OrganizationName translation : getNames()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	/**
	 * Get description translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public OrganizationDescription getDescriptionTranslation(@NotNull Language lang) {

		for (OrganizationDescription translation : getDescriptions()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	public String defaultDescription() {
		OrganizationDescription desc = TranslationUtil.getTranslation(getDescriptions());
		return desc != null ? desc.getName() : "";
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("individualTaxNumber", individualTaxNumber).
				append("kpp", kpp).
				append("juridicalAddress", juridicalAddress).
				append("postalAddress", postalAddress).
				toString();
	}

}
