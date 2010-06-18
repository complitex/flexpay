package org.flexpay.eirc.dao.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.dao.ConsumerDaoExt;
import org.flexpay.eirc.persistence.Consumer;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConsumersDaoExtJdbcImpl extends SimpleJdbcDaoSupport implements ConsumerDaoExt {

	private HibernateTemplate hibernateTemplate;

	/**
	 * Find consumer by example data
	 *
	 * @param pager		 Page
	 * @param personId	  Responsible person id
	 * @param serviceId	 Consumer service id
	 * @param accountNumber Consumer external account number
	 * @param apartmentId   Apartment id
	 * @return list of consumers
	 */
    @Override
	public List<Consumer> findConsumers(Page<?> pager, Long personId, Long serviceId, String accountNumber, Long apartmentId) {
		return getSimpleJdbcTemplate().query(
				"select id from eirc_consumers_tbl " +
				"where person_id=? and service_id=? and external_account_number=? and apartment_id=?",
				new ParameterizedRowMapper<Consumer>() {
                    @Override
					public Consumer mapRow(ResultSet rs, int i) throws SQLException {
						return new Consumer(rs.getLong("id"));
					}
				}, personId, serviceId, accountNumber, apartmentId);
	}

	/**
	 * Find Consumer by provider identifier, account number and service type code
	 *
	 * @param providerId	Provider key
	 * @param accountNumber Service provider internal account number
	 * @param code		  Service type code
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
    @Override
	public Consumer findConsumerByTypeCode(Long providerId, String accountNumber, Long code) {
		Object[] params = {providerId, accountNumber, code};
		List<?> results = hibernateTemplate.findByNamedQuery("Consumer.findConsumersByServiceTypeCode", params);
		return results.isEmpty() ? null : (Consumer) results.get(0);
	}

	/**
	 * Find Consumer by provider identifier, account number and external service code
	 *
	 * @param providerId	Provider key
	 * @param accountNumber Service provider internal account number
	 * @param code		  Service provider external code code
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
    @Override
	public Consumer findConsumerByProviderServiceCode(Long providerId, String accountNumber, String code) {
		Object[] params = {providerId, accountNumber, code};
		List<?> results = hibernateTemplate.findByNamedQuery("Consumer.findConsumersByProviderServiceCode", params);
		return results.isEmpty() ? null : (Consumer) results.get(0);
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}