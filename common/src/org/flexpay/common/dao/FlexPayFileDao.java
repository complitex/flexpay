package org.flexpay.common.dao;

import org.flexpay.common.persistence.FlexPayFile;

import java.util.List;

public interface FlexPayFileDao extends GenericDao<FlexPayFile, Long> {

    List<FlexPayFile> listFilesByModuleName(String moduleName);

}
