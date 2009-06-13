package org.flexpay.payments.process.handlers;

import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;
import org.jbpm.graph.exe.ExecutionContext;
import org.flexpay.common.process.JobManagerAssignmentHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class AccounterAssignmentHandler implements AssignmentHandler {
	protected Logger log = LoggerFactory.getLogger(getClass());
    private static final long serialVersionUID = 1L;
    public final static String ACCOUNTER = "ACCOUNTER";
    public void assign(Assignable assignable, ExecutionContext executionContext) {
        String autoMode = (String) executionContext.getVariable("AUTO_MODE");
		log.debug("AUTO_MODE = {}", autoMode);
        if (autoMode != null && autoMode.equals("true")) {
            assignable.setActorId(JobManagerAssignmentHandler.JOB_MANAGER_ACTOR_NAME);
        } else {
            assignable.setActorId(ACCOUNTER);
        }
    }
}
