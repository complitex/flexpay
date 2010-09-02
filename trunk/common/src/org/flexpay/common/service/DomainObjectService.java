package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface DomainObjectService<T extends DomainObject> {

	@NotNull
	T newInstance();

	@NotNull
	T create(@NotNull T obj) throws FlexPayExceptionContainer;

	@NotNull
	T update(@NotNull T obj) throws FlexPayExceptionContainer;

	void disable(@NotNull Collection<Long> ids);

	@Nullable
	T readFull(@NotNull Stub<? extends T> stub);

    @NotNull
    List<T> readFull(@NotNull Collection<Long> ids, boolean preserveOrder);

	Class<? extends T> getType();

	// todo uncomment findObjects methods
//	@NotNull
//	List<T> findObjects(Page<T> pager);
//
//	@NotNull
//	List<T> findObjects(FetchRange range);
}
