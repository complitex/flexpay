package org.flexpay.common.util;

import org.flexpay.common.persistence.registry.Registry;
import org.jetbrains.annotations.NotNull;

public interface TestRegistryUtil {
    void delete(@NotNull Registry registry);
}
