package org.flexpay.orgs.action.bank;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Bank;
import org.flexpay.orgs.persistence.BankDescription;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.service.BankService;
import org.flexpay.orgs.service.OrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;

public class BankEditAction extends FPActionSupport {

	private OrganizationFilter organizationFilter = new OrganizationFilter();

	private Bank bank = new Bank();
	private Map<Long, String> descriptions = map();

	private String crumbCreateKey;
	private BankService bankService;
	private OrganizationService organizationService;

	public BankEditAction() {
		organizationFilter.setAllowEmpty(false);
	}

	@NotNull
	@Override
	public String doExecute() throws Exception {

		Bank oldBank = bank.isNotNew() ? bankService.read(stub(bank)) : bank;
		if (oldBank == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		bankService.initInstancelessFilter(organizationFilter, oldBank);

		if (isNotSubmit()) {
			if (oldBank.isNotNew()) {
				organizationFilter.setSelectedId(oldBank.getOrganizationStub().getId());
			}
			bank = oldBank;
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

		oldBank.setOrganization(juridicalPerson);
		oldBank.setBankIdentifierCode(bank.getBankIdentifierCode());
		oldBank.setCorrespondingAccount(bank.getCorrespondingAccount());

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			oldBank.setDescription(new BankDescription(value, lang));
		}

		if (oldBank.isNew()) {
			bankService.create(oldBank);
		} else {
			bankService.update(oldBank);
		}

		addActionMessage(getText("orgs.bank.saved"));

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
		if (bank.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	private void initDescriptions() {
		for (BankDescription description : bank.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (descriptions.containsKey(lang.getId())) {
				continue;
			}
			descriptions.put(lang.getId(), "");
		}
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
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
	public void setBankService(BankService bankService) {
		this.bankService = bankService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

}
