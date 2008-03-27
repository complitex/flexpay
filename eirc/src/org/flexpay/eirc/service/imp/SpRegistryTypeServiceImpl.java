package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.eirc.dao.SpRegistryTypeDao;
import org.flexpay.eirc.persistence.SpRegistryType;
import org.flexpay.eirc.service.SpRegistryTypeService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SpRegistryTypeServiceImpl implements SpRegistryTypeService {
	private static Logger log = Logger.getLogger(SpRegistryServiceImpl.class);

	private SpRegistryTypeDao spRegistryTypeDao;

	/**
	 * Read SpRegistryType object by its unique id
	 * 
	 * @param id
	 *            SpRegistryType key
	 * @return SpRegistryType object, or <code>null</code> if object not found
	 */
	public SpRegistryType read(Long id) {
		return spRegistryTypeDao.read(id);
	}

	/**
	 * @param spRegistryTypeDao
	 *            the spRegistryTypeDao to set
	 */
	public void setSpRegistryTypeDao(SpRegistryTypeDao spRegistryTypeDao) {
		this.spRegistryTypeDao = spRegistryTypeDao;
	}
}
