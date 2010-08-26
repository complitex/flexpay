package org.flexpay.eirc.service;

import org.flexpay.common.service.DomainObjectService;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface ConsumerAttributeTypeService extends DomainObjectService<ConsumerAttributeTypeBase> {

	@NotNull
	@Override
	ConsumerAttributeTypeBase newInstance();

	@NotNull
	@Override
	ConsumerAttributeTypeBase create(@NotNull ConsumerAttributeTypeBase obj) throws FlexPayExceptionContainer;

	@NotNull
	@Override
	ConsumerAttributeTypeBase update(@NotNull ConsumerAttributeTypeBase obj) throws FlexPayExceptionContainer;

	@Override
	void disable(@NotNull Collection<Long> ids);

	@Override
	ConsumerAttributeTypeBase readFull(@NotNull Stub<? extends ConsumerAttributeTypeBase> stub);

	@Override
	Class<? extends ConsumerAttributeTypeBase> getType();

	/**
	 * Find attribute type by unique code
	 *
	 * @param code Unique attribute type code
	 * @return Attribute type
	 */
	@Nullable
	ConsumerAttributeTypeBase readByCode(String code);

}
