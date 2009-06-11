package org.flexpay.payments.process.handlers;

import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;
import org.jbpm.graph.exe.ExecutionContext;
import org.flexpay.common.process.JobManagerAssignmentHandler;


public class PaymentCollectorAssignmentHandler implements AssignmentHandler {
    private static final long serialVersionUID = 1L;
    public final static String PAYMENT_COLLECTOR = "PAYMENT_COLLECTOR";

    public void assign(Assignable assignable, ExecutionContext executionContext) {
        String autoMode = (String) executionContext.getVariable("AUTO_MODE");
        if (autoMode != null && autoMode.equals("true")) {
            assignable.setActorId(JobManagerAssignmentHandler.JOB_MANAGER_ACTOR_NAME);
        } else {
            assignable.setActorId(PAYMENT_COLLECTOR);
        }
    }
}
