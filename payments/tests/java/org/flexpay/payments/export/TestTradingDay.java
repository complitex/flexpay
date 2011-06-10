package org.flexpay.payments.export;

import junit.framework.Assert;
import org.apache.commons.lang.time.DateUtils;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.flexpay.common.drools.utils.WorkItemCompleteLocker;
import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.dao.WorkItemDao;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.util.DateUtil;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.max;
import static junit.framework.Assert.assertTrue;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.junit.Assert.assertNotNull;

public class TestTradingDay extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ProcessManager processManager;

	@Autowired
	private ProcessDefinitionManager processDefinitionManager;

	@Autowired
	private WorkItemDao workItemDao;

	@Resource(name = "statefulKnowledgeSession")
	private StatefulKnowledgeSession ksession;

	private static Random random = new Random();

	public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException {

		processDefinitionManager.deployProcessDefinition("TradingDaySchedulingJob", true);

		Map<String, Object> parameters = map();
		ProcessInstance processInstance = processManager.startProcess("TradingDaySchedulingJob", parameters);
		Assert.assertNotNull("Process did not start: Object is null", processInstance);
		Assert.assertNotNull("Process did not start: Process instance id is null", processInstance.getId());
		assertTrue("Process state is not running", processInstance.getState() == ProcessInstance.STATE.RUNNING);

		processManager.join(processInstance.getId());
		ProcessInstance process = processManager.getProcessInstance(processInstance.getId());
		assertNotNull("ProcessInstance not found", process);
		log.debug("ProcessInstance work: {}", process.getParameters().get(PaymentCollectorTradingDayConstants.PROCESS_STATUS));
	}

	@Test
	public void testTradingDay() throws ProcessDefinitionException, ProcessInstanceException, InterruptedException {

		Date now = new Date();
		Date endDate = DateUtils.addHours(DateUtil.truncateDay(now), 8);
		endDate = DateUtils.addMinutes(endDate, 10);
		long duration = max(0L, endDate.getTime() - now.getTime());
		log.debug("Now date: {}, End date: {}, odds: {}", new Object[]{now, endDate, duration});

		ksession.getWorkItemManager().registerWorkItemHandler("TestSleep", new WorkItemHandler() {

			@Override
			public void executeWorkItem(final WorkItem workItem, final WorkItemManager manager) {
				log.debug("Test work item: {}, process id: {}", workItem.getId(), workItem.getProcessInstanceId());
				new Thread(new Runnable() {

					@Override
					public void run() {
						log.debug("Test parameters: {}", workItem.getParameters());
						try {
							Thread.sleep(generateRandom(100));
						} catch (InterruptedException e) {
							log.error("Interrupted", e);
						}
						workItemDao.completeWorkItem(workItem.getId(), null);
					}
				}).start();
			}

			@Override
			public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

			}
		});

		processDefinitionManager.deployProcessDefinition("CashboxTradingDay", true);
		//processDefinitionManager.deployProcessDefinition("PaymentPointTradingDay", true);
		//processDefinitionManager.deployProcessDefinition("PaymentCollectorTradingDay", true);

		System.out.println("Start process PaymentCollectorTradingDay");

		Map<String, Object> parameters = map();
		//parameters.put("paymentPointId", 1L);
		parameters.put("paymentCollectorId", 1L);
		//parameters.put("cashboxId", 1L);
		//ProcessInstance processInstance = processManager.startProcess("PaymentPointTradingDay", parameters);
		ProcessInstance processInstance = processManager.startProcess("PaymentCollectorTradingDay", parameters);
		//ProcessInstance processInstance = processManager.startProcess("CashboxTradingDay", parameters);
		Assert.assertNotNull("Process did not start: Object is null", processInstance);
		Assert.assertNotNull("Process did not start: Process instance id is null", processInstance.getId());
		assertTrue("Process state is not running", processInstance.getState() == ProcessInstance.STATE.RUNNING);

		/*Thread.sleep(5000);
		processManager.signalExecution(processInstance, "Close");
		Thread.sleep(5000);
		processManager.signalExecution(processInstance, "Close");
		Thread.sleep(5000);
		processManager.signalExecution(processInstance, "Approve");
		Thread.sleep(5000);
		processManager.signalExecution(processInstance, "Approve");
		*/
		Thread.sleep(60000);

		/*
		List<ProcessInstance> processInstances = processManager.getProcessInstances();
		for (ProcessInstance instance : processInstances) {

			if ("CashboxTradingDay".equals(instance.getProcessDefinitionId()) && instance.isRunning()) {
				completeHumanTask(instance, "PaymentCollector", "Close");
				completeHumanTask(instance, "PaymentCollector", "Approve");
			}

		}
                 */
		Thread.sleep(duration + 60000);

		//waitWhileProcessInstanceWillComplete(processInstance);

		System.out.println("End process PaymentCollectorTradingDay");
	}

	private void completeHumanTask(ProcessInstance instance, String actorId, String result) throws InterruptedException {
		System.out.println("complete human task (processId=" + instance.getId() + ")");
		int i = 20;
		while (i > 0) {
			if (processManager.completeHumanTask(instance, actorId, result)) {
				break;
			} else {
				i--;
				Thread.sleep(200);
			}
		}
		assertTrue("Did not completed human task (process instance=" + instance.getId() + ", signal=" + result + ")", i > 0);
	}

	private ProcessInstance waitWhileProcessInstanceWillComplete(ProcessInstance processInstance) throws InterruptedException {
		ProcessInstance pi = null;
		int k = 0;
		while(k < 100) {
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
			k++;
		}
		return pi;
	}

	private static long generateRandom(long n) {
		return Math.abs(random.nextLong()) % n;
	}
}
