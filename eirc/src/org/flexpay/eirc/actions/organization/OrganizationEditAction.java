package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.OrganizationDescription;
import org.flexpay.eirc.persistence.OrganizationName;
import org.flexpay.eirc.service.OrganizationService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class OrganizationEditAction extends FPActionSupport {

	private transient OrganizationService organizationService;

	private Organization organization = new Organization();
	private Map<Long, String> names = map();
	private Map<Long, String> descriptions = map();

	@NotNull
	public String doExecute() throws Exception {

		if (organization.getId() == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		Organization org = organizationService.read(organization);

		if (!isSubmit()) {
			organization = org;
			initNames();
			initDescriptions();
			return INPUT;
		}

		if (log.isInfoEnabled()) {
			log.info("Organization names: " + names);
			log.info("Organization descriptions: " + descriptions);
		}

		org.setKpp(organization.getKpp());
		org.setIndividualTaxNumber(organization.getIndividualTaxNumber());
		org.setJuridicalAddress(organization.getJuridicalAddress());
		org.setPostalAddress(organization.getPostalAddress());

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			OrganizationName organizationName = new OrganizationName();
			organizationName.setLang(lang);
			organizationName.setName(value);
			org.setName(organizationName);
		}

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			OrganizationDescription organizationDescription = new OrganizationDescription();
			organizationDescription.setLang(lang);
			organizationDescription.setName(value);
			org.setDescription(organizationDescription);
		}

		organizationService.save(org);

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
		for (OrganizationName name : organization.getNames()) {
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
		for (OrganizationDescription description : organization.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (descriptions.containsKey(lang.getId())) {
				continue;
			}
			descriptions.put(lang.getId(), "");
		}
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
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

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
