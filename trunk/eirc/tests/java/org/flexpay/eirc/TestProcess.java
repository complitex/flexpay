package org.flexpay.eirc;

import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.BeforeTransaction;

public class TestProcess extends EircSpringBeanAwareTestCase {

	@Autowired
	protected ProcessDefinitionManager processDefinitionManager;

	@Test
	@BeforeTransaction
	public void testLoad() throws ProcessDefinitionException {
		processDefinitionManager.deployProcessDefinition("ParseRegistry", true);
	}

	@Test
    @BeforeTransaction
	public void testLoadProcessRegistryWorkflow() throws ProcessDefinitionException {
		processDefinitionManager.deployProcessDefinition("ProcessRegistryWorkflow", true);
	}

	@Test
    @BeforeTransaction
	public void testLoadProcessRegistryRecordsWorkflow() throws ProcessDefinitionException {
		processDefinitionManager.deployProcessDefinition("ProcessRegistryRecordsWorkflow", true);
	}

	@Test
    @BeforeTransaction
	public void testLoadGenerateQuittancesWorkflow() throws ProcessDefinitionException {
		processDefinitionManager.deployProcessDefinition("GenerateQuittances", true);
	}

	@Test
    @BeforeTransaction
	public void testLoadGenerateQuittancePDFWorkflow() throws ProcessDefinitionException {
		processDefinitionManager.deployProcessDefinition("GenerateQuittancePDF", true);
	}
}
