package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.PaymentsCollector;
import org.flexpay.eirc.persistence.PaymentsCollectorDescription;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.service.OrganizationService;
import org.flexpay.eirc.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class PaymentsCollectorEditAction extends FPActionSupport {

	private PaymentsCollectorService instanceService;
	private OrganizationService organizationService;

	private PaymentsCollector instance = new PaymentsCollector();
	private OrganizationFilter organizationFilter = new OrganizationFilter();
	private Map<Long, String> descriptions = map();

	@NotNull
	public String doExecute() throws Exception {

		if (instance.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		PaymentsCollector oldInstance = instance.isNotNew() ? instanceService.read(stub(instance)) : instance;
		if (oldInstance == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		instanceService.initInstancelessFilter(organizationFilter, oldInstance);

		// prepare initial setup
		if (!isSubmit()) {
			if (oldInstance.isNotNew()) {
				organizationFilter.setSelectedId(oldInstance.getOrganization().getId());
			}
			instance = oldInstance;
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

		log.debug("Descriptions: {}", descriptions);

		oldInstance.setOrganization(juridicalPerson);

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			PaymentsCollectorDescription description = new PaymentsCollectorDescription();
			description.setLang(lang);
			description.setName(value);
			oldInstance.setDescription(description);
		}

		if (oldInstance.isNew()) {
			instanceService.create(oldInstance);
		} else {
			instanceService.update(oldInstance);
		}

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
		for (PaymentsCollectorDescription description : instance.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (descriptions.containsKey(lang.getId())) {
				continue;
			}
			descriptions.put(lang.getId(), "");
		}
	}

	public PaymentsCollector getInstance() {
		return instance;
	}

	public void setInstance(PaymentsCollector instance) {
		this.instance = instance;
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
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setInstanceService(PaymentsCollectorService instanceService) {
		this.instanceService = instanceService;
	}
}