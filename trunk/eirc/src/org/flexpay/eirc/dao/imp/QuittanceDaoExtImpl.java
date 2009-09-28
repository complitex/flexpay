package org.flexpay.eirc.dao.imp;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.job.JobExecutionContext;
import org.flexpay.common.process.job.JobExecutionContextHolder;
import static org.flexpay.common.util.CollectionUtils.ar;
import org.flexpay.eirc.dao.QuittanceDaoExt;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.Date;

public class QuittanceDaoExtImpl extends JdbcDaoSupport implements QuittanceDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Generate current snapshot of details and create quittances for the following processing
	 *
	 * @param organizationStub ServiceOrganization stub to generate quittances for
	 * @param townStub		 Town stub to generate quittances in
	 * @param dateFrom		 Period begin date
	 * @param dateTill		 Period end date
	 * @return number of generated quittances
	 */
	public long createQuittances(Stub<EircServiceOrganization> organizationStub, Stub<Town> townStub, Date dateFrom, Date dateTill) {

		JobExecutionContext executionContext = JobExecutionContextHolder.getContext();
		if (executionContext != null) {
			executionContext.setTotalSize(3);
		}
		
		Date now = new Date();
		long count = generateQuittances(townStub.getId(), organizationStub.getId(), dateFrom, dateTill, now);
		if (executionContext != null) {
			executionContext.setComplete(1);
		}

		long detailsCount = generateDetailsReferences(dateFrom, dateTill, now);
		if (executionContext != null) {
			executionContext.setComplete(2);
		}

		long updatedCount = updateOrderNumbers(dateFrom, dateTill, now);
		if (executionContext != null) {
			executionContext.setComplete(3);
		}

		log.info(String.format("Generated quittances statistics.\n" +
							   "Total quittances: %d\n" +
							   "Total details: %d\n" +
							   "Total quittance numbers updated: %d", count, detailsCount, updatedCount));

		return count;
	}

	private long generateQuittances(Long townId, Long organisationId, Date dateFrom, Date dateTill, Date now) {
		String insertSql = "insert into eirc_quittances_tbl " +
						   "(service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date) " +
						   "select b.eirc_service_organization_id, acc.id, 0, ?, ?, ? " +
						   "from eirc_eirc_accounts_tbl acc " +
						   "	inner join ab_apartments_tbl ap on ap.id=acc.apartment_id " +
						   "	inner join ab_buildings_tbl b on b.id=ap.building_id " +
						   "	inner join ab_districts_tbl d on b.district_id=d.id " +
						   "	inner join ab_towns_tbl t on t.id=d.town_id " +
						   "where t.id=? and b.eirc_service_organization_id=? and acc.status=0";
		return getJdbcTemplate().update(insertSql, ar(dateFrom, dateTill, now, townId, organisationId));
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

		String tmpTable = "create temporary table tmp_eirc_quittances_tbl ( " +
						  "	order_number BIGINT, " +
						  "	eirc_account_id BIGINT " +
						  ")";
		getJdbcTemplate().update(tmpTable);

		String tmpIndex = "alter table tmp_eirc_quittances_tbl add index I_account_id (eirc_account_id)";
		getJdbcTemplate().update(tmpIndex);

//		getJdbcTemplate().batchUpdate(ar(tmpTable, tmpIndex));

		String sqlTempQuittances = "insert into tmp_eirc_quittances_tbl (order_number, eirc_account_id) " +
								   "(select max(q.order_number), q.eirc_account_id " +
								   "from eirc_quittances_tbl q " +
								   "where q.date_till>=? " +
								   "group by q.eirc_account_id)";
		getJdbcTemplate().update(sqlTempQuittances, ar(dateTill));

		String insertSql = "update eirc_quittances_tbl q " +
						   "	inner join tmp_eirc_quittances_tbl t on (q.eirc_account_id = t.eirc_account_id) " +
						   "set q.order_number=(t.order_number + 1) " +
						   "where q.order_number=0 and q.date_from=? and q.date_till=? and q.creation_date=?";
		long count = getJdbcTemplate().update(insertSql, ar(dateFrom, dateTill, now));

		getJdbcTemplate().update("drop table tmp_eirc_quittances_tbl");

		return count;
	}
}
