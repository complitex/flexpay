package org.flexpay.common.util.config;

import net.sourceforge.navigator.menu.MenuComponent;
import org.flexpay.common.persistence.Language;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class UserPreferences {

	private String userName = "";
	private Locale locale;

	private Integer pageSize = 20;

	private MenuComponent menuComponent = new MenuComponent();

	private Long countryFilterValue;
	private Long regionFilterValue;
	private Long townFilterValue;
	private Long districtFilterValue;
	private Long streetFilterValue;
	private Long buildingFilterValue;
	private Long apartmentFilterValue;

	private static final String WW_TRANS_I18_N_LOCALE = "WW_TRANS_I18N_LOCALE";


	/**
	 * Get current user session preferences
	 *
	 * @param request Http request
	 * @return UserPreferences
	 */
	public static UserPreferences getPreferences(HttpServletRequest request) {
		UserPreferences prefs = (UserPreferences) WebUtils.getSessionAttribute(
				request, ApplicationConfig.USER_PREFERENCES_SESSION_ATTRIBUTE);

		// Not found in session, create default one
		if (prefs == null) {
			prefs = new UserPreferences();
			Language lang = ApplicationConfig.getDefaultLanguage();
			prefs.setLocale(lang.getLocale());
		}

		Locale locale = (Locale) WebUtils.getSessionAttribute(request, WW_TRANS_I18_N_LOCALE);
		if (locale != null) {
			prefs.setLocale(locale);
		}

		return prefs;
	}

	/**
	 * Save user preferences
	 *
	 * @param request	 Http request
	 * @param preferences User preferences
	 */
	public static void setPreferences(HttpServletRequest request, UserPreferences preferences) {
		WebUtils.setSessionAttribute(request,
				ApplicationConfig.USER_PREFERENCES_SESSION_ATTRIBUTE, preferences);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public MenuComponent getMenuComponent() {
		return menuComponent;
	}

	public void setMenuComponent(MenuComponent menuComponent) {
		this.menuComponent = menuComponent;
	}

	public Long getCountryFilterValue() {
		return countryFilterValue;
	}

	public void setCountryFilterValue(Long countryFilterValue) {
		this.countryFilterValue = countryFilterValue;
	}

	public Long getRegionFilterValue() {
		return regionFilterValue;
	}

	public void setRegionFilterValue(Long regionFilterValue) {
		this.regionFilterValue = regionFilterValue;
	}

	public Long getTownFilterValue() {
		return townFilterValue;
	}

	public void setTownFilterValue(Long townFilterValue) {
		this.townFilterValue = townFilterValue;
	}

	public Long getDistrictFilterValue() {
		return districtFilterValue;
	}

	public void setDistrictFilterValue(Long districtFilterValue) {
		this.districtFilterValue = districtFilterValue;
	}

	public Long getStreetFilterValue() {
		return streetFilterValue;
	}

	public void setStreetFilterValue(Long streetFilterValue) {
		this.streetFilterValue = streetFilterValue;
	}

	public Long getBuildingFilterValue() {
		return buildingFilterValue;
	}

	public void setBuildingFilterValue(Long buildingFilterValue) {
		this.buildingFilterValue = buildingFilterValue;
	}

	public Long getApartmentFilterValue() {
		return apartmentFilterValue;
	}

	public void setApartmentFilterValue(Long apartmentFilterValue) {
		this.apartmentFilterValue = apartmentFilterValue;
	}

}
