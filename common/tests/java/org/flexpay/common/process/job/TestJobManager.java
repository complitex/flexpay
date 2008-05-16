package org.flexpay.common.process.job;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessManagerConfiguration;
import org.flexpay.common.process.exception.JobConfigurationNotFoundException;
import org.flexpay.common.process.exception.JobClassNotFoundException;
import org.flexpay.common.process.exception.JobInstantiationException;
import junit.framework.TestCase;

import java.util.HashMap;
import java.io.Serializable;

public class TestJobManager extends TestCase {

    public static volatile boolean job_finished = false;
    public static volatile boolean job_waiting = true;
    public final static String TEST_STRING = "test string";


    public void setUp() throws Exception {
        job_finished = false;
        ProcessManager.unload();
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAddJob_Added() {
        try {
            new ProcessManagerConfiguration() {
                {
                    instance = this;
                }

                public String getJobClazzName(String jobName) throws JobConfigurationNotFoundException {
                    return "org.flexpay.common.process.job." + jobName;
                }
            };
            new ProcessManager() {
                {
                    instance = this;
                }

                public synchronized void jobFinished(Long taskId, HashMap<Serializable, Serializable> parameters, String transition) {
                    job_finished = true;
                    instance = null;
                }
            };

            HashMap<Serializable, Serializable> parameters = new HashMap <Serializable, Serializable>();
            JobManager jobManager = JobManager.getInstance();
            jobManager.addJob(1, 1, "MockJob", parameters);
            while (!job_finished) {
                assertEquals(1,jobManager.getJobList().size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testJobClassConfigurationNotFound(){
        try {
            new ProcessManagerConfiguration() {
                {
                    instance = this;
                }

                public String getJobClazzName(String jobName) throws JobConfigurationNotFoundException {
                    throw new JobConfigurationNotFoundException("configuration for " + jobName+" not found" );
//                    return "org.flexpay.common.process.job." + jobName;
                }
            };
            new ProcessManager() {
                {
                    instance = this;
                }

                public synchronized void jobFinished(Long taskId, HashMap<Serializable, Serializable> parameters, String transition) {
                    job_finished = true;
                    instance = null;
                }
            };
            JobManager jobManager = JobManager.getInstance();
            jobManager.addJob(1, 1, "--=No One Job has this long name=--", new HashMap<Serializable, Serializable>());
        } catch(JobConfigurationNotFoundException e){
            assertTrue(true);
        } catch (JobClassNotFoundException e) {
            fail();
        } catch (JobInstantiationException e) {
            fail();
        }
    }

        public void testJobClassNotFound(){
        try {
            new ProcessManagerConfiguration() {
                {
                    instance = this;
                }

                public String getJobClazzName(String jobName) throws JobConfigurationNotFoundException {
                    return "org.flexpay.common.process.job." + jobName;
                }
            };
            new ProcessManager() {
                {
                    instance = this;
                }

                public synchronized void jobFinished(Long taskId, HashMap<Serializable, Serializable> parameters, String transition) {
                    job_finished = true;
                    instance = null;
                }
            };
            JobManager jobManager = JobManager.getInstance();
            jobManager.addJob(1, 1, "--=No One Job has this long name=--", new HashMap<Serializable, Serializable>());
        } catch(JobConfigurationNotFoundException e){
            fail();
        } catch (JobClassNotFoundException e) {
            assertTrue(true);
        } catch (JobInstantiationException e) {
            fail();
        }
    }

     public void testJobInstantiation(){
        try {
            new ProcessManagerConfiguration() {
                {
                    instance = this;
                }

                public String getJobClazzName(String jobName) throws JobConfigurationNotFoundException {
                    return "org.flexpay.common.process.job." + jobName;
                }
            };
            new ProcessManager() {
                {
                    instance = this;
                }

                public synchronized void jobFinished(Long taskId, HashMap<Serializable, Serializable> parameters, String transition) {
                    job_finished = true;
                    instance = null;
                }
            };
            JobManager jobManager = JobManager.getInstance();
            jobManager.addJob(1, 1, "MockFakeJob", new HashMap<Serializable, Serializable>());
        } catch(JobConfigurationNotFoundException e){
            fail();
        } catch (JobClassNotFoundException e) {
            fail();
        } catch (JobInstantiationException e) {
            assertTrue(true);
        }
    }
}
