package org.flexpay.sz.service;

import java.util.List;

import org.flexpay.sz.persistence.Oszn;

public interface OsznService {

	/**
	 * Read Oszn object by its unique id
	 * 
	 * @param id
	 *            Oszn key
	 * @return Oszn object, or <code>null</code> if object not found
	 */
	Oszn read(Long id);
	
	/**
	 * Get a list of available entities
	 *
	 * @return List of Entity
	 */
	List<Oszn> getEntities();

}
