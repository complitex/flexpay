package org.flexpay.sz.action.szfile;

import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class SzFilesListAction extends FPActionWithPagerSupport<SzFile> {

	private List<SzFile> szFiles = list();

	private SzFileService szFileService;

	@NotNull
	@Override
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

	public List<SzFile> getSzFiles() {
		return szFiles;
	}

	@Required
	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

}
