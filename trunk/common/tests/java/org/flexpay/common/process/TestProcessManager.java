package org.flexpay.common.process;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import static org.junit.Assert.*;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;

import java.util.List;
import java.util.HashMap;
import java.io.Serializable;

public class TestProcessManager extends SpringBeanAwareTestCase {

//    @Autowired
//    private ProcessManager processManager;
    volatile private static Thread thread = null;

//    private static final ProcessDefinition processDefinition = ProcessDefinition.parseXmlString(
    private static final String processDefinitionString =
      "<process-definition name=\"TestProcessDefinition\">" +
      "  <start-state>" +
      "    <transition to='s' />" +
      "  </start-state>" +
      "  <state name='s'>" +
      "    <transition to='end' />" +
      "  </state>" +
      "  <end-state name='end' />" +
      "</process-definition>";


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

    @Test
    public void testDeploy(){
        ProcessDefinition processDefinition = ProcessDefinition.parseXmlString(processDefinitionString);
        ProcessManager.getInstance().deployProcessDefinition(processDefinition, true);

        JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
        List li = jbpmContext.getGraphSession().findAllProcessDefinitionVersions(processDefinition.getName());
        assertEquals(1,li.size());
        jbpmContext.close();
        undeployTestProcessDefinition(processDefinition);
    }

    @Test
    public void testCreateProcess() throws ProcessInstanceException, ProcessDefinitionException {
        ProcessDefinition processDefinition = ProcessDefinition.parseXmlString(processDefinitionString);
        ProcessManager.getInstance().deployProcessDefinition(processDefinition,true);
        ProcessManager.getInstance().createProcess("testProcessDefinition",new HashMap<Serializable,Serializable>());
        assertEquals(1,ProcessManager.getInstance().getProcessList().size());
        undeployTestProcessDefinition(processDefinition);
    }

    private void undeployTestProcessDefinition(ProcessDefinition processDefinition){
        JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
        jbpmContext.getGraphSession().deleteProcessDefinition(processDefinition);
        jbpmContext.close();
    }
}
