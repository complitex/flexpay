package org.flexpay.orgs.action.serviceorganization;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganizationDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.OrgsObjectsFactory;
import org.flexpay.orgs.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;

public class ServiceOrganizationEditAction extends FPActionSupport {

	private OrganizationFilter organizationFilter = new OrganizationFilter();
	private ServiceOrganization serviceOrganization = ServiceOrganization.newInstance();
	private Map<Long, String> descriptions = map();

	private String crumbCreateKey;
	private ServiceOrganizationService serviceOrganizationService;
	private OrganizationService organizationService;
	private OrgsObjectsFactory objectsFactory;

	public ServiceOrganizationEditAction() {
		organizationFilter.setAllowEmpty(false);
	}

	@NotNull
	@Override
	public String doExecute() throws Exception {

		serviceOrganization = serviceOrganization.isNew()
								  ? objectsFactory.newServiceOrganization()
								  : serviceOrganizationService.read(stub(serviceOrganization));
		if (serviceOrganization == null) {
			addActionError(getText("common.error.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		serviceOrganizationService.initInstancelessFilter(organizationFilter, serviceOrganization);

		if (isNotSubmit()) {
			if (serviceOrganization.isNotNew()) {
				organizationFilter.setSelectedId(serviceOrganization.getOrganizationStub().getId());
			}
			initDescriptions();
			return INPUT;
		}

		if (!organizationFilter.needFilter()) {
			addActionError(getText("orgs.error.orginstance.no_organization_selected"));
			return INPUT;
		}
		Organization juridicalPerson = organizationService.readFull(organizationFilter.getSelectedStub());
		if (juridicalPerson == null) {
			addActionError(getText("orgs.error.orginstance.no_organization"));
			return INPUT;
		}

		serviceOrganization.setOrganization(juridicalPerson);

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			serviceOrganization.setDescription(new ServiceOrganizationDescription(value, lang));
		}

		if (serviceOrganization.isNew()) {
			serviceOrganizationService.create(serviceOrganization);
		} else {
			serviceOrganizationService.update(serviceOrganization);
		}

		addActionMessage(getText("orgs.service_organization.saved"));

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
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	@Override
	protected void setBreadCrumbs() {
		if (serviceOrganization.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
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

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setObjectsFactory(OrgsObjectsFactory objectsFactory) {
		this.objectsFactory = objectsFactory;
	}
}
