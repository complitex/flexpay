package org.flexpay.ab.actions;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegionCreate extends FPActionSupport implements ServletRequestAware {

	private static Logger log = Logger.getLogger(RegionCreate.class);

	private HttpServletRequest request;
	private RegionService regionService;
	private CountryService countryService;
	private static final String[] EMPTY_ARRAY = {};

	private CountryFilter countryFilter = new CountryFilter();
	private boolean isDeleting = false;
	private boolean haveErrors = false;

	/**
	 * {@inheritDoc}
	 */
	public String execute() throws FlexPayException {

		List<RegionName> regionNames = initRegionNames();
		UserPreferences prefs = UserPreferences.getPreferences(request);
		countryFilter = countryService.initFilter(countryFilter, prefs.getLocale());

		boolean needCreate = isPost() && !isDeleting && !haveErrors &&
							 StringUtils.isBlank(request.getParameter("add_name"));

		// Need to create new Region
		if (needCreate) {
			try {
				regionService.create(regionNames, countryFilter);
				return SUCCESS;
			} catch (FlexPayExceptionContainer e) {
				addActionErrors(e);
			}
		}

		request.setAttribute("region_names", regionNames);

		return INPUT;
	}

	private List<RegionName> initRegionNames() throws FlexPayException {

		List<RegionName> regionNames = new ArrayList<RegionName>();

		String[] ids = getIds();
		for (String id : ids) {
			initRegionName(id, regionNames);
		}

		if (!isPost()
			|| StringUtils.isNotBlank(request.getParameter("add_name"))
			|| regionNames.isEmpty()) {
			initRegionName("new_", regionNames);
		}

		return regionNames;
	}

	private void initRegionName(String id, List<RegionName> regionNames)
			throws FlexPayException {

		if (request.getParameter("delete_" + id) != null) {
			isDeleting = true;
			return;
		}

		RegionName regionName = new RegionName();
		setRegionId(id, regionName);

		regionName.setBegin(getDateParam(request, "from_" + id));
		regionName.setEnd(getDateParam(request, "till_" + id));
		if (!DateIntervalUtil.isValid(regionName)) {
			addActionError(getText("error.invalid_interval", DateIntervalUtil.format(regionName)));
			haveErrors = true;
		}

		Set<RegionNameTranslation> translations = initRegionNameTranslations(id);
		regionName.setTranslations(translations);

		regionNames.add(regionName);
	}

	private Set<RegionNameTranslation> initRegionNameTranslations(String id)
			throws FlexPayException {

		List<Language> langs = ApplicationConfig.getInstance().getLanguages();
		UserPreferences prefs = UserPreferences.getPreferences(request);
		Set<RegionNameTranslation> translations = new HashSet<RegionNameTranslation>();

		for (Language lang : langs) {
			RegionNameTranslation translation = new RegionNameTranslation();
			translation.setLang(lang);
			LangNameTranslation languageName = LanguageUtil.getLanguageName(lang, prefs.getLocale());
			translation.setLangTranslation(languageName);

			if (isPost()) {
				translation.setName(request.getParameter("name_" + id + "_" + lang.getId()));
			}

			translations.add(translation);
		}

		return translations;
	}

	private void setRegionId(String id, RegionName name) {
		// New region, set it empty
		if (id.startsWith("new_")) {
			return;
		}

		log.info("id: " + id);

		name.setId(Long.parseLong(id));
	}

	/**
	 * Get array of ids in view separated by spaces
	 *
	 * @return array of ids in view
	 */
	private String[] getIds() {

		String idsParam = request.getParameter("ids");
		if (StringUtils.isBlank(idsParam)) {
			return EMPTY_ARRAY;
		}

		return StringUtils.split(idsParam);
	}

	/**
	 * Setter for property 'regionService'.
	 *
	 * @param regionService Value to set for property 'regionService'.
	 */
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	/**
	 * Setter for property 'countryService'.
	 *
	 * @param countryService Value to set for property 'countryService'.
	 */
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Getter for property 'post'.
	 *
	 * @return Value for property 'post'.
	 */
	private boolean isPost() {
		return "post".equalsIgnoreCase(request.getMethod());
	}

	/**
	 * Getter for property 'countryFilter'.
	 *
	 * @return Value for property 'countryFilter'.
	 */
	public CountryFilter getCountryFilter() {
		return countryFilter;
	}

	/**
	 * Setter for property 'countryFilter'.
	 *
	 * @param countryFilter Value to set for property 'countryFilter'.
	 */
	public void setCountryFilter(CountryFilter countryFilter) {
		this.countryFilter = countryFilter;
	}
}
