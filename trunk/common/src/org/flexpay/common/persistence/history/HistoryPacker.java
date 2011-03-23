package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * History packer generates {@link org.flexpay.common.persistence.file.FPFile} with a set of history records
 */
public interface HistoryPacker {

	/**
	 * Generate pack of history records for consumer
	 *
	 * @param stub Consumer stub
	 * @return FPFile if there were some new records, or <code>null</code> otherwise
	 * @throws Exception if failure occurs while packing
	 */
	@NotNull
	List<FPFile> packHistory(@NotNull Stub<HistoryConsumer> stub) throws Exception;

	/**
	 * Set the size of a group history is packed in, used to limit result file size
	 *
	 * @param groupSize Max group size
	 */
	void setGroupSize(int groupSize);

	void setPagingSize(int pagingSize);
}
