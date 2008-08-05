package org.flexpay.eirc.actions.organisation;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.Bank;
import org.flexpay.eirc.persistence.BankDescription;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.service.BankService;
import org.flexpay.eirc.service.OrganisationService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EditBankAction extends FPActionSupport {

	private BankService bankService;
	private OrganisationService organisationService;

	private Bank bank = new Bank();
	private OrganisationFilter organisationFilter = new OrganisationFilter();
	private Map<Long, String> descriptions = map();

	@NotNull
	public String doExecute() throws Exception {

		if (bank.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		Bank oldBank = bankService.read(bank);
		if (oldBank == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		bankService.initBanklessFilter(organisationFilter, oldBank);

		// prepare initial setup
		if (!isSubmit()) {
			if (oldBank.isNotNew()) {
				organisationFilter.setSelectedId(oldBank.getOrganisation().getId());
			}
			bank = oldBank;
			initDescriptions();
			return INPUT;
		}

		if (!organisationFilter.needFilter()) {
			addActionError(getText("eirc.error.bank.no_organisation_selected"));
			return INPUT;
		}

		if (log.isDebugEnabled()) {
			log.debug("Bank descriptions: " + descriptions);
		}

		Organisation juridicalPerson = organisationService.read(
				new Organisation(organisationFilter.getSelectedId()));
		oldBank.setOrganisation(juridicalPerson);
		oldBank.setBankIdentifierCode(bank.getBankIdentifierCode());
		oldBank.setCorrespondingAccount(bank.getCorrespondingAccount());

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			BankDescription description = new BankDescription();
			description.setLang(lang);
			description.setName(value);
			oldBank.setDescription(description);
		}

		bankService.save(oldBank);

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

	public OrganisationFilter getOrganisationFilter() {
		return organisationFilter;
	}

	public void setOrganisationFilter(OrganisationFilter organisationFilter) {
		this.organisationFilter = organisationFilter;
	}

	public void setBankService(BankService bankService) {
		this.bankService = bankService;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}
}
