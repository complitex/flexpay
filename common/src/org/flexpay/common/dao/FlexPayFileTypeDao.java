package org.flexpay.common.dao;

import org.flexpay.common.persistence.FlexPayFileType;

import java.util.List;

public interface FlexPayFileTypeDao extends GenericDao<FlexPayFileType, Long> {

    List<FlexPayFileType> listFileTypesByModuleName(String moduleName);

    List<FlexPayFileType> listTypesByName(String name);

}
