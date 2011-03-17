package org.flexpay.common.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.UserRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.List;

public interface UserRoleService {
	/**
	 * Read UserRole object by its unique id
	 *
	 * @param userRoleStub UserRole stub
	 * @return UserRole object, or <code>null</code> if object not found
	 */
	@Secured(Roles.USER_ROLE_READ)
	@Nullable
	UserRole readFull(@NotNull Stub<UserRole> userRoleStub);

	/**
	 * Read user roles collection by theirs ids
	 *
	  * @param userRoleIds UserRole ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found user roles
	 */
	@Secured ({Roles.USER_ROLE_READ})
	@NotNull
	List<UserRole> readFull(@NotNull Collection<Long> userRoleIds, boolean preserveOrder);

	/**
	 * Lookup user roles by query. Query is a string which may contains in user role name:
	 *
	 * @param query searching string
	 * @return List of founded user roles
	 */
	@Secured (Roles.USER_ROLE_READ)
	@NotNull
	List<UserRole> findByQuery(@NotNull String query);

	/**
	 * Find user role by external Id
	 *
	 * @param externalId External Id
	 * @return User role or null if role did not  find
	 */
	@Nullable
	UserRole findByExternalId(@NotNull String externalId);

	/**
	 * Get user roles
	 *
	 * @return List all user roles
	 */
	@NotNull
	List<UserRole> getAllUserRoles();

}
