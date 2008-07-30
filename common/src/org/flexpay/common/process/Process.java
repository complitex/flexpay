package org.flexpay.common.process;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Process {

    private long id;
    private String logFileName;
    private Date processStartDate;
    private Date processEndDate;
    private ProcessState processState;
    private String processDefinitionName;
    private long processInstaceId;
    private long processDefenitionVersion;

//    private ProcessStatus processStatus = new ProcessStatus();
    private Map<Serializable,Serializable> parameters = new HashMap<Serializable,Serializable>() ;

//    public process (long id){
    public Process (){
//        processStatus.setStart(new Date());
//        processStatus.setId(id);
//        processStatus.setState(ProcessState.WAITING);
//        this.processStartDate = new Date();
//        this.processState = ProcessState.WAITING;
//        this.processDefinitionName = processDefinitionName;
    }

//    public synchronized void setParameter(String name, String value){
//        parameters.put(name,value);
//    }
//
//    public synchronized String getParameter(String name){
//        return parameters.get(name);
//    }

//    public ProcessStatus getProcessStatus() {
//        return processStatus;
//    }
//
//    public void setProcessStatus(ProcessStatus processStatus) {
//        this.processStatus = processStatus;
//    }

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
        if (processState == null){
            if (processStartDate == null){
                return ProcessState.WAITING;
            }else if (processEndDate == null){
                return ProcessState.RUNING;
            }else {
                return ProcessState.COMPLITED;
            }
        } else {
            return processState;
        }
    }

    protected void setProcessState(ProcessState processState) {
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
}
