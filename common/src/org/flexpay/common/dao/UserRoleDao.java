package org.flexpay.common.dao;

import org.flexpay.common.persistence.UserRole;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface UserRoleDao extends GenericDao<UserRole, Long> {
	/**
	 * Load all user roles
	 *
	 * @param status status of desired user roles
	 * @return List of countries
	 */
	List<UserRole> listUserRoles(int status);

	List<UserRole> findByQuery(String query);

	List<UserRole> findByNameAndLanguage(String name, Long languageId);

	List<UserRole> findByExternalId(String externalId);
}
