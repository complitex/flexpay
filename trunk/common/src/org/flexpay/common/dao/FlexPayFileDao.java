package org.flexpay.common.dao;

import org.flexpay.common.persistence.FlexPayFile;
import org.jetbrains.annotations.NotNull;

public interface FlexPayFileDao {

    FlexPayFile save(@NotNull FlexPayFile flexPayFile);

    void delete(@NotNull FlexPayFile flexPayFile);

    FlexPayFile read(@NotNull Long id);

}
