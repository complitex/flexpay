package org.flexpay.eirc.dao.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.eirc.dao.SpRegistryRecordDaoExt;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
	@SuppressWarnings({"unchecked"})
	public List<SpRegistryRecord> listRecordsForUpdate(final Long id, final Page pager) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Long count = (Long) session.getNamedQuery("SpRegistryRecord.listRecords.count")
						.setLong(0, id).uniqueResult();
				pager.setTotalElements(count.intValue());

				return session.getNamedQuery("SpRegistryRecord.listRecords")
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize())
						.setLong(0, id)
						.list();
			}
		});
	}

	/**
	 * Filter registry records
	 *
	 * @param registryId			Registry key
	 * @param importErrorTypeFilter Error type filter
	 * @param recordStatusFilter	Records status filter
	 * @param pager				 Page
	 * @return list of registry records
	 */
	@SuppressWarnings({"unchecked"})
	public List<SpRegistryRecord> filterRecords(Long registryId, ImportErrorTypeFilter importErrorTypeFilter,
												RegistryRecordStatusFilter recordStatusFilter, final Page<SpRegistryRecord> pager) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(SpRegistryRecord.class, "rr")
				.createAlias("rr.spRegistry", "r")
				.add(Restrictions.eq("r.id", registryId))
				.createAlias("rr.importError", "e")
				.setFetchMode("rr.importError", FetchMode.JOIN)
				.createAlias("rr.recordStatus", "rs")
				.setFetchMode("rr.recordStatus", FetchMode.JOIN);

		// filter by record status
		if (recordStatusFilter.needFilter()) {
			criteria.add(Restrictions.eq("rs.code", recordStatusFilter.getSelectedStatus()));
		}

		if (importErrorTypeFilter.needFilter()) {
			if (importErrorTypeFilter.needFilterWithoutErrors()) {
				criteria.add(Restrictions.isNull("rr.importError"));
			} else {
				criteria.add(Restrictions.eq("e.status", ImportError.STATUS_ACTIVE))
						.add(Restrictions.eq("e.objectType", importErrorTypeFilter.getSelectedType()));
			}
		}

		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				Number count = (Number) criteria.setProjection(Projections.count("rr.id"))
						.getExecutableCriteria(session).uniqueResult();
				if (count == null) {
					pager.setTotalElements(0);
				} else {
					pager.setTotalElements(count.intValue());
				}

				// http://forum.hibernate.org/viewtopic.php?t=939039
				criteria.setProjection(null)
						.setResultTransformer(Criteria.ROOT_ENTITY);

				return criteria.getExecutableCriteria(session)
						.setMaxResults(pager.getPageSize())
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.list();
			}
		});
	}

	/**
	 * Count number of error in registry
	 *
	 * @param registryId Registry to count errors for
	 * @return number of errors
	 */
	public int getErrorsNumber(final Long registryId) {
		Number count = (Number) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createQuery("select count(r.id) from SpRegistryRecord r where r.spRegistry.id=? and r.importError is not null")
						.setLong(0, registryId).uniqueResult();
			}
		});
		return count.intValue();
	}

	public DataSourceDescription getDataSourceDescription(final Long id) {
		return (DataSourceDescription) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.getNamedQuery("SpRegistryRecord.findDataSourceDescription")
						.setLong(0, id).uniqueResult();
			}
		});
	}
}
