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

		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource("WEB-INF/eirc/process/ParseRegistryProcess.xml");
        processManager.deployProcessDefinition(processDefinition, true);
    }

    @Test
	@NotTransactional
	public void testLoadProcessRegistryWorkflow() {
        ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource("WEB-INF/eirc/process/ProcessRegistryWorkflow.xml");
        processManager.deployProcessDefinition(processDefinition, true);
    }

    @Test
	@NotTransactional
	public void testLoadGenerateQuitancesWorkflow() {
        ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource("WEB-INF/eirc/process/GenerateQuitances.xml");
        processManager.deployProcessDefinition(processDefinition, true);
    }
    
    @Test
	@NotTransactional
	public void testLoadGenerateQuitancePDFWorkflow() {
        ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource("WEB-INF/eirc/process/GenerateQuitancePDF.xml");
        processManager.deployProcessDefinition(processDefinition, true);
    }
}
