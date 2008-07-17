package org.flexpay.ab.persistence;

import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DomainObject;

import java.util.Collections;
import java.util.Set;

/**
 * BuildingAttributeType
 */
public class BuildingAttributeType extends DomainObject {

	private Set<BuildingAttributeTypeTranslation> translations = Collections.emptySet();

	public BuildingAttributeType() {
	}

	public BuildingAttributeType(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'translations'.
	 *
	 * @return Value for property 'translations'.
	 */
	public Set<BuildingAttributeTypeTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
	public void setTranslations(Set<BuildingAttributeTypeTranslation> translations) {
		this.translations = translations;
	}

	/**
	 * Check if attribute type is a building number
	 *
	 * @return <code>true</code> if attribute type is a building number
	 */
	public boolean isBuildingNumber() {
		BuildingAttributeType type = ApplicationConfig.getBuildingAttributeTypeNumber();
		return getId().equals(type.getId());
	}

	/**
	 * Check if attribute type is a bulk number
	 *
	 * @return <code>true</code> if attribute type is a bulk number
	 */
	public boolean isBulkNumber() {
		BuildingAttributeType type = ApplicationConfig.getBuildingAttributeTypeBulk();
		return equals(type);
	}
}
