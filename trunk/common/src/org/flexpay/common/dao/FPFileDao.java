package org.flexpay.common.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.file.FPFile;

import java.util.List;

public interface FPFileDao extends GenericDao<FPFile, Long> {

    List<FPFile> listFilesByModuleName(String moduleName, Page<FPFile> pager);

}
