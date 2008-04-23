package org.flexpay.eirc.service.imp;

import java.util.List;

import org.apache.log4j.Logger;
import org.flexpay.eirc.dao.SpRegistryStatusDao;
import org.flexpay.eirc.persistence.SpRegistryArchiveStatus;
import org.flexpay.eirc.persistence.SpRegistryStatus;
import org.flexpay.eirc.service.SpRegistryStatusService;

public class SpRegistryStatusServiceImpl implements SpRegistryStatusService {
	private static Logger log = Logger
			.getLogger(SpRegistryStatusServiceImpl.class);

	private SpRegistryStatusDao spRegistryStatusDao;

	/**
	 * Read SpRegistryStatus object by its unique id
	 * 
	 * @param id
	 *            SpRegistryStatus key
	 * @return SpRegistryStatus object, or <code>null</code> if object not
	 *         found
	 */
	public SpRegistryStatus read(Long id) {
		return spRegistryStatusDao.read(id);
	}

	/**
	 * Read SpRegistryStatus object by its unique code
	 * 
	 * @param code
	 *            SpRegistryStatus code
	 * @return SpRegistrytatus object, or <code>null</code> if object not
	 *         found
	 */
	public SpRegistryStatus findByCode(int code) {
		List<SpRegistryStatus> statuses = spRegistryStatusDao.findByCode(code);
		if (statuses.size() < 1) {
			return null;
		}
		return statuses.get(0);
	}

	/**
	 * @param spRegistryStatusDao
	 *            the spRegistryStatusDao to set
	 */
	public void setSpRegistryStatusDao(SpRegistryStatusDao spRegistryStatusDao) {
		this.spRegistryStatusDao = spRegistryStatusDao;
	}

}
