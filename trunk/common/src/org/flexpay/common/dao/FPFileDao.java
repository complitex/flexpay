package org.flexpay.common.dao;

import org.flexpay.common.persistence.FPFile;

import java.util.List;

public interface FPFileDao extends GenericDao<FPFile, Long> {

    List<FPFile> listFilesByModuleName(String moduleName);

}
