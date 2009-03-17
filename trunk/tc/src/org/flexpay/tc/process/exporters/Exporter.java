package org.flexpay.tc.process.exporters;

import org.flexpay.common.exception.FlexPayException;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for exporting purpose
 */
public interface Exporter {

	/**
	 * Begin export procedure
	 *
	 * @throws FlexPayException throws flexpay exception when filed to begin export process
	 */
	public void beginExport() throws FlexPayException;

	/**
	 * Export parameters
	 *
	 * @param params params to export
	 * @throws FlexPayException throws flexpay exception when can't export data
	 */
	public void export(@NotNull Object[] params) throws FlexPayException;

	/**
	 * Commit transaction
	 *
	 * @throws FlexPayException throws flexpay exception when filed to commit transaction
	 */
	public void commit() throws FlexPayException;

	/**
	 * Rollback transaction
	 *
	 * @throws FlexPayException throws FlexPayException when can't rollback transaction
	 */
	public void rollback() throws FlexPayException;

	/**
	 * End export procedure
	 */
	public void endExport();

}
