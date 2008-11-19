package org.flexpay.eirc.actions.organisation;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.OrganisationDescription;
import org.flexpay.eirc.persistence.OrganisationName;
import org.flexpay.eirc.service.OrganisationService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class OrganisationEditAction extends FPActionSupport {

	private transient OrganisationService organisationService;

	private Organisation organisation = new Organisation();
	private Map<Long, String> names = map();
	private Map<Long, String> descriptions = map();

	@NotNull
	public String doExecute() throws Exception {

		if (organisation.getId() == null) {
			// todo: notify that no object was selected
			addActionError("No object was selected");
			return REDIRECT_SUCCESS;
		}

		Organisation org = organisationService.read(organisation);

		if (!isSubmit()) {
			organisation = org;
			initNames();
			initDescriptions();
			return INPUT;
		}

		if (log.isInfoEnabled()) {
			log.info("Organisation names: " + names);
			log.info("Organisation descriptions: " + descriptions);
		}

		org.setKpp(organisation.getKpp());
		org.setIndividualTaxNumber(organisation.getIndividualTaxNumber());
		org.setJuridicalAddress(organisation.getJuridicalAddress());
		org.setPostalAddress(organisation.getPostalAddress());

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			OrganisationName organisationName = new OrganisationName();
			organisationName.setLang(lang);
			organisationName.setName(value);
			org.setName(organisationName);
		}

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			OrganisationDescription organisationDescription = new OrganisationDescription();
			organisationDescription.setLang(lang);
			organisationDescription.setName(value);
			org.setDescription(organisationDescription);
		}

		organisationService.save(org);

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

	private void initNames() {
		for (OrganisationName name : organisation.getNames()) {
			names.put(name.getLang().getId(), name.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
		}
	}

	private void initDescriptions() {
		for (OrganisationDescription description : organisation.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (descriptions.containsKey(lang.getId())) {
				continue;
			}
			descriptions.put(lang.getId(), "");
		}
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public Map<Long, String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Map<Long, String> descriptions) {
		this.descriptions = descriptions;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}
}
