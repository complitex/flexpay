package org.flexpay.common.process;

import org.flexpay.common.exception.FlexPayException;

public class ProcessManagerConfiguration {

    public static final String CONFIG_FILE = "process_manager.cfg.xml";

    protected static ProcessManagerConfiguration instance = null;
    private static String connectionURL;
    private static String connectionUsername;
    private static String connectionPassword;

    private ProcessManagerConfiguration(){}

    public synchronized static ProcessManagerConfiguration getInstance(){
        if (instance == null) instance = new ProcessManagerConfiguration();
        return instance;
    }


    public String getJobClazzName(String jobName) throws FlexPayException {
       return "";

    }


    public static String getConnectionURL() {
        return connectionURL;
    }

    public static String getConnectionUsername() {
        return connectionUsername;
    }

    public static String getConnectionPassword() {
        return connectionPassword;
    }
}
