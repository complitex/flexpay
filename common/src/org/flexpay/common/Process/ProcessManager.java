package org.flexpay.common.Process;

public class ProcessManager {
    /**
     * singleton instance
     */
    private static ProcessManager instance;

    /**
     * protected constructor
     */
    protected ProcessManager(){

    }

    /**
     *
     * @return ProcessManager instance
     */
    public synchronized static ProcessManager getInstance(){
        if (instance == null){
            instance = new ProcessManager();
        }
        return instance;
    }

    /**
     *
     * @param id -  Process ID
     * @return ProcessStatus
     */
    public ProcessStatus getProcessStatus(int id){
        return new ProcessStatus();
    }
}
