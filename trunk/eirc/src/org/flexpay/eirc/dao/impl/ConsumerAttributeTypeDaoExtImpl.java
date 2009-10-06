package org.flexpay.eirc.dao.impl;

import org.flexpay.eirc.dao.ConsumerAttributeTypeDaoExt;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeSimple;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeEnum;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.dao.support.DataAccessUtils;

public class ConsumerAttributeTypeDaoExtImpl extends HibernateDaoSupport implements ConsumerAttributeTypeDaoExt {

	@Override
	public ConsumerAttributeTypeSimple readFullSimpleType(Long id) {
		return (ConsumerAttributeTypeSimple) DataAccessUtils.uniqueResult(getHibernateTemplate()
				.findByNamedQuery("ConsumerAttributeTypeBase.readFull", id));
	}

	@Override
	public ConsumerAttributeTypeEnum readFullEnumType(Long id) {
		ConsumerAttributeTypeEnum type = (ConsumerAttributeTypeEnum) DataAccessUtils.uniqueResult(
				getHibernateTemplate().findByNamedQuery("ConsumerAttributeTypeBase.readFull", id));
		if (type != null) {
			getHibernateTemplate().initialize(type.getValues());
		}
		return type;
	}

	@Override
	public ConsumerAttributeTypeBase findAtributeTypeByCode(String code) {
		return (ConsumerAttributeTypeBase) DataAccessUtils.uniqueResult(getHibernateTemplate()
				.findByNamedQuery("ConsumerAttributeTypeBase.findAtributeTypeByCode", code));
	}
}
