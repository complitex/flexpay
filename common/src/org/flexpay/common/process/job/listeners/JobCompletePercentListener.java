package org.flexpay.common.process.job.listeners;

import org.flexpay.common.process.ContextCallback;
import org.flexpay.common.process.ProcessManagerImpl;
import org.flexpay.common.process.job.JobExecutionContext;
import org.flexpay.common.process.job.JobExecutionListener;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener that sets up job completion percent progress in process context variables
 */
public class JobCompletePercentListener implements JobExecutionListener {

	private static final Logger LOG = LoggerFactory.getLogger(JobCompletePercentListener.class);

	public static final String PARAM_COMPLETE_PERCENT = "COMPLETE_PERCENT";

	@Override
	public void jobStarted(final JobExecutionContext jobContext) {

		ProcessManagerImpl.getInstance().execute(new ContextCallback<Void>() {
			@Override
			public Void doInContext(@NotNull JbpmContext context) {
				TaskInstance instance = context.getTaskInstance(jobContext.getTaskId());
				if (instance == null) {
					LOG.warn("Task start requested, but not found {}", jobContext.getTaskId());
					return null;
				}

				instance.getProcessInstance().getContextInstance().setVariable(PARAM_COMPLETE_PERCENT, 0L);
				jobContext.setParameter(PARAM_COMPLETE_PERCENT, 0L);

				return null;
			}
		});
	}

	@Override
	public void jobProgressUpdated(final JobExecutionContext jobContext) {

		long total = jobContext.getTotalSize();
		long completed = jobContext.getComplete();

		final long percent = total <= 0 ? 0 : (100L * completed / total);
		Long paramPercent = (Long) jobContext.getParameter(PARAM_COMPLETE_PERCENT);
		if (paramPercent != null && percent == paramPercent) {
			return;
		}

		LOG.debug("Setting task #{} completion {}%", jobContext.getTaskId(), percent);

		// save percent only if value changed
		ProcessManagerImpl.getInstance().execute(new ContextCallback<Void>() {
			@Override
			public Void doInContext(@NotNull JbpmContext context) {
				TaskInstance instance = context.getTaskInstance(jobContext.getTaskId());
				if (instance == null) {
					LOG.warn("Task complete requested, but not found {}", jobContext.getTaskId());
					return null;
				}

				instance.getProcessInstance().getContextInstance().deleteVariable(PARAM_COMPLETE_PERCENT);
				jobContext.setParameter(PARAM_COMPLETE_PERCENT, percent);

				return null;
			}
		});
	}

	@Override
	public void jobCompleted(final JobExecutionContext jobContext) {

		ProcessManagerImpl.getInstance().execute(new ContextCallback<Void>() {
			@Override
			public Void doInContext(@NotNull JbpmContext context) {
				TaskInstance instance = context.getTaskInstance(jobContext.getTaskId());
				if (instance == null) {
					LOG.warn("Task complete requested, but not found {}", jobContext.getTaskId());
					return null;
				}

				instance.getProcessInstance().getContextInstance().deleteVariable(PARAM_COMPLETE_PERCENT);
				jobContext.removeParameter(PARAM_COMPLETE_PERCENT);

				return null;
			}
		});
	}
}
