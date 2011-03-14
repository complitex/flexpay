package org.flexpay.orgs.action.subdivision;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.Subdivision;
import org.flexpay.orgs.persistence.SubdivisionDescription;
import org.flexpay.orgs.persistence.SubdivisionName;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.SubdivisionFilter;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class SubdivisionEditAction extends FPActionSupport {

	private Subdivision subdivision = new Subdivision();
	private Organization organization = new Organization();
	private OrganizationFilter organizationFilter = new OrganizationFilter();
	private SubdivisionFilter subdivisionFilter = new SubdivisionFilter();
	private Map<Long, String> descriptions = map();
	private Map<Long, String> names = map();

	private String crumbCreateKey;
	private SubdivisionService subdivisionService;
	private OrganizationService organizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		Subdivision oldSubdivision = subdivision.isNew() ? new Subdivision(0L) :
									 subdivisionService.read(stub(subdivision));
		if (oldSubdivision == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		organizationService.initFilter(organizationFilter);
		subdivisionService.initFilter(subdivisionFilter, stub(organization));

		if (isNotSubmit()) {
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
									   new Organization(organizationFilter.getSelectedStub()) : null;
		oldSubdivision.setJuridicalPerson(juridicalPerson);

		Subdivision parent = subdivisionFilter.needFilter() ?
							 new Subdivision(subdivisionFilter.getSelectedStub()) : null;
		oldSubdivision.setParentSubdivision(parent);

		oldSubdivision.setHeadOrganization(organization);
		oldSubdivision.setRealAddress(subdivision.getRealAddress());

		for (Map.Entry<Long, String> entry : descriptions.entrySet()) {
			String value = entry.getValue();
			Language lang = getLang(entry.getKey());
			oldSubdivision.setDescription(new SubdivisionDescription(value, lang));
		}
		for (Map.Entry<Long, String> entry : names.entrySet()) {
			String value = entry.getValue();
			Language lang = getLang(entry.getKey());
			oldSubdivision.setName(new SubdivisionName(value, lang));
		}

		if (oldSubdivision.isNew()) {
			subdivisionService.create(oldSubdivision);
		} else {
			subdivisionService.update(oldSubdivision);
		}

		subdivision = oldSubdivision;

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
		if (subdivision.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
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

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}

}
