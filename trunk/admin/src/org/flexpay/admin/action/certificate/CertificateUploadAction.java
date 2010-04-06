package org.flexpay.admin.action.certificate;

import com.opensymphony.xwork2.Action;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.service.CertificateService;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileInputStream;

public class CertificateUploadAction extends FPActionSupport {

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
				return Action.INPUT;
			}

			certificateService.addCertificate(alias, description, new FileInputStream(certificateFile));

			return FPActionSupport.REDIRECT_SUCCESS;
		}

		return Action.INPUT;
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

		return Action.INPUT;
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
