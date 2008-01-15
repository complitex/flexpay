package org.flexpay.ab.actions.nametimedependent;

import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.ServletActionContext;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.*;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public abstract class CreateAction<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends ActionBase<TV, DI, NTD, T> implements Preparable {

	private List<T> nameTranslations = new ArrayList<T>();
	private Date date;
	private NTD object;

	/**
	 * {@inheritDoc}
	 */
	public void prepare() {
		HttpServletRequest request = ServletActionContext.getRequest();
		List<Language> langs = ApplicationConfig.getInstance().getLanguages();
		for (Language lang : langs) {
			T nameTranslation = nameTimeDependentService.getEmptyNameTranslation();
			nameTranslation.setLang(lang);
			nameTranslation.setName(request.getParameter("translation." + lang.getId()));
			nameTranslations.add(nameTranslation);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String execute() {
		try {
			Collection<PrimaryKeyFilter> filters = parentService.initFilters(getFilters(), userPreferences.getLocale());
			setFilters(filters);
			if (isPost()) {
				object = nameTimeDependentService.create(nameTranslations, filters, date);

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
	 * Getter for property 'nameTranslations'.
	 *
	 * @return Value for property 'nameTranslations'.
	 */
	public List<T> getNameTranslations() {
		return nameTranslations;
	}

	/**
	 * Getter for property 'date'.
	 *
	 * @return Value for property 'date'.
	 */
	public String getDate() {
		String dt = DateIntervalUtil.format(date);
		return "-".equals(dt) ? "" : dt;
	}

	/**
	 * Setter for property 'date'.
	 *
	 * @param dt Value to set for property 'date'.
	 */
	public void setDate(String dt) {
		try {
			date = DateIntervalUtil.parse(dt);
		} catch (ParseException e) {
			date = ApplicationConfig.getInstance().getPastInfinite();
		}
	}

	/**
	 * Getter for property 'region'.
	 *
	 * @return Value for property 'region'.
	 */
	public NTD getObject() {
		return object;
	}
}
