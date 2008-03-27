package org.flexpay.eirc.dao.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.dao.ConsumerDaoExt;
import org.flexpay.eirc.persistence.Consumer;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConsumersDaoExtJdbcImpl extends SimpleJdbcDaoSupport implements ConsumerDaoExt {

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
	public List<Consumer> findConsumers(Page pager, Long personId, Long serviceId, String accountNumber, Long apartmentId) {
		return getSimpleJdbcTemplate().query(
				"select id from eirc_consumers_tbl " +
				"where person_id=? and service_id=? and external_account_number=? and apartment_id=?",
				new ParameterizedRowMapper<Consumer>() {
					public Consumer mapRow(ResultSet rs, int i) throws SQLException {
						return new Consumer(rs.getLong("id"));
					}
				}, personId, serviceId, accountNumber, apartmentId);
	}
}