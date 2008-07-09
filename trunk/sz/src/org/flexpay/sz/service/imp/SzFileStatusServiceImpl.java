package org.flexpay.sz.service.imp;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.sz.dao.SzFileStatusDao;
import org.flexpay.sz.persistence.SzFileStatus;
import org.flexpay.sz.service.SzFileStatusService;

import java.util.Map;

public class SzFileStatusServiceImpl implements SzFileStatusService {

	Map<Long, SzFileStatus> cashe = CollectionUtils.map();

	private SzFileStatusDao szFileStatusDao;

	/**
	 * Read SzFileStatus object by its unique id
	 *
	 * @param id SzFileStatus key
	 * @return SzFileStatus object, or <code>null</code> if object not found
	 */
	public SzFileStatus read(Long id) {
		SzFileStatus szFileStatus = cashe.get(id);
		if (szFileStatus == null) {
			szFileStatus = szFileStatusDao.read(id);
			cashe.put(id, szFileStatus);
		}

		return szFileStatus;
	}

	public void setSzFileStatusDao(SzFileStatusDao szFileStatusDao) {
		this.szFileStatusDao = szFileStatusDao;
	}
}
