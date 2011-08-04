package org.flexpay.payments.export;

import junit.framework.Assert;
import org.apache.commons.lang.time.DateUtils;
import org.drools.SessionConfiguration;
import org.drools.command.SingleSessionCommandService;
import org.drools.command.impl.CommandBasedStatefulKnowledgeSession;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.impl.StatefulKnowledgeSessionImpl;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.drools.util.ChainedProperties;
import org.drools.util.ClassLoaderUtil;
import org.drools.util.CompositeClassLoader;
import org.flexpay.common.drools.utils.WorkItemCompleteLocker;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.dao.WorkItemDao;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;
import org.flexpay.payments.util.impl.PaymentsTestOrganizationUtil;
import org.flexpay.payments.util.impl.PaymentsTestPaymentCollectorUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.*;
import java.sql.Time;
import java.util.*;

import static java.lang.Math.max;
import static junit.framework.Assert.assertTrue;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.payments.process.export.ExportJobParameterNames.*;
import static org.junit.Assert.assertNotNull;

public class TestTradingDay extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ProcessManager processManager;

	@Autowired
	private ProcessDefinitionManager processDefinitionManager;

	@Autowired
	private WorkItemDao workItemDao;

    @Resource(name="paymentsTestPaymentCollectorUtil")
    private PaymentsTestPaymentCollectorUtil paymentsTestPaymentCollectorUtil;

    @Resource(name="paymentsTestOrganizationUtil")
    private PaymentsTestOrganizationUtil organizationUtil;

    @Resource(name="paymentCollectorService")
    private PaymentCollectorService paymentCollectorService;

	@Resource(name="statefulKnowledgeSession")
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

	public void testPaymentCollectorTimeInterval() throws FlexPayExceptionContainer {
		// create organization
		Organization organization = organizationUtil.create("112");
        Assert.assertNotNull("Did not create register organization", organization);

		// create payment collector
		PaymentCollector paymentCollector = paymentsTestPaymentCollectorUtil.create(organization);

		EndTimeFilter endTimeFilter = new EndTimeFilter(false);
		endTimeFilter.setStringDate("01:00");
		paymentCollector.setTradingDayEndTime(new Time(endTimeFilter.getHours(), endTimeFilter.getMinutes(), endTimeFilter.getSeconds()));

		paymentCollectorService.update(paymentCollector);

		paymentCollector = paymentCollectorService.read(Stub.stub(paymentCollector));

		log.debug("Set payment collector end trading day: {}", paymentCollector.getTradingDayEndTime());

		endTimeFilter = new EndTimeFilter(paymentCollector.getTradingDayEndTime(), false);

		log.debug("Set time filter end trading day: {}", endTimeFilter.getStringDate());
	}

	private static final int HOURS = 10;
	private static final int MINUTES = 0;

	//@Test
	public void testFillSession() throws InterruptedException, IOException {
		/*
		log.debug("start fireAllRules");
		ksession.fireAllRules();
		Thread.sleep(5000);
		log.debug("end fireAllRules");
		for (org.drools.runtime.process.ProcessInstance processInstance : ksession.getProcessInstances()) {
			log.debug("Process instance: {}", processInstance);
		}
		for (ProcessInstance processInstance : processManager.getProcessInstances()) {
			log.debug("Process instance in log: {}", processInstance);
			WorkflowProcessInstance processRuntime = (WorkflowProcessInstance) ksession.getProcessInstance(processInstance.getId());
			log.debug("Process instance: {}", processRuntime);
			log.debug("Process timers {}", ((InternalProcessRuntime) processRuntime.getKnowledgeRuntime().getProcessRuntime()).getTimerManager().getTimers());
		}
		*/
		String fileName = System.getProperty("drools.session.conf");
		if (fileName == null) {
			fileName = System.getenv("drools.session.conf");
			System.setProperty("drools.session.conf", fileName);
		}
		File file = new File( fileName );
		log.debug("drools.session.conf={} ({}, {})", new Object[]{fileName, file.toURI(), file.exists()});

		log.debug("StatefulKnowledgesession class: {}", ((KnowledgeCommandContext) ((SingleSessionCommandService) ((CommandBasedStatefulKnowledgeSession) ksession).getCommandService()).getContext())
				.getStatefulKnowledgesession().getClass());

		StatefulKnowledgeSessionImpl session = (StatefulKnowledgeSessionImpl)((KnowledgeCommandContext) ((SingleSessionCommandService) ((CommandBasedStatefulKnowledgeSession) ksession).getCommandService()).getContext()).getStatefulKnowledgesession();
		log.debug("Signal manager factory class: {}", ((SessionConfiguration)session.getSessionConfiguration()).getSignalManagerFactory());
		log.debug("Timer service class: {}", ((SessionConfiguration)session.getSessionConfiguration()).newTimerService().getClass());
		log.debug("Work item manager factory class: {}", ((SessionConfiguration) session.getSessionConfiguration()).getWorkItemManagerFactory().getClass());

		List<Properties> props = new ArrayList<Properties>();
		CompositeClassLoader classLoader = ClassLoaderUtil.getClassLoader(null, SessionConfiguration.class, false);
		ChainedProperties chainedProperties = new ChainedProperties("session.conf", classLoader);
		ByteArrayOutputStream stream = new ByteArrayOutputStream(8000);
		ObjectOutput output = new ObjectOutputStream(stream);
		chainedProperties.writeExternal(output);
		log.debug("drools.session.conf: {}", stream.toString());

		Date now = new Date();
		Date endDate = DateUtils.addHours(DateUtil.truncateDay(now), HOURS);
		endDate = DateUtils.addMinutes(endDate, MINUTES);
		long duration = max(endDate.getTime() - now.getTime(), 0);
		Thread.sleep(duration);
	}

	@Test
	public void testTradingDay() throws ProcessDefinitionException, ProcessInstanceException, InterruptedException, FlexPayExceptionContainer {

		Date now = new Date();
		Date endDate = DateUtils.addMinutes(now, 3);
		//Date endDate = DateUtils.addHours(DateUtil.truncateDay(now), HOURS);
		//endDate = DateUtils.addMinutes(endDate, MINUTES);
		Time endTime = new Time(endDate.getTime());
		long duration = max(0L, endDate.getTime() - now.getTime());
		log.debug("Now date: {}, End date: {}, End time:{}, odds: {}", new Object[]{now, endDate, endTime, duration});
		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(1L));
		assertNotNull("Payment collector did not find", paymentCollector);
		paymentCollector.setTradingDayEndTime(endTime);
		paymentCollectorService.update(paymentCollector);

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
		processDefinitionManager.deployProcessDefinition("PaymentPointTradingDay", true);
		processDefinitionManager.deployProcessDefinition("PaymentCollectorTradingDay", true);

		System.out.println("Start process PaymentCollectorTradingDay");

		Map<String, Object> parameters = map();
		parameters.put(BEGIN_DATE, now);
		parameters.put(END_DATE, endDate);
		parameters.put(ORGANIZATION_ID, 4L);
		//parameters.put("paymentPointId", 1L);
		parameters.put("paymentCollectorId", 1L);
		//parameters.put("cashboxId", 1L);
		parameters.put(TRADING_DAY_END_DATE, endDate);
		//ProcessInstance processInstance = processManager.startProcess("PaymentPointTradingDay", parameters);
		ProcessInstance processInstance = processManager.startProcess("PaymentCollectorTradingDay", parameters);
		//ProcessInstance processInstance = processManager.startProcess("CashboxTradingDay", parameters);
		Assert.assertNotNull("Process did not start: Object is null", processInstance);
		Assert.assertNotNull("Process did not start: Process instance id is null", processInstance.getId());
		assertTrue("Process state is not running", processInstance.getState() == ProcessInstance.STATE.RUNNING);

		Thread.sleep(10000);

		/*processManager.signalExecution(processInstance, "Close");
		Thread.sleep(5000);
		processManager.signalExecution(processInstance, "Close");
		Thread.sleep(5000);
		processManager.signalExecution(processInstance, "Approve");
		Thread.sleep(5000);
		processManager.signalExecution(processInstance, "Approve");
		*/
		//Thread.sleep(duration);


		List<ProcessInstance> processInstances = processManager.getProcessInstances();
		for (ProcessInstance instance : processInstances) {

			if ("CashboxTradingDay".equals(instance.getProcessDefinitionId()) && instance.isRunning()) {
				log.debug("Send message to {}", instance);
				processManager.messageExecution(instance, "closeTradingDayByAdmin", "CloseByAdminMessage");
				Thread.sleep(500);
			}

		}

		/*
		log.debug("Context session: {}",
				((KnowledgeCommandContext) ((SingleSessionCommandService) ((CommandBasedStatefulKnowledgeSession) ksession).getCommandService()).getContext())
						.getStatefulKnowledgesession().getClass());
		for (ProcessInstance instance : processManager.getProcessInstances()) {
			log.debug("Process instance in log: {}", instance);
			WorkflowProcessInstance processRuntime = (WorkflowProcessInstance) ksession.getProcessInstance(instance.getId());
			if (processRuntime != null) {
				log.debug("Process instance: {}", processRuntime);
				for (NodeInstance nodeInstance : processRuntime.getNodeInstances()) {
					log.debug("Process node instance: {}", nodeInstance);
					if (nodeInstance instanceof CompositeContextNodeInstance) {
						log.debug("Process node CompositeContextNodeInstance instances: {}",
								((CompositeContextNodeInstance)nodeInstance).getNodeInstances());
						log.debug("Process timer instances ids: {}",
								((CompositeContextNodeInstance)nodeInstance).getTimerInstances());
						//for (Long timerId : ((CompositeContextNodeInstance)nodeInstance).getTimerInstances()) {
						log.debug("Process knowledge runtime: {}", ((CompositeContextNodeInstance)nodeInstance).
								getProcessInstance().getKnowledgeRuntime());
						//TimerManager timerManager = ((InternalProcessRuntime)((CompositeContextNodeInstance)nodeInstance).
						//		getProcessInstance().getKnowledgeRuntime().getProcessRuntime()).getTimerManager();
						//log.debug("Process timer instances: {}", timerManager.getTimers());
						//}
					}
				}
			}
		}

    */
		Thread.sleep(60000);

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
