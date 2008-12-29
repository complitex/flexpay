package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.Subdivision;
import org.flexpay.eirc.persistence.SubdivisionDescription;
import org.flexpay.eirc.persistence.SubdivisionName;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.persistence.filters.SubdivisionFilter;
import org.flexpay.eirc.service.OrganizationService;
import org.flexpay.eirc.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SubdivisionEditAction extends FPActionSupport {

	private SubdivisionService subdivisionService;
	private OrganizationService organizationService;

	private Subdivision subdivision = new Subdivision();
	private Organization headOrganization = new Organization();
	private OrganizationFilter organizationFilter = new OrganizationFilter();
	private SubdivisionFilter subdivisionFilter = new SubdivisionFilter();
	private Map<Long, String> descriptions = map();
	private Map<Long, String> names = map();

	@NotNull
	public String doExecute() throws Exception {

		if (subdivision.getId() == null || headOrganization.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		Subdivision oldSubdivision = subdivisionService.read(subdivision);
		if (oldSubdivision == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		organizationService.initFilter(organizationFilter);
		organizationFilter.setAllowEmpty(true);
		subdivisionService.initFilter(subdivisionFilter, stub(headOrganization));
		subdivisionFilter.setAllowEmpty(true);

		// prepare initial setup
		if (!isSubmit()) {
			if (oldSubdivision.isNotNew()) {
				Organization juridicalPerson = oldSubdivision.getJuridicalPerson();
				if (juridicalPerson != null) {
					organizationFilter.setSelectedId(juridicalPerson.getId());
				}
				Subdivision parent = oldSubdivision.getParentSubdivision();
				if (parent != null) {
					subdivisionFilter.setSelectedId(parent.getId());
				}
			}
			subdivision = oldSubdivision;
			initNames();
			initDescriptions();
			return INPUT;
		}

		Organization juridicalPerson = organizationFilter.needFilter() ?
									   new Organization(organizationFilter.getSelectedId()) : null;
		oldSubdivision.setJuridicalPerson(juridicalPerson);

		Subdivision parent = subdivisionFilter.needFilter() ?
							 new Subdivision(subdivisionFilter.getSelectedId()) : null;
		oldSubdivision.setParentSubdivision(parent);

		oldSubdivision.setHeadOrganization(headOrganization);
		oldSubdivision.setRealAddress(subdivision.getRealAddress());

		for (Map.Entry<Long, String> entry : descriptions.entrySet()) {
			String value = entry.getValue();
			Language lang = getLang(entry.getKey());
			SubdivisionDescription description = new SubdivisionDescription();
			description.setLang(lang);
			description.setName(value);
			oldSubdivision.setDescription(description);
		}
		for (Map.Entry<Long, String> entry : names.entrySet()) {
			String value = entry.getValue();
			Language lang = getLang(entry.getKey());
			SubdivisionName name = new SubdivisionName();
			name.setLang(lang);
			name.setName(value);
			oldSubdivision.setName(name);
		}

		subdivisionService.save(oldSubdivision);

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
		for (SubdivisionName description : subdivision.getNames()) {
			names.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
		}
	}

	private void initDescriptions() {
		for (SubdivisionDescription description : subdivision.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (descriptions.containsKey(lang.getId())) {
				continue;
			}
			descriptions.put(lang.getId(), "");
		}
	}

	public Subdivision getSubdivision() {
		return subdivision;
	}

	public void setSubdivision(Subdivision subdivision) {
		this.subdivision = subdivision;
	}

	public Organization getHeadOrganization() {
		return headOrganization;
	}

	public void setHeadOrganization(Organization headOrganization) {
		this.headOrganization = headOrganization;
	}

	public OrganizationFilter getOrganizationFilter() {
		return organizationFilter;
	}

	public void setOrganizationFilter(OrganizationFilter organizationFilter) {
		this.organizationFilter = organizationFilter;
	}

	public SubdivisionFilter getSubdivisionFilter() {
		return subdivisionFilter;
	}

	public void setSubdivisionFilter(SubdivisionFilter subdivisionFilter) {
		this.subdivisionFilter = subdivisionFilter;
	}

	public Map<Long, String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Map<Long, String> descriptions) {
		this.descriptions = descriptions;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}
}