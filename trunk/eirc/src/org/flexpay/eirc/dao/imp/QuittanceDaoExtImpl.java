package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.dao.QuittanceDaoExt;
import static org.flexpay.common.util.CollectionUtils.ar;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.apache.log4j.Logger;

import java.util.Date;

public class QuittanceDaoExtImpl extends JdbcDaoSupport implements QuittanceDaoExt {

	private Logger log = Logger.getLogger(getClass());

	/**
	 * Generate current snapshot of details and create quittances for the following processing
	 *
	 * @param dateFrom Period begin date
	 * @param dateTill Period end date
	 * @return number of generated quittances
	 */
	public long createQuittances(Date dateFrom, Date dateTill) {

		Date now = new Date();
		long count = generateQuittances(dateFrom, dateTill, now);

		long detailsCount = generateDetailsReferences(dateFrom, dateTill, now);
		long updatedCount = updateOrderNumbers(dateFrom, dateTill, now);

		if (log.isInfoEnabled()) {
			log.info(String.format("Generated quittances statistics.\n" +
								   "Total quittances: %d\n" +
								   "Total details: %d\n" +
								   "Total quittance numbers updated: %d", count, detailsCount, updatedCount));
		}

		return count;
	}

	private long generateQuittances(Date dateFrom, Date dateTill, Date now) {
		String insertSql = "insert into eirc_quittances_tbl " +
						   "(service_organisation_id, eirc_account_id, order_number, date_from, date_till, creation_date) " +
						   "select b.eirc_service_organisation_id, acc.id, 0, ?, ?, ? " +
						   "from eirc_eirc_accounts_tbl acc " +
						   "	inner join ab_apartments_tbl ap on ap.id=acc.apartment_id " +
						   "	inner join ab_buildings_tbl b on b.id=ap.building_id " +
						   "where acc.status=0";
		return getJdbcTemplate().update(insertSql, ar(dateFrom, dateTill, now));
	}

	private long generateDetailsReferences(Date dateFrom, Date dateTill, Date now) {
		String insertSql = "insert into eirc_quittance_details_quittances_tbl " +
						   "(quittance_id, quittance_details_id) " +
						   "select q.id, qd.id " +
						   "from eirc_quittances_tbl q " +
						   "	inner join eirc_eirc_accounts_tbl acc on acc.id=q.eirc_account_id " +
						   "	left join eirc_consumers_tbl con on con.eirc_account_id=acc.id " +
						   "	left join eirc_quittance_details_tbl qd on qd.consumer_id=con.id " +
						   "where q.order_number=0 and q.date_from=? and q.date_till=? " +
						   "	and q.creation_date=? and qd.month>=? and qd.month<?";
		return getJdbcTemplate().update(insertSql, ar(dateFrom, dateTill, now, dateFrom, dateTill));
	}

	private long updateOrderNumbers(Date dateFrom, Date dateTill, Date now) {

		String insertSql = "update eirc_quittances_tbl set order_number=(" +
						   "	select max(q.order_number) + 1 as next_number " +
						   "	from eirc_quittances_tbl q " +
						   "	where q.eirc_account_id=eirc_account_id and q.date_till=?) " +
						   "where order_number=0 and date_from=? and date_till=? and creation_date=?";
		return getJdbcTemplate().update(insertSql, ar(dateTill, dateFrom, dateTill, now));
	}
}
