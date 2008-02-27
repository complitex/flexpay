package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.dao.AccountStatusDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class AccountStatusDaoImpl extends HibernateDaoSupport implements AccountStatusDao {

	@SuppressWarnings ({"unchecked"})
	public List getAccountStatuses() {
		return getHibernateTemplate().findByNamedQuery("AccountStatus.findAll");
	}
}
