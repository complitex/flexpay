package org.flexpay.ab.util;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestTownTypeUtil {

    @Nullable
    TownType create(@NotNull String name, @NotNull String shortName);

    @Nullable
    TownTypeTranslation addTranslation(@NotNull TownType townType, @NotNull String name, @NotNull String shortName, @NotNull String langIsoCode);

    void delete(@NotNull TownType townType);

}
