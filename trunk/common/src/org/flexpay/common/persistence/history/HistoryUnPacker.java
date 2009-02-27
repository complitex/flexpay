package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.FPFile;
import org.jetbrains.annotations.NotNull;

/**
 * History unpacker parses {@link org.flexpay.common.persistence.FPFile} containing set of history records
 */
public interface HistoryUnPacker {

	/**
	 * Generate pack of history records for consumer
	 *
	 * @param file FPFile containing history records
	 * @throws Exception if failure occurs while unpacking
	 */
	void unpackHistory(@NotNull FPFile file) throws Exception;
}