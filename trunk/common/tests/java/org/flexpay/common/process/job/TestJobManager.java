package org.flexpay.common.process.job;

import org.flexpay.common.process.exception.JobConfigurationNotFoundException;
import org.flexpay.common.process.exception.JobInstantiationException;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TestJobManager extends SpringBeanAwareTestCase {

	public final static String TEST_STRING = "test string";

	@Autowired
	private JobManager jobManager;

	@Test
	@Ignore
	public void testAddJobAdded() throws Exception {
		HashMap<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
		jobManager.addJob(1, 1, "MockJob", parameters);
		assertEquals("Incorrect", 1, jobManager.getJobList().size());
	}

	@Test (expected = JobConfigurationNotFoundException.class)
	@Ignore
	public void testJobClassConfigurationNotFound() throws Exception {
		jobManager.addJob(1, 1, "--=No One Job has this long name=--", new HashMap<Serializable, Serializable>());
	}

	@Test (expected = JobInstantiationException.class)
	@Ignore
	public void testJobInstantiation() throws Exception {
		jobManager.addJob(1, 1, "MockFakeJob", new HashMap<Serializable, Serializable>());
	}
}
