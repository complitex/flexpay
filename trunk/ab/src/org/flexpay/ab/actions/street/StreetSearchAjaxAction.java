package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Search streets by name
 */
public class StreetSearchAjaxAction extends FPActionSupport {

	private StreetService streetService;
	private List<StreetVis> streetVisList;
	private String searchString;
	private Town town = new Town();

	@NotNull
	public String doExecute() throws FlexPayException {
		List<Street> streetList = streetService.findByTownAndName(
				stub(town), "%" + searchString + "%");

		streetVisList = new ArrayList<StreetVis>();
		for (Street street : streetList) {
			StreetVis streetVis = new StreetVis();
			streetVis.setId(street.getId());
			streetVis.setName(getTranslation(
					street.getCurrentName().getTranslations()).getName());
			streetVis.setType(getTranslation(
					street.getCurrentType().getTranslations()).getShortName());
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

	/**
	 * @param searchString the streetVar to set
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public Town getTown() {
		return town;
	}

	public void setTown(Town town) {
		this.town = town;
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
