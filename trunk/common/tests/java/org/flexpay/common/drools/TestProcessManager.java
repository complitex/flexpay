package org.flexpay.common.drools;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.drools.KnowledgeBase;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.definition.process.Process;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.drools.util.codec.Base64;
import org.flexpay.common.drools.utils.WorkItemCompleteLocker;
import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.dao.WorkItemDao;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.persistence.ProcessDefinition;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static junit.framework.Assert.*;
import static org.flexpay.common.util.CollectionUtils.list;

public class TestProcessManager extends SpringBeanAwareTestCase {

	@Autowired
	private ProcessManager processManager;

	@Autowired
	private ProcessDefinitionManager processDefinitionManager;

	@Autowired
	private WorkItemDao workItemDao;

	@Resource(name = "statefulKnowledgeSession")
	private StatefulKnowledgeSession ksession;

	public void testStartProcess() {
		org.drools.runtime.process.ProcessInstance processInstance = ksession.startProcess("UserTask", CollectionUtils.<String, Object>map());
		assertTrue(processInstance.getState() == org.drools.runtime.process.ProcessInstance.STATE_ACTIVE);

		org.drools.runtime.process.ProcessInstance processInstance2 = ksession.startProcess("UserTask", CollectionUtils.<String, Object>map());
		assertTrue(processInstance2.getState() == org.drools.runtime.process.ProcessInstance.STATE_ACTIVE);

		ProcessInstance pi = processManager.getProcessInstance(processInstance.getId());
		assertTrue("Process instance execute human task", processManager.isHumanTaskExecute(pi));
		assertTrue("Human task did not complete for user", processManager.completeHumanTask(pi, "john", null));

		ProcessInstance pi2 = processManager.getProcessInstance(processInstance2.getId());
		assertTrue("Process instance execute human task", processManager.isHumanTaskExecute(pi2));
		assertTrue("Human task did not complete for user", processManager.completeHumanTask(pi2, "luck", null));

		/*
		HumanTaskHandler taskHandler = null;

		for (Map.Entry<String, WorkItemHandler> entry : ((SessionConfiguration) ksession.getSessionConfiguration()).getWorkItemHandlers().entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue().toString());
			if (entry.getValue() instanceof HumanTaskHandler) {
				taskHandler = (HumanTaskHandler) entry.getValue();
			}
		}
		assertNotNull("Human task handler did not find", taskHandler);
		for (Map.Entry<Long, WorkItem> workItemWorkItemManagerEntry : taskHandler.getWorkItems().entrySet()) {
			System.out.println("process instance id=" + workItemWorkItemManagerEntry.getKey() +
					", actor id=" + workItemWorkItemManagerEntry.getValue().getParameter("ActorId"));
		}

		assertTrue("Process instance 1 did not have work item", taskHandler.getWorkItems().containsKey(processInstance.getId()));
		assertTrue("Process instance 2 did not have work item", taskHandler.getWorkItems().containsKey(processInstance2.getId()));

		System.out.println("Execeute work item manager class: " + ksession.getWorkItemManager().getClass());

		for (Map.Entry<Long, WorkItem> workItemWorkItemManagerEntry : taskHandler.getWorkItems().entrySet()) {
			ksession.getWorkItemManager().completeWorkItem(workItemWorkItemManagerEntry.getKey(), null);
		}
              */
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertProcessInstanceCompleted(processInstance2.getId(), ksession);
	}

