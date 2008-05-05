package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.dao.SpRegistryStatusDao;
import org.flexpay.eirc.persistence.SpRegistryStatus;
import org.flexpay.eirc.service.SpRegistryStatusService;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SpRegistryStatusServiceImpl implements SpRegistryStatusService {

	private SpRegistryStatusDao spRegistryStatusDao;
	private Map<Integer, SpRegistryStatus> code2StatusCache;

	/**
	 * Read SpRegistryStatus object by its unique code
	 *
	 * @param code SpRegistryStatus code
	 * @return SpRegistrytatus object, or <code>null</code> if object not found
	 */
	public SpRegistryStatus findByCode(int code) {
		if (code2StatusCache == null) {
			List<SpRegistryStatus> statuses = spRegistryStatusDao.findAll();
			code2StatusCache = new HashMap<Integer, SpRegistryStatus>();
			for (SpRegistryStatus status : statuses) {
				code2StatusCache.put(status.getCode(), status);
			}
		}

		return code2StatusCache.get(code);
	}

	/**
	 * @param spRegistryStatusDao the spRegistryStatusDao to set
	 */
	public void setSpRegistryStatusDao(SpRegistryStatusDao spRegistryStatusDao) {
		this.spRegistryStatusDao = spRegistryStatusDao;
	}
}
