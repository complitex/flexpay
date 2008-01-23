package org.flexpay.sz.actions;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.io.FileUtils;
import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.persistence.ImportFile;
import org.flexpay.sz.service.ImportFileService;

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
	private Long regionId;

	private List<RegionName> regionNames;
	private RegionService regionService;
	private CountryService countryService;
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

				Region region = regionService.read(regionId);
				importFile.setRegion(region);
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

		CountryFilter countryFilter = new CountryFilter();
		Page pager = new Page();
		countryFilter = countryService.initFilter(countryFilter,
				userPreferences.getLocale());
		ArrayStack filtersStack = new ArrayStack();
		filtersStack.push(countryFilter);
		regionNames = regionService.findNames(filtersStack, pager);

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

	public List<RegionName> getRegionNames() {
		return regionNames;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	public static Map<Integer, String> getMonths() {
		return months;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public void setImportFileService(ImportFileService importFileService) {
		this.importFileService = importFileService;
	}
}
