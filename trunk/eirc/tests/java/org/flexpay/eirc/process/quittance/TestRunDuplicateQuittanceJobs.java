package org.flexpay.eirc.process.quittance;

import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Map;

import static org.junit.Assert.*;

public class TestRunDuplicateQuittanceJobs extends EircSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("processManager")
	private ProcessManager processManager;

	@Test
	public void testGenerateQuittances() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_FROM, new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_TILL, new GregorianCalendar(2008, 5, 30).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_SERVICE_ORGANIZATION_ID, 1L);
		contextVariables.put(GenerateQuittanceJob.PARAM_TOWN_ID, 2L);

		long p1Id = processManager.createProcess("GenerateQuittances", contextVariables);
		long p2Id = processManager.createProcess("GenerateQuittances", contextVariables);

		ProcessWaiter pw1 = new ProcessWaiter(p1Id, SecurityUtil.getAuthentication());
		ProcessWaiter pw2 = new ProcessWaiter(p2Id, SecurityUtil.getAuthentication());
		Thread thr1 = new Thread(pw1);
		Thread thr2 = new Thread(pw2);
		thr1.start();
		thr2.start();
		thr1.join();
		thr2.join();

		assertEquals("First process failed", ProcessState.COMPLETED, pw1.getProcess().getProcessState());
		// Second process should be killed by voters
		assertEquals("Second process failed", ProcessState.COMPLETED_WITH_ERRORS, pw2.getProcess().getProcessState());
	}

	@Test
	public void testGenerateQuittancesLinear() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_FROM, new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_TILL, new GregorianCalendar(2008, 5, 30).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_SERVICE_ORGANIZATION_ID, 1L);
		contextVariables.put(GenerateQuittanceJob.PARAM_TOWN_ID, 2L);

		long p1Id = processManager.createProcess("GenerateQuittances", contextVariables);

		ProcessWaiter pw1 = new ProcessWaiter(p1Id, SecurityUtil.getAuthentication());
		Thread thr1 = new Thread(pw1);
		thr1.start();
		thr1.join();

		long p2Id = processManager.createProcess("GenerateQuittances", contextVariables);
		ProcessWaiter pw2 = new ProcessWaiter(p2Id, SecurityUtil.getAuthentication());
		Thread thr2 = new Thread(pw2);
		thr2.start();
		thr2.join();

		assertEquals("First process failed", ProcessState.COMPLETED, pw1.getProcess().getProcessState());
		assertEquals("Second process failed", ProcessState.COMPLETED, pw2.getProcess().getProcessState());
	}

	@Test
	public void testGenerateQuittances2() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_FROM, new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_TILL, new GregorianCalendar(2008, 5, 30).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_SERVICE_ORGANIZATION_ID, 1L);
		contextVariables.put(GenerateQuittanceJob.PARAM_TOWN_ID, 2L);

		long p1Id = processManager.createProcess("GenerateQuittances", contextVariables);
		long p2Id = processManager.createProcess("GenerateQuittancePDF", contextVariables);

		ProcessWaiter pw1 = new ProcessWaiter(p1Id, SecurityUtil.getAuthentication());
		ProcessWaiter pw2 = new ProcessWaiter(p2Id, SecurityUtil.getAuthentication());
		Thread thr1 = new Thread(pw1);
		Thread thr2 = new Thread(pw2);
		thr1.start();
		thr2.start();
		thr1.join();
		thr2.join();

		assertEquals("First process failed", ProcessState.COMPLETED, pw1.getProcess().getProcessState());

		// Second process should wait for the first completion
		assertEquals("Second process failed", ProcessState.COMPLETED, pw2.getProcess().getProcessState());
	}

	private final class ProcessWaiter implements Runnable {

		private long pid;
		private Process process;
		private Authentication authentication;

		private ProcessWaiter(long pid, Authentication authentication) {
			this.pid = pid;
			this.authentication = authentication;
		}

        @Override
		public void run() {
			try {

				SecurityUtil.setAuthentication(authentication);

				processManager.join(pid);
				process = processManager.getProcessInstanceInfo(pid);
			} catch (Exception ex) {
				log.error("Failed getting process instance #" + pid, ex);
				fail(ex.getMessage());
			}

			assertNotNull("Process not found #" + pid, process);
		}

		public Process getProcess() {
			return process;
		}
	}
}
