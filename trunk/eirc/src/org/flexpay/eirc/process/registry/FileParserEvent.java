package org.flexpay.eirc.process.registry;

import org.apache.log4j.Logger;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.sp.SpFileParser;
import org.flexpay.common.process.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

public class FileParserEvent extends ActionHandler{

    private SpFileParser parser;
    private SpFileService spFileService;
    private Logger log = Logger.getLogger(getClass());

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
