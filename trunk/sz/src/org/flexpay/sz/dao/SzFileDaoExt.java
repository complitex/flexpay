package org.flexpay.sz.dao;

import org.flexpay.common.persistence.FPFileStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface SzFileDaoExt {

	void updateStatus(@NotNull Collection<Long> fileIds, @NotNull FPFileStatus status);

}
