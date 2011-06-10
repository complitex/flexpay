package org.flexpay.common.action.jbpm;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestProcessesCleanupAction extends SpringBeanAwareTestCase {

	@Autowired
	private ProcessesCleanupAction action;
	@Autowired
	private ProcessManager processManager;

	@Test
	public void testFindProcessesToDelete() {
		/*
		Long count = (Long) jpaTemplate.execute(new JpaCallback<Object>() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {

				String name = action.getProcessNameFilter().getSelectedName();

				return entityManager.createNamedQuery("Process.listForDelete.count")
						.setParameter(0, action.getBeginDateFilter().getDate())
						.setParameter(1, action.getEndDateFilter().getDate())
						.setParameter(2, name)
						.setParameter(3, StringUtils.isEmpty(name) ? 1 : 0)
						.getSingleResult();
			}
		});

		assertNotNull("No count found", count);
		assertTrue("No processes found to delete", count > 0L);
		*/
	}

	@Test
	public void testCleanup() throws Exception {

		assertEquals("Invalid result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		Long processId = action.getProcessId();
		assertNotNull("No cleanup process created", processId);
		processManager.join(processId);

		ProcessInstance process = processManager.getProcessInstance(processId);
		assertNotNull("ProcessInstance not found", process);
		assertEquals("Invalid process state", ProcessInstance.STATE.ENDED, process.getState());
	}

	@Test
	public void testCleanupWithName() throws Exception {

		action.getProcessNameFilter().setSelectedId(1L);
		assertEquals("Invalid result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		Long processId = action.getProcessId();
		assertNotNull("No cleanup process created", processId);
		processManager.join(processId);

		ProcessInstance process = processManager.getProcessInstance(processId);
		assertNotNull("ProcessInstance not found", process);
		assertEquals("Invalid process state", ProcessInstance.STATE.ENDED, process.getState());
	}

	@Before
	public void runProcesses() throws Exception {
		/*
		processManager.deployProcessDefinition(
				getFileStream("org/flexpay/common/process/TestProcessDefinition.xml"), true);
		processManager.deployProcessDefinition(
				getFileStream("org/flexpay/common/process/TestProcessDefinition2.xml"), true);
              */
		ProcessInstance processInstance = processManager.startProcess("testProcessDefinition2", null);
		assertNotNull("Process testProcessDefinition2 did not start", processInstance);
		processManager.join(processInstance.getId());
		processInstance = processManager.startProcess("TestProcessDefinition", null);
		assertNotNull("Process TestProcessDefinition did not start", processInstance);
		processManager.join(processInstance.getId());
	}

	@Before
	public void prepareAction() {

		action.getBeginDateFilter().setStringDate("1988/01/01");
		action.getEndDateFilter().setStringDate("2030/01/01");
		// todo: subject to replace with predefined process name
//		action.getProcessNameFilter().setSelectedId(1L);
		action.setSubmitted("submitted");
	}
}
