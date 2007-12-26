package org.flexpay.ab.actions.region;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Region simple editor
 */
public class RegionEditSimple extends FPActionSupport implements Preparable {

	private static Logger log = Logger.getLogger(RegionEditSimple.class);

	private RegionService regionService;
	private Date date;
	private Region region = new Region();
	private Long temporalId;
	private List<RegionNameTranslation> nameTranslations = new ArrayList<RegionNameTranslation>();

	public void prepare() {
		HttpServletRequest request = ServletActionContext.getRequest();
		temporalId = Long.parseLong(request.getParameter("temporalId"));

		Map<Long, RegionNameTranslation> langToTranslationMap =
				regionService.getTranslations(temporalId);

		if (!isPost()) {
			RegionNameTemporal temporal = regionService.readRegionNameTemporal(temporalId);
			log.info("Temporal: " + temporal);
			date = temporal != null ? temporal.getBegin() : null;
		}

		if (log.isInfoEnabled()) {
			log.info("Translations map: " + langToTranslationMap);
		}

		List<Language> langs = ApplicationConfig.getInstance().getLanguages();
		for (Language lang : langs) {
			RegionNameTranslation nameTranslation = langToTranslationMap.get(lang.getId());
			if (nameTranslation == null) {
				nameTranslation = new RegionNameTranslation();
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
				region = regionService.save(region, temporalId, nameTranslations, date);
				Map session = ActionContext.getContext().getSession();
				session.put(RegionView.ATTRIBUTE_REGION, region);

				return SUCCESS;
			}
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
	public List<RegionNameTranslation> getNameTranslations() {
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
	public Region getRegion() {
		return region;
	}

	/**
	 * Setter for property 'region'.
	 *
	 * @param region Value to set for property 'region'.
	 */
	public void setRegion(Region region) {
		this.region = region;
	}
}
