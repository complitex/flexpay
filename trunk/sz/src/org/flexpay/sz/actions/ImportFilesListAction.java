package org.flexpay.sz.actions;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.util.List;

public class ImportFilesListAction extends FPActionWithPagerSupport<SzFile> {

	private List<SzFile> szFiles;

	private SzFileService szFileService;

	@NotNull
	public String doExecute() throws FlexPayException {

		szFiles = szFileService.listSzFiles(getPager());

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public String getSeparator() {
		return File.separator;
	}

	public List<SzFile> getSzFiles() {
		return szFiles;
	}

	@Required
	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

}
