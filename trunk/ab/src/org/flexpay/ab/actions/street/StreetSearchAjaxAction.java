package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * Search streets by name
 */
public class StreetSearchAjaxAction extends FPActionSupport {

	private String q;
	private String townId;
	private List<StreetVis> streetVisList;

	private StreetService streetService;

	@NotNull
	public String doExecute() throws FlexPayException {

		Long townIdLong;

		try {
			townIdLong = Long.parseLong(townId);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", townId);
			return SUCCESS;
		}

		List<Street> streets = streetService.findByTownAndQuery(new Stub<Town>(townIdLong), "%" + q + "%");
		log.debug("Found streets: {}", streets);

		streetVisList = new ArrayList<StreetVis>();
		for (Street street : streets) {
			StreetVis streetVis = new StreetVis();
			streetVis.setId(street.getId());
			streetVis.setName(getTranslation(street.getCurrentName().getTranslations()).getName());
			streetVis.setType(getTranslation(street.getCurrentType().getTranslations()).getShortName());
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
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	@Override
	protected void setBreadCrumbs() {

	}

	public void setQ(String q) {
		this.q = q;
	}

	public void setTownId(String townId) {
		this.townId = townId;
	}

	public List<StreetVis> getStreetVisList() {
		return streetVisList;
	}

	public static class StreetVis {

		private Long id;
		private String name;
		private String type;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

}
