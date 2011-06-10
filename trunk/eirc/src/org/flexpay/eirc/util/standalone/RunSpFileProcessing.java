package org.flexpay.eirc.util.standalone;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.standalone.StandaloneTask;
import org.flexpay.eirc.action.spfile.SpFileAction;
import org.flexpay.eirc.action.spfile.SpFileUploadAction;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class RunSpFileProcessing implements StandaloneTask {

	private Logger log = LoggerFactory.getLogger(getClass());

	private String moduleName;
	private RegistryProcessor registryProcessor;
	private SpFileUploadAction spFileUploadAction;
	private SpFileAction fileAction;
	private FPFileService fpFileService;
	private RegistryFileService fileService;
	private RegistryService registryService;
	private RegistryDao registryDao;
	private ProcessManager processManager;

	/**
	 * Execute task
	 */
	public void execute() {

		try {
//			loadRegistryOpenAccountsBig();
//			loadRegistryQuittancesBig();
//			importRecords();
//			importOpenAccounts();
			processOpenAccounts();
//			importQuittancesBig();
//			processQuittancesBig();
		} catch (Throwable e) {
			log.error("Failed processing registry file", e);
		}
	}

	private void loadRegistryOpenAccountsBig() throws Throwable {
		uploadRegistry("org/flexpay/eirc/action/sp/ree_open.txt.gz");
	}

	private void loadRegistryQuittancesBig() throws Throwable {
		uploadRegistry("org/flexpay/eirc/action/sp/ree_quittances.2008.06.txt.gz");
	}

	private void importOpenAccounts() throws Throwable {
		importRegistry(1L);
	}

	private void processOpenAccounts() throws Throwable {
		processRegistry(37L);
	}

	private void importQuittancesBig() throws Throwable {
		importRegistry(3L);
	}

	private void processQuittancesBig() throws Throwable {
		processRegistry(3L);
	}

	private void processRegistry(Long registryId) throws Throwable {
		Registry registry = registryService.readWithContainers(new Stub<Registry>(registryId));
		Assert.notNull(registry, "Registry not found #" + registryId);
		log.debug("Starting registry processing");
		long time = System.currentTimeMillis();

		Map<String, Object> contextVariables = CollectionUtils.map();
		contextVariables.put("registryId", registryId);

		ProcessInstance process = processManager.startProcess("ProcessingDBRegistry", contextVariables);
		if (process == null) {
			log.error("ProcessInstance did not start");
		}

		while (process != null && !process.hasEnded()) {
			process = processManager.getProcessInstance(process.getId());
		}
		if (process == null) {
			log.error("ProcessInstance deleted");
		}

		if (log.isDebugEnabled()) {
			log.debug("Processing took {} ms", System.currentTimeMillis() - time);
		}
	}

	private void importRegistry(Long registryId) throws Throwable {
		Registry registry = registryService.readWithContainers(new Stub<Registry>(registryId));

		long time = System.currentTimeMillis();

		log.debug("Starting registry importing");
		ProcessingContext context = new ProcessingContext();
		context.setRegistry(registry);
		registryProcessor.startRegistryProcessing(context);

		try {
			registryProcessor.importConsumers(context);
		} finally {
			registryProcessor.endRegistryProcessing(context);
		}

		if (log.isDebugEnabled()) {
			log.debug("Import took {} ms", System.currentTimeMillis() - time);
		}
	}

	private void uploadRegistry(String path) throws Throwable {
		long time = System.currentTimeMillis();
		uploadFile(path);

		if (log.isDebugEnabled()) {
			log.debug("Upload took {} ms", System.currentTimeMillis() - time);
		}
	}

	private void deleteRecords(FPFile file) {
		for (Registry registry : fileService.getRegistries(file)) {
			registryDao.deleteRecordContainers(registry.getId());
			registryDao.deleteRegistryContainers(registry.getId());
			registryDao.deleteRecords(registry.getId());
			registryDao.delete(registry);
		}
	}

	private FPFile uploadFile(String fileName) throws Throwable {
		FPFile newFile = createSpFile(fileName);

		fileAction.setSpFile(newFile);
		fileAction.setAction(SpFileAction.LOAD_TO_DB_ACTION);

		try {
			fileAction.execute();
		} catch (Exception e) {
			deleteRecords(newFile);
			deleteFile(newFile);
			throw e;
		}
		return newFile;
	}

	private FPFile createSpFile(String spFile) throws Throwable {
		String name = StringUtil.getFileName(spFile);
		String extension = StringUtil.getFileExtension(name);
		File tmpDataFile = File.createTempFile(name, extension);
		tmpDataFile.deleteOnExit();
		OutputStream os = null;
		InputStream is = null;
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			os = new FileOutputStream(tmpDataFile);

			is = getClass().getClassLoader().getResourceAsStream(spFile);
			if (is == null) {
				throw new RuntimeException("Cannot find source file");
			}
			IOUtils.copy(is, os);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}

		log.debug("Upload file: {}", tmpDataFile);

		spFileUploadAction.setUpload(tmpDataFile);
		spFileUploadAction.setUploadFileName(name);

		spFileUploadAction.execute();
		return getLastFile();
	}

	private FPFile getLastFile() {
		List<FPFile> spFiles = fpFileService.getFilesByModuleName(moduleName, new Page<FPFile>(1000));
		return spFiles.get(spFiles.size() - 1);
	}

	private void deleteFile(FPFile file) {
		fpFileService.delete(file);
		spFileUploadAction.getUpload().delete();
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

	@Required
	public void setRegistryProcessor(RegistryProcessor registryProcessor) {
		this.registryProcessor = registryProcessor;
	}

	@Required
	public void setSpFileUploadAction(SpFileUploadAction spFileUploadAction) {
		this.spFileUploadAction = spFileUploadAction;
	}

	@Required
	public void setFileAction(SpFileAction fileAction) {
		this.fileAction = fileAction;
	}

	@Required
	public void setFileService(RegistryFileService fileService) {
		this.fileService = fileService;
	}

	@Required
	public void setSpRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
