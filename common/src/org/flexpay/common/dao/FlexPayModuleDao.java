package org.flexpay.common.dao;

import org.flexpay.common.persistence.FlexPayModule;

import java.util.List;

public interface FlexPayModuleDao extends GenericDao<FlexPayModule, Long> {

    List<FlexPayModule> listModulesByName(String name);

}
