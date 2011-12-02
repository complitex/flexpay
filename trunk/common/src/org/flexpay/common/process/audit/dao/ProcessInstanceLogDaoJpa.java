package org.flexpay.common.process.audit.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.hibernate.HibernateException;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.process.persistence.ProcessInstance.STATE.*;

public class ProcessInstanceLogDaoJpa extends JpaDaoSupport implements ProcessInstanceLogDao {

	private static final Logger log = LoggerFactory.getLogger(ProcessInstanceLogDaoJpa.class);

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public List<ProcessInstanceLog> findProcessInstances() {
		return getJpaTemplate().find("FROM ProcessInstanceLog");
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public List<ProcessInstanceLog> findProcessInstances(@NotNull String processId) {
		return getJpaTemplate().find("FROM ProcessInstanceLog p WHERE p.processId = ?", processId);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public ProcessInstanceLog findProcessInstance(final long processInstanceId) {
		log.debug("Execute findProcessInstance for processInstanceId = {}", processInstanceId);
		/*
		List<ProcessInstanceLog> result =
				getJpaTemplate().find("FROM ProcessInstanceLog p WHERE p.processInstanceId = ?", processInstanceId);

		return result != null && result.size() != 0? result.get(result.size() - 1 ): null;
              */
		return getJpaTemplate().execute(new JpaCallback<ProcessInstanceLog>() {
			@Override
			public ProcessInstanceLog doInJpa(EntityManager entityManager) throws PersistenceException {
				List resultList = entityManager
						.createQuery("FROM ProcessInstanceLog " +
                                "WHERE processInstanceId = :processInstance " +
                                "ORDER BY start DESC")
						.setParameter("processInstance", processInstanceId)
						.getResultList();
				ProcessInstanceLog result = resultList != null && resultList.size() != 0 ?
						(ProcessInstanceLog) resultList.get(resultList.size() - 1 ) : null;
				if (result != null) {
					List<?> endDates = entityManager
							.createQuery("SELECT p.end " +
                                    "FROM ProcessInstanceLog p " +
                                    "WHERE p.processInstanceId = :processInstance " +
                                    "ORDER BY start DESC")
							.setParameter("processInstance", processInstanceId)
							.getResultList();
                    if (endDates == null || endDates.isEmpty()) {
                        log.error("Can't get endDate for processInstanceLog = {}", processInstanceId);
                        result.setEnd(null);
                    } else {
                        result.setEnd((Date) endDates.get(0));
                    }
					log.debug("End date: {}", result.getEnd());
				}
				return result;
				//find(ProcessInstanceLog.class, processInstanceId);
			}
		});
	}

	@Nullable
	@Override
	public ProcessInstanceLog findNotEndedProcessInstance(long processInstaceId) {
		@SuppressWarnings("unchecked")
		List<ProcessInstanceLog> result = getJpaTemplate().
				find("FROM ProcessInstanceLog p WHERE p.processInstanceId = ? and p.end is null", processInstaceId);

		return result != null && result.size() != 0? result.get(result.size() - 1): null;
	}

	@NotNull
	@Override
	public ProcessInstanceLog create(@NotNull final ProcessInstanceLog processInstance) {
		getJpaTemplate().persist(processInstance);
		return processInstance;
	}

	@NotNull
	@Override
	public ProcessInstanceLog update(@NotNull final ProcessInstanceLog processInstance) {
		return getJpaTemplate().execute(new JpaCallback<ProcessInstanceLog>() {
			@Override
			public ProcessInstanceLog doInJpa(EntityManager entityManager) throws PersistenceException {
				return entityManager.merge(processInstance);
			}
		});
		//return getJpaTemplate().merge(processInstance);
	}

	@Override
	public List<ProcessInstanceLog> findProcessInstances(ProcessSorter sorter, final Page pager, Date startFrom, Date endBefore,
													  ProcessInstance.STATE state, String name) {

		final StringBuilder hql = new StringBuilder("select distinct pi " +
													"from ProcessInstanceLog pi ");

		final StringBuilder cntHql = new StringBuilder("select count(pi) " +
													   "from ProcessInstanceLog pi ");

		final StringBuilder filterHql = getFilterAndSortHql(sorter, state, startFrom, endBefore, name);
		hql.append(filterHql);
		cntHql.append(filterHql);

		return getProcessInstances(pager, hql, cntHql, startFrom, endBefore, state, name);
	}

	private List<ProcessInstanceLog> getProcessInstances(Page pager, StringBuilder hql, StringBuilder cntHql,
													  Date startFrom, Date endBefore, ProcessInstance.STATE state, String name) {

		List<ProcessInstanceLog> instances;
		if (pager != null) {
			instances = getProcessInstancesPage(pager, hql, cntHql, startFrom, endBefore, state, name);
		} else {
			instances = getAllProcessesInstances(hql, startFrom, endBefore, state, name);
		}

		return instances;
	}

	private StringBuilder getFilterAndSortHql(ProcessSorter sorter, ProcessInstance.STATE state, Date startFrom, Date endBefore,
											  String name) {
		final StringBuilder filterHql = new StringBuilder();
		addFiltering(filterHql, state, startFrom, endBefore, name);
		addSorting(filterHql, sorter);
		return filterHql;
	}

	private void addFiltering(StringBuilder hql, ProcessInstance.STATE state, Date startFrom, Date endBefore, String name) {

		StringBuilder whereClause = new StringBuilder();

		if (name != null) {
			appendHqlWhereClause(whereClause, "pi.processId = :name");
		}

		if (state != null) {
			switch (state) {
				case ENDED:
					appendHqlWhereClause(whereClause, "pi.end is not null");
					break;
				case RUNNING:
					appendHqlWhereClause(whereClause, "pi.start is not null and pi.end is null");
					break;
				case PENDING:
					appendHqlWhereClause(whereClause, "pi.start is null");
					break;
				case SUSPENDED:
					appendHqlWhereClause(whereClause, "pi.start is not null and pi.end is null");
					break;
				default:
					break;
			}
		}

		if (startFrom != null && state != PENDING) {
			appendHqlWhereClause(whereClause, "pi.start >= :startFrom");
		}

		if (endBefore != null && state != RUNNING) {
			appendHqlWhereClause(whereClause, "pi.end <= :endBefore");
		}

		if (whereClause.length() > 0) {
			hql.append(" where ").append(whereClause);
		}
	}


	private void appendHqlWhereClause(StringBuilder whereClause, String appendix) {

		if (whereClause.length() > 0) {
			whereClause.append(" and ");
		}
		whereClause.append(appendix);
	}

	private void addSorting(StringBuilder hql, ProcessSorter sorter) {

        hql.append(" order by ");

		if (sorter != null) {
			StringBuilder orderByClause = new StringBuilder();
			sorter.setOrderBy(orderByClause);

			if (orderByClause.length() > 0) {
				hql.append(orderByClause).append(",");
			}
		}
        hql.append("pi.id desc");
	}

	private void setQueryParameters(Query query, ProcessInstance.STATE state, Date startFrom, Date endBefore, String name) {

		if (name != null) {
			query.setParameter("name", name);
		}

		if (startFrom != null && state != PENDING) {
			query.setParameter("startFrom", startFrom);
		}

		if (endBefore != null && state != RUNNING) {
			query.setParameter("endBefore", endBefore);
		}

	}

	@SuppressWarnings ({"unchecked"})
	private List<ProcessInstanceLog> getAllProcessesInstances(final StringBuilder hql, final Date startFrom,
														   final Date endBefore, final ProcessInstance.STATE state,
														   final String name) {

		return (List<ProcessInstanceLog>) getJpaTemplate().executeFind(new JpaCallback<List<ProcessInstanceLog>>() {
			@Override
			public List<ProcessInstanceLog> doInJpa(EntityManager entityManager) throws HibernateException {
				Query query = entityManager.createQuery(hql.toString());
				setQueryParameters(query, state, startFrom, endBefore, name);
				return (List<ProcessInstanceLog>) query.getResultList();
			}
		});
	}

	@SuppressWarnings ({"unchecked"})
	private List<ProcessInstanceLog> getProcessInstancesPage(final Page pager, final StringBuilder hql,
														  final StringBuilder cntHql, final Date startFrom,
														  final Date endBefore, final ProcessInstance.STATE state,
														  final String name) {

		return (List<ProcessInstanceLog>) getJpaTemplate().executeFind(new JpaCallback<List<ProcessInstanceLog>>() {
            @Override
			public List<ProcessInstanceLog> doInJpa(EntityManager entityManager) throws HibernateException {

				// getting total elements number for pager
				Query cntQuery = entityManager.createQuery(cntHql.toString());
				setQueryParameters(cntQuery, state, startFrom, endBefore, name);
				Long count = (Long) cntQuery.getSingleResult();
				pager.setTotalElements(count.intValue());

				Query query = entityManager.createQuery(hql.toString());
				setQueryParameters(query, state, startFrom, endBefore, name);
				query.setFirstResult(pager.getThisPageFirstElementNumber());
				query.setMaxResults(pager.getPageSize());

				return (List<ProcessInstanceLog>) query.getResultList();
			}
		});
	}
}
