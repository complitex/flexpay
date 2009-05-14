package org.flexpay.eirc.util.standalone;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.standalone.StandaloneTask;
import org.flexpay.eirc.actions.SpFileAction;
import org.flexpay.eirc.actions.UploadFileAction;
import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class RunSpFileProcessing implements StandaloneTask {

	private Logger log = LoggerFactory.getLogger(getClass());

	private String moduleName;
	private RegistryProcessor registryProcessor;
	private UploadFileAction spFileUploadAjaxAction;
	private SpFileAction fileAction;
	private FPFileService fpFileService;
	private RegistryFileService fileService;
	private RegistryService registryService;
	private RegistryDao registryDao;

	/**
	 * Execute task
	 */
	public void execute() {

		try {
//			loadRegistryOpenAccountsBig();
//			loadRegistryQuittancesBig();
//			importRecords();
//			importOpenAccounts();
//			processOpenAccounts();
//			importQuittancesBig();
			processQuittancesBig();
		} catch (Throwable e) {
			log.error("Failed processing registry file", e);
		}
	}

	private void loadRegistryOpenAccountsBig() throws Throwable {
		uploadRegistry("org/flexpay/eirc/actions/sp/ree_open.txt.gz");
	}

	private void loadRegistryQuittancesBig() throws Throwable {
		uploadRegistry("org/flexpay/eirc/actions/sp/ree_quittances.2008.06.txt.gz");
	}

	private void importOpenAccounts() throws Throwable {
		importRegistry(1L);
	}

	private void processOpenAccounts() throws Throwable {
		processRegistry(1L);
	}

	private void importQuittancesBig() throws Throwable {
		importRegistry(3L);
	}

	private void processQuittancesBig() throws Throwable {
		processRegistry(3L);
	}

	private void processRegistry(Long registryId) throws Throwable {
		Registry registry = registryService.readWithContainers(new Stub<Registry>(registryId));

		try {
			log.debug("Starting registry processing");
			long time = System.currentTimeMillis();
			registryProcessor.processRegistry(registry);

			if (log.isDebugEnabled()) {
				log.debug("Processing took {} ms", System.currentTimeMillis() - time);
			}
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				log.error("Exception cought", e);
			}
			throw c;
		}
	}

	private void importRegistry(Long registryId) throws Throwable {
		Registry registry = registryService.readWithContainers(new Stub<Registry>(registryId));

		long time = System.currentTimeMillis();

		log.debug("Starting registry importing");
		registryProcessor.startRegistryProcessing(registry);

		try {
			registryProcessor.importConsumers(registry);
		} finally {
			registryProcessor.endRegistryProcessing(registry);
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

		fileAction.setSpFileId(newFile.getId());
		fileAction.setAction("loadToDb");

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

		spFileUploadAjaxAction.setUpload(tmpDataFile);
		spFileUploadAjaxAction.setUploadFileName(name);

		spFileUploadAjaxAction.execute();
		return getLastFile();
	}

	private FPFile getLastFile() {
		List<FPFile> spFiles = fpFileService.getFilesByModuleName(moduleName);
		return spFiles.get(spFiles.size() - 1);
	}

	private void deleteFile(FPFile file) {
		fpFileService.delete(file);
		spFileUploadAjaxAction.getUpload().delete();
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
	public void setSpFileUploadAjaxAction(UploadFileAction spFileUploadAjaxAction) {
		this.spFileUploadAjaxAction = spFileUploadAjaxAction;
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

}