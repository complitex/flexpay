package org.flexpay.eirc.actions.serviceprovider;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceProviderDescription;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.service.OrganizationService;
import org.flexpay.eirc.service.SPService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ServiceProviderEditAction extends FPActionSupport {

	private SPService spService;
	private OrganizationService organizationService;

	private OrganizationFilter organizationFilter = new OrganizationFilter();
	private ServiceProvider provider = new ServiceProvider();
	private Map<Long, String> descriptions = map();

	@NotNull
	public String doExecute() throws Exception {

		if (provider.getId() == null) {
			addActionError("No object was selected");
			return REDIRECT_SUCCESS;
		}

		ServiceProvider serviceProvider = spService.read(provider);
		organizationFilter = spService.initOrganizationFilter(organizationFilter, serviceProvider);
		if (organizationFilter.getOrganizations().isEmpty()) {
			addActionError(getText("eirc.error.service_provider.no_providerless_organization"));
			return INPUT;
		}

		if (!isSubmit()) {
			provider = serviceProvider;
			initDescriptions();
			return INPUT;
		}

		if (log.isInfoEnabled()) {
			log.info("Service provider descriptions: " + descriptions);
		}

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			ServiceProviderDescription description = new ServiceProviderDescription();
			description.setLang(lang);
			description.setName(value);
			if (log.isInfoEnabled()) {
				log.info("Setting description: " + description);
			}
			serviceProvider.setDescription(description);
		}

		if (log.isInfoEnabled()) {
			log.info("New Service provider descriptions: " + serviceProvider.getDescriptions());
		}

		Organization organization = organizationService.read(new Organization(organizationFilter.getSelectedId()));
		serviceProvider.setOrganization(organization);
		spService.save(serviceProvider);

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

	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

}
