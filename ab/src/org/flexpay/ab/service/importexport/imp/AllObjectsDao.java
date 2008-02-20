package org.flexpay.ab.service.importexport.imp;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.flexpay.common.persistence.DomainObject;

public class AllObjectsDao {

    private HibernateTemplate hibernateTemplate;

    public void save(DomainObject domainObject) {
        hibernateTemplate.saveOrUpdate(domainObject);
    }

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
