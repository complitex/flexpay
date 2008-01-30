package org.flexpay.common.persistence.filter;

import org.flexpay.common.persistence.Translation;

import java.util.Collection;
import java.util.Collections;

public class NameFilter<T extends Translation> extends PrimaryKeyFilter {

	private Collection<T> translations = Collections.emptyList();

	/**
	 * Getter for property 'names'.
	 *
	 * @return Value for property 'names'.
	 */
	public Collection<T> getNames() {
		return translations;
	}

	/**
	 * Setter for property 'names'.
	 *
	 * @param names Value to set for property 'names'.
	 */
	public void setNames(Collection<T> names) {
		this.translations = names;
	}
}
