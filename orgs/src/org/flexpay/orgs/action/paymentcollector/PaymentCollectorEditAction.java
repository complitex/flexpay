package org.flexpay.orgs.action.paymentcollector;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;

public class PaymentCollectorEditAction extends FPActionSupport {

	private PaymentCollector collector = new PaymentCollector();
	private OrganizationFilter organizationFilter = new OrganizationFilter();
	private Map<Long, String> descriptions = map();
	private String email;

	private String crumbCreateKey;
	private PaymentCollectorService collectorService;
	private OrganizationService organizationService;

	public PaymentCollectorEditAction() {
		organizationFilter.setAllowEmpty(false);
	}

	@NotNull
	@Override
	public String doExecute() throws Exception {

		PaymentCollector oldCollector = collector.isNew() ? collector : collectorService.read(stub(collector));
		if (oldCollector == null) {
			addActionError(getText("common.error.object_not_selected"));
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
			addActionError(getText("orgs.error.orginstance.no_organization_selected"));
			return INPUT;
		}
		Organization juridicalPerson = organizationService.readFull(organizationFilter.getSelectedStub());
		if (juridicalPerson == null) {
			addActionError(getText("orgs.error.orginstance.no_organization"));
			return INPUT;
		}

		oldCollector.setOrganization(juridicalPerson);

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			oldCollector.setDescription(new PaymentCollectorDescription(value, lang));
		}

		oldCollector.setEmail(email);

		if (oldCollector.isNew()) {
			collectorService.create(oldCollector);
		} else {
			collectorService.update(oldCollector);
		}

		addActionMessage(getText("orgs.payment_collector.saved"));

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
		for (PaymentCollectorDescription description : collector.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (descriptions.containsKey(lang.getId())) {
				continue;
			}
			descriptions.put(lang.getId(), "");
		}
	}

	public PaymentCollector getCollector() {
		return collector;
	}

	public void setCollector(PaymentCollector collector) {
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
	public void setCollectorService(PaymentCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}
