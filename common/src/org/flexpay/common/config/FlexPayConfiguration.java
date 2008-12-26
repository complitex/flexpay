package org.flexpay.common.config;

import java.io.File;

public class FlexPayConfiguration {

    /**
     * Rolling DatePattern
     */
    public static final String defaultRollingPattern = "\'.\'yyyy-MM-dd";

    /**
     * Pattern of recort in log files
     */
    public static final String defaultPattern = "%d{dd.MM.yyyy HH:mm:ss}: [%c][%t] %-5p - %m%n";

    /**
     * Singlton pattern
      */
    private static FlexPayConfiguration instance;

    protected FlexPayConfiguration(){

    }

    public static FlexPayConfiguration getInstance(){
        if (instance == null){
            instance = new FlexPayConfiguration();
        }
        return instance;
    }

    public String getLogRollingPattern() {
        return defaultRollingPattern;
    }

    public String getLogPattern() {
        return defaultPattern;
    }

    public String getLogLevel() {
        return "DEBUG";
    }

    public File getLogDirectory() {
        return new File("flexpay/logs");
    }

    public String getLogFileName() {
        return "FlexPay.log";

    }

}
