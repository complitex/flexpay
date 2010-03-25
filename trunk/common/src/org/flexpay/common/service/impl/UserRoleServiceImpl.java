package org.flexpay.common.service.impl;

import org.flexpay.common.dao.UserRoleDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.UserRole;
import org.flexpay.common.service.UserRoleService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional(readOnly = true)
public class UserRoleServiceImpl implements UserRoleService {
	private Logger log = LoggerFactory.getLogger(getClass());

	private UserRoleDao userRoleDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserRole readFull(@NotNull Stub<UserRole> userRoleStub) {
		return userRoleDao.readFull(userRoleStub.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public List<UserRole> readFull(@NotNull Collection<Long> userRoleIds, boolean preserveOrder) {
		return userRoleDao.readFullCollection(userRoleIds, preserveOrder);
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public List<UserRole> findByQuery(@NotNull String query) {
		return userRoleDao.findByQuery(query.toUpperCase());
	}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	@Override
	public UserRole findByExternalId(@NotNull String externalId) {
		List<UserRole> roles = userRoleDao.findByExternalId(externalId);
		if (roles.size() == 0) {
			return null;
		}
		return roles.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public List<UserRole> getAllUserRoles() {
		return userRoleDao.listUserRoles(UserRole.STATUS_ACTIVE);
	}

	@Required
	public void setUserRoleDao(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}
}
