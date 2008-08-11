package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.eirc.dao.SpRegistryTypeDao;
import org.flexpay.eirc.persistence.RegistryType;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.flexpay.eirc.service.SpRegistryTypeService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class RegistryTypeServiceImpl implements SpRegistryTypeService {

	private Logger log = Logger.getLogger(getClass());

	private SpRegistryTypeDao spRegistryTypeDao;

	/**
	 * Read SpRegistryType object by its unique id
	 *
	 * @param id SpRegistryType key
	 * @return SpRegistryType object, or <code>null</code> if object not found
	 */
	public RegistryType read(Long id) {
		return spRegistryTypeDao.read(id);
	}

	/**
	 * init filter
	 *
	 * @param registryTypeFilter filter to init
	 */
	public void initFilter(RegistryTypeFilter registryTypeFilter) {
		List<RegistryType> types = spRegistryTypeDao.findAll();
		registryTypeFilter.setRegistryTypes(types);

		if (log.isDebugEnabled()) {
			log.debug("Registry types: " + types);
		}
	}

	/**
	 * @param spRegistryTypeDao the spRegistryTypeDao to set
	 */
	public void setSpRegistryTypeDao(SpRegistryTypeDao spRegistryTypeDao) {
		this.spRegistryTypeDao = spRegistryTypeDao;
	}
}
