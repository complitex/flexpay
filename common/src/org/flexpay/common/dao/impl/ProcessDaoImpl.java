package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.ProcessDao;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.util.CollectionUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.beans.factory.annotation.Required;
import org.jbpm.graph.exe.ProcessInstance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.io.File;
import java.io.Serializable;

/**
 * Hibernate-based implementation of {@link org.flexpay.common.dao.ProcessDao}
 */
public class ProcessDaoImpl implements ProcessDao {

	private static final Logger log = LoggerFactory.getLogger(ProcessDaoImpl.class);

	private HibernateTemplate hibernateTemplate;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings ({"unchecked"})
	public List<Process> findAllProcesses(ProcessSorter sorter) {

		StringBuilder hql = new StringBuilder("select distinct pi " +
											  "from org.jbpm.graph.exe.ProcessInstance pi " +
											  "left join fetch pi.processDefinition pd ");

		if (sorter != null) {
			StringBuilder orderByClause = new StringBuilder();
			sorter.setOrderBy(orderByClause);

			if (orderByClause.length() > 0) {
				hql.append(" ORDER BY ").append(orderByClause);
			}
		}

		List<ProcessInstance> instances = (List<ProcessInstance>) hibernateTemplate.find(hql.toString());
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