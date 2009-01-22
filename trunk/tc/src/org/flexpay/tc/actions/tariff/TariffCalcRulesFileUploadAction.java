package org.flexpay.tc.actions.tariff;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.FPFileType;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.persistence.TariffCalculationRulesFileTranslation;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.util.Map;

public class TariffCalcRulesFileUploadAction implements UserPreferencesAware, SessionAware {

	@NonNls
	protected Logger log = LoggerFactory.getLogger(getClass());

	private Map sessionMap;
	private String rnd;
	private String stringResult;
	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	private Map<Long, String> names = CollectionUtils.treeMap();
	private Map<Long, String> descriptions = CollectionUtils.treeMap();

    private String moduleName;
	private TariffCalculationRulesFileService tariffCalculationRulesFileService;
    private FPFileService fpFileService;
	protected UserPreferences userPreferences;

	@NotNull
	public String execute() {

		if (uploadFileName == null) {
			log.warn("Error: uploadFileName is null");
			setStringResult(ActionSupport.ERROR);
			return ActionSupport.ERROR;
		}

		FPFileType fileType = fpFileService.getTypeByFileName(uploadFileName, moduleName);
		if (fileType == null) {
			log.warn("Unknown file type");
			setStringResult(ActionSupport.ERROR);
			return ActionSupport.ERROR;
		}

		try {
			TariffCalculationRulesFile file = new TariffCalculationRulesFile();
			file.setType(fileType);
			file.setFileStatus(fpFileService.getStatusByCodeAndModule(TariffCalculationRulesFile.IMPORTED_FILE_STATUS, moduleName));
			FPFile fileOnServer = new FPFile();
			fileOnServer.setModule(fpFileService.getModuleByName(moduleName));
			fileOnServer.setOriginalName(uploadFileName);
			fileOnServer.setUserName(getUserPreferences().getUserName());
			File fileOnSystem = FPFileUtil.saveToFileSystem(fileOnServer, upload);
			fileOnServer.setNameOnServer(fileOnSystem.getName());
			fileOnServer.setSize(fileOnSystem.length());
			file.setFile(fileOnServer);
			file.setUserName(getUserPreferences().getUserName());

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

			tariffCalculationRulesFileService.save(file);
			log.info("File uploaded {}", file);
			setStringResult(ActionSupport.SUCCESS);
		} catch (Exception e) {
			log.error("Unknown file type", e);
			setStringResult(ActionSupport.ERROR);
		}

		return ActionSupport.SUCCESS;
	}

	public Language getLang(@NotNull Long id) {
		for (Language lang : ApplicationConfig.getLanguages()) {
			if (id.equals(lang.getId())) {
				return lang;
			}
		}

		return null;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setSession(Map sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getRnd() {
		return rnd;
	}

	public void setRnd(String rnd) {
		this.rnd = rnd;
	}

	public String getStringResult() {
		return stringResult;
	}

	public void setStringResult(String stringResult) {
		this.stringResult = stringResult;
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

