package org.flexpay.ab.actions.nametimedependent;

import com.opensymphony.xwork2.Preparable;
import org.apache.commons.collections.ArrayStack;
import org.apache.struts2.ServletActionContext;
import org.flexpay.common.persistence.*;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class CreateAction<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends ActionBase<TV, DI, NTD, T> implements Preparable {

	protected List<T> nameTranslations = new ArrayList<T>();
	protected Date date;
	protected NTD object;

	/**
	 * {@inheritDoc}
	 */
	public void prepare() {
		@NonNls
		HttpServletRequest request = ServletActionContext.getRequest();
		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			T nameTranslation = nameTimeDependentService.getEmptyNameTranslation();
			nameTranslation.setLang(lang);
			String name = request.getParameter("translation." + lang.getId());
			if (name != null) {
				nameTranslation.setName(name);
			}
			nameTranslations.add(nameTranslation);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public String doExecute() throws Exception {
		ArrayStack filterArrayStack = getFilters();
		for (Object filter : filterArrayStack) {
			((PrimaryKeyFilter<?>) filter).initFilter(session);
		}
		ArrayStack filters = parentService.initFilters(filterArrayStack,
				userPreferences.getLocale());
		setFilters(filters);
		if (isSubmit()) {
			object = nameTimeDependentService.create(null, nameTranslations, filters, date);

			return SUCCESS;
		}
		return INPUT;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

	public List<T> getNameTranslations() {
		return nameTranslations;
	}

	public String getDate() {
		String dt = DateUtil.format(date);
		return "-".equals(dt) ? "" : dt;
	}

	public void setDate(String dt) {
		date = DateUtil.parseDate(dt, ApplicationConfig.getPastInfinite());
	}

	public NTD getObject() {
		return object;
	}

}
