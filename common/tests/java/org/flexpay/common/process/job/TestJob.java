package org.flexpay.common.process.job;

import junit.framework.TestCase;

import java.util.HashMap;
import java.io.Serializable;

import org.flexpay.common.process.ProcessManager;


public class TestJob extends TestCase {
    private final static String TEST_STRING = "test string";
    public TestJob(String name) {
        super(name);
    }

    volatile boolean is_job_finished = false;

    public void setUp() throws Exception {
        this.is_job_finished = false;
        ProcessManager.unload();
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRun() throws Exception {
        ProcessManager pm = new ProcessManager() {
            {
                instance = this;
            }

            public void jobFinished(Long taskId, HashMap<Serializable, Serializable> parameters, String transition) {
                is_job_finished = true;
            }
        };

        Job testJob = new MockJobNext();
        testJob.setId("1");
        JobManager jobManager = JobManager.getInstance();
        HashMap<Serializable,Serializable> parameters = new HashMap<Serializable, Serializable>();
        parameters.put(TEST_STRING, TEST_STRING);
        jobManager.addJob(testJob, parameters);
        while (!is_job_finished) {
        }
        assertEquals(1, parameters.size());
        assertEquals(TEST_STRING, parameters.get(TEST_STRING));
    }


    public void testJobException() throws Exception {
        ProcessManager pm = new ProcessManager() {
            {
                instance = this;
            }
            public synchronized void jobFinished(Long taskId, HashMap<Serializable, Serializable> parameters, String transition) {
                is_job_finished = true;
                assertEquals(Job.ERROR, transition);
            }
        };

        Job testJob = new MockJobException();
        testJob.setId("1");
        JobManager jobManager = JobManager.getInstance();
        HashMap<Serializable,Serializable> parameters = new HashMap<Serializable, Serializable>();
        parameters.put(TEST_STRING, TEST_STRING);
        jobManager.addJob(testJob, parameters);
        while (!is_job_finished) {
        }
        parameters = testJob.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(TEST_STRING, parameters.get(TEST_STRING));
        assertEquals(Boolean.TRUE, testJob.getParameters().get(Job.STATUS_ERROR));
    }


    /**
     * Mock job for Normal job execution
     */
    public static class MockJobNext extends Job {
        public String execute(HashMap param) {
            assertTrue(true);
            assertEquals((String) param.get(TEST_STRING), TEST_STRING);
            return Job.NEXT;
        }
    }

    /**
     * Mock job for Job execution with exception
     */
    public static class MockJobException extends Job {
        public String execute(HashMap param) {
            assertTrue(true);
            throw new RuntimeException();
        }
    }
}
