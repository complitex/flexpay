package org.flexpay.ab.action.town;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class TownTypeEditAction extends FPActionSupport {

	private TownType townType = new TownType();
	private Map<Long, String> names = treeMap();
	private Map<Long, String> shortNames = treeMap();

	private String crumbCreateKey;
	private TownTypeService townTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (townType == null || townType.getId() == null) {
			log.warn("Incorrect town type id");
			addActionError(getText("ab.error.town_type.incorrect_town_type_id"));
			return REDIRECT_ERROR;
		}

		if (townType.isNotNew()) {
			Stub<TownType> stub = stub(townType);
			townType = townTypeService.readFull(stub);

			if (townType == null) {
				log.warn("Can't get town type with id {} from DB", stub.getId());
				addActionError(getText("ab.error.town_type.cant_get_town_type"));
				return REDIRECT_ERROR;
			} else if (townType.isNotActive()) {
				log.warn("Town type with id {} is disabled", stub.getId());
				addActionError(getText("ab.error.town_type.cant_get_town_type"));
				return REDIRECT_ERROR;
			}

		}

		correctNames();

		if (isNotSubmit()) {
			initData();
			return INPUT;
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			TownTypeTranslation translation = new TownTypeTranslation(value, lang);
			translation.setShortName(shortNames.get(name.getKey()));
			townType.setTranslation(translation);
		}

		if (townType.isNew()) {
			townTypeService.create(townType);
		} else {
			townTypeService.update(townType);
		}

		addActionMessage(getText("ab.town_type.saved"));

		return REDIRECT_SUCCESS;
	}

	private void correctNames() {
		if (names == null) {
			log.warn("Names parameter is null");
			names = treeMap();
		}
		if (shortNames == null) {
			log.warn("Short names parameter is null");
			shortNames = treeMap();
		}
		Map<Long, String> newNames = treeMap();
		Map<Long, String> newShortNames = treeMap();
		for (Language lang : getLanguages()) {
			newNames.put(lang.getId(), names.containsKey(lang.getId()) ? names.get(lang.getId()) : "");
			newShortNames.put(lang.getId(), shortNames.containsKey(lang.getId()) ? shortNames.get(lang.getId()) : "");
		}
		names = newNames;
		shortNames = newShortNames;
	}

	private void initData() {

		for (TownTypeTranslation name : townType.getTranslations()) {
			names.put(name.getLang().getId(), name.getName());
			shortNames.put(name.getLang().getId(), name.getShortName());
		}

		for (Language lang : getLanguages()) {
			if (!names.containsKey(lang.getId())) {
				names.put(lang.getId(), "");
				shortNames.put(lang.getId(), "");
			}
		}
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

	@Override
	protected void setBreadCrumbs() {
		if (townType != null && townType.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}


	public TownType getTownType() {
		return townType;
	}

	public void setTownType(TownType townType) {
		this.townType = townType;
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

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
