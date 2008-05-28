package org.flexpay.eirc.actions.organisation;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.OrganisationDescription;
import org.flexpay.eirc.persistence.OrganisationName;
import org.flexpay.eirc.service.OrganisationService;

import java.util.HashMap;
import java.util.Map;

public class EditOrganisationAction extends FPActionSupport {

	private OrganisationService organisationService;

	private Organisation organisation = new Organisation();
	private Map<Long, String> names = new HashMap<Long, String>();
	private Map<Long, String> descriptions = new HashMap<Long, String>();

	public String execute() throws Exception {

		if (organisation.getId() == null) {
			// todo: notify that no object was selected
			addActionError("No object was selected");
			return SUCCESS;
		}

		Organisation org = organisationService.read(organisation);

		if (!isPost()) {
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
		org.setUniqueId(organisation.getUniqueId());

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
			org.addDescription(organisationDescription);
		}

		try {
			organisationService.save(org);
		} catch (FlexPayExceptionContainer container) {
			addActionErrors(container);
			return INPUT;
		}

		return SUCCESS;
	}

	private void initNames() {
		for (OrganisationName name : organisation.getNames()) {
			names.put(name.getLang().getId(), name.getName());
		}

		for (Language lang : ApplicationConfig.getInstance().getLanguages()) {
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

		for (Language lang : ApplicationConfig.getInstance().getLanguages()) {
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
