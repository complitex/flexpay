package org.flexpay.tc.actions.sewertype;

import org.flexpay.bti.persistence.SewerType;
import org.flexpay.bti.persistence.SewerTypeTranslation;
import org.flexpay.bti.service.SewerTypesService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class SewerTypeEditAction extends FPActionSupport {

	private SewerType sewerType = new SewerType();
	private Map<Long, String> names = CollectionUtils.treeMap();
	private Map<Long, String> descriptions = CollectionUtils.treeMap();

	private SewerTypesService sewerTypesService;

	@NotNull
	public String doExecute() {

		if (sewerType.getId() == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		SewerType type = sewerType.isNew() ? sewerType : sewerTypesService.getSewerType(stub(sewerType));

		if (!isSubmit()) {
			sewerType = type;
			initNames();
			return INPUT;
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			SewerTypeTranslation translation = new SewerTypeTranslation();
			translation.setLang(lang);
			translation.setName(value);
			translation.setDescription(descriptions.get(name.getKey()));
			type.setTranslation(translation);
		}

		sewerTypesService.save(type);

		return REDIRECT_SUCCESS;

	}

	private void initNames() {
		for (SewerTypeTranslation name : sewerType.getTranslations()) {
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
		return SUCCESS;
	}

	public SewerType getSewerType() {
		return sewerType;
	}

	public void setSewerType(SewerType sewerType) {
		this.sewerType = sewerType;
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

	@Required
	public void setSewerTypesService(SewerTypesService sewerTypesService) {
		this.sewerTypesService = sewerTypesService;
	}

}
