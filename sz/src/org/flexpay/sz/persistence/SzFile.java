package org.flexpay.sz.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.common.persistence.FPFileType;

import java.util.Date;

public class SzFile extends DomainObject {

	public static final Long IMPORTING_FILE_STATUS = 0L;
	public static final Long IMPORTED_FILE_STATUS = 1L;
	public static final Long PROCESSING_FILE_STATUS = 2L;
	public static final Long PROCESSED_FILE_STATUS = 3L;
	public static final Long PROCESSED_WITH_ERRORS_FILE_STATUS = 4L;
	public static final Long DELETING_FILE_STATUS = 5L;
	public static final Long DELETING_FROM_DB_FILE_STATUS = 6L;

	public static final Long TARIFF_FILE_TYPE = 1L;
	public static final Long CHARACTERISTIC_FILE_TYPE = 2L;
	public static final Long SRV_TYPES_FILE_TYPE = 3L;
	public static final Long FORM2_FILE_TYPE = 4L;
	public static final Long CHARACTERISTIC_RESPONSE_FILE_TYPE = 5L;
	public static final Long SRV_TYPES_RESPONSE_FILE_TYPE = 6L;
	public static final Long SUBSIDY_FILE_TYPE = 7L;

	private Integer fileYear;
	private Integer fileMonth;
	private Date importDate = new Date();
	private Boolean isActually = true;
	private String userName;
	private Oszn oszn;
	private FPFile uploadedFile;
	private FPFile fileToDownload;
	private FPFileType type;
	private FPFileStatus status;

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

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Boolean getIsActually() {
        return isActually;
    }

    public void setIsActually(Boolean isActually) {
        this.isActually = isActually;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Oszn getOszn() {
		return oszn;
	}

	public void setOszn(Oszn oszn) {
		this.oszn = oszn;
	}

    public FPFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(FPFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public FPFile getFileToDownload() {
        return fileToDownload;
    }

    public void setFileToDownload(FPFile fileToDownload) {
        this.fileToDownload = fileToDownload;
    }

	public FPFileType getType() {
		return type;
	}

	public void setType(FPFileType type) {
		this.type = type;
	}

	public FPFileStatus getStatus() {
		return status;
	}

	public void setStatus(FPFileStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("SzFile {").
				append("idr", getId()).
				append("fileYear", fileYear).
				append("fileMonth", fileMonth).
				append("importDate", importDate).
				append("isActually", isActually).
				append("userName", userName).
				append("uploadedFile", uploadedFile).
				append("fileToDownload", fileToDownload).
				append("}").toString();
	}

}
