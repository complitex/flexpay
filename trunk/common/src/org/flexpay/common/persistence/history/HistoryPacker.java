package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * History packer generates {@link org.flexpay.common.persistence.FPFile} with a set of history records
 */
public interface HistoryPacker {

	/**
	 * Generate pack of history records for consumer
	 *
	 * @param stub Consumer stub
	 * @return FPFile if there were some new records, or <code>null</code> otherwise
	 * @throws Exception if failure occurs while packing
	 */
	@Nullable
	FPFile packHistory(@NotNull Stub<HistoryConsumer> stub) throws Exception;
}
