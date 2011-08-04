package org.flexpay.common.process.audit;

import org.drools.WorkingMemory;
import org.drools.audit.WorkingMemoryLogger;
import org.drools.audit.event.LogEvent;
import org.drools.audit.event.RuleFlowLogEvent;
import org.drools.audit.event.RuleFlowNodeLogEvent;
import org.drools.audit.event.RuleFlowVariableLogEvent;
import org.drools.event.KnowledgeRuntimeEventManager;
import org.flexpay.common.process.audit.dao.NodeInstanceLogDao;
import org.flexpay.common.process.audit.dao.ProcessInstanceLogDao;
import org.flexpay.common.process.audit.dao.VariableInstanceLogDao;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

public class JPAWorkingMemoryDbLogger extends WorkingMemoryLogger {

	private static final int MAX_STRING_LENGTH = 4000;

	private ProcessInstanceLogDao processInstanceLogDao;
	private VariableInstanceLogDao variableInstanceLogDao;
	private NodeInstanceLogDao nodeInstanceLogDao;

	public JPAWorkingMemoryDbLogger(WorkingMemory workingMemory) {
        super(workingMemory);
    }

    public JPAWorkingMemoryDbLogger(KnowledgeRuntimeEventManager session) {
    	super(session);
    }

    public void logEventCreated(LogEvent logEvent) {
        switch (logEvent.getType()) {
            case LogEvent.BEFORE_RULEFLOW_CREATED:
                RuleFlowLogEvent processEvent = (RuleFlowLogEvent) logEvent;
                addProcessLog(processEvent.getProcessInstanceId(), processEvent.getProcessId());
                break;
            case LogEvent.AFTER_RULEFLOW_COMPLETED:
            	processEvent = (RuleFlowLogEvent) logEvent;
                endProcessLog(processEvent.getProcessInstanceId());
                break;
            case LogEvent.BEFORE_RULEFLOW_NODE_TRIGGERED:
            	RuleFlowNodeLogEvent nodeEvent = (RuleFlowNodeLogEvent) logEvent;
            	addNodeEnterLog(nodeEvent.getProcessInstanceId(), nodeEvent.getProcessId(), nodeEvent.getNodeInstanceId(), nodeEvent.getNodeId(), nodeEvent.getNodeName());
                break;
            case LogEvent.BEFORE_RULEFLOW_NODE_EXITED:
            	nodeEvent = (RuleFlowNodeLogEvent) logEvent;
            	addNodeExitLog(nodeEvent.getProcessInstanceId(), nodeEvent.getProcessId(), nodeEvent.getNodeInstanceId(), nodeEvent.getNodeId(), nodeEvent.getNodeName());
                break;
            case LogEvent.AFTER_VARIABLE_INSTANCE_CHANGED:
            	RuleFlowVariableLogEvent variableEvent = (RuleFlowVariableLogEvent) logEvent;
            	addVariableLog(variableEvent.getProcessInstanceId(), variableEvent.getProcessId(), variableEvent.getVariableInstanceId(), variableEvent.getVariableId(), variableEvent.getObjectToString());
                break;
            default:
                // ignore all other events
        }
    }

    public void addProcessLog(long processInstanceId, String processId) {
        ProcessInstanceLog log = new ProcessInstanceLog(processInstanceId, processId);
        processInstanceLogDao.create(log);
    }

    @SuppressWarnings("unchecked")
    public void endProcessLog(long processInstanceId) {
        ProcessInstanceLog processInstanceLog = processInstanceLogDao.findNotEndedProcessInstance(processInstanceId);
        if (processInstanceLog != null) {
            processInstanceLog.setEnd(new Date());
            processInstanceLogDao.update(processInstanceLog);
        }
    }

    public void addNodeEnterLog(long processInstanceId, String processId, String nodeInstanceId, String nodeId, String nodeName) {
        NodeInstanceLog log = new NodeInstanceLog(
    		NodeInstanceLog.TYPE_ENTER, processInstanceId, processId, nodeInstanceId, nodeId, nodeName);
        nodeInstanceLogDao.create(log);
    }

    public void addNodeExitLog(long processInstanceId,
            String processId, String nodeInstanceId, String nodeId, String nodeName) {
        NodeInstanceLog log = new NodeInstanceLog(
            NodeInstanceLog.TYPE_EXIT, processInstanceId, processId, nodeInstanceId, nodeId, nodeName);
        nodeInstanceLogDao.create(log);
    }

    public void addVariableLog(long processInstanceId, String processId, String variableInstanceId, String variableId, String objectToString) {
		VariableInstanceLog log = new VariableInstanceLog(
    		processInstanceId, processId, variableInstanceId, variableId, org.apache.commons.lang.StringUtils.left(objectToString, MAX_STRING_LENGTH));
        variableInstanceLogDao.create(log);
    }

	@Required
	public void setProcessInstanceLogDao(ProcessInstanceLogDao processInstanceLogDao) {
		this.processInstanceLogDao = processInstanceLogDao;
	}

	@Required
	public void setVariableInstanceLogDao(VariableInstanceLogDao variableInstanceLogDao) {
		this.variableInstanceLogDao = variableInstanceLogDao;
	}

	@Required
	public void setNodeInstanceLogDao(NodeInstanceLogDao nodeInstanceLogDao) {
		this.nodeInstanceLogDao = nodeInstanceLogDao;
	}
}
