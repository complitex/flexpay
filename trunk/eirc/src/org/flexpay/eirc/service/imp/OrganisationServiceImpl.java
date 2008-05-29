package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.eirc.dao.OrganisationDao;
import org.flexpay.eirc.dao.OrganisationDaoExt;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.OrganisationDescription;
import org.flexpay.eirc.persistence.OrganisationName;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.service.OrganisationService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public class OrganisationServiceImpl implements OrganisationService {

	private Logger log = Logger.getLogger(getClass());

	private OrganisationDao organisationDao;
	private OrganisationDaoExt organisationDaoExt;

	/**
	 * Find organisation by its id
	 *
	 * @param organisationId Organisation id
	 * @return Organisation if found, or <code>null</code> otherwise
	 */
	public Organisation getOrganisation(String organisationId) {
		List<Organisation> organisations = organisationDao.findOrganisationsById(
				organisationId);
		return organisations.isEmpty() ? null : organisations.get(0);
	}

	/**
	 * Initialize organisations filter
	 *
	 * @param organisationFilter Filter to initialize
	 */
	public void initFilter(OrganisationFilter organisationFilter) {
		List<Organisation> organisations = organisationDao.findAllOrganisations();
		organisationFilter.setOrganisations(organisations);

		if (log.isDebugEnabled()) {
			log.debug("Init organisations filter: " + organisations);
		}
	}

	public List<Organisation> listOrganisations(Page<Organisation> pager) {
		return organisationDao.findOrganisations(pager);
	}

	/**
	 * Disable organisations
	 *
	 * @param objectIds Organisations identifiers to disable
	 */
	@Transactional(readOnly = false)
	public void disable(Set<Long> objectIds) {
		for (Long id : objectIds) {
			Organisation organisation = organisationDao.read(id);
			if (organisation != null) {
				organisation.setStatus(ObjectWithStatus.STATUS_DISABLED);
				organisationDao.update(organisation);
			}
		}
	}

	/**
	 * Read full organisation info
	 *
	 * @param stub Organisation stub
	 * @return Organisation
	 */
	public Organisation read(Organisation stub) {
		if (stub.isNotNew()) {
			return organisationDao.readFull(stub.getId());
		}

		return new Organisation(0L);
	}

	/**
	 * Save or update organisation
	 *
	 * @param organisation Organisation to save
	 */
	@Transactional(readOnly = false)
	public void save(Organisation organisation) throws FlexPayExceptionContainer {
		validate(organisation);
		if (organisation.getId() == null || organisation.getId() == 0) {
			organisation.setId(null);
			organisationDao.create(organisation);
		} else {
			organisationDao.update(organisation);
		}
	}

	public void validate(Organisation organisation) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();
		if (StringUtils.isBlank(organisation.getUniqueId())) {
			container.addException(new FlexPayException("No unique id", "eirc.error.organisation.no_unique_id"));
		} else {
			Organisation orgById = getOrganisationStub(organisation.getUniqueId());
			// found organisation but it is not validated one
			if (orgById != null && !orgById.getId().equals(organisation.getId())) {
				container.addException(new FlexPayException(
						"Unique id already accupied", "eirc.error.organisation.unique_id_occupied"));
			}
			// no organisation found, if this is not a new object this means we are going to change unique id,
			// add exception this way
			else if (orgById == null && organisation.getId() != null && organisation.getId() > 0) {
				container.addException(new FlexPayException(
						"Unique id changed forbidden", "eirc.error.organisation.unique_id_change_forbidden"));
			}
		}

		boolean defaultNameFound = false;
		for (OrganisationName name : organisation.getNames()) {
			if (name.getLang().isDefault() && StringUtils.isNotBlank(name.getName())) {
				defaultNameFound = true;
			}
		}
		if (!defaultNameFound) {
			container.addException(new FlexPayException(
					"No default lang name", "eirc.error.organisation.no_default_lang_name"));
		}

		boolean defaultDescFound = false;
		for (OrganisationDescription description : organisation.getDescriptions()) {
			if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
				defaultDescFound = true;
			}
		}
		if (!defaultDescFound) {
			container.addException(new FlexPayException(
					"No default lang desc", "eirc.error.organisation.no_default_lang_description"));
		}

		if (!container.isEmpty()) {
			throw container;
		}
	}

	private Organisation getOrganisationStub(String uniqueId) {
		return organisationDaoExt.getOrganisationStub(uniqueId);
	}

	/**
	 * Setter for property 'organisationDao'.
	 *
	 * @param organisationDao Value to set for property 'organisationDao'.
	 */
	public void setOrganisationDao(OrganisationDao organisationDao) {
		this.organisationDao = organisationDao;
	}

	public void setOrganisationDaoExt(OrganisationDaoExt organisationDaoExt) {
		this.organisationDaoExt = organisationDaoExt;
	}
}
