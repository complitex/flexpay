package org.flexpay.tc.action.tariff;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.file.FPFileType;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.persistence.TariffCalculationRulesFileTranslation;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;

public class TariffCalcRulesFileEditAction extends FPActionSupport {

	private TariffCalculationRulesFile rulesFile = new TariffCalculationRulesFile();
	private Map<Long, String> names = CollectionUtils.treeMap();
	private Map<Long, String> descriptions = CollectionUtils.treeMap();

	private File upload;
	private String uploadFileName;
	private String uploadContentType;

	private String crumbCreateKey;
	private String moduleName;
	private TariffCalculationRulesFileService tariffCalculationRulesFileService;
	private FPFileService fpFileService;

	@NotNull
	@Override
	public String doExecute() {

		rulesFile = rulesFile.isNew() ? rulesFile : tariffCalculationRulesFileService.read(stub(rulesFile));

		if (rulesFile == null) {
			addActionError(getText("common.error.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		if (isNotSubmit()) {
			initNames();
			return INPUT;
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			Long key = name.getKey();
			String value = name.getValue();
			Language lang = getLang(key);
			if (lang.isDefault() && StringUtils.isEmpty(value)) {
				addActionError(getText("tc.error.rules_files.name_is_required"));
				return INPUT;
			}
			TariffCalculationRulesFileTranslation translation = new TariffCalculationRulesFileTranslation(value, lang);
			translation.setDescription(descriptions.get(key));
			log.debug("translation: {}", translation);
			rulesFile.setTranslation(translation);
		}

		for (TariffCalculationRulesFileTranslation t : rulesFile.getTranslations()) {
			log.debug("translation: {} - {}", t.getName(), t.getDescription());
		}

		String userName = SecurityUtil.getUserName();
		if (rulesFile.isNew()) {

			if (uploadFileName == null) {
				log.warn("Error: uploadFileName is null");
				addActionError(getText("tc.error.upload_file_name_is_null"));
				return INPUT;
			}

			FPFileType fileType = fpFileService.getTypeByFileName(uploadFileName, moduleName);
			if (fileType == null) {
				log.warn("Unknown file type");
				addActionError(getText("tc.error.unknown_file_type"));
				return INPUT;
			}

			rulesFile.setType(fileType);
			FPFile fileOnServer = new FPFile();
			fileOnServer.setModule(fpFileService.getModuleByName(moduleName));
			fileOnServer.setOriginalName(uploadFileName);
			fileOnServer.setUserName(userName);
			try {
				FPFileUtil.copy(upload, fileOnServer);
			} catch (Exception e) {
				log.error("Error creating file on server", e);
				addActionError(getText("tc.error.cant_create_file_on_system"));
				return INPUT;
			}
			rulesFile.setFile(fileOnServer);
			rulesFile.setUserName(userName);

		}

		if (rulesFile.isNew()) {
			tariffCalculationRulesFileService.create(rulesFile);
			log.debug("Tariff calculation rules file created {}", rulesFile);
		} else {
			tariffCalculationRulesFileService.update(rulesFile);
			log.debug("Tariff calculation rules file updated {}", rulesFile);
		}

		return REDIRECT_SUCCESS;
	}

	private void initNames() {
		for (TariffCalculationRulesFileTranslation name : rulesFile.getTranslations()) {
			names.put(name.getLang().getId(), name.getName());
			descriptions.put(name.getLang().getId(), name.getDescription());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
			descriptions.put(lang.getId(), "");
		}
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	@Override
	protected void setBreadCrumbs() {
		if (rulesFile.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public TariffCalculationRulesFile getRulesFile() {
		return rulesFile;
	}

	public void setRulesFile(TariffCalculationRulesFile rulesFile) {
		this.rulesFile = rulesFile;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public Map<Long, String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Map<Long, String> descriptions) {
		this.descriptions = descriptions;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setTariffCalculationRulesFileService(TariffCalculationRulesFileService tariffCalculationRulesFileService) {
		this.tariffCalculationRulesFileService = tariffCalculationRulesFileService;
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

}
