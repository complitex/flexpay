package org.flexpay.common.process;

import java.util.Date;
import java.util.HashMap;

public class Process {

    private ProcessStatus processStatus = new ProcessStatus();
    private HashMap <String,String> parameters = new HashMap<String,String>() ; 

//    public process (long id){
    public Process (){
        processStatus.setStart(new Date());
//        processStatus.setId(id);
        processStatus.setState(ProcessState.WAITING);
    }

    public synchronized void setParameter(String name, String value){
        parameters.put(name,value);
    }

    public synchronized String getParameter(String name){
        return parameters.get(name);
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }
}
