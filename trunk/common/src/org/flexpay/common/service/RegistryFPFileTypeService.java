package org.flexpay.common.service;

import org.flexpay.common.persistence.registry.RegistryFPFileType;
import org.jetbrains.annotations.Nullable;

public interface RegistryFPFileTypeService {
    /**
     * Read RegistryFPFileType by id
     * @param id ID
     * @return RegistryFPFileType object or <code>null</code>
     */
    @Nullable
    RegistryFPFileType read(Long id);

    /**
     * Read RegistryFPFileType by code
     *
     * @param code code of type
     * @return RegistryFPFileType object or <code>null</code>
     */
    @Nullable
    RegistryFPFileType findByCode(int code);
}
