package org.flexpay.common.service.imp;

import org.flexpay.common.dao.registry.RegistryArchiveStatusDao;
import org.flexpay.common.persistence.registry.RegistryArchiveStatus;
import org.flexpay.common.service.RegistryArchiveStatusService;

import java.util.List;

public class RegistryArchiveStatusServiceImpl implements RegistryArchiveStatusService {

	private RegistryArchiveStatusDao registryArchiveStatusDao;

	/**
	 * Read RegistryArchiveStatus object by its unique id
	 *
	 * @param id RegistryArchiveStatus key
	 * @return RegistryArchiveStatus object, or <code>null</code> if object not found
	 */
	public RegistryArchiveStatus read(Long id) {
		return registryArchiveStatusDao.read(id);
	}

	/**
	 * Read RegistryArchiveStatus object by its unique code
	 *
	 * @param code RegistryArchiveStatus code
	 * @return RegistryArchiveStatus object, or <code>null</code> if object not found
	 */
	public RegistryArchiveStatus findByCode(int code) {
		List<RegistryArchiveStatus> statuses = registryArchiveStatusDao.findByCode(code);
		if (statuses.size() < 1) {
			return null;
		}
		return statuses.get(0);
	}

	/**
	 * @param registryArchiveStatusDao the registryArchiveStatusDao to set
	 */
	public void setRegistryArchiveStatusDao(RegistryArchiveStatusDao registryArchiveStatusDao) {
		this.registryArchiveStatusDao = registryArchiveStatusDao;
	}
}
