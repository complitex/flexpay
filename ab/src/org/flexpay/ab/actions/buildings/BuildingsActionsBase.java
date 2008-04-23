package org.flexpay.ab.actions.buildings;

import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.BuildingAttribute;
import org.flexpay.ab.persistence.BuildingAttributeTypeTranslation;
import org.flexpay.common.actions.FPActionSupport;

import java.util.Collection;

public abstract class BuildingsActionsBase extends FPActionSupport {

	private static Logger log = Logger.getLogger(BuildingsActionsBase.class);

	public String getBuildingNumber(Collection<BuildingAttribute> attributes) {

		try {
			log.debug("Getting building number");

			StringBuilder number = new StringBuilder();
			for (BuildingAttribute attribute : attributes) {
				if (attribute == null) {
					continue;
				}
				BuildingAttributeTypeTranslation attributeTypeTranslation =
						getTranslation(attribute.getBuildingAttributeType().getTranslations());
				if (attributeTypeTranslation.getShortName() != null) {
					number.append(attributeTypeTranslation.getShortName() + ' ');
				} else {
					number.append(attributeTypeTranslation.getName()).append(' ');
				}

				number.append(attribute.getValue()).append(' ');
			}

			if (log.isDebugEnabled()) {
				log.debug("Building: " + number);
			}

			return number.toString().trim();
		} catch (Exception e) {
			log.error("Exception", e);
			return "error";
		}
	}
}
