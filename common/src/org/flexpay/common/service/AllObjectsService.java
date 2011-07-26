package org.flexpay.common.service;

import org.flexpay.common.persistence.DomainObject;

import java.util.List;

public interface AllObjectsService<T extends DomainObject> extends JpaSetService {

	/**
	 * Get all objects
	 *
	 * @return List of all objects
	 */
	List<T> getAll();

}
