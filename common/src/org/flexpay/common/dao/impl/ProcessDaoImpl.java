package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.ProcessDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.util.CollectionUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.beans.factory.annotation.Required;
import org.jbpm.graph.exe.ProcessInstance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.Serializable;

/**
 * Hibernate-based implementation of {@link org.flexpay.common.dao.ProcessDao}
 */
public class ProcessDaoImpl implements ProcessDao {

	private HibernateTemplate hibernateTemplate;

	/**
	 * {@inheritDoc}
	 */	
	public List<Process> findProcesses(ProcessSorter sorter, final Page<Process> pager) {

		final StringBuilder hql = new StringBuilder("select distinct pi " +
													"from org.jbpm.graph.exe.ProcessInstance pi " +
													"left join fetch pi.processDefinition pd ");

		final StringBuilder cntHql = new StringBuilder("select count(pi) " +
													   "from org.jbpm.graph.exe.ProcessInstance pi");

		if (sorter != null) {
			StringBuilder orderByClause = new StringBuilder();
			sorter.setOrderBy(orderByClause);

			if (orderByClause.length() > 0) {
				hql.append(" ORDER BY ").append(orderByClause);
			}
		}

		List<ProcessInstance> instances;
		if (pager != null) {
			instances = getProcessInstancesPage(pager, hql, cntHql);
		} else {
			instances = getAllProcessesInstances(hql);
		}

		List<Process> processes = CollectionUtils.list();
		for (ProcessInstance processInstance : instances) {
			processes.add(getProcessInfo(processInstance.getId()));
		}

		return processes;
	}

	@SuppressWarnings ({"unchecked"})
	private List<ProcessInstance> getAllProcessesInstances(StringBuilder hql) {

		return (List<ProcessInstance>) hibernateTemplate.find(hql.toString());
	}

	@SuppressWarnings ({"unchecked"})
	private List<ProcessInstance> getProcessInstancesPage(final Page<Process> pager, final StringBuilder hql, final StringBuilder cntHql) {
		return (List<ProcessInstance>) hibernateTemplate.executeFind(new HibernateCallback() {
			public List<ProcessInstance> doInHibernate(Session session) throws HibernateException {
				Query cntQuery = session.createQuery(cntHql.toString());
				Long count = (Long) cntQuery.uniqueResult();
				pager.setTotalElements(count.intValue());

				return (List<ProcessInstance>) session.createQuery(hql.toString())
												.setFirstResult(pager.getThisPageFirstElementNumber())
												.setMaxResults(pager.getPageSize())
												.list();
			}
		});
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