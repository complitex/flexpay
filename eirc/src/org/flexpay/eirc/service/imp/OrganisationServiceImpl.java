package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.OrganisationDao;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.OrganisationDescription;
import org.flexpay.eirc.persistence.OrganisationName;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.service.OrganisationService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class OrganisationServiceImpl implements OrganisationService {

	private Logger log = Logger.getLogger(getClass());

	private OrganisationDao organisationDao;

	/**
	 * Find organisation by its id
	 *
	 * @param stub Organisation stub
	 * @return Organisation if found, or <code>null</code> otherwise
	 */
	public Organisation getOrganisation(Stub<Organisation> stub) {
		return organisationDao.read(stub.getId());
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
	@Transactional (readOnly = false)
	public void disable(Set<Long> objectIds) {
		for (Long id : objectIds) {
			Organisation organisation = organisationDao.read(id);
			if (organisation != null) {
				organisation.disable();
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
			return organisationDao.readFull(new Stub<Organisation>(stub).getId());
		}

		return new Organisation(0L);
	}

	/**
	 * Save or update organisation
	 *
	 * @param organisation Organisation to save
	 */
	@Transactional (readOnly = false)
	public void save(Organisation organisation) throws FlexPayExceptionContainer {
		validate(organisation);
		if (organisation.isNew()) {
			organisation.setId(null);
			organisationDao.create(organisation);
		} else {
			organisationDao.update(organisation);
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	public void validate(Organisation organisation) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

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

		if (StringUtils.isBlank(organisation.getJuridicalAddress())) {
			container.addException(new FlexPayException(
					"No juridical address", "eirc.error.organisation.no_juridical_address"));
		}

		if (StringUtils.isBlank(organisation.getPostalAddress())) {
			container.addException(new FlexPayException(
					"No postal address", "eirc.error.organisation.no_postal_address"));
		}

		if (!container.isEmpty()) {
			throw container;
		}
	}

	/**
	 * Setter for property 'organisationDao'.
	 *
	 * @param organisationDao Value to set for property 'organisationDao'.
	 */
	public void setOrganisationDao(OrganisationDao organisationDao) {
		this.organisationDao = organisationDao;
	}
}
