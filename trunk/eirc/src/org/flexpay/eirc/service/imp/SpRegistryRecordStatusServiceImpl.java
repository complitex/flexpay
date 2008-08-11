package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.dao.SpRegistryRecordStatusDao;
import org.flexpay.eirc.persistence.RegistryRecordStatus;
import org.flexpay.eirc.service.SpRegistryRecordStatusService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpRegistryRecordStatusServiceImpl implements SpRegistryRecordStatusService {

	private SpRegistryRecordStatusDao spRegistryRecordStatusDao;
	private Map<Integer, RegistryRecordStatus> code2StatusCache;

	public RegistryRecordStatus findByCode(int code) {
		if (code2StatusCache == null) {
			List<RegistryRecordStatus> statuses = spRegistryRecordStatusDao.findAll();
			code2StatusCache = new HashMap<Integer, RegistryRecordStatus>();
			for (RegistryRecordStatus status : statuses) {
				code2StatusCache.put(status.getCode(), status);
			}
		}

		return code2StatusCache.get(code);
	}

	public void setSpRegistryRecordStatusDao(SpRegistryRecordStatusDao spRegistryRecordStatusDao) {
		this.spRegistryRecordStatusDao = spRegistryRecordStatusDao;
	}
}