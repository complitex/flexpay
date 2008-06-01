package org.flexpay.common.process;

import java.util.Date;
import java.util.HashMap;
import java.io.Serializable;

public class Process {

    private long id;
    private String logFileName;
    private Date process_start_date;
    private Date process_end_date;
    private ProcessState processState;
    private String processDefinitionName;
    private long processInstaceId;
    private long processDefenitionVersion;

//    private ProcessStatus processStatus = new ProcessStatus();
    private HashMap <Serializable,Serializable> parameters = new HashMap<Serializable,Serializable>() ;

//    public process (long id){
    public Process (){
//        processStatus.setStart(new Date());
//        processStatus.setId(id);
//        processStatus.setState(ProcessState.WAITING);
//        this.process_start_date = new Date();
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

    public Date getProcess_start_date() {
        return process_start_date;
    }

    public void setProcess_start_date(Date process_start_date) {
        this.process_start_date = process_start_date;
    }

    public Date getProcess_end_date() {
        return process_end_date;
    }

    public void setProcess_end_date(Date process_end_date) {
        this.process_end_date = process_end_date;
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

    public HashMap<Serializable, Serializable> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<Serializable, Serializable> parameters) {
        this.parameters = parameters;
    }

    public ProcessState getProcessState() {
        if (processState == null){
            if (process_start_date == null){
                return ProcessState.WAITING;
            }else if (process_end_date == null){
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
