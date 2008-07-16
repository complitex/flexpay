package org.flexpay.eirc.actions;

import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.sp.SpFileFormatException;
import org.flexpay.eirc.sp.SpFileUtil;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NonNls;

public class SpFileAction extends FPActionSupport {

	private Long spFileId;
	@NonNls
	private String action;
	private SpFileFormatException spFileFormatException;

	private SpFileService spFileService;

	public String doExecute() throws Exception {
		SpFile spFile = spFileService.read(spFileId);
		if ("loadToDb".equals(action)) {
			try {
				SpFileUtil.loadToDb(spFile);
			} catch (SpFileFormatException e) {
				spFileFormatException = e;
			}
		} else if ("loadFromDb".equals(action)) {
			// SzFileUtil.loadFromDb(szFile);
		} else if ("deleteFromDb".equals(action)) {
			// SzFileUtil.deleteRecords(szFile);
		} else if ("fullDelete".equals(action)) {
			// SzFileUtil.delete(szFile);
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	/**
	 * @param spFileId the spFileId to set
	 */
	public void setSpFileId(Long spFileId) {
		this.spFileId = spFileId;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(@NonNls String action) {
		this.action = action;
	}

	/**
	 * @param spFileService the spFileService to set
	 */
	public void setSpFileService(SpFileService spFileService) {
		this.spFileService = spFileService;
	}

	/**
	 * @return the spFileFormatException
	 */
	public SpFileFormatException getSpFileFormatException() {
		return spFileFormatException;
	}
}
