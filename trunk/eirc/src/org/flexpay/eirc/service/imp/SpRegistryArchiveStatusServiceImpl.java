package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.dao.SpRegistryArchiveStatusDao;
import org.flexpay.eirc.persistence.RegistryArchiveStatus;
import org.flexpay.eirc.service.SpRegistryArchiveStatusService;

import java.util.List;

public class SpRegistryArchiveStatusServiceImpl implements SpRegistryArchiveStatusService {

	private SpRegistryArchiveStatusDao spRegistryArchiveStatusDao;

	/**
	 * Read SpRegistryArchiveStatus object by its unique id
	 *
	 * @param id SpRegistryArchiveStatus key
	 * @return SpRegistryArchiveStatus object, or <code>null</code> if object not found
	 */
	public RegistryArchiveStatus read(Long id) {
		return spRegistryArchiveStatusDao.read(id);
	}

	/**
	 * Read SpRegistryArchiveStatus object by its unique code
	 *
	 * @param code SpRegistryArchiveStatus code
	 * @return SpRegistryArchiveStatus object, or <code>null</code> if object not found
	 */
	public RegistryArchiveStatus findByCode(int code) {
		List<RegistryArchiveStatus> statuses = spRegistryArchiveStatusDao.findByCode(code);
		if (statuses.size() < 1) {
			return null;
		}
		return statuses.get(0);
	}

	/**
	 * @param spRegistryArchiveStatusDao the spRegistryArchiveStatusDao to set
	 */
	public void setSpRegistryArchiveStatusDao(
			SpRegistryArchiveStatusDao spRegistryArchiveStatusDao) {
		this.spRegistryArchiveStatusDao = spRegistryArchiveStatusDao;
	}

}
