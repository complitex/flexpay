package org.flexpay.eirc.actions;

import java.util.ArrayList;
import java.util.List;

import org.flexpay.common.exception.FlexPayException;

public class BuildingAjaxAction {

	private Long streetId;
	private List<BuildingStub> buildingList;

	public String execute() throws FlexPayException {
		buildingList = getBuildingListByStreetId(streetId);

		return "success";
	}

	List<BuildingStub> getBuildingListByStreetId(Long streetId) {
		List<BuildingStub> buildingList = new ArrayList<BuildingStub>();
		if (streetId == null) {
			//return buildingList;
		}

		buildingList.add(new BuildingStub(1L, "1"));
		buildingList.add(new BuildingStub(2L, "2"));
		buildingList.add(new BuildingStub(3L, "3"));

		return buildingList;
	}

	static class BuildingStub {
		private Long id;
		private String name;

		BuildingStub(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
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
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

	}

	/**
	 * @param streetId
	 *            the streetId to set
	 */
	public void setStreetId(Long streetId) {
		this.streetId = streetId;
	}

	/**
	 * @return the buildingList
	 */
	public List<BuildingStub> getBuildingList() {
		return buildingList;
	}

}
