package org.flexpay.common.process;

import java.io.File;
import java.util.Date;

public class ProcessStatus {
    private File log;
    private Date start;
    private Date end;
    private long id;
    private ProcessState state;

    public File getLog() {
        return log;
    }

    public void setLog(File log) {
        this.log = log;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }
}
