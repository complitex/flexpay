package org.flexpay.sz.persistence;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.sz.convert.NotSupportedOperationException;
import org.flexpay.sz.convert.SzFileUtil;

public class SzFile implements java.io.Serializable {
	public static final Integer IMPORTING_FILE_STATUS = 0;
	public static final Integer IMPORTED_FILE_STATUS = 1;
	public static final Integer MARKED_FOR_PROCESSING_FILE_STATUS = 2;
	public static final Integer PROCESSING_FILE_STATUS = 3;
	public static final Integer PROCESSED_FILE_STATUS = 4;
	public static final Integer MARK_AS_DELETED_FILE_STATUS = 5;

	public static final Integer VALID_FILE_VALIDATION = 1;
	public static final Integer INVALID_FILE_VALIDATION = 2;

	private Long id;
	private Oszn oszn;

	private String requestFileName;
	private String internalRequestFileName;
	private String internalResponseFileName;

	private SzFileType szFileType;
	private Integer fileYear;
	private Integer fileMonth;
	private String userName;
	private Date importDate;
	private SzFileStatus szFileStatus;
	private SzFileActualityStatus szFileActualityStatus;

	public SzFile() {
	}

	public static String getRandomString() {
		return System.currentTimeMillis() + "-" + Math.random();
	}

	public File getRequestFile(File parentDir) {
		String yyyy_mm = fileYear + "_" + ((fileMonth + 1) <= 9 ? "0" : "")
				+ (fileMonth + 1);
		File file = new File(parentDir, yyyy_mm);
		file = new File(file, internalRequestFileName);

		return file;
	}

	public File getResponseFile(File parentDir) {
		String yyyy_mm = fileYear + "_" + ((fileMonth + 1) <= 9 ? "0" : "")
				+ (fileMonth + 1);
		File file = new File(parentDir, yyyy_mm);
		file = new File(file, internalResponseFileName);

		return file;
	}

	public String getYyyyMm() {
		return fileYear + "_" + ((fileMonth + 1) <= 9 ? "0" : "")
				+ (fileMonth + 1);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Oszn getOszn() {
		return oszn;
	}

	public void setOszn(Oszn oszn) {
		this.oszn = oszn;
	}

	public SzFileType getSzFileType() {
		return szFileType;
	}

	public void setSzFileType(SzFileType szFileType) {
		this.szFileType = szFileType;
	}

	public String getRequestFileName() {
		return requestFileName;
	}

	public void setRequestFileName(String requestFileName) {
		this.requestFileName = requestFileName;
	}

	public String getInternalRequestFileName() {
		return internalRequestFileName;
	}

	public void setInternalRequestFileName(String internalRequestFileName) {
		this.internalRequestFileName = internalRequestFileName;
	}

	public String getInternalResponseFileName() {
		return internalResponseFileName;
	}

	public void setInternalResponseFileName(String internalResponseFileName) {
		this.internalResponseFileName = internalResponseFileName;
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Id", id)
				.append("Oszn", oszn)
				.append("Request file name", requestFileName)
				.append("Internal request file name", internalRequestFileName)
				.append("Internal response file name", internalResponseFileName)
				.append("File type", szFileType).append("File year", fileYear)
				.append("File month", fileMonth).append("User name", userName)
				.append("Import date", importDate).append("Sz file status",
						szFileStatus).append("Sz file actuality status",
						szFileActualityStatus).toString();
	}

	public SzFileStatus getSzFileStatus() {
		return szFileStatus;
	}

	public void setSzFileStatus(SzFileStatus szFileStatus) {
		this.szFileStatus = szFileStatus;
	}

	public SzFileActualityStatus getSzFileActualityStatus() {
		return szFileActualityStatus;
	}

	public void setSzFileActualityStatus(
			SzFileActualityStatus szFileActualityStatus) {
		this.szFileActualityStatus = szFileActualityStatus;
	}
}
