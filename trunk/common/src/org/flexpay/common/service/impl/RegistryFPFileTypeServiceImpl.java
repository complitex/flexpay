package org.flexpay.common.service.impl;

import org.flexpay.common.dao.registry.RegistryFPFileTypeDao;
import org.flexpay.common.persistence.registry.RegistryFPFileType;
import org.flexpay.common.service.RegistryFPFileTypeService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional(readOnly = true)
public class RegistryFPFileTypeServiceImpl implements RegistryFPFileTypeService {

    private RegistryFPFileTypeDao registryFPFileTypeDao;

    /**
     * Read RegistryFPFileType by id
     *
     * @param id ID
     * @return RegistryFPFileType object or <code>null</code>
     */
    @Nullable
	@Override
    public RegistryFPFileType read(Long id) {
        return registryFPFileTypeDao.readFull(id);
    }

    /**
     * Read RegistryFPFileType by code
     *
     * @param code code of type
     * @return RegistryFPFileType object or <code>null</code>
     */
	@Override
    public RegistryFPFileType findByCode(int code) {
        List<RegistryFPFileType> findRegistryFPFileTypes = registryFPFileTypeDao.findByCode(code);
        return findRegistryFPFileTypes.isEmpty() ? null : findRegistryFPFileTypes.get(0);
    }

    @Override
    public List<RegistryFPFileType> findByCodes(@NotNull Collection<Integer> codes) {
        return registryFPFileTypeDao.findByCodes(codes);
    }

    @Required
    public void setFpFileRegistryTypeDao(RegistryFPFileTypeDao registryFPFileTypeDao) {
        this.registryFPFileTypeDao = registryFPFileTypeDao;
    }

}
