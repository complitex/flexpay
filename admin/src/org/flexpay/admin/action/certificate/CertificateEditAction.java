package org.flexpay.admin.action.certificate;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.CertificateService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.CertificateFactory;

public class CertificateEditAction extends FPActionSupport {

	private Certificate certificate;

	private String alias;
	private String description;
	private File certificateFile;
	private String certificateFileContentType;
	private String certificateFileFileName;

	private CertificateService certificateService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		certificate = certificateService.readFull(new Stub<Certificate>(certificate.getId()));
		alias = certificate.getAlias();

		if (isSubmit()) {
			if (!doValidate()) {
				return INPUT;
			}

			if (certificateFile != null) {
				certificateService.replaceCertificate(alias, new FileInputStream(certificateFile));
			}

			certificate.setDescription(description);
			certificateService.update(certificate);
			return REDIRECT_SUCCESS;
		} else {			
			description = certificate.getDescription();
		}

		return INPUT;
	}

	private boolean doValidate() {
		
		if (StringUtils.isNotEmpty(certificateFileFileName)) {
			try {
				CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
				certificateFactory.generateCertificate(new FileInputStream(certificateFile));
			} catch (Exception e) {
				addActionError(getText("admin.error.certificate.file_is_bad"));
				return false;
			}
		}

		return true;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
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
