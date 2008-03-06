package org.flexpay.eirc.actions;

import java.util.ArrayList;
import java.util.List;

import org.flexpay.common.exception.FlexPayException;

public class CorrespondenceExternalAction {
	
	private List<String> elementList; // TODO It's just stub
	
	public String execute() throws FlexPayException {
		elementList = retrieveElementList();

		return "success";
	}
	
	private List<String> retrieveElementList() {
		// TODO It's just stub
		List<String> elementList = new ArrayList<String>();
		elementList.add("record1");
		elementList.add("record2");
		elementList.add("record3");
		elementList.add("record4");
		elementList.add("record5");
		elementList.add("record6");
		
		return elementList;
	}

	/**
	 * @return the elementList
	 */
	public List<String> getElementList() {
		return elementList;
	}
	
	


}
