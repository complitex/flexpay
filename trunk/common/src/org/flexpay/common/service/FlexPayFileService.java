package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.common.persistence.FlexPayFileType;
import org.flexpay.common.persistence.FlexPayFileStatus;
import org.flexpay.common.persistence.FlexPayModule;

import java.util.List;

public interface FlexPayFileService {

    FlexPayFile create(FlexPayFile file) throws FlexPayException;

    FlexPayFile update(FlexPayFile file) throws FlexPayException;

    void delete(FlexPayFile file);

    void deleteFromFileSystem(FlexPayFile file);

    FlexPayFile read(Long fileId) throws FlexPayException;

    List<FlexPayFile> getFilesByModuleName(String moduleName);

    FlexPayFileType getTypeByName(String name);

    FlexPayFileStatus getStatusByName(String name);

    FlexPayModule getModuleByName(String name);

}
