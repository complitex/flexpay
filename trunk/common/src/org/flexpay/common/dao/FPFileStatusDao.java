package org.flexpay.common.dao;

import org.flexpay.common.persistence.FPFileStatus;

import java.util.List;

public interface FPFileStatusDao extends GenericDao<FPFileStatus, Long> {

    List<FPFileStatus> listStatusesByCode(Long code);

	List<FPFileStatus> listStatusesByCodeAndModule(Long code, String moduleName);

}
