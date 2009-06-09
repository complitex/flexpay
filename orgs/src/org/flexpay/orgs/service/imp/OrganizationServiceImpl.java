package org.flexpay.orgs.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.orgs.dao.OrganizationDao;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.OrganizationDescription;
import org.flexpay.orgs.persistence.OrganizationName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class OrganizationServiceImpl implements OrganizationService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private SessionUtils sessionUtils;
	private ModificationListener<Organization> modificationListener;

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

	/**
	 * {@inheritDoc}
	 */
	public List<Organization> listOrganizations(Page<Organization> pager) {
		return organizationDao.findOrganizations(pager);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Organization> listOrganizationsWithCollectors() {
		return organizationDao.findOrganizationsWithCollectors();
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

				modificationListener.onDelete(organization);

				log.info("Disabled: {}", organization);
			}
		}
	}

	@Transactional (readOnly = false)
	@NotNull
	public Organization create(@NotNull Organization organization) throws FlexPayExceptionContainer {
		validate(organization);

		organization.setId(null);
		organizationDao.create(organization);

		modificationListener.onCreate(organization);

		return organization;
	}

	/**
	 * Update organization
	 *
	 * @param organization Organization to save
	 * @return Updated object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	public Organization update(@NotNull Organization organization) throws FlexPayExceptionContainer {
		validate(organization);

		Organization old = readFull(stub(organization));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + organization));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, organization);

		organizationDao.update(organization);
		return organization;
	}

	/**
	 * Delete Organization object
	 *
	 * @param organizationStub organization stub
	 */
    @Transactional (readOnly = false)
	public void delete(@NotNull Stub<Organization> organizationStub) {
		Organization organization = organizationDao.read(organizationStub.getId());

		if (organization == null) {
			log.debug("Can't find organization with id {}", organizationStub.getId());
			return;
		}

		organizationDao.delete(organization);
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

	@Required
	public void setOrganizationDao(OrganizationDao organizationDao) {
		this.organizationDao = organizationDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<Organization> modificationListener) {
		this.modificationListener = modificationListener;
	}
}
