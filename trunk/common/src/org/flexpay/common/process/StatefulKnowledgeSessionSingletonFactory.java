package org.flexpay.common.process;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.BPMN2ProcessFactory;
import org.drools.compiler.ProcessBuilderFactory;
import org.drools.io.ResourceFactory;
import org.drools.marshalling.impl.ProcessMarshallerFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessRuntimeFactory;
import org.jbpm.bpmn2.BPMN2ProcessProviderImpl;
import org.jbpm.marshalling.impl.ProcessMarshallerFactoryServiceImpl;
import org.jbpm.process.builder.ProcessBuilderFactoryServiceImpl;
import org.jbpm.process.instance.ProcessRuntimeFactoryServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class StatefulKnowledgeSessionSingletonFactory implements StatefulKnowledgeSessionFactory {

	private static ThreadLocal<StatefulKnowledgeSession> ksession = new ThreadLocal<StatefulKnowledgeSession>() {
		@Override
		protected StatefulKnowledgeSession initialValue() {
			return null;
		}
	};
	private String processDirectory;
	private AbstractPlatformTransactionManager transactionManager;

	@Override
	@NotNull
	public StatefulKnowledgeSession getSession() {
		if (ksession.get() == null) {
			ksession.set(newStatefulKnowledgeSession());
		}
		return ksession.get();
	}

	private StatefulKnowledgeSession newStatefulKnowledgeSession() {
		try {
			if (processDirectory == null) {
				throw new IllegalArgumentException("jbpm.console.directory property not found, did you set it?");
			}
			File file = new File(processDirectory);
			if (!file.exists()) {
				throw new IllegalArgumentException("Could not find " + processDirectory);
			}
			if (!file.isDirectory()) {
				throw new IllegalArgumentException(processDirectory + " is not a directory");
			}
			ProcessBuilderFactory.setProcessBuilderFactoryService(new ProcessBuilderFactoryServiceImpl());
			ProcessMarshallerFactory.setProcessMarshallerFactoryService(new ProcessMarshallerFactoryServiceImpl());
			ProcessRuntimeFactory.setProcessRuntimeFactoryService(new ProcessRuntimeFactoryServiceImpl());
			BPMN2ProcessFactory.setBPMN2ProcessProvider(new BPMN2ProcessProviderImpl());
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			for (File subfile: file.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.endsWith(".bpmn2") || name.endsWith(".bpmn");
					}})) {
				System.out.println("Loading process " + subfile.getName());
				kbuilder.add(ResourceFactory.newFileResource(subfile), ResourceType.BPMN2);
			}
			KnowledgeBase kbase = kbuilder.newKnowledgeBase();
			StatefulKnowledgeSession knowledgeSession;
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(
					"org.jbpm.persistence.jpa");
	        Environment env = KnowledgeBaseFactory.newEnvironment();
	        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
			env.set(EnvironmentName.TRANSACTION_MANAGER, transactionManager);
			Properties properties = new Properties();
			properties.put("drools.processInstanceManagerFactory", "org.jbpm.persistence.processinstance.JPAProcessInstanceManagerFactory");
			properties.put("drools.processSignalManagerFactory", "org.jbpm.persistence.processinstance.JPASignalManagerFactory");
			KnowledgeSessionConfiguration config = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(properties);
			try {
				System.out.println("Loading session data ...");
                knowledgeSession = JPAKnowledgeService.loadStatefulKnowledgeSession(
						1, kbase, config, env);
			} catch (RuntimeException e) {
				System.out.println("Error loading session data: " + e.getMessage());
				if (e instanceof IllegalStateException) {
				    Throwable cause = ((IllegalStateException) e).getCause();
				    if (cause instanceof InvocationTargetException) {
				        cause = cause.getCause();
	                    if (cause != null && "Could not find session data for id 1".equals(cause.getMessage())) {
	                        System.out.println("Creating new session data ...");
	                        knowledgeSession = JPAKnowledgeService.newStatefulKnowledgeSession(
	                            kbase, config, env);
	                    } else {
	                        System.err.println("Error loading session data: " + cause);
	                        throw e;
	                    }
				    } else {
                        System.err.println("Error loading session data: " + cause);
    					throw e;
    				}
				} else {
                    System.err.println("Error loading session data: " + e.getMessage());
                    throw e;
				}
			}
			//new WorkingMemoryDbLogger(knowledgeSession);
			/*
			CommandBasedWSHumanTaskHandler handler = new CommandBasedWSHumanTaskHandler(knowledgeSession);
			properties = new Properties();
			try {
				properties.load(ProcessJbpmDaoImpl.class.getResourceAsStream("/jbpm.console.properties"));
			} catch (IOException e) {
				throw new RuntimeException("Could not load jbpm.console.properties", e);
			}
			handler.setConnection(
				properties.getProperty("jbpm.console.task.service.host"),
				new Integer(properties.getProperty("jbpm.console.task.service.port")));
			knowledgeSession.getWorkItemManager().registerWorkItemHandler(
				"Human Task", handler);
			handler.connect();
			System.out.println("Successfully loaded default package from Guvnor");
			*/
			return knowledgeSession;
		} catch (Throwable t) {
			throw new RuntimeException(
				"Could not initialize stateful knowledge session: "
					+ t.getMessage(), t);
		}
	}

	@Required
	public void setProcessDirectory(String processDirectory) {
		this.processDirectory = processDirectory;
	}

	@Required
	public void setTransactionManager(AbstractPlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
