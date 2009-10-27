package org.flexpay.ab.actions.country;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

public class CountryCreateAction extends FPActionSupport {

	private Map<Long, String> names = treeMap();
	private Map<Long, String> shortNames = treeMap();
	private Country country = new Country();

	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		if (isNotSubmit()) {
			initData();
			return INPUT;
		}

		List<CountryNameTranslation> countryNames = list();
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			if (lang.isDefault()) {
				boolean error = false;
				if (StringUtils.isEmpty(value)) {
					addActionError(getText("ab.error.country.full_name_is_required"));
					error = true;
				}
				if (StringUtils.isEmpty(shortNames.get(name.getKey()))) {
					addActionError(getText("ab.error.country.short_name_is_required"));
					error = true;
				}
				if (error) {
					return INPUT;
				}
			}
			if (shortNames.get(name.getKey()).length() > CountryNameTranslation.SHORT_NAME_LENGTH) {
				addActionError(getText("ab.error.country.short_name_is_too_long", new String[] {CountryNameTranslation.SHORT_NAME_LENGTH + ""}));
				return INPUT;
			}
			CountryNameTranslation translation = new CountryNameTranslation(value, lang);
			translation.setShortName(shortNames.get(name.getKey()));
			countryNames.add(translation);
		}

		country = countryService.create(countryNames);

		addActionError(getText("ab.country.created"));
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

	private void initData() throws FlexPayException {
		for (Language lang : ApplicationConfig.getLanguages()) {
			names.put(lang.getId(), "");
			shortNames.put(lang.getId(), "");
		}
	}

	public Country getCountry() {
		return country;
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

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
