package org.flexpay.common.dao;

import org.flexpay.common.persistence.file.FPFileType;

import java.util.List;

public interface FPFileTypeDao extends GenericDao<FPFileType, Long> {

    List<FPFileType> listFileTypesByModuleName(String moduleName);

    List<FPFileType> listTypesByCode(Long code);

	List<FPFileType> listTypesByCodeAndModule(Long code, String moduleName);

}
