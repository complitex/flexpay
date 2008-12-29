package org.flexpay.sz.service.imp;

import org.flexpay.sz.dao.OsznDao;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.service.OsznService;

import java.util.List;

public class OsznServiceImpl implements OsznService {

	private OsznDao osznDao;

	/**
	 * Read Oszn object by its unique id
	 * 
	 * @param id
	 *            Oszn key
	 * @return Oszn object, or <code>null</code> if object not found
	 */
	public Oszn read(Long id) {
		return osznDao.read(id);
	}

	/**
	 * Get a list of available entities
	 * 
	 * @return List of entities
	 */
	public List<Oszn> getEntities() {
		return osznDao.listOszn();
	}

	public void setOsznDao(OsznDao osznDao) {
		this.osznDao = osznDao;
	}

}
