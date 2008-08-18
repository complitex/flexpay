package org.flexpay.common.process;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.exception.JobConfigurationNotFoundException;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.job.JobManager;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ProcessDefinition;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestProcessManager extends SpringBeanAwareTestCase {

//    @Autowired
	//    private ProcessManager processManager;
//    volatile private static Thread thread = null;

	volatile private static String eventExecuted = "";
	private static final String EVENT_EXECUTED = "EVENT EXECUTED";
	volatile private static int counter = 0;
	//    private static final ProcessDefinition processDefinition = ProcessDefinition.parseXmlString(
	private static final String processDefinitionString =
			"<process-definition name=\"TestProcessDefinition\">" +
			"  <start-state>" +
			"    <transition to='s' />" +
			"  </start-state>" +
			"  <state name='s'>" +
			"    <transition to='end' />" +
			"  </state>" +
			"  <end-state name='end' />" +
			"</process-definition>";

	private static final String testProcessDefinition =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"\n" +
			"<process-definition name=\"testProcessDefinition2\"\n" +
			"    xmlns=\"http://jbpm.org/3/jpdl\"\n" +
			"    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
			"    xsi:schemaLocation=\"http://jbpm.org/3/jpdl http://jbpm.org/xsd/jpdl-3.0.xsd\">\n" +
			"\n" +
			"  <swimlane name=\"worker\">\n" +
			"    <assignment class=\"org.flexpay.common.process.JobManagerAssignmentHandler\" />\n" +
			"  </swimlane>\n" +
			"\n" +
			"\n" +
			"  <start-state name=\"start\">\n" +
			"    <transition to=\"MockJobFirst\" />\n" +
			"  </start-state>\n" +
			"\n" +
			"  <task-node name=\"MockJobFirst\">\n" +
			"    <task swimlane=\"worker\" blocking=\"yes\">\n" +
			"    </task>\n" +
			"    <transition name=\"next\" to=\"MockJobLast\" />\n" +
			"    <transition name=\"error\" to=\"end\" />\n" +
			"  </task-node>\n" +
			"\n" +
			"  <task-node name=\"MockJobLast\">\n" +
			"    <task swimlane=\"worker\" blocking=\"yes\">\n" +
//            "        <event type=\"task-end\">\n" +
//            "            <action class=\"org.flexpay.common.process.TestEvent\" >\n" +
//            "                <eventExecuted>EVENT EXECUTED</eventExecuted>\n" +
//            "            </action>\n" +
//            "          </event>\n" +
"    </task>\n" +
"    <transition name=\"next\" to=\"end\" />\n" +
"    <transition name=\"error\" to=\"end\" />\n" +
"            <event type=\"node-leave\" >\n" +
"                <action class=\"org.flexpay.common.process.TestEvent\" name=\"TestEvent\">\n" +
"                    <eventExecuted>EVENT EXECUTED</eventExecuted>\n" +
"                </action>\n" +
"            </event>" +
"  </task-node>" +
//            "  <task-node name=\"MockJobLast\">\n" +
//            "    <task swimlane=\"worker\" blocking=\"yes\">\n" +
//            "    </task>\n" +
//            "    <transition name=\"next\" to=\"end\" />\n" +
//            "    <transition name=\"error\" to=\"end\" />\n" +
//            "    <event type=\"node-leave\">\n" +
//            "        <action class=\"org.flexpay.common.process.TestEvent\" name=\"TestEvent\">\n" +
//            "            <eventExecuted>"+EVENT_EXECUTED+"</eventExecuted>\n" +
//            "        </action>\n" +
//            "      </event>\n" +
//            "  </task-node>\n" +
"\n" +
"  <end-state name=\"end\" />\n" +
"\n" +
"</process-definition>";

	@Autowired
	private JbpmConfiguration jbpmConfiguration;

	@Before
	public void setUp() {
		ProcessManager.getInstance().stopProcessManager();
		ProcessManager.unload();
		clearDB();
	}

	@After
	public void tearDown() {
		ProcessManager.getInstance().stopProcessManager();
//        try {
//            thread.join();
//        }
//        catch (InterruptedException e) {
//            Thread.interrupted(); // clear interrupded flag
//        }
		ProcessManager.unload();
		clearDB();
//        thread = null;
	}

	@Test
	public void testProcessManager() throws InterruptedException, JobConfigurationNotFoundException {

		new ProcessManagerConfiguration() {// ProcessManagerConfiguration pmc =

			{
				instance = this;
			}

			public String getJobClazzName(String jobName) throws JobConfigurationNotFoundException {
				return "org.flexpay.common.process.TestProcessManager$" + jobName;
			}
		};

		ProcessManager pm = new ProcessManager() {
			{
				instance = this;
			}

			public synchronized void jobFinished(Long taskId, HashMap<Serializable, Serializable> parameters, String transition) {
				super.jobFinished(taskId, parameters, transition);
				if (++counter >= 2) {
					stopProcessManager();
					instance = null;
				}
			}
		};
//        pmc.getJobClazzName("z");
		ProcessDefinition processDefinition = null;
		try {

			pm.setJbpmConfiguration(jbpmConfiguration);
			JobManager.getInstance();

			processDefinition = ProcessDefinition.parseXmlString(testProcessDefinition);
			ProcessManager.getInstance().deployProcessDefinition(processDefinition, true);


			pm.createProcess("testProcessDefinition2", new HashMap<Serializable, Serializable>());

			pm.run();

			assertEquals(2, counter);
			//@TODO why event class was not executed?
			assertEquals(EVENT_EXECUTED, eventExecuted);

		} catch (ProcessInstanceException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			fail();
		} catch (ProcessDefinitionException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			fail();
		} finally {
			if (processDefinition != null) {
				undeployTestProcessDefinition(processDefinition);
			}
		}

	}

	public JbpmConfiguration getJbpmConfiguration() {
		return jbpmConfiguration;
	}

	public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
		this.jbpmConfiguration = jbpmConfiguration;
	}

	@Test
	public void testDeploy() {
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlString(processDefinitionString);
		ProcessManager.getInstance().setJbpmConfiguration(jbpmConfiguration);
		ProcessManager.getInstance().deployProcessDefinition(processDefinition, true);

		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		List li = jbpmContext.getGraphSession().findAllProcessDefinitionVersions(processDefinition.getName());
		assertEquals(1, li.size());
		jbpmContext.close();
		undeployTestProcessDefinition(processDefinition);
	}

	@Test
	public void testCreateProcess() throws ProcessInstanceException, ProcessDefinitionException {
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlString(processDefinitionString);
		ProcessManager.getInstance().setJbpmConfiguration(jbpmConfiguration);
		ProcessManager.getInstance().deployProcessDefinition(processDefinition, true);
		ProcessManager.getInstance().createProcess("testProcessDefinition", new HashMap<Serializable, Serializable>());
		assertEquals(1, ProcessManager.getInstance().getProcessList().size());
		undeployTestProcessDefinition(processDefinition);
	}

	private void undeployTestProcessDefinition(ProcessDefinition processDefinition) {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		jbpmContext.getGraphSession().deleteProcessDefinition(processDefinition);
		jbpmContext.close();
	}


	public static class MockJobFirst extends Job {

		public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
//            counter++;
			return Job.RESULT_NEXT;
		}
	}

	public static class MockJobLast extends Job {

		public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
//            counter++;
			return Job.RESULT_NEXT;
		}
	}

	@Test
	@Ignore
	public void clearDB() {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		GraphSession graphSession = jbpmContext.getGraphSession();

		for (Object pd : graphSession.findAllProcessDefinitions()) {
			graphSession.deleteProcessDefinition((ProcessDefinition) pd);
		}
		jbpmContext.close();

	}

	public static String getEventExecuted() {
		return eventExecuted;
	}

	public static void setEventExecuted(String eventExecuted) {
		TestProcessManager.eventExecuted = eventExecuted;
	}
}
