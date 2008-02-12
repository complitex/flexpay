package org.flexpay.common.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import java.io.File;

/**
 * Wrapper to FlexPayLogger (FlexPay Wrapper Logger)
 */
public class FPLogger {
    
    /**
     * fully qualified class name
     */
    private static String FQCN = org.flexpay.common.logger.FPLogger.class.getName() + ".";

    /**
     * Debug level constant
     */
    public static final Priority DEBUG = Level.DEBUG;
    /**
     * Error level constant
     */
    public static final Priority ERROR = Level.ERROR;
    /**
     * Warning level constant
     */
    public static final Priority WARN = Level.WARN;
    /**
     * Information level constant
     */
    public static final Priority INFO = Level.INFO;
    /**
     * Fatal error level constant
     */
    public static final Priority FATAL = Level.FATAL;

    /**
     * Log Message
     *
     * @param level  message level
     * @param message message to log
     */
    public static void logMessage(Priority level, String message) {
        FlexPayLogger.getLogger().log(FQCN, level, message, null);
        
    }

    /**
     * Log message with FlexPayException stack trace
     *
     * @param level message level
     * @param e       exception
     * @param message message to log
     */
    public static void logMessage(Priority level, String message, Throwable e) {
        FlexPayLogger.getLogger().log(FQCN, level, message, e);
    }

    /**
     * Redirect log file to other file
     *
     * @param file new file
     * @return true if redirected success. If has error in create file that false
     */
    public static boolean redirectLogFile(File file) {
        return FlexPayLogger.redirectLogFile(file);
    }

}
