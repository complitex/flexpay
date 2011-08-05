package org.flexpay.common.service.impl;

import org.flexpay.common.dao.FPFileDao;
import org.flexpay.common.dao.FPFileStatusDao;
import org.flexpay.common.dao.FPFileTypeDao;
import org.flexpay.common.dao.FPModuleDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.file.FPFileStatus;
import org.flexpay.common.persistence.file.FPFileType;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.SecurityUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class need to work with FPFile, FPModule, FPFileType, FPFileStatus entities.
 * <p/>
 * FPModule - simple entity with one field - moduleName.
 * <p/>
 * FPFile has a reference on FPModule, in which it was create. FpFile has a string-path to file on file system. One file
 * on file system - one FPFile in database.
 * <p/>
 * FPFileStatus and FPFileType - entities for defining type and current status of file. This entities both references on
 * FPModule.
 */
@Transactional (readOnly = true)
public class FPFileServiceImpl implements FPFileService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private FPFileDao fpFileDao;
	private FPFileTypeDao fpFileTypeDao;
	private FPFileStatusDao fpFileStatusDao;
	private FPModuleDao fpModuleDao;

	/**
	 * Create FPFile entity in database
	 *
	 * @param file FPFile entity
	 * @return created file
	 * @throws FlexPayException
	 */
	@NotNull
	@Transactional (readOnly = false)
	@Override
	public FPFile create(@NotNull FPFile file) throws FlexPayException {
		file.setUserName(SecurityUtil.getUserName());
		file.updateSize();
		fpFileDao.create(file);

		log.debug("Created new FPFile: {}", file);

		return file;
	}

	/**
	 * Update existing file in database
	 *
	 * @param file file to update
	 * @return updated file
	 * @throws FlexPayException
	 */
	@NotNull
	@Transactional (readOnly = false)
	@Override
	public FPFile update(@NotNull FPFile file) throws FlexPayException {
		file.updateSize();
		fpFileDao.update(file);
		return file;
	}

	/**
	 * Update existing files in database
	 *
	 * @param files files to update
	 * @return updated files
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@NotNull
	@Transactional (readOnly = false)
	@Override
	public List<FPFile> update(@NotNull List<FPFile> files) throws FlexPayException {
		for (FPFile file : files) {
			update(file);
		}

		return files;
	}

	/**
	 * Delete FPFile entity from database and also delete file from file system
	 *
	 * @param file file to delete
	 */
	@Transactional (readOnly = false)
	@Override
	public void delete(@NotNull FPFile file) {
		deleteFromFileSystem(file);
		if (file.isNotNew()) {
			fpFileDao.delete(file);
		}
	}

	/**
	 * Delete file only from file system
	 *
	 * @param file FPFile entity
	 */
	@Override
	public void deleteFromFileSystem(@NotNull FPFile file) {
		String localPath = FPFileUtil.getFileLocalPath(file);
		File fileToDelete = new File(localPath);
		if (!fileToDelete.delete()) {
			log.error("Error deleting file from server: {}", localPath);
		}
	}

	/**
	 * Load FPFile entity from database by file id
	 *
	 * @param stub stub of FPFile entity
	 * @return Loaded FPFile
	 */
	@Override
	public FPFile read(@NotNull Stub<FPFile> stub) {
		FPFile file = fpFileDao.readFull(stub.getId());
		if (file == null) {
			log.warn("No user with file id {} in database", stub.getId());
		}
		return file;
	}

	/**
	 * Get FPFiles from database by module name,
	 *
	 * @param moduleName name of module
	 * @param pager FPFiles pager
	 * 
	 * @return list of FPFiles
	 */
	@Override
	public List<FPFile> getFilesByModuleName(String moduleName, Page<FPFile> pager) {
		return fpFileDao.listFilesByModuleName(moduleName, pager);
	}

	/**
	 * Get FPModule from database by name
	 *
	 * @param name name of module
	 * @return FPModule
	 */
	@Override
	public FPModule getModuleByName(String name) {
		List<FPModule> modules = fpModuleDao.listModulesByName(name);
		if (modules.isEmpty()) {
			throw new IllegalArgumentException("Unknown module: " + name);
		}
		return modules.get(0);
	}

	/**
	 * Get FPFileType by name and module name. FPFileType has unique file-mask and by file name we can define type. Module
	 * name come out as second criterion
	 *
	 * @param fileName   name of file
	 * @param moduleName name of module
	 * @return FPFileType
	 */
	@Override
	public FPFileType getTypeByFileName(String fileName, String moduleName) {
		List<FPFileType> types = fpFileTypeDao.listFileTypesByModuleName(moduleName);
		for (FPFileType type : types) {
			Matcher m = Pattern.compile(type.getFileMask()).matcher(fileName);
			if (m.matches()) {
				return type;
			}
		}

		return null;
	}

	/**
	 * Get FPFileType by unique code
	 *
	 * @param code code of FPFileType
	 * @return FPFileType with this unique code
	 */
	@Override
	public FPFileType getTypeByCode(Long code) {
		List<FPFileType> l = fpFileTypeDao.listTypesByCode(code);
		return l.isEmpty() ? null : l.get(0);
	}

	/**
	 * Get FPFileStatus by unique code
	 *
	 * @param code code of FPFileStatus
	 * @return FPFileStatus with this unique code
	 */
	@Override
	public FPFileStatus getStatusByCode(Long code) {
		List<FPFileStatus> l = fpFileStatusDao.listStatusesByCode(code);
		return l.isEmpty() ? null : l.get(0);
	}

	/**
	 * Get FPFileType by unique code and module name. Each FPFileType has a unique (only for module!) code and by this code
	 * we can find types for some modules. Module name come out as second criterion
	 *
	 * @param code	   unique code of FPFileType
	 * @param moduleName name of module
	 * @return FPFileType by unique code and module name
	 */
	@Override
	public FPFileType getTypeByCodeAndModule(Long code, String moduleName) {
		List<FPFileType> l = fpFileTypeDao.listTypesByCodeAndModule(code, moduleName);
		return l.isEmpty() ? null : l.get(0);
	}

	/**
	 * Get FPFileStatus by unique code and module name. Each FPFileStatus has a unique (only for module!) code and by this
	 * code we can find statuses for some modules. Module name come out as second criterion
	 *
	 * @param code	   unique code of FPFileStatus
	 * @param moduleName name of module
	 * @return FPFileStatus by unique code and module name
	 */
	@Override
	public FPFileStatus getStatusByCodeAndModule(Long code, String moduleName) {
		List<FPFileStatus> l = fpFileStatusDao.listStatusesByCodeAndModule(code, moduleName);
		return l.isEmpty() ? null : l.get(0);
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        fpFileDao.setJpaTemplate(jpaTemplate);
        fpFileTypeDao.setJpaTemplate(jpaTemplate);
        fpFileStatusDao.setJpaTemplate(jpaTemplate);
        fpModuleDao.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setFpFileDao(FPFileDao fpFileDao) {
		this.fpFileDao = fpFileDao;
	}

	@Required
	public void setFpFileTypeDao(FPFileTypeDao fpFileTypeDao) {
		this.fpFileTypeDao = fpFileTypeDao;
	}

	@Required
	public void setFpFileStatusDao(FPFileStatusDao fpFileStatusDao) {
		this.fpFileStatusDao = fpFileStatusDao;
	}

	@Required
	public void setFpModuleDao(FPModuleDao fpModuleDao) {
		this.fpModuleDao = fpModuleDao;
	}

}
