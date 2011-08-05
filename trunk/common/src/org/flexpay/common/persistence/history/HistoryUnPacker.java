package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.JpaSetService;
import org.jetbrains.annotations.NotNull;

/**
 * History unpacker parses {@link org.flexpay.common.persistence.file.FPFile} containing set of history records
 */
public interface HistoryUnPacker extends JpaSetService {

	/**
	 * Generate pack of history records for consumer
	 *
	 * @param file FPFile containing history records
	 * @throws Exception if failure occurs while unpacking
	 */
	void unpackHistory(@NotNull FPFile file) throws Exception;
}