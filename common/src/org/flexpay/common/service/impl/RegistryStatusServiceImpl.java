package org.flexpay.common.service.impl;

import org.flexpay.common.dao.registry.RegistryStatusDao;
import org.flexpay.common.persistence.registry.RegistryStatus;
import org.flexpay.common.service.RegistryStatusService;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryStatusServiceImpl implements RegistryStatusService {

	private Map<Integer, RegistryStatus> code2StatusCache;

	private RegistryStatusDao registryStatusDao;

	/**
	 * Read RegistryStatus object by its unique code
	 *
	 * @param code RegistryStatus code
	 * @return Registrytatus object, or <code>null</code> if object not found
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
