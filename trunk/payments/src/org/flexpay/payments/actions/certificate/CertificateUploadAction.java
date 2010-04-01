package org.flexpay.payments.actions.certificate;

import org.apache.commons.lang.StringUtils;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.service.CertificateService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileInputStream;

public class CertificateUploadAction extends AccountantAWPActionSupport {

	private String alias;
	private String description;
	private File certificateFile;
	private String certificateFileContentType;
	private String certificateFileFileName;

	private CertificateService certificateService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isSubmit()) {
			if (!doValidate()) {
				return INPUT;
			}

			certificateService.addCertificate(alias, description, new FileInputStream(certificateFile));

			return REDIRECT_SUCCESS;
		}

		return INPUT;
	}

	private boolean doValidate() {

		if (StringUtils.isEmpty(alias)) {
			addActionError(getText("payments.error.certificate.alias_is_empty"));
			return false;
		}

		if (certificateService.aliasExists(alias)) {
			addActionError(getText("payments.error.certificate.alias_exists"));
			return false;
		}

		if (certificateFile == null) {
			addActionError(getText("payments.error.certificate.file_is_empty"));
			return false;
		}

		return true;
	}

	@NotNull
	@Override
	protected String getErrorResult() {

		return INPUT;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getCertificateFile() {
		return certificateFile;
	}

	public void setCertificateFile(File certificateFile) {
		this.certificateFile = certificateFile;
	}

	public String getCertificateFileContentType() {
		return certificateFileContentType;
	}

	public void setCertificateFileContentType(String certificateFileContentType) {
		this.certificateFileContentType = certificateFileContentType;
	}

	public String getCertificateFileFileName() {
		return certificateFileFileName;
	}

	public void setCertificateFileFileName(String certificateFileFileName) {
		this.certificateFileFileName = certificateFileFileName;
	}

	@Required
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}
}
