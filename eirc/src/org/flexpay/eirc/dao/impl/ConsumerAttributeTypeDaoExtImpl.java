package org.flexpay.eirc.dao.impl;

import org.flexpay.eirc.dao.ConsumerAttributeTypeDaoExt;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeEnum;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeSimple;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

public class ConsumerAttributeTypeDaoExtImpl extends HibernateDaoSupport implements ConsumerAttributeTypeDaoExt {

	@Override
	public ConsumerAttributeTypeSimple readFullSimpleType(Long id) {
		return (ConsumerAttributeTypeSimple) uniqueResult((List<?>) getHibernateTemplate().
                findByNamedQuery("ConsumerAttributeTypeBase.readFull", id));
	}

	@Override
	public ConsumerAttributeTypeEnum readFullEnumType(Long id) {
		ConsumerAttributeTypeEnum type = (ConsumerAttributeTypeEnum) uniqueResult((List<?>) getHibernateTemplate().
                findByNamedQuery("ConsumerAttributeTypeBase.readFull", id));
		if (type != null) {
			getHibernateTemplate().initialize(type.getValues());
		}
		return type;
	}

	@Override
	public ConsumerAttributeTypeBase findAttributeTypeByCode(String code) {
		return (ConsumerAttributeTypeBase) uniqueResult((List<?>) getHibernateTemplate().
                findByNamedQuery("ConsumerAttributeTypeBase.findAttributeTypeByCode", code));
	}
}
