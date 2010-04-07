package org.flexpay.admin.action.certificate;

import com.opensymphony.xwork2.Action;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.service.CertificateService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.CertificateFactory;

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
			addActionError(getText("admin.error.certificate.alias_is_empty"));
			return false;
		}

		if (certificateService.aliasExists(alias)) {
			addActionError(getText("admin.error.certificate.alias_exists"));
			return false;
		}

		if (certificateFile == null) {
			addActionError(getText("admin.error.certificate.file_is_empty"));
			return false;
		}

		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			certificateFactory.generateCertificate(new FileInputStream(certificateFile));
		} catch (Exception e) {
			addActionError(getText("admin.error.certificate.file_is_bad"));
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
