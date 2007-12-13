package org.flexpay.ab.actions;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.Region;
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
import java.util.*;

public class RegionEdit extends FPActionSupport implements ServletRequestAware, SessionAware {

	private static Logger log = Logger.getLogger(RegionEdit.class);

	private HttpServletRequest request;
	private Map<String, Object> session;

	private RegionService regionService;
	private CountryService countryService;

	private CountryFilter countryFilter = new CountryFilter();
	private boolean isDeleting = false;
	private boolean haveErrors = false;

	private Long id;

	/**
	 * {@inheritDoc}
	 */
	public String execute() throws FlexPayException {

		if (id == null) {
			// No id specified, redirect successfully
			log.debug("No id specified, redirecting to success view");
			addActionError(getText("error.region_no_id"));
			RegionsList.setActionErrors(session, getActionErrors());
			return SUCCESS;
		}

		Region region = regionService.read(id);
		if (region == null) {
			addActionError(getText("error.region_invalid_id"));
			TownTypesList.setActionErrors(session, getActionErrors());
			return SUCCESS;
		}

		Collection<RegionName> regionNames = initRegionNames(region);

		UserPreferences prefs = UserPreferences.getPreferences(request);
		countryFilter = countryService.initFilter(countryFilter, prefs.getLocale());

		boolean needCreate = isPost() && !isDeleting && !haveErrors &&
							 StringUtils.isBlank(request.getParameter("add_name"));

		// Need to create a new Region
		if (needCreate) {
			try {
				regionService.update(region, regionNames, countryFilter);
				return SUCCESS;
			} catch (FlexPayExceptionContainer e) {
				addActionErrors(e);
			}
		}

		request.setAttribute("region_names", regionNames);

		return INPUT;
	}

	private Collection<RegionName> initRegionNames(Region region) throws FlexPayException {

		List<RegionName> regionNames = new ArrayList<RegionName>();
		regionNames.addAll(region.getNames());

		String[] ids = getIds(regionNames);
		for (String id : ids) {
			initRegionName(id, regionNames);
		}

		if ((isPost() && StringUtils.isNotBlank(request.getParameter("add_name")))
				|| regionNames.isEmpty()) {
			initRegionName("new_", regionNames);
		}

		return regionNames;
	}

	private void initRegionName(String id, List<RegionName> regionNames)
			throws FlexPayException {

		RegionName regionName = getRegionName(id, regionNames);

		if (request.getParameter("delete_" + id) != null) {
			isDeleting = true;
			regionNames.remove(regionName);
			return;
		}

		if (isPost()) {
			regionName.setBegin(getDateParam(request, "from_" + id));
			regionName.setEnd(getDateParam(request, "till_" + id));
			if (!DateIntervalUtil.isValid(regionName)) {
				addActionError(getText(
						"error.invalid_interval", DateIntervalUtil.format(regionName)));
				haveErrors = true;
			}
		}

		Set<RegionNameTranslation> translations = initRegionNameTranslations(id, regionName);
		regionName.setTranslations(translations);

		if (regionNames.contains(regionName)) {
			regionNames.remove(regionName);
		}
		regionNames.add(regionName);
	}

	private Set<RegionNameTranslation> initRegionNameTranslations(String id, RegionName regionName)
			throws FlexPayException {

		List<Language> langs = ApplicationConfig.getInstance().getLanguages();
		UserPreferences prefs = UserPreferences.getPreferences(request);
		Set<RegionNameTranslation> translations = new HashSet<RegionNameTranslation>();

		for (Language lang : langs) {
			RegionNameTranslation translation = getRegionNameTranslation(lang, regionName);
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

	private RegionNameTranslation getRegionNameTranslation(Language lang, RegionName regionName) {
		for (RegionNameTranslation nameTranslation : regionName.getTranslations()) {
			if (nameTranslation.getLang().equals(lang)) {
				return nameTranslation;
			}
		}

		return new RegionNameTranslation();
	}

	private RegionName getRegionName(String id, Collection<RegionName> names) {
		// New region, set it empty
		if (id.startsWith("new_")) {
			return new RegionName();
		}

		for (RegionName name : names) {
			if (id.equals(name.getId().toString())) {
				return name;
			}
		}

		return null;
	}

	/**
	 * Get array of ids in view separated by spaces
	 *
	 * @param regionNames Collection of existing region names
	 * @return array of ids in view
	 */
	private String[] getIds(Collection<RegionName> regionNames) {

		Set<String> ids = new HashSet<String>();

		// add region names to the list if get request recieved
		if (!isPost()) {
			for (RegionName name : regionNames) {
				ids.add(name.getId().toString());
			}
		}

		String idsParam = request.getParameter("ids");
		if (StringUtils.isNotBlank(idsParam)) {
			ids.addAll(Arrays.asList(StringUtils.split(idsParam)));
		}

		// Filter region names that do no participate in view, were deleted or whatever
		Collection<RegionName> viewNames = new ArrayList<RegionName>();
		for (RegionName name : regionNames) {
			String nameId = name.getId().toString();
			if (ids.contains(nameId)) {
				viewNames.add(name);
			}
		}
		regionNames.clear();
		regionNames.addAll(viewNames);

		return ids.toArray(new String[ids.size()]);
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

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter for property 'id'.
	 *
	 * @param id Value to set for property 'id'.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	@SuppressWarnings ({"unchecked"})
	public void setSession(Map session) {
		this.session = session;
	}
}
