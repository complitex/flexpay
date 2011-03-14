package org.flexpay.common.action;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.process.ProcessLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProcessLogDownloadServlet extends HttpServlet {

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Writes contents of log file with given process id to http response
     *
     * @param request http request. Must contain paramter named "processId"
     * @param response http response
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException in case of i/o error occurres on server or connection
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String processIdParam = request.getParameter("processId");
            if (null == processIdParam) {
                log.warn("No process id received");
                return;
            }

            Long processId = Long.parseLong(processIdParam);

            File logFile = ProcessLogger.getLogFile(processId);
            if (null == logFile) {
                log.warn("No log file was found for process#{}", processId);
                return;
            }

            log.info("Sending log file for process #{} to client", processId);

            writeFileToResponse(response, logFile);

            log.info("Log file for process #{} was successfully sent to client", processId);

        } finally {
            IOUtils.closeQuietly(response.getOutputStream());
        }
    }

    private void writeFileToResponse(HttpServletResponse response, File file) throws IOException {

        // setting header
        response.setContentType("multipart/form-data");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        // writing file contents to response
        InputStream in = new FileInputStream(file);
        IOUtils.copyLarge(in, response.getOutputStream());
        IOUtils.closeQuietly(in);
    }

}
