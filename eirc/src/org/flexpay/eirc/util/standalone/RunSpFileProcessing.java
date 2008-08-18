package org.flexpay.eirc.util.standalone;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.standalone.StandaloneTask;
import org.flexpay.eirc.actions.SpFileAction;
import org.flexpay.eirc.actions.SpFileCreateAction;
import org.flexpay.eirc.dao.RegistryDao;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class RunSpFileProcessing implements StandaloneTask {

	@NonNls
	private Logger log = Logger.getLogger(getClass());

	private RegistryProcessor registryProcessor;
	private SpFileCreateAction fileCreateAction;
	private SpFileAction fileAction;
	private SpFileService fileService;
	private SpRegistryService registryService;
	private RegistryDao registryDao;

	public void setRegistryProcessor(RegistryProcessor registryProcessor) {
		this.registryProcessor = registryProcessor;
	}

	public void setFileCreateAction(SpFileCreateAction fileCreateAction) {
		this.fileCreateAction = fileCreateAction;
	}

	public void setFileAction(SpFileAction fileAction) {
		this.fileAction = fileAction;
	}

	public void setFileService(SpFileService fileService) {
		this.fileService = fileService;
	}

	public void setSpRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	public void setRegistryService(SpRegistryService registryService) {
		this.registryService = registryService;
	}

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
			importQuittancesBig();
//			processQuittancesBig();
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
		importRegistry(2L);
	}

	private void processQuittancesBig() throws Throwable {
		processRegistry(2L);
	}

	private void processRegistry(Long registryId) throws Throwable {
		SpRegistry registry = registryService.readWithContainers(new Stub<SpRegistry>(registryId));

		try {
			log.debug("Starting registry processing");
			long time = System.currentTimeMillis();
			registryProcessor.processRegistry(registry);

			if (log.isDebugEnabled()) {
				log.debug("Processing took " + (System.currentTimeMillis() - time) + "ms");
			}
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				log.error("Exception cought", e);
			}
			throw c;
		}
	}

	private void importRegistry(Long registryId) throws Throwable {
		SpRegistry registry = registryService.readWithContainers(new Stub<SpRegistry>(registryId));

		long time = System.currentTimeMillis();

		log.debug("Starting registry importing");
		registryProcessor.startRegistryProcessing(registry);

		try {
			registryProcessor.importConsumers(registry);
		} finally {
			registryProcessor.endRegistryProcessing(registry);
		}

		if (log.isDebugEnabled()) {
			log.debug("Import took " + (System.currentTimeMillis() - time) + "ms");
		}
	}

	private void uploadRegistry(String path) throws Throwable {
		long time = System.currentTimeMillis();
		uploadFile(path);

		if (log.isDebugEnabled()) {
			log.debug("Upload took " + (System.currentTimeMillis() - time) + "ms");
		}
	}

	private void deleteRecords(SpFile file) {
		for (SpRegistry registry : fileService.getRegistries(file)) {
			registryDao.deleteRecordContainers(registry.getId());
			registryDao.deleteRegistryContainers(registry.getId());
			registryDao.deleteRecords(registry.getId());
			registryDao.delete(registry);
		}
	}

	private SpFile uploadFile(String fileName) throws Throwable {
		SpFile newFile = createSpFile(fileName);

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

	private SpFile createSpFile(String spFile) throws Throwable {
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

		if (log.isDebugEnabled()) {
			log.debug("Upload file: " + tmpDataFile);
		}

		fileCreateAction.setUpload(tmpDataFile);
		fileCreateAction.setUploadFileName(name);
		fileCreateAction.setSubmitted("submit");

		fileCreateAction.execute();
		return getLastFile();
	}

	private SpFile getLastFile() {
		List<SpFile> spFiles = fileService.getEntities();
		return spFiles.get(spFiles.size() - 1);
	}

	private void deleteFile(SpFile file) {
		fileService.delete(file);
		fileCreateAction.getUpload().delete();

		if (file.getRequestFile() != null) {
			file.getRequestFile().delete();
		}
	}
}
