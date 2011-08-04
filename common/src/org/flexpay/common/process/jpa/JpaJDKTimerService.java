package org.flexpay.common.process.jpa;

import org.drools.time.Job;
import org.drools.time.JobContext;
import org.drools.time.Trigger;
import org.flexpay.common.process.ProcessDefinitionManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class JpaJDKTimerService extends org.drools.persistence.jpa.JpaJDKTimerService {

	private static final Logger log = LoggerFactory.getLogger(JpaJDKTimerService.class);

	@Override
	protected Callable<Void> createCallableJob(Job job, JobContext ctx, Trigger trigger, JDKJobHandle handle, ScheduledThreadPoolExecutor scheduler) {
		return new JpaJDKCallableJob( job,
                ctx,
                trigger,
                handle,
                this.scheduler );
	}

	public class JpaJDKCallableJob extends org.drools.persistence.jpa.JpaJDKTimerService.JpaJDKCallableJob {

		public JpaJDKCallableJob(Job job,
                                 JobContext ctx,
                                 Trigger trigger,
                                 JDKJobHandle handle,
                                 ScheduledThreadPoolExecutor scheduler) {
            super(job, ctx, trigger, handle, scheduler);
        }

		@Override
		public Void call() throws Exception {
			waitWhileProcessDefinitionWillLoad();
			return super.call();
		}
	}

	private void waitWhileProcessDefinitionWillLoad() {
		while (!ProcessDefinitionManagerImpl.PROCESS_DEFINITIONS_LOADED) {
			log.debug("Wait while process definition manager will load process definitions. Sleep 5 sec.");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				log.debug("Interrupted signal to event");
			}
		}
	}
}
