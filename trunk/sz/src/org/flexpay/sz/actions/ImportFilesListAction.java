package org.flexpay.sz.actions;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ImportFilesListAction extends FPActionWithPagerSupport<SzFile> {

	private List<SzFileWrapper> szFileWrapperList;

	private SzFileService szFileService;

	@NotNull
	public String doExecute() throws FlexPayException {

		List<SzFile> szFileList = szFileService.listSzFiles(getPager());
		szFileWrapperList = new ArrayList<SzFileWrapper>();
		for (SzFile szFile : szFileList) {
			SzFileWrapper wrapper = new SzFileWrapper(getLocale());
			wrapper.setSzFile(szFile);
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
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public String getSeparator() {
		return File.separator;
	}

	public List<SzFileWrapper> getSzFileWrapperList() {
		return szFileWrapperList;
	}

	public static class SzFileWrapper {

		private SzFile szFile;
		private String fileMonth;
		private Locale locale;

		public SzFileWrapper(Locale locale) {
			this.locale = locale;
		}

		public SzFile getSzFile() {
			return szFile;
		}

		public void setSzFile(SzFile szFile) {
			this.szFile = szFile;
		}

		public String getFileMonth() {
			if (szFile == null) {
				return null;
			}
			Calendar c = Calendar.getInstance();
			c.set(Calendar.MONTH, szFile.getFileMonth());
			DateFormat df = new SimpleDateFormat("MMMMM", getLocale());
			return df.format(c.getTime());
		}

		public Locale getLocale() {
			return locale;
		}

		public void setLocale(Locale locale) {
			this.locale = locale;
		}

	}

	@Required
	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

}
