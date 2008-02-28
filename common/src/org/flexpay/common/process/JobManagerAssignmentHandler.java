package org.flexpay.common.process;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.exe.Assignable;

public class JobManagerAssignmentHandler {

    private static final long serialVersionUID = 1L;
    public final static String JOB_MANAGER_ACTOR_NAME = "FLEXPAY_JOB_MANAGER_GtY2FtE6VmH7sjGf7Gj1Okjg356yFGf45";
    public void assign(Assignable assignable, ExecutionContext executionContext) {
        assignable.setActorId(JOB_MANAGER_ACTOR_NAME);
    }
}
