package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.UserPreferencesDao;
import org.flexpay.common.util.config.UserPreferences;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.dao.support.DataAccessUtils;

public class UserPreferencesDaoImpl extends HibernateDaoSupport implements UserPreferencesDao {

	@Override
	public void update(UserPreferences preferences) {
		getHibernateTemplate().update(preferences);
	}

	@Override
	public UserPreferences findByUserName(String uid) {
		return (UserPreferences) DataAccessUtils.uniqueResult(
				getHibernateTemplate().findByNamedQuery("UserPreferences.findByName", uid));
	}
}
