package org.flexpay.common.process;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;

public class Process {

	/**
	 * Name of a parameter holding process instance id
	 */
	public static final String PROCESS_INSTANCE_ID = "ProcessInstanceID";

	private long id;
	private String logFileName;
	private Date processStartDate;
	private Date processEndDate;
	private ProcessState processState;
	private String processDefinitionName;
	private long processInstaceId;
	private long processDefenitionVersion;

	private Map<Serializable, Serializable> parameters = map();

	public Process() {
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public Date getProcessStartDate() {
		return processStartDate;
	}

	public void setProcessStartDate(Date processStartDate) {
		this.processStartDate = processStartDate;
	}

	public Date getProcessEndDate() {
		return processEndDate;
	}

	public void setProcessEndDate(Date processEndDate) {
		this.processEndDate = processEndDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public Map<Serializable, Serializable> getParameters() {
		return parameters;
	}

	public void setParameters(Map<Serializable, Serializable> parameters) {
		this.parameters = parameters;
	}

	public ProcessState getProcessState() {
		if (processState == null) {
			if (processStartDate == null) {
				return ProcessState.WAITING;
			} else if (processEndDate == null) {
				return ProcessState.RUNING;
			} else {
				return ProcessState.COMPLETED;
			}
		} else {
			return processState;
		}
	}

	public void setProcessState(ProcessState processState) {
		this.processState = processState;
	}

	public long getProcessInstaceId() {
		return processInstaceId;
	}

	public void setProcessInstaceId(long processInstaceId) {
		this.processInstaceId = processInstaceId;
	}

	public long getProcessDefenitionVersion() {
		return processDefenitionVersion;
	}

	public void setProcessDefenitionVersion(long processDefenitionVersion) {
		this.processDefenitionVersion = processDefenitionVersion;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("id", id).
                append("logFileName", logFileName).
                append("processStartDate", processStartDate).
                append("processEndDate", processEndDate).
                append("processState", processState).
                append("processDefinitionName", processDefinitionName).
                append("processInstaceId", processInstaceId).
                append("processDefenitionVersion", processDefenitionVersion).
                append("parameters", parameters).
                toString();
    }
}
