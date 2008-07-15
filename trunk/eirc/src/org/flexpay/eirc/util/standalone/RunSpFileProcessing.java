package org.flexpay.eirc.util.standalone;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.util.standalone.StandaloneTask;
import org.flexpay.eirc.actions.SpFileAction;
import org.flexpay.eirc.actions.SpFileCreateAction;
import org.flexpay.eirc.dao.SpRegistryDao;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessor;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class RunSpFileProcessing implements StandaloneTask {

	@NonNls
	private Logger log = Logger.getLogger(getClass());

	private ServiceProviderFileProcessor fileProcessor;
	@NonNls
	private HibernateTemplate hibernateTemplate;
	private SpFileCreateAction fileCreateAction;
	private SpFileAction fileAction;
	private SpFileService fileService;
	private SpRegistryDao spRegistryDao;

	public void setFileProcessor(ServiceProviderFileProcessor fileProcessor) {
		this.fileProcessor = fileProcessor;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
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

	public void setSpRegistryDao(SpRegistryDao spRegistryDao) {
		this.spRegistryDao = spRegistryDao;
	}

	/**
	 * Execute task
	 */
	public void execute() {

		try {
			testProcessOpenSubAccountsRegistrySmall();
		} catch (Throwable e) {
			log.error("Failed processing registry file", e);
		}
	}

	private void sqlLogMarker() {
		hibernateTemplate.bulkUpdate("delete SpFile where internalRequestFileName=" +
				"'\n==============================================================\n'");
	}

	public void testProcessOpenSubAccountsRegistrySmall() throws Throwable {
		System.out.println("ree_open_2_small start");
		SpFile file = uploadFile("org/flexpay/eirc/actions/sp/ree_open_2_small.txt");

		try {
			sqlLogMarker();
			fileProcessor.processFile(file);
			sqlLogMarker();
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				e.printStackTrace();
			}
			throw c;
		} finally {
			deleteRecords(file);
			deleteFile(file);
			System.out.println("ree_open_2_small end");
		}
	}

	protected void deleteRecords(SpFile file) {
		for (SpRegistry registry : fileService.getRegistries(file)) {
			spRegistryDao.deleteRecordContainers(registry.getId());
			spRegistryDao.deleteRegistryContainers(registry.getId());
			spRegistryDao.deleteRecords(registry.getId());
			spRegistryDao.delete(registry);
		}
	}

	@SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
	protected SpFile uploadFile(String fileName) throws Throwable {
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
		if (fileAction.getSpFileFormatException() != null) {
			throw fileAction.getSpFileFormatException();
		}
		return newFile;
	}

	protected SpFile createSpFile(String spFile) throws Throwable {
		File tmpDataFile = File.createTempFile("sp_sample", ".txt");
		tmpDataFile.deleteOnExit();
		OutputStream os = null;
		InputStream is = null;
		try {
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

		fileCreateAction.setUpload(tmpDataFile);
		fileCreateAction.setUploadFileName("sp.txt");
		fileCreateAction.setSubmit("submit");

		fileCreateAction.execute();
		return getLastFile();
	}

	private SpFile getLastFile() {
		List<SpFile> spFiles = fileService.getEntities();
		return spFiles.get(spFiles.size() - 1);
	}

	protected void deleteFile(SpFile file) {
		fileService.delete(file);
		fileCreateAction.getUpload().delete();

		if (file.getRequestFile() != null) {
			file.getRequestFile().delete();
		}
	}
}
