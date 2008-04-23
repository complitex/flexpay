package org.flexpay.eirc.service.imp;

import java.util.List;

import org.apache.log4j.Logger;
import org.flexpay.eirc.dao.SpRegistryArchiveStatusDao;
import org.flexpay.eirc.persistence.SpRegistryArchiveStatus;
import org.flexpay.eirc.service.SpRegistryArchiveStatusService;

public class SpRegistryArchiveStatusServiceImpl implements
		SpRegistryArchiveStatusService {
	private static Logger log = Logger
			.getLogger(SpRegistryArchiveStatusServiceImpl.class);

	private SpRegistryArchiveStatusDao spRegistryArchiveStatusDao;

	/**
	 * Read SpRegistryArchiveStatus object by its unique id
	 * 
	 * @param id
	 *            SpRegistryArchiveStatus key
	 * @return SpRegistryArchiveStatus object, or <code>null</code> if object
	 *         not found
	 */
	public SpRegistryArchiveStatus read(Long id) {
		return spRegistryArchiveStatusDao.read(id);
	}

	/**
	 * Read SpRegistryArchiveStatus object by its unique code
	 * 
	 * @param code
	 *            SpRegistryArchiveStatus code
	 * @return SpRegistryArchiveStatus object, or <code>null</code> if object
	 *         not found
	 */
	public SpRegistryArchiveStatus findByCode(int code) {
		List<SpRegistryArchiveStatus> statuses = spRegistryArchiveStatusDao
				.findByCode(code);
		if (statuses.size() < 1) {
			return null;
		}
		return statuses.get(0);
	}

	/**
	 * @param spRegistryArchiveStatusDao
	 *            the spRegistryArchiveStatusDao to set
	 */
	public void setSpRegistryArchiveStatusDao(
			SpRegistryArchiveStatusDao spRegistryArchiveStatusDao) {
		this.spRegistryArchiveStatusDao = spRegistryArchiveStatusDao;
	}

}
