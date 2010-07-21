package org.flexpay.eirc.process.quittance;

import org.flexpay.common.process.LyfecycleVote;
import org.flexpay.common.process.LyfecycleVoter;
import org.flexpay.common.process.ProcessHelper;
import org.flexpay.common.util.CollectionUtils;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Quittance job voter cancells duplicates of GenerateQuittanceJob or GenerateQuittancesPDFJasperJob that are already in
 * progress or postpone them if parameters differ
 */
public class QuittanceJobVoter implements LyfecycleVoter {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private ProcessHelper processHelper;

	/**
	 * Vote for task start
	 *
	 * @param instance TaskInstance to vote for
	 * @return LyfecycleVote
	 */
	@NotNull
    @Override
	public LyfecycleVote onStart(@NotNull TaskInstance instance) {

		log.debug("Voting on task start: {}, pid={}", instance.getName(), instance.getId());

		// allow to start non quittance jobs
		if (!GenerateQuittanceJob.JOB_NAME.equals(instance.getName()) &&
			!GenerateQuittancesPDFJasperJob.JOB_NAME.equals(instance.getName())) {
			return LyfecycleVote.START;
		}

		Set<String> taskNames = CollectionUtils.set(
				GenerateQuittanceJob.JOB_NAME,
				GenerateQuittancesPDFJasperJob.JOB_NAME);
		List<TaskInstance> running = processHelper.getRunningTasks(taskNames);

		// no quittance tasks running start it
		if (running.isEmpty()) {
			return LyfecycleVote.START;
		}

		// check if there is a task with the same name and parameters and cancel it as a duplicate
		for (TaskInstance task : running) {
			if (instance.getName().equals(task.getName()) && isSameParameters(instance, task)) {
				return LyfecycleVote.CANCEL;
			}
		}

		// there is already a quittance task, postpone current candidate
		return LyfecycleVote.POSTPONE;
	}

	/**
	 * Check if two set of parameters for jobs of this class are the same
	 *
	 * @param i1 Set of parameters of one job
	 * @param i2 Set of parameters of another job
	 * @return <code>true</code> if parameters are the same, or <code>false</code> otherwise
	 */
	@SuppressWarnings ({"unchecked"})
	private boolean isSameParameters(TaskInstance i1, TaskInstance i2) {

		Collection<String> parameterNames = Collections.emptySet();
		if (GenerateQuittanceJob.JOB_NAME.equals(i1.getName())) {
			parameterNames = GenerateQuittanceJob.getParameterNames();
		} else if (GenerateQuittancesPDFJasperJob.JOB_NAME.equals(i1.getName())) {
			parameterNames = GenerateQuittancesPDFJasperJob.getParameterNames();
		}
		ProcessInstance pi1 = i1.getProcessInstance();
		ProcessInstance pi2 = i2.getProcessInstance();

		return CollectionUtils.isSame(parameterNames,
				pi1.getContextInstance().getVariables(),
				pi2.getContextInstance().getVariables());
	}

	/**
	 * Method setProcessHelper sets the processHelper of this QuittanceJobVoter object.
	 *
	 * @param processHelper the processHelper of this QuittanceJobVoter object.
	 */
    @Required
	public void setProcessHelper(ProcessHelper processHelper) {
		this.processHelper = processHelper;
	}

}
