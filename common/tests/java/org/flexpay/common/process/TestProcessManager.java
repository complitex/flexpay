package org.flexpay.common.process;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import static org.junit.Assert.*;
import org.jbpm.JbpmConfiguration;

public class TestProcessManager extends SpringBeanAwareTestCase {

//    @Autowired
//    private ProcessManager processManager;
    volatile private static Thread thread = null;
//    @Autowired
//    private HibernateTemplate hibernateTemplate;
    @Autowired
    private JbpmConfiguration jbpmConfiguration;

//    private ProcessManager processManagerInstance;
    @Before
    public void setUp(){
        ProcessManager.getInstance().stop();
        ProcessManager.unload();
        ProcessManager.getInstance().setJbpmConfiguration(jbpmConfiguration);
        ProcessManager.getInstance().run();
        thread = new Thread(ProcessManager.getInstance(), "ProcessManagerThread");
        thread.start();
    }

    @After
    public void tearDown(){
        ProcessManager.getInstance().stop();
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            Thread.interrupted(); // clear interrupded flag
        }
        ProcessManager.unload();
        thread = null;
    }

    @Test
    public void testProcessManager() throws InterruptedException {
        Thread.sleep(10000);
    }

    public JbpmConfiguration getJbpmConfiguration() {
        return jbpmConfiguration;
    }

    public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
        this.jbpmConfiguration = jbpmConfiguration;
    }
}
