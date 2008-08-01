package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.dao.SpRegistryDao;
import org.flexpay.eirc.dao.SpRegistryDaoExt;
import org.flexpay.eirc.dao.OrganisationDao;
import org.flexpay.eirc.dao.RegistryContainerDao;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.RegistryContainer;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.SpRegistryService;
import org.springframework.transaction.annotation.Transactional;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public class SpRegistryServiceImpl implements SpRegistryService {
	private Logger log = Logger.getLogger(getClass());

	private SpRegistryDao spRegistryDao;
	private SpRegistryDaoExt spRegistryDaoExt;
	private RegistryContainerDao registryContainerDao;
	private OrganisationDao organisationDao;

	private SpRegistryRecordService spRegistryRecordService;

	/**
	 * Create SpRegistry
	 *
	 * @param registry SpRegistry object
	 * @return created SpRegistry object
	 */
	@Transactional(readOnly = false)
	public SpRegistry create(SpRegistry registry) throws FlexPayException {
		registry.setRecipient(organisationDao.read(registry.getRecipient().getId()));
		registry.setSender(organisationDao.read(registry.getSender().getId()));
		spRegistryDao.create(registry);

		for (RegistryContainer container : registry.getContainers()) {
			registryContainerDao.create(container);
		}

		if (log.isDebugEnabled()) {
			log.debug("Created SpRegistry: " + registry);
		}

		return registry;
	}

	/**
	 * Get all SpRegistry by SpFile in page mode
	 *
	 * @param pager Page object
	 * @return List of SpRegistry objects for pager
	 */
	@Transactional(readOnly = false)
	public List<SpRegistry> findObjects(Page<SpRegistry> pager, Long spFileId) {
		return spRegistryDao.findObjects(pager, spFileId);
	}

	/**
	 * Read SpRegistry object by its unique id
	 *
	 * @param id SpRegistry key
	 * @return SpRegistry object, or <code>null</code> if object not found
	 */
	public SpRegistry read(Long id) {
		SpRegistry registry = spRegistryDao.readFull(id);
		if (registry == null) {
			if (log.isDebugEnabled()) {
				log.debug("Registry #" + id + " not found");
			}
			return null;
		}
		registry.setErrorsNumber(spRegistryRecordService.getErrorsNumber(registry));

		return registry;
	}

	/**
	 * Update SpRegistry
	 *
	 * @param spRegistry SpRegistry to update for
	 * @return Updated SpRegistry object
	 * @throws FlexPayException if SpRegistry object is invalid
	 */
	@Transactional(readOnly = false)
	public SpRegistry update(SpRegistry spRegistry) throws FlexPayException {
		spRegistryDao.update(spRegistry);

		return spRegistry;
	}

	@Transactional(readOnly = false)
	public void delete(SpRegistry spRegistry) {
		spRegistryDao.delete(spRegistry);
	}

	/**
	 * Find registries
	 *
	 * @param senderFilter	sender organisation filter
	 * @param recipientFilter recipient organisation filter
	 * @param typeFilter	  registry type filter
	 * @param fromDate		registry generation start date
	 * @param tillDate		registry generation end date
	 * @param pager		   Page
	 * @return list of registries matching specified criteria
	 */
	public List<SpRegistry> findObjects(OrganisationFilter senderFilter, OrganisationFilter recipientFilter,
										RegistryTypeFilter typeFilter, Date fromDate, Date tillDate, Page pager) {
		return spRegistryDaoExt.findRegistries(senderFilter, recipientFilter,
				typeFilter, fromDate, tillDate, pager);
	}

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	public Collection<SpRegistry> findObjects(@NotNull Set<Long> objectIds) {
		return spRegistryDaoExt.findRegistries(objectIds);
	}

	/**
	 * @param spRegistryDao the spRegistryDao to set
	 */
	public void setSpRegistryDao(SpRegistryDao spRegistryDao) {
		this.spRegistryDao = spRegistryDao;
	}

	public void setSpRegistryDaoExt(SpRegistryDaoExt spRegistryDaoExt) {
		this.spRegistryDaoExt = spRegistryDaoExt;
	}

	public void setSpRegistryRecordService(SpRegistryRecordService spRegistryRecordService) {
		this.spRegistryRecordService = spRegistryRecordService;
	}

	public void setOrganisationDao(OrganisationDao organisationDao) {
		this.organisationDao = organisationDao;
	}

	public void setRegistryContainerDao(RegistryContainerDao registryContainerDao) {
		this.registryContainerDao = registryContainerDao;
	}
}
