package org.flexpay.common.logger;

import org.apache.log4j.*;
import org.apache.log4j.varia.NullAppender;
import org.flexpay.common.config.FlexPayConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Wrapper to Log4J
 */
public class FlexPayLogger {

    /**
     * name of logger
     */
    public static final String loggerName = "FlexPayLogger";
    /**
     * name of appender
     */
    public static final String appenderName = "FlexPayAppender";
    /**
     * Log file name
     */
    
    protected static final String defaultLogFileName = "FlexPay.log";
    /**
     * Path to Log file
     */
    public static final File defaultLogPath = new File(System.getProperty("java.io.tmpdir"));
    /**
     * Rolling DatePattern
     */
    public static final String defaultRollingPattern = "\'.\'yyyy-MM-dd";
    /**
     * Pattern of recort in log files
     */
    public static final String defaultPattern = "%d{dd.MM.yyyy HH:mm:ss}: [%c][%t] %-5p - %m%n";
    /**
     * Get instance of FlexPay Logger
     *
     * @return Log4J Logger
     */
    public static synchronized Logger getLogger() {
        Logger logger = Logger.getLogger(loggerName);
        if (logger.getAppender(appenderName) == null) {
            logger.addAppender(makeAppender(getLogFile()));
            logger.setLevel(getLogLevel());
        }
        return logger;
    }
    /**
     * Get instance of FlexPay Null Logger
     *
     * @return Log4J Logger
     */
    public static synchronized Logger getNullLogger() {
        Logger logger = Logger.getLogger("FlexPayNullLogger");
        if (logger.getAppender("FlexPayNullAppender") == null) {
            Appender a = new NullAppender();
            a.setName("FlexPayNullAppender");
            logger.addAppender(a);
        }
        return logger;
    }
    /**
     * Get log file from FlexPayConfiguration properties for FlexPay Logger
     *
     * @return log file
     */
    protected static File getLogFile() {
        File logFile;
            File logPath = FlexPayConfiguration.getInstance().getLogDirectory();
            if (!logPath.exists()) {
                logPath.mkdirs();
            }
            String logFileName = FlexPayConfiguration.getInstance().getLogFileName();
            logFile = new File(logPath,
                    (logFileName != null) ? logFileName : defaultLogFileName);
        return logFile;
    }

    /**
     * Get log file rolling pattern from FlexPayConfiguration properties for FlexPay Logger
     *
     * @return log file rolling pattern
     */
    protected static String getRollingPattern() {
        String pattern = defaultRollingPattern;
            if (FlexPayConfiguration.getInstance().getLogRollingPattern() != null) {
                pattern = FlexPayConfiguration.getInstance().getLogRollingPattern();
            }
        return pattern;
    }

    /**
     * Get log pattern from FlexPayConfiguration properties for FlexPay Logger
     *
     * @return log pattern
     */
    protected static String getLogPattern() {
        String pattern = defaultPattern;
            if (FlexPayConfiguration.getInstance().getLogPattern() != null) {
                pattern = FlexPayConfiguration.getInstance().getLogPattern();
            }
        return pattern;
    }

    /**
     * Get log level from FlexPayConfiguration properties for FlexPay Logger
     *
     * @return logger Level
     */
    protected static Level getLogLevel() {
        try {
            // Set logging level (from FlexPayConfiguration)
            return Level.toLevel(FlexPayConfiguration.getInstance().getLogLevel());
        } catch (Exception e) {
            System.err.println("Couldn't read log level from config file. It will be initialized to DEBUG.");
            return Level.DEBUG;
        }
    }

    /**
     * Create new logger appender
     *
     * @param logFile log file for creating appender
     * @return appender (File or Console)
     */
    protected static Appender makeAppender(File logFile) {
        Appender a;
        try {           
            a = new DailyRollingFileAppender(
                    new PatternLayout(getLogPattern()),
                    logFile.getAbsolutePath(),
                    getRollingPattern());
        } catch (IOException e) {
            System.err.println("Error creating file appender. Will be return console appender.");
            a = new ConsoleAppender(new PatternLayout(getLogPattern()));
        }
        a.setName(appenderName);
        return a;
    }

    /**
     * Redirect log file to other file
     *
     * @param file new file
     * @return true if redirected success. If has error in create file that false
     */
    public static boolean redirectLogFile(File file) {
        Logger logger = getLogger();
        Appender a = makeAppender(file);
        if (a instanceof FileAppender) {
            Appender oldApp = logger.getAppender(appenderName);
            oldApp.close();
            logger.removeAppender(oldApp);
            logger.addAppender(a);
            return true;
        }
        return false;
    }

}
