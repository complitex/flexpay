package org.flexpay.eirc.service.imp;

import org.flexpay.common.dao.registry.RegistryTypeDao;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.eirc.service.SpRegistryTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class RegistryTypeServiceImpl implements SpRegistryTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private RegistryTypeDao registryTypeDao;

	/**
	 * Read SpRegistryType object by its unique id
	 *
	 * @param id SpRegistryType key
	 * @return SpRegistryType object, or <code>null</code> if object not found
	 */
	public RegistryType read(Long id) {
		return registryTypeDao.read(id);
	}

	/**
	 * init filter
	 *
	 * @param registryTypeFilter filter to init
	 */
	public void initFilter(RegistryTypeFilter registryTypeFilter) {
		List<RegistryType> types = registryTypeDao.findAll();
		registryTypeFilter.setRegistryTypes(types);

		log.debug("Registry types: {}" + types);
	}

	/**
	 * @param registryTypeDao the spRegistryTypeDao to set
	 */
	public void setSpRegistryTypeDao(RegistryTypeDao registryTypeDao) {
		this.registryTypeDao = registryTypeDao;
	}
}
