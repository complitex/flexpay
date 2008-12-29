package org.flexpay.common.process;

import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;

/**
 * Listener 
 */
public interface LyfecycleVoter {

	/**
	 * Vote for task start
	 *
	 * @param instance TaskInstance to vote for
	 * @return LyfecycleVote
	 */
	@NotNull
	LyfecycleVote onStart(@NotNull TaskInstance instance);
}
