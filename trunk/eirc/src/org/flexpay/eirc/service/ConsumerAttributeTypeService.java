package org.flexpay.eirc.service;

import org.flexpay.common.service.DomainObjectService;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface ConsumerAttributeTypeService extends DomainObjectService<ConsumerAttributeTypeBase> {

	/**
	 * Find attribute type by unique code
	 *
	 * @param code Unique attribute type code
	 * @return Attribute type
	 */
	@Nullable
	ConsumerAttributeTypeBase readByCode(String code);

    @NotNull
    List<ConsumerAttributeTypeBase> getByUniqueCode(Collection<String> codes);

}
