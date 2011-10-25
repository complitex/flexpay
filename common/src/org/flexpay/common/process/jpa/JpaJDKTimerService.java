package org.flexpay.common.process.jpa;

import org.drools.time.*;
import org.drools.time.impl.TimerJobInstance;
import org.flexpay.common.process.ProcessDefinitionManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class JpaJDKTimerService extends org.drools.persistence.jpa.JpaJDKTimerService {

	private static final Logger log = LoggerFactory.getLogger(JpaJDKTimerService.class);

    private Map<Long, TimerJobInstance> timerInstances;

    public JpaJDKTimerService() {
        this( 1 );
        timerInstances = new ConcurrentHashMap<Long, TimerJobInstance>();
    }

    public JpaJDKTimerService(int size) {
        super( size );
        timerInstances = new ConcurrentHashMap<Long, TimerJobInstance>();
    }

	@Override
    protected Callable<Void> createCallableJob(Job job,
                                               JobContext ctx,
                                               Trigger trigger,
                                               JDKJobHandle handle,
                                               InternalSchedulerService scheduler) {
        JpaJDKCallableJob jobInstance = new JpaJDKCallableJob( new SelfRemovalJob( job ),
                                                               new SelfRemovalJobContext( ctx,
                                                                                          timerInstances ),
                                                               trigger,
                                                               handle,
                                                               scheduler );

        this.timerInstances.put( handle.getId(),
                                 jobInstance );
        return jobInstance;
    }

	@Override
    public Collection<TimerJobInstance> getTimerJobInstances() {
        return timerInstances.values();
    }

	public class JpaJDKCallableJob extends org.drools.persistence.jpa.JpaJDKTimerService.JpaJDKCallableJob {

		public JpaJDKCallableJob(Job job,
                                 JobContext ctx,
                                 Trigger trigger,
                                 JDKJobHandle handle,
                                 InternalSchedulerService scheduler) {
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
