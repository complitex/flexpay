package org.flexpay.common.process;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;


public class TestEvent implements ActionHandler {
    public String eventExecuted;

    public void execute(ExecutionContext executionContext) throws Exception {
       TestProcessManager.setEventExecuted(eventExecuted);
        System.out.println(eventExecuted);
    }

    public String getEventExecuted() {
        return eventExecuted;
    }

    public void setEventExecuted(String eventExecuted) {
        this.eventExecuted = eventExecuted;
    }
}
