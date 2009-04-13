package org.flexpay.eirc.service.imp;

import org.flexpay.common.dao.registry.RegistryTypeDao;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.eirc.service.SpRegistryTypeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
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
	 * Read SpRegistryType object by its unique code
	 *
	 * @param code SpRegistryType code
	 * @return SpRegistryType object, or <code>null</code> if object not found
	 */
	public RegistryType findByCode(@NotNull Integer code) {
		List<RegistryType> types = registryTypeDao.findByCode(code);
		return types.isEmpty() ? null : types.get(0);
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

	@Required
	public void setSpRegistryTypeDao(RegistryTypeDao registryTypeDao) {
		this.registryTypeDao = registryTypeDao;
	}

}
