package org.flexpay.payments.process.handlers;

import org.flexpay.common.process.JobManagerAssignmentHandler;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccounterAssignmentHandler implements AssignmentHandler {

	protected Logger log = LoggerFactory.getLogger(getClass());

    public final static String ACCOUNTER = "ACCOUNTER";

	private static final long serialVersionUID = 1L;

    @Override
	public void assign(Assignable assignable, ExecutionContext executionContext) {
        String autoMode = (String) executionContext.getVariable(PaymentCollectorTradingDayConstants.AUTO_MODE);
		log.debug("AUTO_MODE = {}", autoMode);
        if (autoMode != null && autoMode.equals("true")) {
            assignable.setActorId(JobManagerAssignmentHandler.JOB_MANAGER_ACTOR_NAME);
        } else {
            assignable.setActorId(ACCOUNTER);
        }
    }

}
