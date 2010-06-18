package org.flexpay.eirc.dao.impl;

import org.flexpay.eirc.dao.QuittanceReportDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class QuittanceReportDaoImpl extends JdbcDaoSupport implements QuittanceReportDao {

	@SuppressWarnings ({"unchecked"})
    @Override
	public List<Object[]> listPrintInfos(Long serviceOrganizationId, Date begin, Date end) {
		Object params[] = {serviceOrganizationId, begin, end};
		return getJdbcTemplate().query(
				"select q.id, a.id, q.order_number, ap.id, b.id " +
				"from eirc_quittances_tbl q " +
				"	left join eirc_eirc_accounts_tbl a on q.eirc_account_id=a.id" +
				"	left join ab_apartments_tbl ap on a.apartment_id=ap.id" +
				"	left join ab_buildings_tbl b on ap.building_id=b.id " +
				"where b.eirc_service_organization_id=? and q.date_from>=? and q.date_till<=? " +
				"order by b.id, a.id, a.account_number, q.order_number desc",
				params, new RowMapper() {
                    @Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new Object[]{
								rs.getLong(1), // quittance id
								rs.getLong(2), // account id
								rs.getInt(3), // order number
								rs.getLong(4), // apartment id
								rs.getLong(5), // building id
						};
					}
				}
		);
	}
}
