package org.flexpay.common.process.job;

public class JobExecutionContextHolder {

	private static final ThreadLocal<JobExecutionContext> EXECUTION_CONTEXT = new ThreadLocal<JobExecutionContext>();

	public static void setContext(JobExecutionContext context) {
		EXECUTION_CONTEXT.set(context);
	}

	public static JobExecutionContext getContext() {
		return EXECUTION_CONTEXT.get();
	}
}
