package org.flexpay.sz.persistence;

import java.io.File;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Region;

public class ImportFile implements java.io.Serializable {
	public static final Integer IMPORTING_FILE_STATUS = 0;
	public static final Integer IMPORTED_FILE_STATUS = 1;
	public static final Integer MARKED_FOR_PROCESSING_FILE_STATUS = 2;
	public static final Integer PROCESSING_FILE_STATUS = 3;
	public static final Integer PROCESSED_FILE_STATUS = 4;
	public static final Integer MARK_AS_DELETED_FILE_STATUS = 5;

	public static final Integer VALID_FILE_VALIDATION = 1;
	public static final Integer INVALID_FILE_VALIDATION = 2;

	public static final String CHARACTERISTIC_FILE_TYPE = "characteristic";
	public static final String SERVICE_TYPE_FILE_TYPE = "service_type";
	public static final String FORM_2_FILE_TYPE = "form_2";

	private Long id;
	private Region region;
	private String originalFileName;
	private String requestFileName;
	private String resultFileName;
	private String fileType;
	private Integer fileYear;
	private Integer fileMonth;
	private String userName;
	private Date importDate;
	private Integer fileStatus;
	private Integer fileValidation;

	public ImportFile() {
	}

	public static String getRandomString() {
		return System.currentTimeMillis() + "-" + Math.random();
	}

	public File getFile(File parentDir) {
		String yyyy_mm = fileYear + "_" + ((fileMonth + 1) <= 9 ? "0" : "")
				+ (fileMonth + 1);
		File file = new File(parentDir, yyyy_mm);
		file = new File(file, requestFileName);

		return file;
	}

	public static String getFileType(String fileName) {
		if (fileName == null || "".equals(fileName)) {
			return null;
		}
		Matcher m = Pattern.compile("\\d{8}\\u002Ea\\d{2}").matcher(fileName);
		if (m.matches()) {
			return ImportFile.CHARACTERISTIC_FILE_TYPE;
		}
		m = Pattern.compile("\\d{8}\\u002Eb\\d{2}").matcher(fileName);
		if (m.matches()) {
			return ImportFile.SERVICE_TYPE_FILE_TYPE;
		}
		m = Pattern.compile("\\d{8}\\u002Ee\\d{2}").matcher(fileName);
		if (m.matches()) {
			return ImportFile.FORM_2_FILE_TYPE;
		}

		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getRequestFileName() {
		return requestFileName;
	}

	public void setRequestFileName(String requestFileName) {
		this.requestFileName = requestFileName;
	}

	public String getResultFileName() {
		return resultFileName;
	}

	public void setResultFileName(String resultFileName) {
		this.resultFileName = resultFileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getFileYear() {
		return fileYear;
	}

	public void setFileYear(Integer fileYear) {
		this.fileYear = fileYear;
	}

	public Integer getFileMonth() {
		return fileMonth;
	}

	public void setFileMonth(Integer fileMonth) {
		this.fileMonth = fileMonth;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Integer getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(Integer fileStatus) {
		this.fileStatus = fileStatus;
	}

	public Integer getFileValidation() {
		return fileValidation;
	}

	public void setFileValidation(Integer fileValidation) {
		this.fileValidation = fileValidation;
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Id", id).append("Region", region).append(
						"Original file name", originalFileName).append(
						"Request file name", requestFileName).append(
						"Result file name", resultFileName).append("File type",
						fileType).append("File year", fileYear).append(
						"File month", fileMonth).append("User name", userName)
				.append("Import date", importDate).append("File status",
						fileStatus).append("File validation", fileValidation)
				.toString();
	}
}
