package org.flexpay.orgs.actions.paymentscollector;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.PaymentsCollectorDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class PaymentsCollectorEditAction extends FPActionSupport {

	private PaymentsCollector collector = new PaymentsCollector();
	private OrganizationFilter organizationFilter = new OrganizationFilter();
	private Map<Long, String> descriptions = map();
	private String email;

	private String crumbCreateKey;
	private PaymentsCollectorService collectorService;
	private OrganizationService organizationService;

	public PaymentsCollectorEditAction() {
		organizationFilter.setAllowEmpty(false);
	}

	@NotNull
	@Override
	public String doExecute() throws Exception {

		PaymentsCollector oldCollector = collector.isNew() ? collector : collectorService.read(stub(collector));
		if (oldCollector == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		collectorService.initInstancelessFilter(organizationFilter, oldCollector);

		if (isNotSubmit()) {
			if (oldCollector.isNotNew()) {
				organizationFilter.setSelectedId(oldCollector.getOrganizationStub().getId());
				email = oldCollector.getEmail();
			}
			collector = oldCollector;
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

		oldCollector.setOrganization(juridicalPerson);

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			oldCollector.setDescription(new PaymentsCollectorDescription(value, lang));
		}

		oldCollector.setEmail(email);

		if (oldCollector.isNew()) {
			collectorService.create(oldCollector);
		} else {
			collectorService.update(oldCollector);
		}

		addActionMessage(getText("orgs.payments_collector.saved"));

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
		if (collector.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	private void initDescriptions() {
		for (PaymentsCollectorDescription description : collector.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (descriptions.containsKey(lang.getId())) {
				continue;
			}
			descriptions.put(lang.getId(), "");
		}
	}

	public PaymentsCollector getCollector() {
		return collector;
	}

	public void setCollector(PaymentsCollector collector) {
		this.collector = collector;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setCollectorService(PaymentsCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}