	public void testStartProcess2() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException {
		/*for (Process process : ksession.getKnowledgeBase().getProcesses()) {
			System.out.println("Loading process from Guvnor: " + process.getId());
		}
		for (ProcessDefinition definition : processDefinitionManager.getProcessDefinitions()) {
			System.out.println("Process definition: " + definition.getName() + " (" + definition.getVersion() + ")");
		}
		*/

		ProcessInstance processInstance = processManager.startProcess("UserTask", CollectionUtils.<String, Object>map());

		assertNotNull("Process did not start: Object is null", processInstance);
		assertNotNull("Process did not start: Process instance id is null", processInstance.getId());
		assertTrue("Process state is not running", processInstance.getState() == ProcessInstance.STATE.RUNNING);

		System.out.println("Process instance id: " + processInstance.getId() + ", process definition version: " + processInstance.getProcessDefenitionVersion());

		assertTrue("Process instance execute human task", processManager.isHumanTaskExecute(processInstance));
		assertTrue("Human task did not complete for user", processManager.completeHumanTask(processInstance, "john", null));
		assertProcessInstanceCompleted(processInstance.getId(), ksession);

		/*
		for (KnowledgePackage kpackage : ksession.getKnowledgeBase().getKnowledgePackages()) {
			System.out.println("kpackage: " + kpackage.getName());
			for (Process process : kpackage.getProcesses()) {
				System.out.println("process: " + process.getId());
			}
			String fileName = kpackage.getName() + ".pkg";
			write((KnowledgePackageImp) kpackage, fileName);
			postFile(fileName);
		}
		*/
	}

	public void testStartProcess3() throws ProcessInstanceException, ProcessDefinitionException {
		KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("Guvnor default");
		kagent.applyChangeSet(ResourceFactory.newClassPathResource("definitions/ChangeSet.xml"));
		kagent.monitorResourceChangeEvents(false);
		KnowledgeBase kbase = kagent.getKnowledgeBase();
		for (Process process : kbase.getProcesses()) {
			System.out.println("Loading process from Guvnor: " + process.getName() + ", version: " + process.getVersion());
		}

		StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
		org.drools.runtime.process.ProcessInstance processInstance2 = session.startProcess("UserTask", CollectionUtils.<String, Object>map());

		System.out.println("Drools process instance id: " + processInstance2.getId() + ", process definition version: " + processInstance2.getProcess().getVersion());

		ProcessInstance processInstance = processManager.getProcessInstance(processInstance2.getId());

		assertNotNull("Process did not start: Object is null", processInstance);
		assertNotNull("Process did not start: Process instance id is null", processInstance.getId());
		//assertTrue("Process state is not running", processInstance.getState() == ProcessInstance.STATE.RUNNING);

		System.out.println("Process instance id: " + processInstance.getId() + ", process definition version: " + processInstance.getProcessDefinitionId());

		assertTrue("Process instance execute human task", processManager.isHumanTaskExecute(processInstance));
		assertTrue("Human task did not complete for user", processManager.completeHumanTask(processInstance, "john", null));
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
	}

	@Transactional
	public void testTask() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException, MalformedURLException {

		String fileName = System.getProperty("drools.session.conf");
		File file = new File( fileName );
		log.debug("drools.session.conf={} ({}, {})", new Object[]{fileName, file.toURI(), file.exists()});

		final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

		ksession.getWorkItemManager().registerWorkItemHandler("MyTask", new WorkItemHandler() {

			@Override
			public void executeWorkItem(final WorkItem workItem, final WorkItemManager manager) {
				log.debug("Test work item: {}, process id: {}", workItem.getId(), workItem.getProcessInstanceId());
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							log.debug("sleep thread {} (process = {})", Thread.currentThread().getId(), workItem.getProcessInstanceId());
							Thread.sleep(20000);
							log.debug("wake up thread {} (process = {})", Thread.currentThread().getId(), workItem.getProcessInstanceId());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						for (Map.Entry<String, Object> entry : workItem.getParameters().entrySet()) {
							log.debug("Param: {}={}", entry.getKey(), entry.getValue());
						}
						Map<String, Object> results = new HashMap<String, Object>();
						if (StringUtils.equals("value2", (String) workItem.getParameter("inputParam"))) {
							results.put("Result", "NEXT");
						} else {
							results.put("Result", "ERROR");
						}
						log.debug("Complete work item {}, result {}", workItem.getId(), results);
						workItemDao.completeWorkItem(workItem.getId(), results);
						log.debug("Completed work item {}", workItem.getId());
					}
				}).start();
			}

