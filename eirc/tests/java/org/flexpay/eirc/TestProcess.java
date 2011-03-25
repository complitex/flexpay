package org.flexpay.eirc;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.jbpm.graph.def.ProcessDefinition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.transaction.BeforeTransaction;

public class TestProcess extends EircSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("processManager")
	protected ProcessManager processManager;

	@Test
	@BeforeTransaction
	public void testLoad() {

		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource(
				"WEB-INF/eirc/process/ParseRegistryProcess.xml");
		processManager.deployProcessDefinition(processDefinition, true);
	}

	@Test
    @BeforeTransaction
	public void testLoadProcessRegistryWorkflow() {
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource(
				"WEB-INF/eirc/process/ProcessRegistryWorkflow.xml");
		processManager.deployProcessDefinition(processDefinition, true);
	}

	@Test
    @BeforeTransaction
	public void testLoadProcessRegistryRecordsWorkflow() {
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource(
				"WEB-INF/eirc/process/ProcessRegistryRecordsWorkflow.xml");
		processManager.deployProcessDefinition(processDefinition, true);
	}

	@Test
    @BeforeTransaction
	public void testLoadGenerateQuittancesWorkflow() {
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource(
				"WEB-INF/eirc/process/GenerateQuittances.xml");
		processManager.deployProcessDefinition(processDefinition, true);
	}

	@Test
    @BeforeTransaction
	public void testLoadGenerateQuittancePDFWorkflow() {
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource(
				"WEB-INF/eirc/process/GenerateQuittancePDF.xml");
		processManager.deployProcessDefinition(processDefinition, true);
	}
}
