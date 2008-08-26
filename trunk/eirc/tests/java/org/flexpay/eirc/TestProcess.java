package org.flexpay.eirc;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.jbpm.graph.def.ProcessDefinition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;


public class TestProcess extends SpringBeanAwareTestCase {

	protected ProcessManager processManager;

	@Autowired
	public void setProcessManager(@Qualifier ("processManager") ProcessManager processManager) {
		this.processManager = processManager;
	}

	@Test
	@NotTransactional
	public void testLoad() {

		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource(
				"WEB-INF/eirc/process/ParseRegistryProcess.xml");
		processManager.deployProcessDefinition(processDefinition, true);
	}

	@Test
	@NotTransactional
	public void testLoadProcessRegistryWorkflow() {
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource(
				"WEB-INF/eirc/process/ProcessRegistryWorkflow.xml");
		processManager.deployProcessDefinition(processDefinition, true);
	}

	@Test
	@NotTransactional
	public void testLoadProcessRegistryRecordsWorkflow() {
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource(
				"WEB-INF/eirc/process/ProcessRegistryRecordsWorkflow.xml");
		processManager.deployProcessDefinition(processDefinition, true);
	}

	@Test
	@NotTransactional
	public void testLoadGenerateQuitancesWorkflow() {
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource(
				"WEB-INF/eirc/process/GenerateQuittances.xml");
		processManager.deployProcessDefinition(processDefinition, true);
	}

	@Test
	@NotTransactional
	public void testLoadGenerateQuitancePDFWorkflow() {
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource(
				"WEB-INF/eirc/process/GenerateQuittancePDF.xml");
		processManager.deployProcessDefinition(processDefinition, true);
	}
}
