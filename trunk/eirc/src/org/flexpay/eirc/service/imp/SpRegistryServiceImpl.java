package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.dao.SpRegistryDao;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpRegistryService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SpRegistryServiceImpl implements SpRegistryService {
	private static Logger log = Logger.getLogger(SpRegistryServiceImpl.class);

	private SpRegistryDao spRegistryDao;

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
	 * @param spRegistryDao
	 *            the spRegistryDao to set
	 */
	public void setSpRegistryDao(SpRegistryDao spRegistryDao) {
		this.spRegistryDao = spRegistryDao;
	}

}
