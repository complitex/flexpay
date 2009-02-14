package org.flexpay.tc.process.exporters;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.tc.util.config.ApplicationConfig;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class FileCNExporter implements Exporter{
	
	private PrintStream exportPrintStream;
	private final static String exportFileNamePrefix="CNExportFile_";

	/**
	 * Begin export procedure
	 * @throws FlexPayException throws flexpay exception when filed to begin export process
	 */
	public void beginExport() throws FlexPayException {
		try {
			File f = File.createTempFile(exportFileNamePrefix,"", ApplicationConfig.getTcDataRoot());
			exportPrintStream = new PrintStream(f);
		} catch (FileNotFoundException e) {
			throw new FlexPayException("Can't find file for output.");
		} catch (IOException e) {
			throw new FlexPayException("Can't create export file");
		}
	}

	/**
	 * Export parameters
	 * @param params params to export
	 * @throws FlexPayException throws flexpay exception when can't export data
	 */
	public void export(@NotNull Object[] params) throws FlexPayException {
		if (exportPrintStream != null){
			for (Object o : params){
				exportPrintStream.print(o);
				exportPrintStream.print(" : ");
			}
			exportPrintStream.println();
		}else{
			throw new FlexPayException("File Exporter is not ready");
		}
	}

	/**
	 * Commit transaction
	 * @throws FlexPayException throws flexpay exception when filed to commit transaction
	 */
	public void commit() throws FlexPayException {
		if (exportPrintStream == null){
			throw new FlexPayException("File Exporter is not ready");
		}else{
			exportPrintStream.flush();
		}
	}

	/**
	 * Rollback transaction
	 *
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          throws FlexPayException when can't rollback transaction
	 */
	public void rollback() throws FlexPayException {
		//do nothing
	}

	/**
	 * End export procedure
	 * @throws FlexPayException throws flexpay exception when filed to end export process
	 */
	public void endExport() {
		if (exportPrintStream != null){
			exportPrintStream.flush();
			IOUtils.closeQuietly(exportPrintStream);
		}
	}
}
