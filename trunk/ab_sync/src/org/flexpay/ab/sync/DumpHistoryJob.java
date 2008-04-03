package org.flexpay.ab.sync;

import org.springframework.scheduling.quartz.QuartzJobBean;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.flexpay.ab.service.HistoryDumpService;
import org.apache.log4j.Logger;

import java.util.Date;

public class DumpHistoryJob extends QuartzJobBean {

	private Logger log = Logger.getLogger(getClass());
	private HistoryDumpService historyDumpService;

	/**
	 * Execute the actual job, i.e. run history records retrival
	 */
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if (log.isInfoEnabled()) {
			log.info("Starting dump history at " + new Date());
		}

		try {
			// do the job
			historyDumpService.dumpHistory();
		} catch (Exception e) {
			log.error("Dump history failed", e);
		}
	}

	public void setHistoryDumpService(HistoryDumpService historyDumpService) {
		this.historyDumpService = historyDumpService;
	}
}
