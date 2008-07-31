package org.flexpay.eirc.process.registry;

import org.apache.log4j.Logger;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.sp.SpFileParser;
import org.flexpay.common.process.ActionHandler;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.exception.FlexPayException;
import org.jbpm.graph.exe.ExecutionContext;

import java.io.Serializable;
import java.util.HashMap;

public class FileParserEvent extends Job {

    private SpFileParser parser;
    private SpFileService spFileService;
    private Logger log = Logger.getLogger(getClass());

    public String execute(HashMap<Serializable, Serializable> parameters) throws FlexPayException {
        Long fileID = (Long) parameters.get("FileId");

        SpFile spFile = spFileService.read(fileID);
        if (spFile == null) {
            log.warn("Invalid File ID");
            return RESULT_ERROR;
        }
        try {
            parser.parse(spFile);
        } catch (Exception e) {
            log.warn("Parser exception", e);
            return RESULT_ERROR;
        }
        return RESULT_NEXT;

    }

    public void execute(ExecutionContext executionContext) throws Exception {
        Long fileID = (Long) executionContext.getVariable("FileId");

        SpFile spFile = spFileService.read(fileID);
        if (spFile == null) {
            log.warn("Invalid File ID");
            executionContext.leaveNode(RESULT_ERROR);
            return;
        }
        try {
            parser.parse(spFile);
        } catch (Exception e) {
            log.warn("Parser exception", e);
            executionContext.leaveNode(RESULT_ERROR);
            return;
        }
        executionContext.leaveNode(RESULT_NEXT);
    }

    public void setParser(SpFileParser parser) {
        this.parser = parser;
    }

    public void setSpFileService(SpFileService spFileService) {
        this.spFileService = spFileService;
    }

}
