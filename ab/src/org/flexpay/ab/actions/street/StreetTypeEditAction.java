package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class StreetTypeEditAction extends FPActionSupport {

	private StreetTypeService streetTypeService;

	private StreetType streetType = new StreetType();
	private Map<Long, String> names = CollectionUtils.treeMap();
	private Map<Long, String> shortNames = CollectionUtils.treeMap();

	@NotNull
	public String doExecute() throws Exception {

		if (streetType.getId() == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		StreetType type = streetType.isNew() ? streetType : streetTypeService.read(streetType.getId());

		if (!isSubmit()) {
			streetType = type;
			initNames();
			return INPUT;
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			StreetTypeTranslation translation = new StreetTypeTranslation();
			translation.setLang(lang);
			translation.setName(value);
			translation.setShortName(shortNames.get(name.getKey()));
			type.setTranslation(translation);
		}

		streetTypeService.save(type);

		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	private void initNames() {
		for (StreetTypeTranslation name : streetType.getTranslations()) {
			names.put(name.getLang().getId(), name.getName());
			shortNames.put(name.getLang().getId(), name.getShortName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
			shortNames.put(lang.getId(), "");
		}
	}

	public StreetType getStreetType() {
		return streetType;
	}

	public void setStreetType(StreetType streetType) {
		this.streetType = streetType;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public Map<Long, String> getShortNames() {
		return shortNames;
	}

	public void setShortNames(Map<Long, String> shortNames) {
		this.shortNames = shortNames;
	}

	/**
	 * Setter for property 'streetTypeService'.
	 *
	 * @param streetTypeService Value to set for property 'streetTypeService'.
	 */
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}
}
