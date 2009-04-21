package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.AddressAttribute;
import org.flexpay.ab.persistence.AddressAttributeTypeTranslation;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@Deprecated
public abstract class BuildingsActionsBase extends FPActionWithPagerSupport {

	public String getBuildingNumber(@Nullable Collection<AddressAttribute> attributes) {

		if (attributes == null) {
			return null;
		}

		try {
			StringBuilder number = new StringBuilder();
			for (AddressAttribute attribute : attributes) {
				if (attribute == null) {
					continue;
				}
				AddressAttributeTypeTranslation attributeTypeTranslation =
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
			return ERROR;
		}
	}

}
