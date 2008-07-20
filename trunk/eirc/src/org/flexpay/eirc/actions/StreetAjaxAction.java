package org.flexpay.eirc.actions;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.util.config.ApplicationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO rename to a more meaningfull name or delete
 *
 * @deprecated
 */
public class StreetAjaxAction extends FPActionSupport {

	private StreetService streetService;
	private List<StreetVis> streetVisList;
	private String streetVar;

	public String doExecute() throws FlexPayException {
		List<Street> streetList = streetService.findByTownAndName(ApplicationConfig.getDefaultTownStub(), streetVar + "%");

		streetVisList = new ArrayList<StreetVis>();
		for (Street street : streetList) {
			StreetVis streetVis = new StreetVis();
			streetVis.setId(street.getId());
			streetVis.setName(getTranslation(
					street.getCurrentName().getTranslations()).getName());
			streetVis.setType(getTranslation(
					street.getCurrentType().getTranslations()).getName());
			streetVisList.add(streetVis);
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	protected String getErrorResult() {
		return SUCCESS;
	}

	/**
	 * @param streetVar the streetVar to set
	 */
	public void setStreetVar(String streetVar) {
		this.streetVar = streetVar;
	}

	/**
	 * @param streetService the streetService to set
	 */
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	public static class StreetVis {
		private Long id;
		private String name;
		private String type;

		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
	}

	/**
	 * @return the streetVisList
	 */
	public List<StreetVis> getStreetVisList() {
		return streetVisList;
	}

}
