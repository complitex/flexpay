package org.flexpay.ab.actions.nametimedependent;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.*;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * Region simple editor
 */
public abstract class SimpleEditAction<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends ActionBase<TV, DI, NTD, T> implements Preparable {

	private static Logger log = Logger.getLogger(SimpleEditAction.class);

	private Date date;
	private NTD object;
	private Long temporalId;
	private List<T> nameTranslations = new ArrayList<T>();

	public void prepare() {
		HttpServletRequest request = ServletActionContext.getRequest();
		temporalId = Long.parseLong(request.getParameter("temporalId"));

		Map<Long, T> langToTranslationMap =
				nameTimeDependentService.getTranslations(temporalId);

		if (!isPost()) {
			DI temporal = nameTimeDependentService.readTemporalName(temporalId);
			log.info("Temporal: " + temporal);
			date = temporal != null ? temporal.getBegin() : null;
		}

		if (log.isInfoEnabled()) {
			log.info("Translations map: " + langToTranslationMap);
		}

		List<Language> langs = ApplicationConfig.getInstance().getLanguages();
		for (Language lang : langs) {
			T nameTranslation = langToTranslationMap.get(lang.getId());
			if (nameTranslation == null) {
				nameTranslation = nameTimeDependentService.getEmptyNameTranslation();
				nameTranslation.setLang(lang);
			}
			if (isPost()) {
				nameTranslation.setName(request.getParameter("translation." + lang.getId()));
			}
			nameTranslations.add(nameTranslation);
		}
		if (log.isInfoEnabled()) {
			log.info("Translations to edit: " + nameTranslations);
		}
	}

	@SuppressWarnings ({"unchecked"})
	public String execute() {
		try {
			if (isPost()) {
				object = nameTimeDependentService.updateNameTranslations(object, temporalId, nameTranslations, date);
				Map session = ActionContext.getContext().getSession();
				session.put(ObjectViewAction.ATTRIBUTE_OBJECT, object);

				return SUCCESS;
			}
		} catch (FlexPayExceptionContainer container) {
			addActionErrors(container);
		}
		return INPUT;
	}

	public String getDate() {
		if (date == null) {
			return "";
		}
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
	public List<T> getNameTranslations() {
		return nameTranslations;
	}

	/**
	 * Getter for property 'temporalId'.
	 *
	 * @return Value for property 'temporalId'.
	 */
	public Long getTemporalId() {
		return temporalId;
	}

	/**
	 * Setter for property 'temporalId'.
	 *
	 * @param temporalId Value to set for property 'temporalId'.
	 */
	public void setTemporalId(Long temporalId) {
		this.temporalId = temporalId;
	}

	/**
	 * Getter for property 'region'.
	 *
	 * @return Value for property 'region'.
	 */
	public NTD getObject() {
		return object;
	}

	/**
	 * Setter for property 'region'.
	 *
	 * @param object Value to set for property 'region'.
	 */
	public void setObject(NTD object) {
		this.object = object;
	}

	/**
	 * Get initial set of filters for action
	 *
	 * @return Collection of filters
	 */
	protected Collection<PrimaryKeyFilter> getFilters() {
		return null;
	}

	/**
	 * Set filters for action
	 *
	 * @param filters collection of filters
	 */
	protected void setFilters(Collection<PrimaryKeyFilter> filters) {
	}
}
