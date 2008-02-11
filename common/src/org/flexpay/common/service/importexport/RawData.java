package org.flexpay.common.service.importexport;

import org.flexpay.common.persistence.DomainObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Raw import data for DomainObject
 */
public abstract class RawData<Obj extends DomainObject> {

	private String externalSourceId;
	private Map<String, Object> nameToValuesMap = new HashMap<String, Object>();

	/**
	 * Getter for property 'externalSourceId'.
	 *
	 * @return Value for property 'externalSourceId'.
	 */
	public String getExternalSourceId() {
		return externalSourceId;
	}

	/**
	 * Setter for property 'externalSourceId'.
	 *
	 * @param externalSourceId Value to set for property 'externalSourceId'.
	 */
	public void setExternalSourceId(String externalSourceId) {
		this.externalSourceId = externalSourceId;
	}

	/**
	 * Getter for property 'nameToValuesMap'.
	 *
	 * @return Value for property 'nameToValuesMap'.
	 */
	public Map<String, Object> getNameToValuesMap() {
		return nameToValuesMap;
	}

	/**
	 * Setter for property 'nameToValuesMap'.
	 *
	 * @param nameToValuesMap Value to set for property 'nameToValuesMap'.
	 */
	public void setNameToValuesMap(Map<String, Object> nameToValuesMap) {
		this.nameToValuesMap = nameToValuesMap;
	}

	/**
	 * Add name value attribute pair
	 * 
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void addNameValuePair(String name, Object value) {
		nameToValuesMap.put(name, value);
	}

	/**
	 * Get set of valid attribute names
	 *
	 * @return Set of attribute names;
	 */
	public abstract Collection<String> getPossibleNames();
}
