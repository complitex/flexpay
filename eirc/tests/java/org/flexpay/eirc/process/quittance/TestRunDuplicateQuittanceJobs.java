package org.flexpay.eirc.process.quittance;

import org.flexpay.common.drools.utils.WorkItemCompleteLocker;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.GregorianCalendar;
import java.util.Map;

public class TestRunDuplicateQuittanceJobs extends EircSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("processManager")
	private ProcessManager processManager;

	@Test
	public void testGenerateQuittances() throws Throwable {

		Map<String, Object> contextVariables = CollectionUtils.map();

		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_FROM, new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_TILL, new GregorianCalendar(2008, 5, 30).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_SERVICE_ORGANIZATION_ID, 1L);
		contextVariables.put(GenerateQuittanceJob.PARAM_TOWN_ID, 2L);

		ProcessInstance p1 = processManager.startProcess("GenerateQuittances", contextVariables);
		ProcessInstance p2 = processManager.startProcess("GenerateQuittances", contextVariables);

		p1 = waitWhileProcessInstanceWillComplete(p1);
		p2 = waitWhileProcessInstanceWillComplete(p2);

		//assertEquals("First process failed", ProcessInstance.STATE, p1.getState());
		// Second process should be killed by voters
		//assertEquals("Second process failed", ProcessState.COMPLETED_WITH_ERRORS, p2);
	}

	@Test
	public void testGenerateQuittancesLinear() throws Throwable {

		Map<String, Object> contextVariables = CollectionUtils.map();

		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_FROM, new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_TILL, new GregorianCalendar(2008, 5, 30).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_SERVICE_ORGANIZATION_ID, 1L);
		contextVariables.put(GenerateQuittanceJob.PARAM_TOWN_ID, 2L);

		ProcessInstance p1 = processManager.startProcess("GenerateQuittances", contextVariables);

		p1 = waitWhileProcessInstanceWillComplete(p1);

		ProcessInstance p2 = processManager.startProcess("GenerateQuittances", contextVariables);

		p2 = waitWhileProcessInstanceWillComplete(p2);

		//assertEquals("First process failed", ProcessState.COMPLETED, p1.getState());
		//assertEquals("Second process failed", ProcessState.COMPLETED, p2.getState());
	}

	@Test
	public void testGenerateQuittances2() throws Throwable {

		Map<String, Object> contextVariables = CollectionUtils.map();

		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_FROM, new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_TILL, new GregorianCalendar(2008, 5, 30).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_SERVICE_ORGANIZATION_ID, 1L);
		contextVariables.put(GenerateQuittanceJob.PARAM_TOWN_ID, 2L);

		ProcessInstance p1 = processManager.startProcess("GenerateQuittances", contextVariables);
		ProcessInstance p2 = processManager.startProcess("GenerateQuittancePDF", contextVariables);

		p1 = waitWhileProcessInstanceWillComplete(p1);
		p2 = waitWhileProcessInstanceWillComplete(p2);

		//assertEquals("First process failed", ProcessState.COMPLETED, pw1.getProcess().getProcessState());

		// Second process should wait for the first completion
		//assertEquals("Second process failed", ProcessState.COMPLETED, pw2.getProcess().getProcessState());
	}

	private ProcessInstance waitWhileProcessInstanceWillComplete(ProcessInstance processInstance) throws InterruptedException {
		ProcessInstance pi = null;
		while(true) {
			WorkItemCompleteLocker.lock();
			log.debug("Get process instance: {}", processInstance.getId());
			try {
				pi = processManager.getProcessInstance(processInstance.getId());
			} catch (RuntimeException e) {
				log.error("RuntimeException", e);
				throw e;
			} finally {
				WorkItemCompleteLocker.unlock();
			}
			log.debug("Got process instance: {}", pi);
			if (pi.hasEnded()) {
				log.debug("End process instance");
				break;
			}
			Thread.sleep(500);
		}
		return pi;
	}
}
