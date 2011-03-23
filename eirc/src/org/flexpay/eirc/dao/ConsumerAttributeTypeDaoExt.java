package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeEnum;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeSimple;

public interface ConsumerAttributeTypeDaoExt {

	ConsumerAttributeTypeSimple readFullSimpleType(Long id);

	ConsumerAttributeTypeEnum readFullEnumType(Long id);

	ConsumerAttributeTypeBase findAttributeTypeByCode(String code);
}
