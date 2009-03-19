package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.ServiceOrganization;
import org.flexpay.eirc.persistence.ServiceOrganizationDescription;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.service.OrganizationService;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class ServiceOrganizationEditAction extends FPActionSupport {

	private OrganizationFilter organizationFilter = new OrganizationFilter();
	private ServiceOrganization serviceOrganization = new ServiceOrganization();
	private Map<Long, String> descriptions = map();

	private ServiceOrganizationService serviceOrganizationService;
	private OrganizationService organizationService;

	@NotNull
	public String doExecute() throws Exception {

		if (serviceOrganization.getId() == null) {
            addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		ServiceOrganization oldServiceOrganization = serviceOrganizationService.read(serviceOrganization);
        if (oldServiceOrganization == null) {
            addActionError(getText("error.invalid_id"));
            return REDIRECT_SUCCESS;
        }

        serviceOrganizationService.initServiceOrganizationlessFilter(organizationFilter, oldServiceOrganization);

        // prepare initial setup
        if (!isSubmit()) {
            if (oldServiceOrganization.isNotNew()) {
                organizationFilter.setSelectedId(oldServiceOrganization.getOrganization().getId());
            }
            serviceOrganization = oldServiceOrganization;
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

		log.debug("Service organization descriptions: {}", descriptions);

		oldServiceOrganization.setOrganization(juridicalPerson);

        for (Map.Entry<Long, String> name : descriptions.entrySet()) {
            String value = name.getValue();
            Language lang = getLang(name.getKey());
            ServiceOrganizationDescription description = new ServiceOrganizationDescription();
            description.setLang(lang);
            description.setName(value);
            oldServiceOrganization.setDescription(description);
        }

        serviceOrganizationService.save(oldServiceOrganization);

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
		for (ServiceOrganizationDescription description : serviceOrganization.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (descriptions.containsKey(lang.getId())) {
				continue;
			}
			descriptions.put(lang.getId(), "");
		}
	}

    public ServiceOrganization getServiceOrganization() {
        return serviceOrganization;
    }

    public void setServiceOrganization(ServiceOrganization serviceOrganization) {
        this.serviceOrganization = serviceOrganization;
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
    public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
        this.serviceOrganizationService = serviceOrganizationService;
    }

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

}
