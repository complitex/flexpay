package org.flexpay.common.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.UserPreferencesDao;
import org.flexpay.common.util.config.UserPreferences;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class UserPreferencesDaoImpl extends HibernateDaoSupport implements UserPreferencesDao {

	@Override
	public void save(UserPreferences preferences) {

		if (StringUtils.isEmpty(preferences.getFullName())) {
			preferences.setFullName(preferences.getUsername());
		}
		if (StringUtils.isEmpty(preferences.getLastName())) {
			preferences.setLastName(preferences.getUsername());
		}

		if (preferences.isNew()) {
			preferences.setId(null);
			getHibernateTemplate().save(preferences);
		} else {
			getHibernateTemplate().update(preferences);
		}
	}

	@Override
	public void delete(UserPreferences preferences) {
		getHibernateTemplate().delete(preferences);
	}

	@Override
	public UserPreferences findByUserName(String uid) {
		return (UserPreferences) DataAccessUtils.uniqueResult(
				getHibernateTemplate().findByNamedQuery("UserPreferences.findByName", uid));
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public List<UserPreferences> listAllUser() {
		return getHibernateTemplate().loadAll(UserPreferences.class);
	}
}
