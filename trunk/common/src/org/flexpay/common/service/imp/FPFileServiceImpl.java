package org.flexpay.common.service.imp;

import org.flexpay.common.dao.FPFileDao;
import org.flexpay.common.dao.FPFileStatusDao;
import org.flexpay.common.dao.FPFileTypeDao;
import org.flexpay.common.dao.FPModuleDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.common.persistence.FPFileType;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.context.SecurityContextHolder;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class need to work with FPFile, FPModule, FPFileType,
 * FPFileStatus entities.
 *
 * FPModule - simple entity with one field - moduleName.
 *
 * FPFile has a reference on FPModule, in which it was create.
 * FpFile has a string-path to file on file system.
 * One file on file system - one FPFile in database.
 *
 * FPFileStatus and FPFileType - entities for defining type and
 * current status of file. This entities both references on FPModule.
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
	@Transactional (readOnly = false)
	public FPFile create(FPFile file) throws FlexPayException {
		file.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
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
	@Transactional (readOnly = false)
	public FPFile update(FPFile file) throws FlexPayException {
		fpFileDao.update(file);
		return file;
	}

	/**
	 * Delete FPFile entity from database
	 * and also delete file from file system
	 *
	 * @param file file to delete
	 */
	@Transactional (readOnly = false)
	public void delete(FPFile file) {
		String localPath = FPFileUtil.getFileLocalPath(file);
		File fileToDelete = new File(localPath);
		if (!fileToDelete.delete()) {
			log.error("Error deleting file from server: {}", localPath);
		}
		fpFileDao.delete(file);
	}

	/**
	 * Delete file only from file system
	 *
	 * @param file FPFile entity
	 */
	public void deleteFromFileSystem(FPFile file) {
		String localPath = FPFileUtil.getFileLocalPath(file);
		File fileToDelete = new File(localPath);
		if (!fileToDelete.delete()) {
			log.error("Error deleting file from server: {}", localPath);
		}
	}

	/**
	 * Load FPFile entity from database by file id
	 *
	 * @param fileId id of FPFile entity
	 * @return Loaded FPFile
	 * @throws FlexPayException
	 */
	public FPFile read(Long fileId) throws FlexPayException {
		FPFile file = fpFileDao.readFull(fileId);
		if (file == null) {
			log.warn("No user with file id {} in database", fileId);
		}
		return file;
	}

	/**
	 * Get file from file system by FPFile entity id,
	 * which locating to this file
	 *
	 * @param fileId FPFile entity id
	 * @return file on file system
	 * @throws FlexPayException
	 */
	public File getFileFromFileSystem(Long fileId) throws FlexPayException {
		return FPFileUtil.getFileOnServer(read(fileId));
	}

	/**
	 * Get FPFiles from database by module name,
	 *
	 * @param moduleName name of module
	 * @return list of FPFiles
	 */
	public List<FPFile> getFilesByModuleName(String moduleName) {
		return fpFileDao.listFilesByModuleName(moduleName);
	}

	/**
	 * Get FPModule from database by name
	 *
	 * @param name name of module
	 * @return FPModule
	 */
	public FPModule getModuleByName(String name) {
		List<FPModule> l = fpModuleDao.listModulesByName(name);
		return l.isEmpty() ? null : l.get(0);
	}

	/**
	 * Get FPFileType by name and module name.
	 * FPFileType has unique file-mask and by file name
	 * we can define type. Module name come out as second criterion
	 *
	 * @param fileName name of file
	 * @param moduleName name of module
	 * @return FPFileType
	 */
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
	public FPFileStatus getStatusByCode(Long code) {
		List<FPFileStatus> l = fpFileStatusDao.listStatusesByCode(code);
		return l.isEmpty() ? null : l.get(0);
	}

	/**
	 * Get FPFileType by unique code and module name.
	 * Each FPFileType has a unique (only for module!) code and
	 * by this code we can find types for some modules.
	 * Module name come out as second criterion
	 *
	 * @param code unique code of FPFileType
	 * @param moduleName name of module
	 * @return FPFileType by unique code and module name
	 */
	public FPFileType getTypeByCodeAndModule(Long code, String moduleName) {
		List<FPFileType> l = fpFileTypeDao.listTypesByCodeAndModule(code, moduleName);
		return l.isEmpty() ? null : l.get(0);
	}

	/**
	 * Get FPFileStatus by unique code and module name.
	 * Each FPFileStatus has a unique (only for module!) code and
	 * by this code we can find statuses for some modules.
	 * Module name come out as second criterion
	 *
	 * @param code unique code of FPFileStatus
	 * @param moduleName name of module
	 * @return FPFileStatus by unique code and module name
	 */
	public FPFileStatus getStatusByCodeAndModule(Long code, String moduleName) {
		List<FPFileStatus> l = fpFileStatusDao.listStatusesByCodeAndModule(code, moduleName);
		return l.isEmpty() ? null : l.get(0);
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
