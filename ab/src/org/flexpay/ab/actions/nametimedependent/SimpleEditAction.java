package org.flexpay.ab.actions.nametimedependent;

import com.opensymphony.xwork2.Preparable;
import org.apache.commons.collections.ArrayStack;
import org.apache.struts2.ServletActionContext;
import org.flexpay.common.persistence.*;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Dictionary-object simple editor
 */
public abstract class SimpleEditAction<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends ActionBase<TV, DI, NTD, T> implements Preparable {

	private Date date;
	private NTD object;
	private Long temporalId;
	private List<T> nameTranslations = new ArrayList<T>();

	public Map<Long, T> getTranslations(Long temporalId) {

		DI temporal = nameTimeDependentService.readTemporalName(temporalId);

		if (temporal == null || temporal.getValue() == null) {
			return Collections.emptyMap();
		}

		TemporaryName<TV, T> name = (TemporaryName<TV, T>) temporal.getValue();

		Map<Long, T> map = new HashMap<Long, T>();
		for (T translation : name.getTranslations()) {
			map.put(translation.getLang().getId(), translation);
		}

		return map;
	}

	public void prepare() {
		HttpServletRequest request = ServletActionContext.getRequest();
		temporalId = Long.parseLong(request.getParameter("temporalId"));

		Map<Long, T> langToTranslationMap = getTranslations(temporalId);

		if (isNotSubmit()) {
			DI temporal = nameTimeDependentService.readTemporalName(temporalId);
			log.info("Temporal: {}", temporal);
			date = temporal != null ? temporal.getBegin() : null;
		}

		log.info("Translations map: {}", langToTranslationMap);

		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			T nameTranslation = langToTranslationMap.get(lang.getId());
			if (nameTranslation == null) {
				nameTranslation = nameTimeDependentService.getEmptyNameTranslation();
				nameTranslation.setLang(lang);
			}
			if (isSubmit()) {
				nameTranslation.setName(request.getParameter("translation." + lang.getId()));
			}
			nameTranslations.add(nameTranslation);
		}
		log.info("Translations to edit: {}", nameTranslations);
	}

	@NotNull
	@SuppressWarnings ({"unchecked"})
	public String doExecute() throws Exception {

		if (isSubmit()) {
			object = nameTimeDependentService.updateNameTranslations(object, temporalId, nameTranslations, date);

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

	public String getDate() {
		if (date == null) {
			return "";
		}
		String dt = DateUtil.format(date);
		return "-".equals(dt) ? "" : dt;
	}

	public void setDate(String dt) {
		date = DateUtil.parseDate(dt, ApplicationConfig.getPastInfinite());
	}

	public List<T> getNameTranslations() {
		return nameTranslations;
	}

	public Long getTemporalId() {
		return temporalId;
	}

	public void setTemporalId(Long temporalId) {
		this.temporalId = temporalId;
	}

	public NTD getObject() {
		return object;
	}

	public void setObject(NTD object) {
		this.object = object;
	}

	protected ArrayStack getFilters() {
		return null;
	}

	protected void setFilters(ArrayStack filters) {
	}

}
