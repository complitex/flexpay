package org.flexpay.eirc.service.imp;

import java.util.List;
import java.util.Date;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.dao.SpRegistryDao;
import org.flexpay.eirc.dao.SpRegistryDaoExt;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.flexpay.eirc.service.SpRegistryService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SpRegistryServiceImpl implements SpRegistryService {
	private static Logger log = Logger.getLogger(SpRegistryServiceImpl.class);

	private SpRegistryDao spRegistryDao;
	private SpRegistryDaoExt spRegistryDaoExt;

	/**
	 * Create SpRegistry
	 * 
	 * @param spRegistry
	 *            SpRegistry object
	 * @return created SpRegistry object
	 */
	@Transactional(readOnly = false)
	public SpRegistry create(SpRegistry spRegistry) throws FlexPayException {
		spRegistryDao.create(spRegistry);

		if (log.isDebugEnabled()) {
			log.debug("Created SpRegistry: " + spRegistry);
		}

		return spRegistry;
	}

	/**
	 * Get all SpRegistry by SpFile in page mode
	 * 
	 * @param pager
	 *            Page object
	 * @return List of SpRegistry objects for pager
	 */
	@Transactional(readOnly = false)
	public List<SpRegistry> findObjects(Page<SpRegistry> pager, Long spFileId) {
		return spRegistryDao.findObjects(pager, spFileId);
	}

	/**
	 * Read SpRegistry object by its unique id
	 * 
	 * @param id
	 *            SpRegistry key
	 * @return SpRegistry object, or <code>null</code> if object not found
	 */
	public SpRegistry read(Long id) {
		return spRegistryDao.read(id);
	}

	/**
	 * Update SpRegistry
	 * 
	 * @param spRegistry
	 *            SpRegistry to update for
	 * @return Updated SpRegistry object
	 * @throws FlexPayException
	 *             if SpRegistry object is invalid
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
	 * @param spRegistryDao
	 *            the spRegistryDao to set
	 */
	public void setSpRegistryDao(SpRegistryDao spRegistryDao) {
		this.spRegistryDao = spRegistryDao;
	}

	public void setSpRegistryDaoExt(SpRegistryDaoExt spRegistryDaoExt) {
		this.spRegistryDaoExt = spRegistryDaoExt;
	}
}
