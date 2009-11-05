package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class TownTypeEditAction extends FPActionSupport {

	private TownType townType = new TownType();
	private Map<Long, String> names = treeMap();
	private Map<Long, String> shortNames = treeMap();

	private String crumbCreateKey;
	private TownTypeService townTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		townType = townType.isNew() ? townType : townTypeService.readFull(stub(townType));

		if (townType == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		if (isNotSubmit()) {
			initNames();
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
			log.debug("Town type created {}", townType);
		} else {
			townTypeService.update(townType);
			log.debug("Town type updated {}", townType);
		}

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
		for (TownTypeTranslation name : townType.getTranslations()) {
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

	@Override
	protected void setBreadCrumbs() {
		if (townType.isNew()) {
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
