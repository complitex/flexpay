package org.flexpay.common.util.config;

import net.sourceforge.navigator.menu.MenuComponent;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.actions.breadcrumbs.Crumb;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Stack;
import java.io.Serializable;

public class UserPreferences implements Serializable {

	private String userName = "";
	private Locale locale;

	private Integer pageSize = 20;
	private String testProp = "setup-me";

	private MenuComponent menuComponent = new MenuComponent();

	private String countryFilterValue;
	private String regionFilterValue;
	private String townFilterValue;
	private String districtFilterValue;
	private String streetFilterValue;
	private String buildingFilterValue;
	private String apartmentFilterValue;

	private String activeMenu;
	private Stack<Crumb> crumbs = new Stack<Crumb>();

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

	public String getCountryFilterValue() {
		return countryFilterValue;
	}

	public void setCountryFilterValue(String countryFilterValue) {
		this.countryFilterValue = countryFilterValue;
	}

	public String getRegionFilterValue() {
		return regionFilterValue;
	}

	public void setRegionFilterValue(String regionFilterValue) {
		this.regionFilterValue = regionFilterValue;
	}

	public String getTownFilterValue() {
		return townFilterValue;
	}

	public void setTownFilterValue(String townFilterValue) {
		this.townFilterValue = townFilterValue;
	}

	public String getDistrictFilterValue() {
		return districtFilterValue;
	}

	public void setDistrictFilterValue(String districtFilterValue) {
		this.districtFilterValue = districtFilterValue;
	}

	public String getStreetFilterValue() {
		return streetFilterValue;
	}

	public void setStreetFilterValue(String streetFilterValue) {
		this.streetFilterValue = streetFilterValue;
	}

	public String getBuildingFilterValue() {
		return buildingFilterValue;
	}

	public void setBuildingFilterValue(String buildingFilterValue) {
		this.buildingFilterValue = buildingFilterValue;
	}

	public String getApartmentFilterValue() {
		return apartmentFilterValue;
	}

	public void setApartmentFilterValue(String apartmentFilterValue) {
		this.apartmentFilterValue = apartmentFilterValue;
	}

	public String getTestProp() {
		return testProp;
	}

	public void setTestProp(String testProp) {
		this.testProp = testProp;
	}

	public String getActiveMenu() {
		return activeMenu;
	}

	public void setActiveMenu(String activeMenu) {
		this.activeMenu = activeMenu;
	}

	public Stack<Crumb> getCrumbs() {
		return crumbs;
	}

	public void setCrumbs(Stack<Crumb> crumbs) {
		this.crumbs = crumbs;
	}

}
