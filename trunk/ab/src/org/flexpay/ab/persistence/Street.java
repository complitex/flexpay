package org.flexpay.ab.persistence;

import org.flexpay.ab.persistence.temp.Buildings;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TimeLine;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * Street
 */
public class Street extends NameTimeDependentChild<StreetName, StreetNameTemporal> {

	private Set<District> districts = Collections.emptySet();
	private TimeLine<TownType, TownTypeTemporal> typesTimeLine;
	private Set<Buildings> buildingses = new HashSet<Buildings>(0);

	public Street() {
	}


}


