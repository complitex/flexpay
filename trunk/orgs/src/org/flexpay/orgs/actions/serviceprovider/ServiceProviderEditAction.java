package org.flexpay.orgs.actions.serviceprovider;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class ServiceProviderEditAction extends FPActionSupport {

	private OrganizationFilter organizationFilter = new OrganizationFilter();
	private ServiceProvider provider = new ServiceProvider();
	private Map<Long, String> descriptions = CollectionUtils.map();

	private ServiceProviderService providerService;
	private OrganizationService organizationService;

	@NotNull
	public String doExecute() throws Exception {

		if (provider.getId() == null) {
			addActionError("No object was selected");
			return REDIRECT_SUCCESS;
		}

		ServiceProvider serviceProvider = provider.isNew() ? provider : providerService.read(stub(provider));
		if (serviceProvider == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}
		organizationFilter = providerService.initOrganizationFilter(organizationFilter, serviceProvider);
		if (organizationFilter.getOrganizations().isEmpty()) {
			addActionError(getText("eirc.error.service_provider.no_providerless_organization"));
			return INPUT;
		}

		if (!isSubmit()) {
			provider = serviceProvider;
			initDescriptions();
			return INPUT;
		}

		if (!organizationFilter.needFilter()) {
			addActionError(getText("eirc.error.orginstance.no_organization_selected"));
			return INPUT;
		}
		Organization juridicalPerson = organizationService.readFull(organizationFilter.getSelectedStub());
		if (juridicalPerson == null) {
			addActionError(getText("eirc.error.orginstance.no_organization"));
			return INPUT;
		}

		log.info("Service provider descriptions: {}", descriptions);

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			ServiceProviderDescription description = new ServiceProviderDescription();
			description.setLang(lang);
			description.setName(value);

			log.info("Setting description: {}", description);

			serviceProvider.setDescription(description);
		}

		log.info("New Service provider descriptions: {}", serviceProvider.getDescriptions());

		serviceProvider.setOrganization(juridicalPerson);
		providerService.save(serviceProvider);

		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

	private void initDescriptions() {
		for (ServiceProviderDescription description : provider.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (descriptions.containsKey(lang.getId())) {
				continue;
			}
			descriptions.put(lang.getId(), "");
		}
	}

	public ServiceProvider getProvider() {
		return provider;
	}

	public void setProvider(ServiceProvider provider) {
		this.provider = provider;
	}

	public Map<Long, String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Map<Long, String> descriptions) {
		this.descriptions = descriptions;
	}

	public OrganizationFilter getOrganizationFilter() {
		return organizationFilter;
	}

	public void setOrganizationFilter(OrganizationFilter organizationFilter) {
		this.organizationFilter = organizationFilter;
	}

	@Required
	public void setProviderService(ServiceProviderService providerService) {
		this.providerService = providerService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

}
