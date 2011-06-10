package org.flexpay.common.process;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.flexpay.common.util.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ProcessLogger {

	private static final Logger LOG = LoggerFactory.getLogger(ProcessLogger.class);

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
	 * ProcessInstance log file name prefix
	 */
	public static final String logFileNamePrefix = "Process_";

	/**
	 * ProcessInstance log file name prefix sufix
	 */
	public static final String logFileNameSufix = ".log";

	private static final String categoryPrefix = "process.";

	private static final ThreadLocal<Long> processId = new ThreadLocal<Long>();

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
			LOG.error("Error creating ProcessInstance Logger Appender for ProcessId=" + processId, e);
			return new org.apache.log4j.varia.NullAppender();
		}
	}

	/**
	 * Get log level
	 *
	 * @return logger Level
	 */
	private static Level getLogLevel() {
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
		return new File(getLogPath(), logFileNamePrefix + String.valueOf(processId) + logFileNameSufix);
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
			LOG.debug("Removing process log file {}", logFile);
			logFile.delete();
		}
	}

	/**
	 * Close and remove logger appender (close log file)
	 *
	 * @param processId process identifier
	 */
	public static synchronized void closeLog(long processId) {

		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(categoryPrefix + processId);
		Appender appender = logger.getAppender(appenderName);
		if (appender != null) {
			appender.close();
			logger.removeAppender(appender);
		}
	}

	/**
	 * Get Logger by name and process ID
	 *
	 * @param name	  Logger name
	 * @param processId process ID
	 * @return Logger
	 */
	private static Logger getLogger(String name, long processId) {

		String logName = categoryPrefix + processId + "." + name;
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(logName);
		if (logger.getAppender(ProcessLogger.appenderName) == null) {
			logger.addAppender(ProcessLogger.makeAppender(processId));
			logger.setLevel(getLogLevel());
		}
		return LoggerFactory.getLogger(logName);
	}

	/**
	 * Associate current thread with a specified process.
	 * <p/>
	 * Later invoke of {@link #getLogger(String)} will use appropriate process id
	 *
	 * @param processId ProcessInstance ID
	 */
	public static void setThreadProcessId(Long processId) {
		ProcessLogger.processId.set(processId);
	}

	/**
	 * Get process logger with specified name
	 *
	 * @param name Logger name
	 * @return Logger
	 */
	public static Logger getLogger(String name) {

		if (processId.get() == null) {
			processId.set(0L);
			LOG.warn("Inproper API usage, ProcessInstance ID was not associated with a thread, using 0");
		}
		return getLogger(name, processId.get());
	}

	/**
	 * Get process logger for specified <code>clazz</code>
	 *
	 * @param clazz Logger class
	 * @return Logger
	 */
	public static Logger getLogger(Class<?> clazz) {

		return getLogger(clazz.getName());
	}
}
