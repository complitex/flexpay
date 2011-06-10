package org.flexpay.eirc.process.registry;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.drools.utils.WorkItemCompleteLocker;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.TestRegistryUtil;
import org.flexpay.eirc.action.spfile.SpFileUploadAction;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.eirc.process.registry.GetRegistryMessageActionHandler.PARAM_FILE_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class TestParseFPRegistryProcess extends EircSpringBeanAwareTestCase {
    @Autowired
	private ProcessManager processManager;
	@Autowired
	protected ProcessDefinitionManager processDefinitionManager;
	@Autowired
	private FPFileService fpFileService;
	@Autowired
	@Qualifier("spFileUploadAction")
	private SpFileUploadAction spFileUploadAction;
	@Autowired
	private RegistryService registryService;
	@Autowired
	private RegistryRecordService registryRecordService;
	@Autowired
	@Qualifier ("eircTestRegistryUtil")
	private TestRegistryUtil registryUtil;

	private static final String TEST_FILE = "org/flexpay/eirc/action/sp/ree.txt";

	@Test
	public void testRegistryProcess() throws Throwable {
        processDefinitionManager.deployProcessDefinition("ParseFPRegistry", true);

		Map<String, Object> parameters = map();
		FPFile fpFile = createSpFile(TEST_FILE);
		parameters.put(PARAM_FILE_ID, fpFile.getId());
		ProcessInstance process = processManager.startProcess("ParseFPRegistry", parameters);
		Assert.assertNotNull("ProcessInstance can not created", process);

		// wait completed process 
		process = waitWhileProcessInstanceWillComplete(process);

		parameters = process.getParameters();
		String registryId = (String)parameters.get(ProcessHeaderActionHandler.PARAM_REGISTRY_ID);
		assertNotNull("registryId did not find in process parameters", registryId);
		Registry registry = registryService.read(new Stub<Registry>(Long.parseLong(registryId)));
		assertEquals("Failed number registry records", new Long(4), registry.getRecordsNumber());
		Page<RegistryRecord> page = new Page<RegistryRecord>(20);
		List<RegistryRecord> records = registryRecordService.listRecords(registry,
				new ImportErrorTypeFilter(),
				new RegistryRecordStatusFilter(),
				page);
		assertEquals("Failed number records in db", 4, records.size());
		//registryUtil.delete(registry);
    }

	protected FPFile createSpFile(@NotNull String spFile) throws Throwable {

		String name = StringUtil.getFileName(spFile);
		String extension = StringUtil.getFileExtension(name);
		File tmpDataFile = File.createTempFile(name, extension);
		tmpDataFile.deleteOnExit();
		OutputStream os = null;
		InputStream is = null;
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			os = new FileOutputStream(tmpDataFile);

			is = getFileStream(spFile);
			if (is == null) {
				fail("Cannot find source file " + spFile);
			}
			IOUtils.copy(is, os);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}

		spFileUploadAction.setUpload(tmpDataFile);
		spFileUploadAction.setUploadFileName(name);

		assertEquals("Invalid Struts action result", ActionSupport.SUCCESS, spFileUploadAction.execute());
		return spFileUploadAction.getFpFile();
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
