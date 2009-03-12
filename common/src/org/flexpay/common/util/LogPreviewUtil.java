package org.flexpay.common.util;

import org.apache.commons.io.FileUtils;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.util.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LogPreviewUtil {
    
    private static final Logger log = LoggerFactory.getLogger(LogPreviewUtil.class);
    
    /**
     * Return N last lines of log file for process with given id
     *
     * N is configured in application context by name logPreviewLinesNumber
     *
     * @param processId process id
     * @return N last lines of log file for process with given id
     * @throws IOException if an error faced while accessing log file
     */
    public static String getLogLastLines(long processId) throws IOException {
        File logFile = ProcessLogger.getLogFile(processId);

        if (null == logFile) {
            return null;
        }

        int logPreviewLinesNumber = ApplicationConfig.getLogPreviewLinesNumber();
        try {
            @SuppressWarnings ({"unchecked"})
			List<String> lines = (List<String>) FileUtils.readLines(logFile);
            int linesCount = lines.size();

            if (linesCount == 0) {
                return "";
            }

            if (linesCount > logPreviewLinesNumber) {
                for (int i = 0; i < linesCount - logPreviewLinesNumber; i++)
                    lines.remove(0);
            }

            StringBuilder result = new StringBuilder();
            for (String line : lines) {
                result.append(line);
                result.append('\n');
            }

            return result.toString();
        } catch (IOException e) {
            log.warn("Error opening log file {} for process #{}. Log preview will not be shown.", logFile.getAbsolutePath(), processId);
            return null;
        }
    }
}
