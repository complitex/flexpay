package org.flexpay.eirc.process.quittance;

import org.flexpay.common.process.LyfecycleVote;
import org.flexpay.common.process.LyfecycleVoter;
import org.flexpay.common.process.ProcessHelper;
import org.flexpay.common.util.CollectionUtils;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * Quittance job voter cancells duplicates of GenerateQuittanceJob or GenerateQuittancesPDFJasperJob that are already in
 * progress or postpone them if parameters differ
 */
public class QuittanceJobVoter implements LyfecycleVoter {

	private ProcessHelper processHelper;

	/**
	 * Vote for task start
	 *
	 * @param instance TaskInstance to vote for
	 * @return LyfecycleVote
	 */
	@NotNull
	public LyfecycleVote onStart(@NotNull TaskInstance instance) {
		// allow to start non quittance jobs
		if (!GenerateQuittanceJob.JOB_NAME.equals(instance.getName()) &&
			!GenerateQuittancesPDFJasperJob.JOB_NAME.equals(instance.getName())) {
			return LyfecycleVote.START;
		}

		Set<String> taskNames = CollectionUtils.set(
				GenerateQuittanceJob.JOB_NAME,
				GenerateQuittancesPDFJasperJob.JOB_NAME);
		List<TaskInstance> running = processHelper.getRunningTasks(taskNames);
		if (running.isEmpty()) {
			return LyfecycleVote.START;
		}

		return LyfecycleVote.START;
	}

	/**
	 * Check if two set of parameters for jobs of this class are the same
	 *
	 * @param i1 Set of parameters of one job
	 * @param i2 Set of parameters of another job
	 * @return <code>true</code> if parameters are the same, or <code>false</code> otherwise
	 */
	@SuppressWarnings ({"unchecked"})
	public static boolean isSameParameters(TaskInstance i1, TaskInstance i2) {

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

	public void setProcessHelper(ProcessHelper processHelper) {
		this.processHelper = processHelper;
	}
}
