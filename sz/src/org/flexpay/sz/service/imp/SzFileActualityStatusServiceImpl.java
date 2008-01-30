package org.flexpay.sz.service.imp;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.flexpay.sz.dao.SzFileActualityStatusDao;
import org.flexpay.sz.persistence.SzFileActualityStatus;
import org.flexpay.sz.service.SzFileActualityStatusService;

public class SzFileActualityStatusServiceImpl implements SzFileActualityStatusService {
	private static Logger log = Logger
			.getLogger(SzFileActualityStatusServiceImpl.class);

	Map<Long, SzFileActualityStatus> cashe = new HashMap<Long, SzFileActualityStatus>();

	private SzFileActualityStatusDao szFileActualityStatusDao;

	/**
	 * Read SzFileActualityStatus object by its unique id
	 * 
	 * @param id
	 *            SzFileActualityStatus key
	 * @return SzFileActualityStatus object, or <code>null</code> if object
	 *         not found
	 */
	public SzFileActualityStatus read(Long id) {
		SzFileActualityStatus szFileActualityStatus = cashe.get(id);
		if (szFileActualityStatus == null) {
			szFileActualityStatus = szFileActualityStatusDao.read(id);
			cashe.put(id, szFileActualityStatus);
		}

		return szFileActualityStatus;
	}

	public void setSzFileActualityStatusDao(
			SzFileActualityStatusDao szFileActualityStatusDao) {
		this.szFileActualityStatusDao = szFileActualityStatusDao;
	}
}
