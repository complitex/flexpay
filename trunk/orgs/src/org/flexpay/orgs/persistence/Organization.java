package org.flexpay.orgs.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.DataSourceDescription;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class Organization extends DomainObjectWithStatus {

	private String individualTaxNumber;
	private String kpp; // code prichiny postanovki na nalogoviy uchet (TODO: translate me)
	private String juridicalAddress;
	private String postalAddress;

	private Set<OrganizationDescription> descriptions = Collections.emptySet();
	private Set<OrganizationName> names = Collections.emptySet();

	// instances
	private Set<ServiceProvider> serviceProviders = Collections.emptySet();
	private Set<Bank> banks = Collections.emptySet();
    private Set<ServiceOrganization> serviceOrganizations = Collections.emptySet();
	private Set<PaymentsCollector> paymentsCollectors = Collections.emptySet();

	private Set<BankAccount> accounts = Collections.emptySet();
	private Set<Subdivision> childSubdivisions = Collections.emptySet();
	private Set<Subdivision> dependentSubdivisions = Collections.emptySet();

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

	public Set<PaymentsCollector> getPaymentsCollectors() {
		return paymentsCollectors;
	}

	public void setPaymentsCollectors(Set<PaymentsCollector> paymentsCollectors) {
		this.paymentsCollectors = paymentsCollectors;
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

	public String defaultDescription() {
		OrganizationDescription desc = TranslationUtil.getTranslation(getDescriptions());
		return desc != null ? desc.getName() : "";
	}
}