			@Override
			public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

			}
		});

		processDefinitionManager.deployProcessDefinition("TestWorkHandler2", true);
		System.out.println("deploy process definitiversion=" + processDefinitionManager.getProcessDefinition("TestWorkHandler2").getVersion());


		Map<String, Object> params = CollectionUtils.map();
		params.put("inputParam", "value");
		ProcessInstance processInstance = processManager.startProcess("TestWorkHandler2", params);
		System.out.println("Process instance started: " + processInstance.getId());

		params = CollectionUtils.map();
		params.put("inputParam", "value2");
		ProcessInstance processInstance2 = processManager.startProcess("TestWorkHandler2", params);
		System.out.println("Process instance started: " + processInstance2.getId());

		assertNotNull("Process did not start: Object is null", processInstance);
		assertNotNull("Process did not start: Process instance id is null", processInstance.getId());


		ProcessInstance pi;
		while(true) {
			WorkItemCompleteLocker.lock();
			log.debug("Get process instance1: {}", processInstance.getId());
			try {
				pi = processManager.getProcessInstance(processInstance.getId());
			} catch (RuntimeException e) {
				log.error("RuntimeException", e);
				throw e;
			} finally {
				WorkItemCompleteLocker.unlock();
			}
			log.debug("Got process instance1: {}", pi);
			if (pi.hasEnded()) {
				log.debug("End process instance");

				break;
			}
			Thread.sleep(500);
		}
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		while(true) {
			WorkItemCompleteLocker.lock();
			log.debug("Get process instance2: {}", processInstance2.getId());
			try {
				pi = processManager.getProcessInstance(processInstance2.getId());
			} catch (RuntimeException e) {
				log.error("RuntimeException", e);
				throw e;
			} finally {
				WorkItemCompleteLocker.unlock();
			}
			log.debug("Got process instance2: {}", pi);
			if (pi.hasEnded()) {
				log.debug("End process instance");
				break;
			}
			Thread.sleep(500);
		}
		assertProcessInstanceCompleted(processInstance2.getId(), ksession);
	}

	public void testMultiInstance() throws ProcessDefinitionException, ProcessInstanceException, InterruptedException {

		final Set<Long> childProcesses = Collections.synchronizedSet(new HashSet<Long>());

		ksession.getWorkItemManager().registerWorkItemHandler("MyTask", new WorkItemHandler() {

			private Long sleepTime = 1000L;

			@Override
			public void executeWorkItem(final WorkItem workItem, final WorkItemManager manager) {
				log.debug("Test work item: {}, process id: {}", workItem.getId(), workItem.getProcessInstanceId());
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (StringUtils.isNotEmpty((String)workItem.getParameter("inputParam"))) {
							try {
								long time;
								synchronized (sleepTime) {
									time = sleepTime;
									sleepTime += 500L;
								}
								log.debug("sleep thread {} on time {} (process = {})",
										new Object[]{Thread.currentThread().getId(), time, workItem.getProcessInstanceId()});
								Thread.sleep(time);
								log.debug("wake up thread {} (process = {})", Thread.currentThread().getId(), workItem.getProcessInstanceId());
							} catch (Exception e) {
								log.debug("Exception by sleep work item", e);
							}
						}
						for (Map.Entry<String, Object> entry : workItem.getParameters().entrySet()) {
							log.debug("Param: {}={}", entry.getKey(), entry.getValue());
						}
						Map<String, Object> results = new HashMap<String, Object>();
						if (StringUtils.equals("value2", (String) workItem.getParameter("inputParam"))) {
							results.put("Result", "NEXT");
						} else {
							results.put("Result", "ERROR");
						}
						log.debug("Complete work item {}, result {} (thread={})", new Object[]{workItem.getId(), results, Thread.currentThread().getId()});
						workItemDao.completeWorkItem(workItem.getId(), results);
						log.debug("Completed work item {} (thread={})", workItem.getId(), Thread.currentThread().getId());

						childProcesses.add(workItem.getProcessInstanceId());
					}
				}).start();
			}

			@Override
			public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

			}
		});

		processDefinitionManager.deployProcessDefinition("TestParentProcess3", true);
		processDefinitionManager.deployProcessDefinition("TestSubProcess2", true);

		Map<String, Object> params = CollectionUtils.map();
		List<String> list = list("value1", "value2");
		params.put("list", list);
		ProcessInstance processInstance = processManager.startProcess("TestParentProcess3", params);
		log.debug("Process instance started: {}", processInstance.getId());
		log.debug("Process status: {}", processInstance.getState());

		Thread.sleep(5000);

		for (Long childProcess : childProcesses) {
			processManager.signalExecution(processManager.getProcessInstance(childProcess), "Continue");
			Thread.sleep(1000);
		}

		Thread.sleep(10000);

		ProcessInstance pi;
		while(true) {
			WorkItemCompleteLocker.lock();
			log.debug("Get process instance1: {}", processInstance.getId());
			try {
				pi = processManager.getProcessInstance(processInstance.getId());
			} catch (RuntimeException e) {
				log.error("RuntimeException", e);
				throw e;
			} finally {
				WorkItemCompleteLocker.unlock();
			}
			log.debug("Got process instance1: {}", pi);
			if (pi.hasEnded()) {
				log.debug("End process instance");

				break;
			}
			Thread.sleep(500);
		}
	}

	@Test
	public void testTimerBoundaryEvent() throws ProcessDefinitionException, ProcessInstanceException, InterruptedException {

		ksession.getWorkItemManager().registerWorkItemHandler("MyTask", new WorkItemHandler() {

			@Override
			public void executeWorkItem(final WorkItem workItem, final WorkItemManager manager) {
				log.debug("Test work item: {}, process id: {}", workItem.getId(), workItem.getProcessInstanceId());
			}

			@Override
			public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

			}
		});

		processDefinitionManager.deployProcessDefinition("TestTimerBoundaryEvent2", true);
		Map<String, Object> params = CollectionUtils.map();
		ProcessInstance processInstance = processManager.startProcess("TestTimerBoundaryEvent2", params);

		Thread.sleep(30000);

		assertProcessInstanceCompleted(processInstance.getId(), ksession);
	}

	public void testEventBasedSplit() throws ProcessDefinitionException, ProcessInstanceException, InterruptedException {

		processDefinitionManager.deployProcessDefinition("TestEventBasedSplit", true);
		Map<String, Object> params = CollectionUtils.map();
		ProcessInstance processInstance = processManager.startProcess("TestEventBasedSplit", params);
		Thread.sleep(30000);

		assertProcessInstanceNotCompleted(processInstance.getId(), ksession);

		processManager.signalExecution(processInstance, "SignalValue");
		Thread.sleep(10000);

		assertProcessInstanceCompleted(processInstance.getId(), ksession);

		processManager.signalExecution(processInstance, "SignalValue");

	}

	private StatefulKnowledgeSession restoreSession(StatefulKnowledgeSession ksession) {
		return ksession;
	}

	private void assertProcessInstanceCompleted(long processInstanceId, StatefulKnowledgeSession ksession) {
		assertNull("Process did not complete: " + processInstanceId, ksession.getProcessInstance(processInstanceId));
	}

	private void assertProcessInstanceNotCompleted(long processInstanceId, StatefulKnowledgeSession ksession) {
		assertNotNull("Process completed: " + processInstanceId, ksession.getProcessInstance(processInstanceId));
	}

	private void write(Externalizable externalizable, String name) {
		try {

			FileOutputStream tempFile = new FileOutputStream(name);

			ObjectOutput oos = new ObjectOutputStream(tempFile);
			externalizable.writeExternal(oos);

			oos.flush();
			oos.close();

			tempFile.flush();
			tempFile.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void getDefaultPackage() throws IOException {
		get("defaultPackage");
	}

	private void get(String packageName) throws IOException {
		HttpGet httpget = new HttpGet("http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/backup?packageName=" + packageName);
		httpget.setHeader("Authorization", "BASIC " + new String(Base64.encodeBase64("admin:admin".getBytes())));

		org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

		// Execute the request
		//HttpContext context = new BasicHttpContext();
		//context.setAttribute("packageName", packageName);
		HttpResponse response = httpclient.execute(httpget);
		System.out.println("Content-Disposition: " + response.getHeaders("Content-Disposition")[0].getValue());

		// Examine the response status
		System.out.println(response.getStatusLine());

		// Get hold of the response entity
		HttpEntity entity = response.getEntity();

		// If the response does not enclose an entity, there is no need
		// to worry about connection release
		if (entity != null) {
			InputStream instream = entity.getContent();
			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream));
				// do something useful with the response
				System.out.println(reader.readLine());

			} catch (IOException ex) {

				// In case of an IOException the connection will be released
				// back to the connection manager automatically
				throw ex;

			} catch (RuntimeException ex) {

				// In case of an unexpected exception you may want to abort
				// the HTTP request in order to shut down the underlying
				// connection and release it back to the connection manager.
				httpget.abort();
				throw ex;

			} finally {

				// Closing the input stream will trigger connection release
				instream.close();

			}

			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}

	public void testPostProcess() throws InterruptedException, ProcessInstanceException, ProcessDefinitionException {
		List<ProcessDefinition> processDefinitions = getPackageContent("defaultPackage");
		for (ProcessDefinition processDefinition : processDefinitions) {
			System.out.println(processDefinition);
		}
		postFile("Hello.bpmn");
		deleteFile("BPMN2-UserTask.bpmn");
		//putFile("BPMN2-UserTask.bpmn");
		buildPackage("defaultPackage");
		//getPackageContent("defaultPackage");
		//getPackageContent("defaultPackage", "BPMN2-UserTask.bpmn", "20");
		//getPackageContent("defaultPackage", "BPMN2-UserTask.bpmn", "19");

		//testStartProcess3();
	}

	private void postFile(String fileName) {

		try {
			File file = new File(fileName);
			// FileBody bin = new FileBody(file);
			//StringBody comment = new StringBody("A binary file of some kind");

			//MultipartEntity reqEntity = new MultipartEntity();
			//reqEntity.addPart("bin", bin);
			//reqEntity.addPart("comment", comment);

			//StringEntity myEntity = new StringEntity("important message");
			FileEntity myEntity = new FileEntity(file, "text/plain");

			String uri = "http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/api/packages/defaultPackage/" + fileName;
			System.out.println("Post uri:" + uri);
			HttpPost httppost = new HttpPost(uri);
			httppost.setEntity(myEntity);
			//httppost.setEntity(reqEntity);
			httppost.setHeader("Authorization", "BASIC " + new String(Base64.encodeBase64("admin:admin".getBytes())));

			org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

			HttpResponse response = httpclient.execute(httppost);
			System.out.println("Post file StatusLine: " + response.getStatusLine());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	private void putFile(String fileName) {

		try {
			File file = new File(fileName);
			FileEntity myEntity = new FileEntity(file, "text/plain");

			HttpPut httpput = new HttpPut("http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/api/packages/defaultPackage/" + fileName);
			httpput.setEntity(myEntity);
			httpput.setHeader("Authorization", "BASIC " + new String(Base64.encodeBase64("admin:admin".getBytes())));
			httpput.setHeader("Checkin-Comment", "Commit");

			org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

			HttpResponse response = httpclient.execute(httpput);
			System.out.println("Put file StatusLine: " + response.getStatusLine());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	public void testDelete() {
		deleteFile("Evaluation.bpmn");
	}

	private void deleteFile(String fileName) {

		try {
			HttpDelete httpDelete = new HttpDelete("http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/api/packages/defaultPackage/" + fileName);
			//httppost.setEntity(reqEntity);
			httpDelete.setHeader("Authorization", "BASIC " + new String(Base64.encodeBase64("admin:admin".getBytes())));

			org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

			HttpResponse response = httpclient.execute(httpDelete);
			System.out.println("Delete file StatusLine: " + response.getStatusLine());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	private List<ProcessDefinition> getPackageContent(String packageName) {
		List<ProcessDefinition> processDefinitions = list();
		try {
			HttpGet httpGet = new HttpGet("http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/api/packages/" + packageName);
			httpGet.setHeader("Authorization", "BASIC " + new String(Base64.encodeBase64("admin:admin".getBytes())));

			org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

			HttpResponse response = httpclient.execute(httpGet);
			System.out.println("Post file StatusLine: " + response.getStatusLine());

			BufferedReader r = null;

			try {
				r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line;
				int i = 0;
				SimpleDateFormat sdf = getISODateFormat();
				while((line = r.readLine()) != null) {
					String[] splitResult = StringUtils.split(line, "=");
					if (splitResult.length == 2 && StringUtils.endsWith(splitResult[0], ".bpmn")) {
						String processName = StringUtils.removeEnd(splitResult[0], ".bpmn");
						String[] processInfo = StringUtils.split(splitResult[1], ",");
						if (processInfo.length == 2) {
							Date processLastModified;
							try {
								processLastModified = sdf.parse(processInfo[0]);
							} catch (ParseException e) {
								log.debug("Failed last modified date '{}' of process '{}'", new Object[]{processInfo[0], processName}, e);
								continue;
							}
							Long processVersion;
							try {
								processVersion = Long.parseLong(processInfo[1]);
							} catch (NumberFormatException e) {
								log.debug("Failed process version '{}' of process '{}'", new Object[]{processInfo[1], processName}, e);
								continue;
							}
							ProcessDefinition definition = new ProcessDefinition(processName, processName, processVersion);
							definition.setDeploymentId(splitResult[0]);
							definition.setLastModified(processLastModified);
							definition.setPackageName(packageName);
							processDefinitions.add(definition);
						}
					}
					System.out.println(++i + ":" +line);
				}
			} finally {
				IOUtils.closeQuietly(r);
			}
			System.out.println();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return processDefinitions;
	}

	private String getPackageContent(String packageName, String processFileName, String version) {
		try {
			String uri = "http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/api/packages/"
					+ packageName + "/" + processFileName;
			if (StringUtils.isNotEmpty(version)) {
				uri += "?version=" + version;
			}
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setHeader("Authorization", "BASIC " + new String(Base64.encodeBase64("admin:admin".getBytes())));

			org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

			HttpResponse response = httpclient.execute(httpGet);
			System.out.println("Package content StatusLine: " + response.getStatusLine());
			for (Header header : response.getAllHeaders()) {
				System.out.println("Header:" + header.getName() + "=" + header.getValue());
			}

			BufferedReader r = null;
			try {
				r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				String line;
				int i = 0;
				while((line = r.readLine()) != null) {
					System.out.println(++i + ":" +line);
				}
			} finally {
				IOUtils.closeQuietly(r);
			}
			System.out.println();
			return "";
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return "";
	}

	private void buildPackage(String packageName) {
		try {
			HttpPost httppost = new HttpPost("http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/action/compile");
			//httppost.setHeader("Authorization",
            //         "BASIC " + new String( new Base64().encode( "test:password".getBytes() ) ));
			httppost.setHeader("Authorization", "BASIC " + new String(Base64.encodeBase64("admin:admin".getBytes())));

			List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
			formparams.add(new BasicNameValuePair("package-name", packageName));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");

			httppost.setEntity(entity);

			org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

			HttpResponse response = httpclient.execute(httppost);
			System.out.println("Build package StatusLine: " + response.getStatusLine());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * This is the format used to sent dates as text, always.
	 */
	public static SimpleDateFormat getISODateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	}

	private static class TestWorkItemHandler implements WorkItemHandler {
		private WorkItem workItem;

		public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
			this.workItem = workItem;
		}

		public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		}

		public WorkItem getWorkItem() {
			WorkItem result = this.workItem;
			this.workItem = null;
			return result;
		}

	}
}
