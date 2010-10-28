package org.flexpay.admin.action.certificate;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.CertificateService;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.CertificateFactory;

public class CertificateEditAction extends FPActionSupport {

	private String alias;
	private Certificate certificate = new Certificate();
	private File certificateFile;
	private String certificateFileContentType;
	private String certificateFileFileName;

	private CertificateService certificateService;
	private UserPreferencesService userPreferencesService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		UserPreferences preference = null;
		try {
			preference = userPreferencesService.loadUserByUsername(alias);
		} catch (Throwable t) {
			log.warn("Search user {}", alias, t);
		}
		if (preference == null) {
			addActionError(getText("admin.error.certificate.user_not_found"));
			return INPUT;
		}

		if (isSubmit() && getText("common.save").equals(submitted)) {
			if (!doValidate()) {
				return INPUT;
			}

			log.debug("certificateFile={}, certificateFileName={}", certificateFile, certificateFileFileName);

			if (certificateFile != null && StringUtils.isNotEmpty(certificateFileFileName) && preference.getCertificate() == null) {
				certificateService.addCertificate(alias, certificate.getDescription(), new FileInputStream(certificateFile));
			} else if (certificateFile != null && StringUtils.isNotEmpty(certificateFileFileName) && preference.getCertificate() != null) {
				certificateService.replaceCertificate(alias, certificate.getDescription(), new FileInputStream(certificateFile));
			} else {
				certificateService.editCertificateDescription(alias, certificate.getDescription());
			}
			//addActionMessage(getText("admin.certificate.edited"));
			return REDIRECT_SUCCESS;
		} else if (preference.getCertificate() != null) {
			certificate = preference.getCertificate();
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Certificate getCertificate() {
		return certificate;
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

	@Required
	public void setUserPreferencesService(UserPreferencesService userPreferencesService) {
		this.userPreferencesService = userPreferencesService;
	}
}
