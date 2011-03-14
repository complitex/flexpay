package org.flexpay.eirc.sp;

import junit.framework.Assert;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.action.TestSpFileCreateAction;
import org.flexpay.eirc.process.registry.IterateMBRegistryActionHandler;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.flexpay.payments.service.EircRegistryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class TestMbChargesFileParser2 extends TestSpFileCreateAction {
	@Autowired
	private ProcessManager testProcessManager;
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

		FPFile newFile = createSpFile("org/flexpay/eirc/sp/20090605m_10.nch");

		List<Registry> registries = parseRegistryFile(newFile);

		registryProcessor.registriesProcess(registries);
	}

	@Test
	public void testParseZipFile() throws Throwable {

		FPFile newFile = createSpFile("org/flexpay/eirc/sp/20090605m_10.nch.zip");

		List<Registry> registries = parseRegistryFile(newFile);

		registryProcessor.registriesProcess(registries);
	}

	private List<Registry> parseRegistryFile(FPFile newFile) throws ProcessDefinitionException, ProcessInstanceException, InterruptedException {
		testProcessManager.deployProcessDefinition("ParseMBChargesProcess", true);
		Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
		parameters.put(IterateMBRegistryActionHandler.PARAM_FILE_ID, newFile.getId());

		long processId = testProcessManager.createProcess("ParseMBChargesProcess", parameters);
		assertTrue("Process can not created", processId > 0);
		org.flexpay.common.process.Process process = testProcessManager.getProcessInstanceInfo(processId);
		Assert.assertNotNull("Process did not find", process);

		// wait completed process
		while(!process.getProcessState().isCompleted()) {
			Thread.sleep(100);
			process = testProcessManager.getProcessInstanceInfo(processId);
			Assert.assertNotNull("Process did not loaded", process);
		}

		parameters = process.getParameters();
		Long registryId = (Long)parameters.get(IterateMBRegistryActionHandler.PARAM_REGISTRY_ID);
		assertNotNull("registryId did not find in process parameters", registryId);

		Registry registry = registryService.readWithContainers(new Stub<Registry>(registryId));
		assertNotNull("Registry did not find in db", registry);

		return  CollectionUtils.list(registry);
	}
}
