package org.flexpay.eirc;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.process.ProcessManager;
import org.junit.Test;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.jbpm.graph.def.ProcessDefinition;


public class TestProcess extends SpringBeanAwareTestCase {

    @Autowired
    private ProcessManager processManager;

    @Test
	@NotTransactional
	public void testLoad() {
        ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource("WEB-INF/eirc/process/ParseRegistryProcess.xml");
        processManager.deployProcessDefinition(processDefinition, true);
    }

}
