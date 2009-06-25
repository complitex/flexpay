package org.flexpay.tc.actions.tariff;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.file.FPFileType;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
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

public class TariffCalcRulesFileEditAction extends FPActionSupport {

	private TariffCalculationRulesFile rulesFile = new TariffCalculationRulesFile();
	private Map<Long, String> names = CollectionUtils.treeMap();
	private Map<Long, String> descriptions = CollectionUtils.treeMap();

	private File upload;
	private String uploadFileName;
	private String uploadContentType;

	private String moduleName;
	private TariffCalculationRulesFileService tariffCalculationRulesFileService;
	private FPFileService fpFileService;

	@NotNull
	public String doExecute() {

		if (rulesFile.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		TariffCalculationRulesFile file = rulesFile.isNew() ? rulesFile : tariffCalculationRulesFileService.read(stub(rulesFile));
		if (file == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		if (!isSubmit()) {
			rulesFile = file;
			initNames();
			return INPUT;
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			TariffCalculationRulesFileTranslation translation = new TariffCalculationRulesFileTranslation();
			translation.setLang(lang);
			translation.setName(value);
			translation.setDescription(descriptions.get(name.getKey()));
			file.setTranslation(translation);
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

			file.setType(fileType);
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
			file.setFile(fileOnServer);
			file.setUserName(userName);

		}

		tariffCalculationRulesFileService.save(file);

		log.info("Tariff calculation rules file created {}", file);

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
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
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
