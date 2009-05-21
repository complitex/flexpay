package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.dao.EircRegistryRecordPropertiesDaoExt;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class EircRegistryRecordPropertiesDaoExtImpl extends HibernateDaoSupport implements EircRegistryRecordPropertiesDaoExt {

	public EircRegistryRecordProperties find(Long propertiesId) {
		return (EircRegistryRecordProperties) DataAccessUtils.uniqueResult(getHibernateTemplate()
				.findByNamedQuery("EircRegistryRecordProperties.find", propertiesId));
	}

}
