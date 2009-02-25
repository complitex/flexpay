package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.common.persistence.FPFileType;
import org.flexpay.common.persistence.FPModule;

import java.io.File;
import java.util.List;

/**
 * This class need to work with FPFile, FPModule, FPFileType,
 * FPFileStatus entities.
 *
 * FPModule - simple entity with one field - moduleName.
 * 
 * FPFile have a reference on FPModule, in which it was create.
 * FpFile have a string-path to file on file system.
 * One file on file system - one FPFile in database.
 *
 * FPFileStatus and FPFileType - entities for defining type and
 * current status of file. This entities both references on FPModule.
 */
public interface FPFileService {

	/**
	 * Create FPFile entity in database
	 *
	 * @param file FPFile entity
	 * @return created file
	 * @throws FlexPayException
	 */
    FPFile create(FPFile file) throws FlexPayException;

	/**
	 * Update existing file in database
	 *
	 * @param file file to update
	 * @return updated file
	 * @throws FlexPayException
	 */
    FPFile update(FPFile file) throws FlexPayException;

	/**
	 * Delete FPFile entity from database
	 * and also delete file from file system
	 *
	 * @param file file to delete
	 */
    void delete(FPFile file);

	/**
	 * Delete file only from file system
	 *
	 * @param file FPFile entity
	 */
    void deleteFromFileSystem(FPFile file);

	/**
	 * Load FPFile entity from database by file id
	 *
	 * @param fileId id of FPFile entity
	 * @return Loaded FPFile
	 * @throws FlexPayException
	 */
	FPFile read(Long fileId) throws FlexPayException;

	/**
	 * Get file from file system by FPFile entity id,
	 * which locating to this file
	 *
	 * @param fileId FPFile entity id
	 * @return file on file system
	 * @throws FlexPayException
	 */
	File getFileFromFileSystem(Long fileId) throws FlexPayException;

	/**
	 * Get FPFiles from database by module name,
	 *
	 * @param moduleName name of module
	 * @return list of FPFiles
	 */
	List<FPFile> getFilesByModuleName(String moduleName);

	/**
	 * Get FPModule from database by name
	 *
	 * @param name name of module
	 * @return FPModule
	 */
	FPModule getModuleByName(String name);

	/**
	 * Get FPFileType by name and module name.
	 * FPFileType have s file-mask and by file name
	 * we can define type. Module name come out as second criterion
	 *
	 * @param fileName name of file
	 * @param moduleName name of module
	 * @return FPFileType
	 */
    FPFileType getTypeByFileName(String fileName, String moduleName);

	/**
	 * Get FPFileType by unique code
	 *
	 * @param code code of FPFileType
	 * @return FPFileType with this unique code
	 */
    FPFileType getTypeByCode(Long code);

	/**
	 * Get FPFileStatus by unique code
	 *
	 * @param code code of FPFileStatus
	 * @return FPFileStatus with this unique code
	 */
    FPFileStatus getStatusByCode(Long code);

	/**
	 * Get FPFileType by unique code and module name.
	 * Each FPFileType have a unique (only for module!) code and
	 * by this code we can find types for some modules.
	 * Module name come out as second criterion
	 *
	 * @param code unique code of FPFileType
	 * @param moduleName name of module
	 * @return FPFileType by unique code and module name
	 */
	FPFileType getTypeByCodeAndModule(Long code, String moduleName);

	/**
	 * Get FPFileStatus by unique code and module name.
	 * Each FPFileStatus have a unique (only for module!) code and
	 * by this code we can find statuses for some modules.
	 * Module name come out as second criterion
	 *
	 * @param code unique code of FPFileStatus
	 * @param moduleName name of module
	 * @return FPFileStatus by unique code and module name
	 */
	FPFileStatus getStatusByCodeAndModule(Long code, String moduleName);

}
