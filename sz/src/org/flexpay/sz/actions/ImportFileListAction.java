package org.flexpay.sz.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.convert.NotSupportedOperationException;
import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportFileListAction extends FPActionSupport {

	private SzFileService szFileService;

	private List<SzFileWrapper> szFileWrapperList;

	public String doExecute() throws FlexPayException {

		List<SzFile> szFileList = szFileService.getEntities();
		szFileWrapperList = new ArrayList<SzFileWrapper>();
		for (SzFile szFile : szFileList) {
			SzFileWrapper wrapper = new SzFileWrapper();
			wrapper.setSzFile(szFile);
			try {
				wrapper.setLoadedToDb(SzFileUtil.isLoadedToDb(szFile));
			} catch (NotSupportedOperationException e) {
				wrapper.setLoadedToDb(false);
			}
			szFileWrapperList.add(wrapper);
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

	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

	public String getSzDataRoot() throws IOException {
		return ApplicationConfig.getSzDataRoot().getCanonicalPath();
	}

	public String getSeparator() {
		return File.separator;
	}

	public static class SzFileWrapper {
		private SzFile szFile;
		private boolean isLoadedToDb;

		public SzFile getSzFile() {
			return szFile;
		}

		public void setSzFile(SzFile szFile) {
			this.szFile = szFile;
		}

		public boolean isLoadedToDb() {
			return isLoadedToDb;
		}

		public void setLoadedToDb(boolean isLoadedToDb) {
			this.isLoadedToDb = isLoadedToDb;
		}

	}

	public List<SzFileWrapper> getSzFileWrapperList() {
		return szFileWrapperList;
	}

}
