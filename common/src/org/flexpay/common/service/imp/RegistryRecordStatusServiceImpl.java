package org.flexpay.common.service.imp;

import org.flexpay.common.dao.registry.RegistryRecordStatusDao;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.RegistryRecordStatusService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryRecordStatusServiceImpl implements RegistryRecordStatusService {

	private RegistryRecordStatusDao registryRecordStatusDao;
	private Map<Integer, RegistryRecordStatus> code2StatusCache;

	public RegistryRecordStatus findByCode(int code) {
		if (code2StatusCache == null) {
			List<RegistryRecordStatus> statuses = registryRecordStatusDao.findAll();
			code2StatusCache = new HashMap<Integer, RegistryRecordStatus>();
			for (RegistryRecordStatus status : statuses) {
				code2StatusCache.put(status.getCode(), status);
			}
		}

		return code2StatusCache.get(code);
	}

	public void setSpRegistryRecordStatusDao(RegistryRecordStatusDao registryRecordStatusDao) {
		this.registryRecordStatusDao = registryRecordStatusDao;
	}
}
