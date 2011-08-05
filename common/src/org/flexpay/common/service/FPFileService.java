package org.flexpay.common.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.file.FPFileStatus;
import org.flexpay.common.persistence.file.FPFileType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
public interface FPFileService extends JpaSetService {

	/**
	 * Create FPFile entity in database
	 *
	 * @param file FPFile entity
	 * @return created file
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
    FPFile create(@NotNull FPFile file) throws FlexPayException;

	/**
	 * Update existing file in database
	 *
	 * @param file file to update
	 * @return updated file
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
    FPFile update(@NotNull FPFile file) throws FlexPayException;

	/**
	 * Update existing files in database
	 *
	 * @param files files to update
	 * @return updated files
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
    List<FPFile> update(@NotNull List<FPFile> files) throws FlexPayException;

	/**
	 * Delete FPFile entity from database
	 * and also delete file from file system
	 *
	 * @param file file to delete
	 */
    void delete(@NotNull FPFile file);

	/**
	 * Delete file only from file system
	 *
	 * @param file FPFile entity
	 */
    void deleteFromFileSystem(@NotNull FPFile file);

	/**
	 * Load FPFile entity from database by file id
	 *
	 * @param stub stub of FPFile entity
	 * @return Loaded FPFile
	 */
	FPFile read(@NotNull Stub<FPFile> stub);

	/**
	 * Get FPFiles from database by module name,
	 *
	 * @param moduleName name of module
	 * @param pager FPFiles pager
	 *
	 * @return list of FPFiles
	 */
	List<FPFile> getFilesByModuleName(String moduleName, Page<FPFile> pager);

	/**
	 * Get FPModule from database by name
	 *
	 * @param name name of module
	 * @return FPModule
	 * @throws IllegalArgumentException if no module could be found
	 */
	FPModule getModuleByName(String name) throws IllegalArgumentException;

	/**
	 * Get FPFileType by name and module name.
	 * FPFileType has unique file-mask and by file name
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
	 * Each FPFileType has a unique (only for module!) code and
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
	 * Each FPFileStatus has a unique (only for module!) code and
	 * by this code we can find statuses for some modules.
	 * Module name come out as second criterion
	 *
	 * @param code unique code of FPFileStatus
	 * @param moduleName name of module
	 * @return FPFileStatus by unique code and module name
	 */
	FPFileStatus getStatusByCodeAndModule(Long code, String moduleName);

}
