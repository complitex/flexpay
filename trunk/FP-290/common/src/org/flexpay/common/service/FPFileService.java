package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.common.persistence.FPFileType;
import org.flexpay.common.persistence.FPModule;

import java.io.File;
import java.util.List;

public interface FPFileService {

    FPFile create(FPFile file) throws FlexPayException;

    FPFile update(FPFile file) throws FlexPayException;

    void delete(FPFile file);

    void deleteFromFileSystem(FPFile file);

    FPFile read(Long fileId) throws FlexPayException;

    File getFileFromFileSystem(Long fileId) throws FlexPayException;

    List<FPFile> getFilesByModuleName(String moduleName);

    FPModule getModuleByName(String name);

    FPFileType getTypeByFileName(String fileName, String moduleName);

    FPFileType getTypeByCode(Long code);

    FPFileStatus getStatusByCode(Long code);

}
