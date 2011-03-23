package org.flexpay.common.service.importexport;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;

/**
 * Raw import data for DomainObject
 */
public abstract class RawData<Obj extends DomainObject> implements Serializable {

	private String externalSourceId;
	private Map<String, Serializable> nameToValuesMap = map();

	public String getExternalSourceId() {
		return externalSourceId;
	}

	public void setExternalSourceId(String externalSourceId) {
		this.externalSourceId = externalSourceId;
	}

	public Map<String, Serializable> getNameToValuesMap() {
		return nameToValuesMap;
	}

	public void setNameToValuesMap(Map<String, Serializable> nameToValuesMap) {
		this.nameToValuesMap = nameToValuesMap;
	}

	/**
	 * Add name value attribute pair
	 *
	 * @param name  attribute name
	 * @param value attribute value
	 */
	public void addNameValuePair(String name, Serializable value) {
		nameToValuesMap.put(name, value);
	}

	/**
	 * Get set of valid attribute names
	 *
	 * @return Set of attribute names;
	 */
	public abstract Collection<String> getPossibleNames();

	protected Date getDateParam(String param) {
		Object obj = getNameToValuesMap().get(param);
		return obj == null ? null : (Date) obj;
	}

	protected String getParam(String param) {
		Object obj = getNameToValuesMap().get(param);
		return obj == null ? null : obj.toString();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("externalId", externalSourceId).
				append("values", nameToValuesMap).
				toString();
	}

}
