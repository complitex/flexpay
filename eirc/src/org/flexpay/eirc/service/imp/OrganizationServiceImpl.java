package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.OrganizationDao;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.OrganizationDescription;
import org.flexpay.eirc.persistence.OrganizationName;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class OrganizationServiceImpl implements OrganizationService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private OrganizationDao organizationDao;

	/**
	 * Find organization by its id
	 *
	 * @param stub Organization stub
	 * @return Organization if found, or <code>null</code> otherwise
	 */
	public Organization readFull(Stub<Organization> stub) {
		return organizationDao.readFull(stub.getId());
	}

	/**
	 * Initialize organizations filter
	 *
	 * @param organizationFilter Filter to initialize
	 */
	public void initFilter(OrganizationFilter organizationFilter) {
		List<Organization> organizations = organizationDao.findAllOrganizations();
		organizationFilter.setOrganizations(organizations);

		log.debug("Init organizations filter: {}", organizations);
	}

	public List<Organization> listOrganizations(Page<Organization> pager) {
		return organizationDao.findOrganizations(pager);
	}

	/**
	 * Disable organizations
	 *
	 * @param objectIds Organizations identifiers to disable
	 */
	@Transactional (readOnly = false)
	public void disable(Set<Long> objectIds) {
		for (Long id : objectIds) {
			Organization organization = organizationDao.read(id);
			if (organization != null) {
				organization.disable();
				organizationDao.update(organization);
			}
		}
	}

	/**
	 * Save or update organization
	 *
	 * @param organization Organization to save
	 */
	@Transactional (readOnly = false)
	public void save(Organization organization) throws FlexPayExceptionContainer {
		validate(organization);
		if (organization.isNew()) {
			organization.setId(null);
			organizationDao.create(organization);
		} else {
			organizationDao.update(organization);
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	public void validate(Organization organization) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultNameFound = false;
		for (OrganizationName name : organization.getNames()) {
			if (name.getLang().isDefault() && StringUtils.isNotBlank(name.getName())) {
				defaultNameFound = true;
			}
		}
		if (!defaultNameFound) {
			container.addException(new FlexPayException(
					"No default lang name", "eirc.error.organization.no_default_lang_name"));
		}

		boolean defaultDescFound = false;
		for (OrganizationDescription description : organization.getDescriptions()) {
			if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
				defaultDescFound = true;
			}
		}
		if (!defaultDescFound) {
			container.addException(new FlexPayException(
					"No default lang desc", "eirc.error.organization.no_default_lang_description"));
		}

		if (StringUtils.isBlank(organization.getJuridicalAddress())) {
			container.addException(new FlexPayException(
					"No juridical address", "eirc.error.organization.no_juridical_address"));
		}

		if (StringUtils.isBlank(organization.getPostalAddress())) {
			container.addException(new FlexPayException(
					"No postal address", "eirc.error.organization.no_postal_address"));
		}

		if (!container.isEmpty()) {
			throw container;
		}
	}

	/**
	 * Setter for property 'organizationDao'.
	 *
	 * @param organizationDao Value to set for property 'organizationDao'.
	 */
	public void setOrganizationDao(OrganizationDao organizationDao) {
		this.organizationDao = organizationDao;
	}
}
