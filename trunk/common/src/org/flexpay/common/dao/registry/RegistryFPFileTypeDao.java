package org.flexpay.common.dao.registry;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.persistence.registry.RegistryFPFileType;

import java.util.Collection;
import java.util.List;

public interface RegistryFPFileTypeDao extends GenericDao<RegistryFPFileType, Long> {

    List<RegistryFPFileType> findByCode(int code);

    List<RegistryFPFileType> findByCodes(Collection<Integer> codes);

}
