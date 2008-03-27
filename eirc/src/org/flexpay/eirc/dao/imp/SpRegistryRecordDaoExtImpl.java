package org.flexpay.eirc.dao.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.dao.SpRegistryRecordDaoExt;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.List;

public class SpRegistryRecordDaoExtImpl extends HibernateDaoSupport implements SpRegistryRecordDaoExt {

	/**
	 * List registry records
	 *
	 * @param id	Registry id
	 * @param pager Pager
	 * @return list of records
	 */
	@SuppressWarnings ({"unchecked"})
	public List<SpRegistryRecord> listRecordsForUpdate(final Long id, final Page pager) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Long count = (Long) session.getNamedQuery("SpRegistryRecord.listRecords.count")
						.setLong(0, id).uniqueResult();
				pager.setTotalElements(count.intValue());

				return session.getNamedQuery("SpRegistryRecord.listRecords")
//						.setLockMode("r", LockMode.UPGRADE)
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize())
						.setLong(0, id)
						.list();
			}
		});
	}
}
