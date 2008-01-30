package org.flexpay.sz.service.imp;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.flexpay.sz.dao.SzFileStatusDao;
import org.flexpay.sz.persistence.SzFileStatus;
import org.flexpay.sz.service.SzFileStatusService;

public class SzFileStatusServiceImpl implements SzFileStatusService {

	private static Logger log = Logger.getLogger(SzFileStatusServiceImpl.class);

	Map<Long, SzFileStatus> cashe = new HashMap<Long, SzFileStatus>();

	private SzFileStatusDao szFileStatusDao;

	/**
	 * Read SzFileStatus object by its unique id
	 * 
	 * @param id
	 *            SzFileStatus key
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
