package org.flexpay.eirc;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.process.ProcessManager;
import org.junit.Test;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.jbpm.graph.def.ProcessDefinition;


public class TestProcess extends SpringBeanAwareTestCase {

    @Autowired
    protected ProcessManager processManager;

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
