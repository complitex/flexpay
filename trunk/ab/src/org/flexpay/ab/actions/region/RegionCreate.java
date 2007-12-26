package org.flexpay.ab.actions.region;

import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.ServletActionContext;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegionCreate extends FPActionSupport implements Preparable {

	private RegionService regionService;
	private CountryService countryService;
	private CountryFilter countryFilter;
	private Region region;
	private Date date;
	private List<RegionNameTranslation> nameTranslations = new ArrayList<RegionNameTranslation>();

	public void prepare() {
		HttpServletRequest request = ServletActionContext.getRequest();
		List<Language> langs = ApplicationConfig.getInstance().getLanguages();
		for (Language lang : langs) {
			RegionNameTranslation nameTranslation = new RegionNameTranslation();
			nameTranslation.setLang(lang);
			nameTranslation.setName(request.getParameter("translation." + lang.getId()));
			nameTranslations.add(nameTranslation);
		}
	}

	public String execute() {
		try {
			countryFilter = countryService.initFilter(countryFilter, userPreferences.getLocale());
			if (isPost()) {
				region = regionService.create(nameTranslations, countryFilter, date);
//				Map session = ActionContext.getContext().getSession();
//				session.put(RegionView.ATTRIBUTE_REGION, region);

				return SUCCESS;
			}
		} catch (FlexPayException e) {
			addActionError(e);
		} catch (FlexPayExceptionContainer container) {
			addActionErrors(container);
		}
		return INPUT;
	}

	/**
	 * Setter for property 'service'.
	 *
	 * @param regionService Value to set for property 'service'.
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

	public String getDate() {
		String dt = DateIntervalUtil.format(date);
		return "-".equals(dt) ? "" : dt;
	}

	public void setDate(String dt) {
		try {
			date = DateIntervalUtil.parse(dt);
		} catch (ParseException e) {
			date = ApplicationConfig.getInstance().getPastInfinite();
		}
	}

	/**
	 * Getter for property 'nameTranslations'.
	 *
	 * @return Value for property 'nameTranslations'.
	 */
	public List<RegionNameTranslation> getNameTranslations() {
		return nameTranslations;
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
	 * Getter for property 'region'.
	 *
	 * @return Value for property 'region'.
	 */
	public Region getRegion() {
		return region;
	}
}
