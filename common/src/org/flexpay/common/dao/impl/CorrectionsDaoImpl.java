package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.CorrectionsDao;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.sql.SQLException;

public class CorrectionsDaoImpl implements CorrectionsDao {

	private HibernateTemplate hibernateTemplate;

	/**
	 * Create or update data correction
	 *
	 * @param correction DataCorrection
	 */
	public void save(DataCorrection correction) {
		hibernateTemplate.saveOrUpdate(correction);
	}

	/**
	 * Delete correction
	 *
	 * @param correction DataCorrection
	 */
	public void delete(DataCorrection correction) {
		hibernateTemplate.delete(correction);
	}

	/**
	 * Find domain object by correction
	 *
	 * @param externalId		DataSource internal object id
	 * @param type			  DomainObject type
	 * @param cls			   DomainObject class to retrive
	 * @param sourceDescription Data source description
	 * @return DomainObject
	 */
	public DomainObject findCorrection(String externalId, int type,
									   final Class cls, DataSourceDescription sourceDescription) {

		final DetachedCriteria objectIdCriteria = DetachedCriteria.forClass(DataCorrection.class)
				.add(Restrictions.eq("objectType", type))
				.add(Restrictions.eq("externalId", externalId))
				.add(
						sourceDescription != null ?
						Restrictions.eq("dataSourceDescription", sourceDescription) :
						Restrictions.isNull("dataSourceDescription"))
				.setProjection(Property.forName("internalObjectId"));

		return (DomainObject) hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria cr = session.createCriteria(cls);
				Criteria cr1 = cr.add(Subqueries.propertyEq("id", objectIdCriteria));
				return cr1.uniqueResult();
			}
		});
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}