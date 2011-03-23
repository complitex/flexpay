package org.flexpay.common.action.jbpm;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestProcessesCleanupAction extends SpringBeanAwareTestCase {

	@Autowired
	private ProcessesCleanupAction action;
	@Autowired
	private ProcessManager processManager;

	@Test
	public void testFindProcessesToDelete() {
		Long count = (Long) hibernateTemplate.execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String name = action.getProcessNameFilter().getSelectedName();

				return session.getNamedQuery("Process.listForDelete.count")
						.setParameter(0, action.getBeginDateFilter().getDate())
						.setParameter(1, action.getEndDateFilter().getDate())
						.setParameter(2, name)
						.setParameter(3, StringUtils.isEmpty(name) ? 1 : 0)
						.uniqueResult();
			}
		});

		assertNotNull("No count found", count);
		assertTrue("No processes found to delete", count > 0L);
	}

	@Test
	public void testCleanup() throws Exception {

		assertEquals("Invalid result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		Long processId = action.getProcessId();
		assertNotNull("No cleanup process created", processId);
		processManager.join(processId);

		org.flexpay.common.process.Process process = processManager.getProcessInstanceInfo(processId);
		assertNotNull("Process not found", process);
		assertEquals("Invalid process state", ProcessState.COMPLETED, process.getProcessState());
	}

	@Test
	public void testCleanupWithName() throws Exception {

		action.getProcessNameFilter().setSelectedId(1L);
		assertEquals("Invalid result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		Long processId = action.getProcessId();
		assertNotNull("No cleanup process created", processId);
		processManager.join(processId);

		org.flexpay.common.process.Process process = processManager.getProcessInstanceInfo(processId);
		assertNotNull("Process not found", process);
		assertEquals("Invalid process state", ProcessState.COMPLETED, process.getProcessState());
	}

	@Before
	public void runProcesses() throws Exception {

		processManager.deployProcessDefinition(
				getFileStream("org/flexpay/common/process/TestProcessDefinition.xml"), true);
		processManager.deployProcessDefinition(
				getFileStream("org/flexpay/common/process/TestProcessDefinition2.xml"), true);

		long pid = processManager.createProcess("testProcessDefinition2", null);
		processManager.join(pid);
		pid = processManager.createProcess("TestProcessDefinition", null);
		processManager.join(pid);
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
