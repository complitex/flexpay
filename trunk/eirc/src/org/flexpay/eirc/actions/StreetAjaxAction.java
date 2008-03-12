package org.flexpay.eirc.actions;

import java.util.ArrayList;
import java.util.List;

import org.flexpay.common.exception.FlexPayException;

public class StreetAjaxAction {
	
	private String streetVar;
	private List<StreetStub> streetList;
	
	
	public String execute() throws FlexPayException {
		streetList = getStreetByVar(streetVar);

		return "success";
	}
	
	private List<StreetStub> getStreetByVar(String streetVar) {
		// TODO realize logic
		
		List<StreetStub> streetList = new ArrayList<StreetStub>();
		
		streetList.add(new StreetStub(1L, "Banana street"));
		streetList.add(new StreetStub(2L, "Apple street"));
		streetList.add(new StreetStub(3L, "Wall street"));
		streetList.add(new StreetStub(4L, "Bulvar Kapucinov"));
		
		return streetList;
	}

	/**
	 * @param streetVar the streetVar to set
	 */
	public void setStreetVar(String streetVar) {
		this.streetVar = streetVar;
	}

	/**
	 * @return the streetList
	 */
	public List<StreetStub> getStreetList() {
		return streetList;
	}
	
	static class StreetStub {
		private Long id;
		private String name;
		
		StreetStub(Long id, String name) {
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
		
	}



}
