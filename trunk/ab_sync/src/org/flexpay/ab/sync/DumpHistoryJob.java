package org.flexpay.ab.sync;

import org.flexpay.ab.service.HistoryDumpService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class DumpHistoryJob extends QuartzJobBean {

	private Logger log = LoggerFactory.getLogger(getClass());
	private HistoryDumpService historyDumpService;

	/**
	 * Execute the actual job, i.e. run history records retrival
	 */
    @Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		if (log.isInfoEnabled()) {
			log.info("Starting dump history at {}", new Date());
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
