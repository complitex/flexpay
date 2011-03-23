package org.flexpay.common.process.job;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;

import java.io.Serializable;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.junit.Assert.*;

public class TestJob extends SpringBeanAwareTestCase {

	private final static String TEST_STRING = "test string";

	@Test
	public void testRun() throws Exception {

		Job testJob = new MockJobNext();
		testJob.setId("1");
		JobManager jobManager = JobManager.getInstance();
		Map<Serializable, Serializable> parameters = map();
		parameters.put(TEST_STRING, TEST_STRING);
		jobManager.addJob(testJob, parameters);

		Thread jobThread = testJob.getJobThread();
		assertNotNull("No job thread", jobThread);
		jobThread.join(5 * 1000L);
		assertFalse("Job thread is not complete", jobThread.isAlive());

		assertEquals(1, parameters.size());
		assertEquals(TEST_STRING, parameters.get(TEST_STRING));
	}

	@Test
	public void testJobException() throws Exception {

		Job testJob = new MockJobException();
		testJob.setId("2");
		JobManager jobManager = JobManager.getInstance();
		Map<Serializable, Serializable> parameters = map();
		parameters.put(TEST_STRING, TEST_STRING);
		jobManager.addJob(testJob, parameters);

		Thread jobThread = testJob.getJobThread();
		assertNotNull("No job thread", jobThread);
		jobThread.join(5 * 1000L);
		assertFalse("Job thread is not complete", jobThread.isAlive());

		parameters = testJob.getParameters();
		assertEquals(2, parameters.size());
		assertEquals(TEST_STRING, parameters.get(TEST_STRING));
		assertEquals(Boolean.TRUE, testJob.getParameters().get(Job.STATUS_ERROR));
	}


	/**
	 * Mock job for Normal job execution
	 */
	public static class MockJobNext extends Job {
		public String execute(Map param) {
			assertEquals(param.get(TEST_STRING), TEST_STRING);
			return Job.RESULT_NEXT;
		}
	}

	/**
	 * Mock job for Job execution with exception
	 */
	public static class MockJobException extends Job {
		public String execute(Map param) {
			throw new RuntimeException();
		}
	}
}
