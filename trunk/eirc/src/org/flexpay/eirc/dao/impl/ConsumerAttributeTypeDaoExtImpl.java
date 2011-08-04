package org.flexpay.eirc.dao.impl;

import org.flexpay.eirc.dao.ConsumerAttributeTypeDaoExt;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeEnum;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeSimple;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

public class ConsumerAttributeTypeDaoExtImpl extends JpaDaoSupport implements ConsumerAttributeTypeDaoExt {

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public ConsumerAttributeTypeSimple readFullSimpleType(Long id) {
		return (ConsumerAttributeTypeSimple) uniqueResult((List<?>) getJpaTemplate().
                findByNamedQuery("ConsumerAttributeTypeBase.readFull", id));
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public ConsumerAttributeTypeEnum readFullEnumType(Long id) {
		return (ConsumerAttributeTypeEnum) uniqueResult((List<?>) getJpaTemplate().
                findByNamedQuery("ConsumerAttributeTypeBase.readFull", id));
		// TODO read values
		/*
		if (type != null) {
			getHibernateTemplate().initialize(type.getValues());
		}

		return type;
		*/
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public ConsumerAttributeTypeBase findAttributeTypeByCode(String code) {
		return (ConsumerAttributeTypeBase) uniqueResult((List<?>) getJpaTemplate().
                findByNamedQuery("ConsumerAttributeTypeBase.findAttributeTypeByCode", code));
	}
}
