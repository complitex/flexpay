package org.flexpay.sz.service;

import org.flexpay.sz.persistence.Oszn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
	@NotNull
	List<Oszn> getEntities();

}
