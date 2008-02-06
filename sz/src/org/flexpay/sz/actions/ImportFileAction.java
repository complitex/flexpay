package org.flexpay.sz.actions;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.flexpay.ab.actions.CommonAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.persistence.SzFileType;
import org.flexpay.sz.service.SzFileService;
import org.flexpay.sz.service.OsznService;
import org.flexpay.sz.service.SzFileActualityStatusService;
import org.flexpay.sz.service.SzFileStatusService;
import org.flexpay.sz.service.SzFileTypeService;

public class ImportFileAction extends CommonAction {

	private static Map<Integer, String> months;
	private static Integer[] years;
	static {
		months = new TreeMap<Integer, String>();
		months.put(0, "01");
		months.put(1, "02");
		months.put(2, "03");
		months.put(3, "04");
		months.put(4, "05");
		months.put(5, "06");
		months.put(6, "07");
		months.put(7, "08");
		months.put(8, "09");
		months.put(9, "10");
		months.put(10, "11");
		months.put(11, "12");

		Calendar cal = Calendar.getInstance();
		cal.setTime(ApplicationConfig.getInstance().getPastInfinite());
		int yearFrom = cal.get(Calendar.YEAR);
		cal.setTime(ApplicationConfig.getInstance().getFutureInfinite());
		int yearTill = cal.get(Calendar.YEAR);
		years = new Integer[yearTill - yearFrom + 1];
		for (int i = 0; i <= yearTill - yearFrom; i++) {
			years[i] = yearFrom + i;
		}
	}
	private File upload;
	private String uploadFileName;
	private Integer year;
	private Integer month;
	private Long osznId;
	private List<Oszn> osznList;

	private OsznService osznService;
	private SzFileService szFileService;
	private SzFileTypeService szFileTypeService;
	private SzFileStatusService szFileStatusService;
	private SzFileActualityStatusService szFileActualityStatusService;

	public String execute() throws FlexPayException {
		if (isSubmitted()) {
			SzFileType szFileType = szFileTypeService
					.getByFileName(uploadFileName);
			if (szFileType != null) {
				SzFile szFile = new SzFile();
				szFile.setInternalRequestFileName(SzFile.getRandomString());
				szFile.setFileYear(year);
				szFile.setFileMonth(month);
				File file = szFile.getRequestFile(ApplicationConfig.getInstance()
						.getSzDataRoot());
				try {
					FileUtils.copyFile(upload, file);
				} catch (IOException e) {
					// TODO write error to page
				}

				Oszn oszn = osznService.read(osznId);
				szFile.setOszn(oszn);
				szFile.setRequestFileName(uploadFileName);
				szFile.setSzFileType(szFileType);
				szFile.setUserName("vld"); // TODO set user name
				szFile.setImportDate(new Date());
				szFile.setSzFileStatus(szFileStatusService
						.read(SzFileStatusService.IMPORTED));
				szFile.setSzFileActualityStatus(szFileActualityStatusService
						.read(SzFileActualityStatusService.IS_ACTUAL));
				try {
					szFileService.create(szFile);
				} catch (FlexPayException e) {
					file.delete();
					// TODO write error to page
				}
			} else {
				// TODO write message - wrong file name
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH);
		}

		osznList = osznService.getEntities();

		return "form";
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public static Integer[] getYears() {
		return years;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public List<Oszn> getOsznList() {
		return osznList;
	}

	public Long getOsznId() {
		return osznId;
	}

	public void setOsznId(Long osznId) {
		this.osznId = osznId;
	}

	public void setOsznService(OsznService osznService) {
		this.osznService = osznService;
	}

	public static Map<Integer, String> getMonths() {
		return months;
	}

	public void setSzFileTypeService(SzFileTypeService szFileTypeService) {
		this.szFileTypeService = szFileTypeService;
	}

	public void setSzFileStatusService(SzFileStatusService szFileStatusService) {
		this.szFileStatusService = szFileStatusService;
	}

	public void setSzFileActualityStatusService(
			SzFileActualityStatusService szFileActualityStatusService) {
		this.szFileActualityStatusService = szFileActualityStatusService;
	}

	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}
}
