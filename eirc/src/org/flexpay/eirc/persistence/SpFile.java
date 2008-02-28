package org.flexpay.eirc.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.config.ApplicationConfig;

public class SpFile extends DomainObject {
	private String requestFileName;
	private String internalRequestFileName;
	private String internalResponseFileName;

	private String userName;
	private Date importDate;

	public File saveToFileSystem(File source) throws IOException {
		if(!source.exists()) {
			throw new IllegalArgumentException("File " + source + " must be exist");
		}
		
		this.setInternalRequestFileName(StringUtil.getRandomString());
		File requestFile = this.getRequestFile();
		FileUtils.copyFile(source, requestFile);
		
		return requestFile;
	}
	
	public File getRequestFile() {
		if(internalRequestFileName == null) {
			return null;
		}
		
		return new File(ApplicationConfig.getInstance().getEircDataRoot(),
				internalRequestFileName);
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Id", getId())
				.append("Request file name", requestFileName)
				.append("Internal request file name", internalRequestFileName)
				.append("Internal response file name", internalResponseFileName)
				.append("Import date", importDate)
				.append("User name", userName).toString();
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

}
