package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.ProcessDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.flexpay.common.util.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.graph.exe.ProcessInstance;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Hibernate-based implementation of {@link org.flexpay.common.dao.ProcessDao}
 */
public class ProcessDaoImpl implements ProcessDao {

	private HibernateTemplate hibernateTemplate;

	/**
	 * {@inheritDoc}
	 */
	public List<Process> findProcesses(ProcessSorter sorter, final Page<Process> pager, Date startFrom, Date endBefore, ProcessState state) {

		final StringBuilder hql = new StringBuilder("select distinct pi " +
													"from org.jbpm.graph.exe.ProcessInstance pi " +
													"left join fetch pi.processDefinition pd ");

		final StringBuilder cntHql = new StringBuilder("select count(pi) " +
													   "from org.jbpm.graph.exe.ProcessInstance pi");

		final StringBuilder filterHql = getFilterAndSortHql(sorter, state, startFrom, endBefore);
		hql.append(filterHql);
		cntHql.append(filterHql);

		return convert(getProcessInstances(pager, hql, cntHql, startFrom, endBefore, state));
	}

	private List<ProcessInstance> getProcessInstances(Page<Process> pager, StringBuilder hql, StringBuilder cntHql,
													  Date startFrom, Date endBefore, ProcessState state) {

		List<ProcessInstance> instances;
		if (pager != null) {
			instances = getProcessInstancesPage(pager, hql, cntHql, startFrom, endBefore, state);
		} else {
			instances = getAllProcessesInstances(hql, startFrom, endBefore, state);
		}

		return instances;
	}

	private void addSorting(StringBuilder hql, ProcessSorter sorter) {

		if (sorter != null) {
			StringBuilder orderByClause = new StringBuilder();
			sorter.setOrderBy(orderByClause);

			if (orderByClause.length() > 0) {
				hql.append(" order by ").append(orderByClause);
			}
		}
	}

	private StringBuilder getFilterAndSortHql(ProcessSorter sorter, ProcessState state, Date startFrom, Date endBefore) {
		final StringBuilder filterHql = new StringBuilder();
		addFiltering(filterHql, state, startFrom, endBefore);
		addSorting(filterHql, sorter);
		return filterHql;
	}

	private void addFiltering(StringBuilder hql, ProcessState state, Date startFrom, Date endBefore) {

		StringBuilder whereClause = new StringBuilder();

		if (state != null) {
			switch (state) {
				case COMPLITED:
					appendHqlWhereClause(whereClause, "pi.end is not null");
					break;
				case RUNING:
					appendHqlWhereClause(whereClause, "pi.start is not null and pi.end is null");
					break;
				case WAITING:
					appendHqlWhereClause(whereClause, "pi.start is null");
					break;
				case COMPLITED_WITH_ERRORS:
					// how to deal with this state?
					break;
				default:
					break;
			}
		}

		if (startFrom != null && state != ProcessState.WAITING) {
			appendHqlWhereClause(whereClause, "pi.start > ?");
		}

		if (endBefore != null && state != ProcessState.RUNING) {
			appendHqlWhereClause(whereClause, "pi.end < ?");
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

	private void setQueryParameters(Query query, ProcessState state, Date startFrom, Date endBefore) {

		boolean startIsPresent = startFrom != null && state != ProcessState.WAITING;
		boolean endIsPresent = endBefore != null && state != ProcessState.RUNING;

		// both start and end dates are present
		if (startIsPresent && endIsPresent) {
			query.setDate(0, startFrom);
			query.setDate(1, endBefore);
		}

		// start date is present, end date is not
		if (startIsPresent && !endIsPresent) {
			query.setDate(0, startFrom);
		}

		// end date is present, start is not
		if (!startIsPresent && endIsPresent) {
			query.setDate(0, endBefore);
		}
	}

	@SuppressWarnings ({"unchecked"})
	private List<ProcessInstance> getAllProcessesInstances(final StringBuilder hql, final Date startFrom,
														   final Date endBefore, final ProcessState state) {

		return (List<ProcessInstance>) hibernateTemplate.executeFind(new HibernateCallback() {
			public List<ProcessInstance> doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql.toString());
				setQueryParameters(query, state, startFrom, endBefore);
				return (List<ProcessInstance>) query.list();
			}
		});
	}

	@SuppressWarnings ({"unchecked"})
	private List<ProcessInstance> getProcessInstancesPage(final Page<Process> pager, final StringBuilder hql, final StringBuilder cntHql,
														  final Date startFrom, final Date endBefore, final ProcessState state) {

		return (List<ProcessInstance>) hibernateTemplate.executeFind(new HibernateCallback() {
			public List<ProcessInstance> doInHibernate(Session session) throws HibernateException {

				// getting total elements number for pager
				Query cntQuery = session.createQuery(cntHql.toString());
				setQueryParameters(cntQuery, state, startFrom, endBefore);
				Long count = (Long) cntQuery.uniqueResult();
				pager.setTotalElements(count.intValue());

				Query query = session.createQuery(hql.toString());
				setQueryParameters(query, state, startFrom, endBefore);
				query.setFirstResult(pager.getThisPageFirstElementNumber());
				query.setMaxResults(pager.getPageSize());

				return (List<ProcessInstance>) query.list();
			}
		});
	}

	private List<Process> convert(List<ProcessInstance> instances) {
		List<Process> processes = CollectionUtils.list();
		for (ProcessInstance processInstance : instances) {
			processes.add(getProcessInfo(processInstance.getId()));
		}
		return processes;
	}

	/**
	 * {@inheritDoc}
	 */
	public Process getProcessInfoWithVariables(ProcessInstance processInstance) {

		Process process = getProcessInfo(processInstance);

		@SuppressWarnings ({"unchecked"})
		Map<Serializable, Serializable> parameters = processInstance.getContextInstance().getVariables();
		if (parameters == null) {
			parameters = CollectionUtils.map();
		}
		process.setParameters(parameters);

		if (parameters.containsKey(Job.RESULT_ERROR)) {
			process.setProcessState((ProcessState) parameters.get(Job.RESULT_ERROR));
		}

		return process;
	}

	@SuppressWarnings ({"unchecked"})
	private Process getProcessInfo(@NotNull Long processId) {

		List<ProcessInstance> processInstances = (List<ProcessInstance>) hibernateTemplate.findByNamedQuery("Process.readFull", processId);
		if (processInstances.size() == 0) {
			return null;
		}

		return getProcessInfo(processInstances.get(0));
	}

	private Process getProcessInfo(ProcessInstance processInstance) {

		Process process = new Process();
		if (processInstance == null) {
			return process;
		}

		process.setId(processInstance.getId());
		process.setProcessDefinitionName(processInstance.getProcessDefinition().getName());
		process.setProcessEndDate(processInstance.getEnd());
		process.setProcessStartDate(processInstance.getStart());
		process.setProcessDefenitionVersion(processInstance.getProcessDefinition().getVersion());

		File logFile = ProcessLogger.getLogFile(processInstance.getId());
		if (logFile.exists()) {
			process.setLogFileName(logFile.getAbsolutePath());
		} else {
			process.setLogFileName("");
		}

		return process;
	}

	@Required
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}