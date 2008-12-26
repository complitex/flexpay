package org.flexpay.common.dao;

import org.flexpay.common.persistence.FPFileType;

import java.util.List;

public interface FPFileTypeDao extends GenericDao<FPFileType, Long> {

    List<FPFileType> listFileTypesByModuleName(String moduleName);

    List<FPFileType> listTypesByCode(Long code);

}
