package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.dao.SpRegistryRecordStatusDao;
import org.flexpay.eirc.persistence.SpRegistryRecordStatus;
import org.flexpay.eirc.service.SpRegistryRecordStatusService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpRegistryRecordStatusServiceImpl implements SpRegistryRecordStatusService {

	private SpRegistryRecordStatusDao spRegistryRecordStatusDao;
	private Map<Integer, SpRegistryRecordStatus> code2StatusCache;

	public SpRegistryRecordStatus findByCode(int code) {
		if (code2StatusCache == null) {
			List<SpRegistryRecordStatus> statuses = spRegistryRecordStatusDao.findAll();
			code2StatusCache = new HashMap<Integer, SpRegistryRecordStatus>();
			for (SpRegistryRecordStatus status : statuses) {
				code2StatusCache.put(status.getCode(), status);
			}
		}

		return code2StatusCache.get(code);
	}

	public void setSpRegistryRecordStatusDao(SpRegistryRecordStatusDao spRegistryRecordStatusDao) {
		this.spRegistryRecordStatusDao = spRegistryRecordStatusDao;
	}
}