package org.flexpay.eirc.sp;

import junit.framework.Assert;
import org.flexpay.common.drools.utils.WorkItemCompleteLocker;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.action.TestSpFileCreateAction;
import org.flexpay.eirc.process.registry.helper.ParseRegistryConstants;
import org.flexpay.eirc.process.registry.helper.ProcessingRegistryConstants;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.flexpay.payments.service.EircRegistryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.junit.Assert.assertNotNull;


public class TestMbCorrectionsFileParser2 extends TestSpFileCreateAction {
	@Autowired
	private ProcessManager processManager;
	@Autowired
	private ProcessDefinitionManager processDefinitionManager;
	@Autowired
	private EircRegistryService eircRegistryService;
	@Autowired
	private RegistryService registryService;

	@Autowired
	private ClassToTypeRegistry typeRegistry;
	@Autowired
	private RegistryProcessor registryProcessor;

	@Test
	public void testParseFile() throws Throwable {

		FPFile newFile = createSpFile("org/flexpay/eirc/sp/20090605m_10.ls");

		List<Registry> registries = parseRegistryFile(newFile);
		Registry registry = registries.get(0);
		log.debug("Uploaded registry: {}", registry);

		registry = processingRegistry(registry);
		log.debug("Processed registry: {}", registry);

		//registryProcessor.registriesProcess(registries);
	}

	@Test
	public void testParseZipFile() throws Throwable {

		FPFile newFile = createSpFile("org/flexpay/eirc/sp/20090605m_10.ls.zip");

		List<Registry> registries = parseRegistryFile(newFile);

		Registry registry = registries.get(0);
		log.debug("Uploaded registry: {}", registry);

		registry = processingRegistry(registry);
		log.debug("Processed registry: {}", registry);
	}

	private Registry processingRegistry(Registry registry) throws ProcessDefinitionException, ProcessInstanceException, InterruptedException {
		processDefinitionManager.deployProcessDefinition("ProcessingDBRegistry", true);
		Map<String, Object> parameters = map();
		parameters.put(ProcessingRegistryConstants.REGISTRY_ID, registry.getId());
		ProcessInstance process = processManager.startProcess("ProcessingDBRegistry", parameters);
		Assert.assertNotNull("ProcessInstance can not created", process);
		waitWhileProcessInstanceWillComplete(process);

		registry = registryService.read(Stub.stub(registry));
		return registry;
	}

	private List<Registry> parseRegistryFile(FPFile newFile) throws ProcessDefinitionException, ProcessInstanceException, InterruptedException {
		processDefinitionManager.deployProcessDefinition("ParseMBCorrections", true);
		Map<String, Object> parameters = map();
		parameters.put(ParseRegistryConstants.PARAM_FILE_ID, newFile.getId());

		ProcessInstance process = processManager.startProcess("ParseMBCorrections", parameters);
		Assert.assertNotNull("ProcessInstance did not find", process);

		// wait completed process
		process = waitWhileProcessInstanceWillComplete(process);

		parameters = process.getParameters();
		String registryId = (String)parameters.get(ParseRegistryConstants.PARAM_REGISTRY_ID);
		assertNotNull("registryId did not find in process parameters", registryId);

		Registry registry = registryService.readWithContainers(new Stub<Registry>(Long.parseLong(registryId)));
		assertNotNull("Registry did not find in db", registry);

		return CollectionUtils.list(registry);
	}

	private ProcessInstance waitWhileProcessInstanceWillComplete(ProcessInstance processInstance) throws InterruptedException {
		ProcessInstance pi = null;
		while(true) {
			WorkItemCompleteLocker.lock();
			log.debug("Get process instance: {}", processInstance.getId());
			try {
				pi = processManager.getProcessInstance(processInstance.getId());
			} catch (RuntimeException e) {
				log.error("RuntimeException", e);
				throw e;
			} finally {
				WorkItemCompleteLocker.unlock();
			}
			log.debug("Got process instance: {}", pi);
			if (pi.hasEnded()) {
				log.debug("End process instance");
				break;
			}
			Thread.sleep(500);
		}
		return pi;
	}
}
