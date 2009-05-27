package org.flexpay.common.persistence.history;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This manager saves the last recieved packets
 */
public interface HistoryUnpackManager {

	/**
	 * Notify manager a new history records pack available
	 *
	 * @param pack History records pack
	 * @return Persisted pack back
	 */
	@NotNull
	ExternalHistoryPack create(@NotNull ExternalHistoryPack pack);

	/**
	 * Get next history packs that is to be unpacked
	 *
	 * @return History packs
	 */
	@NotNull
	List<ExternalHistoryPack> getNextPacks();

	/**
	 * Notify manager that the pack was unpacked
	 *
	 * @param pack History records pack
	 * @return Unpack data for external source pack was created for
	 */
	@NotNull
	HistoryUnPackData setLastUnpacked(@NotNull ExternalHistoryPack pack);
}
