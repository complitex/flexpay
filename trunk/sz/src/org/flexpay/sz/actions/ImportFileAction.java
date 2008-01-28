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
import org.flexpay.sz.persistence.ImportFile;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.service.ImportFileService;
import org.flexpay.sz.service.OsznService;

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
	
	private ImportFileService importFileService;

	public String execute() throws FlexPayException {
		if (isSubmitted()) {
			String fileType = ImportFile.getFileType(uploadFileName);
			if (fileType != null) {
				ImportFile importFile = new ImportFile();
				importFile.setRequestFileName(ImportFile.getRandomString());
				importFile.setFileYear(year);
				importFile.setFileMonth(month);
				File file = importFile.getFile(ApplicationConfig.getInstance()
						.getSzDataRoot());
				try {
					FileUtils.copyFile(upload, file);
				} catch (IOException e) {
					// TODO write error to page
				}

				Oszn oszn = osznService.read(osznId);
				importFile.setOszn(oszn);
				importFile.setOriginalFileName(uploadFileName);
				importFile.setFileType(fileType);
				importFile.setUserName("vld"); // TODO set user name
				importFile.setImportDate(new Date());
				importFile.setFileStatus(ImportFile.IMPORTED_FILE_STATUS);
				importFile.setFileValidation(ImportFile.VALID_FILE_VALIDATION);
				try {
					importFileService.create(importFile);
				} catch (FlexPayException e) {
					file.delete();
					// TODO write error to page
				}
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

	public void setImportFileService(ImportFileService importFileService) {
		this.importFileService = importFileService;
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
}
