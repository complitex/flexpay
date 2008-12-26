package org.flexpay.common.dao;

import org.flexpay.common.persistence.FPModule;

import java.util.List;

public interface FPModuleDao extends GenericDao<FPModule, Long> {

    List<FPModule> listModulesByName(String name);

}
