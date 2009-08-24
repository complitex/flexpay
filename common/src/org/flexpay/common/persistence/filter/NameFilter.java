package org.flexpay.common.persistence.filter;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
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

	public NameFilter(@NotNull Stub<T> stub) {
		super(stub);
	}

	@NotNull
	public Collection<Tr> getNames() {
		return translations;
	}

	public void setNames(@NotNull Collection<Tr> names) {
		this.translations = names;
	}

}
