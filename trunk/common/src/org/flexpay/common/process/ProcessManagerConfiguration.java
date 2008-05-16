package org.flexpay.common.process;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.exception.JobConfigurationNotFoundException;

public class ProcessManagerConfiguration {

    public static final String CONFIG_FILE = "process_manager.cfg.xml";

    protected static ProcessManagerConfiguration instance = null;
    private static String connectionURL;
    private static String connectionUsername;
    private static String connectionPassword;

    protected ProcessManagerConfiguration(){}

    public synchronized static ProcessManagerConfiguration getInstance(){
        if (instance == null) instance = new ProcessManagerConfiguration();
        return instance;
    }


    public String getJobClazzName(String jobName) throws JobConfigurationNotFoundException {
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
