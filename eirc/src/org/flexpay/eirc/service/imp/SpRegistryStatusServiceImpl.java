package org.flexpay.eirc.service.imp;

import org.flexpay.common.dao.registry.RegistryStatusDao;
import org.flexpay.common.persistence.registry.RegistryStatus;
import org.flexpay.eirc.service.SpRegistryStatusService;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpRegistryStatusServiceImpl implements SpRegistryStatusService {

	private Map<Integer, RegistryStatus> code2StatusCache;

	private RegistryStatusDao registryStatusDao;

	/**
	 * Read SpRegistryStatus object by its unique code
	 *
	 * @param code SpRegistryStatus code
	 * @return SpRegistrytatus object, or <code>null</code> if object not found
	 */
	public RegistryStatus findByCode(int code) {
		if (code2StatusCache == null) {
			List<RegistryStatus> statuses = registryStatusDao.findAll();
			code2StatusCache = new HashMap<Integer, RegistryStatus>();
			for (RegistryStatus status : statuses) {
				code2StatusCache.put(status.getCode(), status);
			}
		}

		return code2StatusCache.get(code);
	}

	@Required
	public void setSpRegistryStatusDao(RegistryStatusDao registryStatusDao) {
		this.registryStatusDao = registryStatusDao;
	}

}
