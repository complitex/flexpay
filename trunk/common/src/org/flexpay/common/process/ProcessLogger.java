package org.flexpay.common.process;

import org.apache.log4j.*;
import org.flexpay.common.util.config.ApplicationConfig;

import java.io.File;
import java.io.IOException;

public class ProcessLogger {

	private static Logger LOG = Logger.getLogger(ProcessLogger.class.getName());
	/**
	 * Appender name
	 */
	public static final String appenderName = "fileAppender";

	/**
	 * Default log level
	 */
	protected static final Level defaultLogLevel = Level.INFO;

	/**
	 * Default log file record pattern
	 */
	public static final String defaultPattern = "%d{dd.MM.yyyy HH:mm:ss}: [%c] %-5p - %m%n";

	/**
	 * Process log file name prefix
	 */
	public static final String logFileNamePrefix = "Process_";

	/**
	 * Process log file name prefix sufix
	 */

	public static final String logFileNameSufix = ".log";

	/**
	 * Create file appender
	 *
	 * @param processId process identifier
	 * @return log appender
	 */
	protected static Appender makeAppender(long processId) {
		try {
			File logFile = getLogFile(processId);

			if (!logFile.getParentFile().exists()) {
				logFile.getParentFile().mkdirs();
			}

			Appender appender = new FileAppender(new PatternLayout(getLogPattern()), logFile.getAbsolutePath());
			appender.setName(appenderName);

			return appender;
		} catch (IOException e) {
			LOG.error("Error creating Process Logger Appender for ProcessId=" + processId, e);
			return new org.apache.log4j.varia.NullAppender();
		}
	}

	/**
	 * Get log level
	 *
	 * @return logger Level
	 */
	protected static Level getLogLevel() {
		return defaultLogLevel;
	}

	/**
	 * Get log pattern
	 *
	 * @return log file
	 */
	protected static String getLogPattern() {
		return defaultPattern;
	}

	/**
	 * Get process log file
	 *
	 * @param processId process identifier
	 * @return log file
	 */
	public static File getLogFile(long processId) {
		return new File(getLogPath(), logFileNamePrefix+String.valueOf(processId)+logFileNameSufix);
	}

	/**
	 * Get process log path
	 *
	 * @return log path
	 */
	public static File getLogPath() {
		return ApplicationConfig.getProcessLogDirectory();
	}

	/**
	 * Remove process Log File
	 *
	 * @param processId process identifier
	 */
	public static synchronized void removeLogFile(long processId) {
		closeLog(processId);
		File logFile = getLogFile(processId);
		if (logFile.exists()) {
			logFile.delete();
		}
	}

	/**
	 * Close and remove logger appender (close log file)
	 *
	 * @param processId process identifier
	 */
	public static synchronized void closeLog(long processId) {
		Logger logger = Logger.getLogger(String.valueOf(processId));
		Appender appender = logger.getAppender(appenderName);
		if (appender != null) {
			appender.close();
			logger.removeAppender(appender);
		}
	}

	/**
	 * Get Logger by process ID
	 *
	 * @param processId process ID
	 * @return Logger
	 */
	private static Logger getLogger(long processId) {
		Logger logger = Logger.getLogger(String.valueOf(processId));
		if (logger.getAppender(ProcessLogger.appenderName) == null) {
			logger.addAppender(ProcessLogger.makeAppender(processId));
			logger.setLevel(ProcessLogger.getLogLevel());
		}
		return logger;
	}

}