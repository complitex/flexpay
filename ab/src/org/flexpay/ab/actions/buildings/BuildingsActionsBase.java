package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.BuildingAttribute;
import org.flexpay.ab.persistence.BuildingAttributeTypeTranslation;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public abstract class BuildingsActionsBase extends FPActionSupport {

	public String getBuildingNumber(@Nullable Collection<BuildingAttribute> attributes) {

		if (attributes == null) {
			return null;
		}

		try {
			StringBuilder number = new StringBuilder();
			for (BuildingAttribute attribute : attributes) {
				if (attribute == null) {
					continue;
				}
				BuildingAttributeTypeTranslation attributeTypeTranslation =
						getTranslation(attribute.getBuildingAttributeType().getTranslations());
				if (attributeTypeTranslation.getShortName() != null) {
					number.append(attributeTypeTranslation.getShortName()).append(' ');
				} else {
					number.append(attributeTypeTranslation.getName()).append(' ');
				}

				number.append(attribute.getValue()).append(' ');
			}

			return number.toString().trim();
		} catch (Exception e) {
			log.error("Exception", e);
			return "error";
		}
	}
}
