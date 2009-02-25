package org.flexpay.common.persistence.filter;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class NameFilter<T extends DomainObject, Tr extends Translation> extends PrimaryKeyFilter<T> {

	private Collection<Tr> translations = Collections.emptyList();

	public NameFilter() {
	}

	public NameFilter(Long selectedId) {
		super(selectedId);
	}

	/**
	 * Getter for property 'names'.
	 *
	 * @return Value for property 'names'.
	 */
	@NotNull
	public Collection<Tr> getNames() {
		return translations;
	}

	/**
	 * Setter for property 'names'.
	 *
	 * @param names Value to set for property 'names'.
	 */
	public void setNames(@NotNull Collection<Tr> names) {
		this.translations = names;
	}
}
