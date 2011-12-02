package org.flexpay.common.process.audit;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.process.audit.dao.NodeInstanceLogDao;
import org.flexpay.common.process.audit.dao.ProcessInstanceLogDao;
import org.flexpay.common.process.audit.dao.VariableInstanceLogDao;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public class ProcessInstanceDbLogImpl implements ProcessInstanceDbLog {

	private static final Logger log = LoggerFactory.getLogger(ProcessInstanceDbLogImpl.class);

	private ProcessInstanceLogDao processInstanceLogDao;
	private NodeInstanceLogDao nodeInstanceLogDao;
	private VariableInstanceLogDao variableInstanceLogDao;

    @NotNull
	@Override
	public List<ProcessInstanceLog> findProcessInstances() {
    	return processInstanceLogDao.findProcessInstances();
    }

    @NotNull
	@Override
	public List<ProcessInstanceLog> findProcessInstances(@NotNull String processId) {
    	return processInstanceLogDao.findProcessInstances(processId);
    }

	@NotNull
	@Override
	public List<ProcessInstanceLog> findProcessInstances(ProcessSorter sorter, Page pager, Date startFrom, Date endBefore,
													  org.flexpay.common.process.persistence.ProcessInstance.STATE state, String name) {
		return processInstanceLogDao.findProcessInstances(sorter, pager, startFrom, endBefore, state, name);
	}

	@Override
	public ProcessInstanceLog findProcessInstance(long processInstanceId) {
		return processInstanceLogDao.findProcessInstance(processInstanceId);
    }

    @NotNull
	@Override
	public List<NodeInstanceLog> findNodeInstances(long processInstanceId) {
    	return nodeInstanceLogDao.findNodeInstances(processInstanceId);
    }

    @NotNull
	@Override
	public List<NodeInstanceLog> findNodeInstances(long processInstanceId, @NotNull String nodeId) {
    	return nodeInstanceLogDao.findNodeInstances(processInstanceId, nodeId);
    }

    @NotNull
	@Override
	public List<VariableInstanceLog> findVariableInstances(long processInstanceId) {
    	return variableInstanceLogDao.findVariableInstances(processInstanceId);
    }

    @NotNull
	@Override
	public List<VariableInstanceLog> findVariableInstances(long processInstanceId, @NotNull String variableId) {
    	return variableInstanceLogDao.findVariableInstances(processInstanceId, variableId);
    }

	@Override
	public void clear() {

    }

	@Required
	public void setProcessInstanceLogDao(ProcessInstanceLogDao processInstanceLogDao) {
		this.processInstanceLogDao = processInstanceLogDao;
	}

	@Required
	public void setNodeInstanceLogDao(NodeInstanceLogDao nodeInstanceLogDao) {
		this.nodeInstanceLogDao = nodeInstanceLogDao;
	}

	@Required
	public void setVariableInstanceLogDao(VariableInstanceLogDao variableInstanceLogDao) {
		this.variableInstanceLogDao = variableInstanceLogDao;
	}
}
