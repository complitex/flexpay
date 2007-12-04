package org.flexpay.ab.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

public class TownTypesDelete implements ServletRequestAware {

	private static Logger log = Logger.getLogger(TownTypesDelete.class);

	private HttpServletRequest request;
	private TownTypeService townTypeService;

	public String execute() throws Exception {

		if (log.isDebugEnabled()) {
			log.debug(request.getParameterMap().toString());
		}

		Collection<TownType> townTypesToDisable = new ArrayList<TownType>();
		for (TownType townType : townTypeService.getTownTypes()) {
			Long id = townType.getId();
			String param = request.getParameter("town_type_" + id);

			if (log.isDebugEnabled()) {
				log.debug("TownType: " + id + ", param: " + param);
			}

			if (StringUtils.isBlank(param)) {
				continue;
			}
			townTypesToDisable.add(townType);
		}

		if (!townTypesToDisable.isEmpty()) {
			townTypeService.disable(townTypesToDisable);
		} else {
			log.debug("No town types found to disable");
		}

		return ActionSupport.SUCCESS;
	}

	/**
	 * Sets the HTTP request object in implementing classes.
	 *
	 * @param request the HTTP request.
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Setter for property 'townTypeService'.
	 *
	 * @param townTypeService Value to set for property 'townTypeService'.
	 */
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}
}
