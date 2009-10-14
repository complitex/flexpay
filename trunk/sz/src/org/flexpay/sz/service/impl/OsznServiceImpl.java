package org.flexpay.sz.service.impl;

import org.flexpay.sz.dao.OsznDao;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.service.OsznService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class OsznServiceImpl implements OsznService {

	private OsznDao osznDao;

	/**
	 * Read Oszn object by its unique id
	 * 
	 * @param id
	 *            Oszn key
	 * @return Oszn object, or <code>null</code> if object not found
	 */
	@Override
	public Oszn read(Long id) {
		return osznDao.read(id);
	}

	/**
	 * Get a list of available entities
	 * 
	 * @return List of entities
	 */
	@NotNull
	@Override
	public List<Oszn> getEntities() {
		return osznDao.listOszn();
	}

	@Required
	public void setOsznDao(OsznDao osznDao) {
		this.osznDao = osznDao;
	}

}
