package org.flexpay.eirc.dao.impl;

import org.flexpay.common.process.job.JobExecutionContext;
import org.flexpay.common.process.job.JobExecutionContextHolder;
import org.flexpay.eirc.dao.QuittanceDaoExt;
import org.flexpay.eirc.persistence.account.Quittance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class QuittanceDaoExtImpl extends SimpleJdbcDaoSupport implements QuittanceDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Generate current snapshot of details and create quittances for the following processing
	 *
	 * @param options Operation options
	 * @return number of generated quittances
	 */
    @Override
	public long createQuittances(CreateQuittancesOptions options) {

		log.debug("Creating quittances: {}", options);

		JobExecutionContext executionContext = JobExecutionContextHolder.getContext();
		long totalSize = 3;
		if (options.deleteEmptyQuittances) {
			++totalSize;
		}
		if (executionContext != null) {
			executionContext.setTotalSize(totalSize);
		}

		Date now = new Date();
		long count = generateQuittances(options.townStub.getId(), options.organizationStub.getId(),
				options.dateFrom, options.dateTill, now);
		long nStep = 0;
		if (executionContext != null) {
			executionContext.setComplete(++nStep);
		}

		long detailsCount = generateDetailsReferences(options.dateFrom, options.dateTill, now);
		if (executionContext != null) {
			executionContext.setComplete(++nStep);
		}

		long updatedCount = updateOrderNumbers(options.dateFrom, options.dateTill, now);
		if (executionContext != null) {
			executionContext.setComplete(++nStep);
		}

		long emptyQuittancesDeleted = 0;
		if (options.deleteEmptyQuittances) {
			emptyQuittancesDeleted = deleteEmptyQuittances();
			if (executionContext != null) {
				executionContext.setComplete(++nStep);
			}
		}

        log.info("Generated quittances statistics.\nTotal quittances: {}\nTotal details: {}\n" +
                 "Total quittance numbers updated: {}\nTotal empty quittances deleted: {}",
                new Object[] {count, detailsCount, updatedCount, emptyQuittancesDeleted});

		return count;
	}

	private long generateQuittances(Long townId, Long organisationId, Date dateFrom, Date dateTill, Date now) {

/*
        log.debug("townId = {}, organizationId = {}", townId, organisationId);
        log.debug("dateFrom = {}, dateTill = {}", dateFrom, dateTill);
        log.debug("now = {}", now);
*/

		String insertSql = "insert into eirc_quittances_tbl " +
						   "(service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date) " +
						   "select b.eirc_service_organization_id, acc.id, 0, ?, ?, ? " +
						   "from eirc_eirc_accounts_tbl acc " +
						   "	inner join ab_apartments_tbl ap on ap.id=acc.apartment_id " +
						   "	inner join ab_buildings_tbl b on b.id=ap.building_id " +
						   "	inner join ab_districts_tbl d on b.district_id=d.id " +
						   "where d.town_id=? and b.eirc_service_organization_id=? and acc.status=0";
		return getJdbcTemplate().update(insertSql, dateFrom, dateTill, now, townId, organisationId);
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
		return getJdbcTemplate().update(insertSql, dateFrom, dateTill, now, dateFrom, dateTill);
	}

	private long updateOrderNumbers(Date dateFrom, Date dateTill, Date now) {

		String tmpTable = "create temporary table tmp_eirc_quittances_tbl ( " +
						  "	order_number BIGINT, " +
						  "	eirc_account_id BIGINT " +
						  ")";
		getJdbcTemplate().update(tmpTable);

		String tmpIndex = "alter table tmp_eirc_quittances_tbl add index I_account_id (eirc_account_id)";
		getJdbcTemplate().update(tmpIndex);

		String sqlTempQuittances = "insert into tmp_eirc_quittances_tbl (order_number, eirc_account_id) " +
								   "(select max(q.order_number), q.eirc_account_id " +
								   "from eirc_quittances_tbl q " +
								   "where q.date_till>=? " +
								   "group by q.eirc_account_id)";
		getJdbcTemplate().update(sqlTempQuittances, dateTill);

		String insertSql = "update eirc_quittances_tbl q " +
						   "	inner join tmp_eirc_quittances_tbl t on (q.eirc_account_id = t.eirc_account_id) " +
						   "set q.order_number=(t.order_number + 1) " +
						   "where q.order_number=0 and q.date_from=? and q.date_till=? and q.creation_date=?";
		long count = getJdbcTemplate().update(insertSql, dateFrom, dateTill, now);

		getJdbcTemplate().update("drop table tmp_eirc_quittances_tbl");

		return count;
	}

	private long deleteEmptyQuittances() {
		String deleteSQL = "delete q " +
						   "from eirc_quittances_tbl q " +
						   "	left outer join eirc_quittance_details_quittances_tbl qd on qd.quittance_id=q.id " +
						   "where qd.id is null";
		return (long) getJdbcTemplate().update(deleteSQL);
	}

    @NotNull
    @Override
    public List<Quittance> findQuittancesByEIRCAccounts(Collection<Long> accountIds) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("accountIds", accountIds);

        return getSimpleJdbcTemplate().query("select id " +
                           "from eirc_quittances_tbl q " +
                           "    inner join (" +
                           "        select q1.eirc_account_id, max(q1.order_number) as maxOrderNumber, max(q1.date_from) as maxDateFrom " +
                           "        from eirc_quittances_tbl q1 " +
                           "        where q1.eirc_account_id in (:accountIds) " +
                           "        group by q1.eirc_account_id " +
                           "    ) qj on q.eirc_account_id = qj.eirc_account_id and q.order_number = qj.maxOrderNumber and q.date_From = qj.maxDateFrom " +
                           "order by q.date_from desc",	new RowMapper<Quittance>() {
                            @Override
                            public Quittance mapRow(ResultSet rs, int i) throws SQLException {
                                log.debug("ResultSet = {}", rs);
                                return new Quittance(rs.getLong("id"));
                            }
                        }, parameters);
    }

    @NotNull
    @Override
    public List<Quittance> findQuittancesByEIRCAccountsAndServiceType(Collection<Long> accountIds, Long serviceTypeId) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("accountIds", accountIds);
        parameters.addValue("serviceTypeId", serviceTypeId);

        return getSimpleJdbcTemplate().query("select id " +
                           "from eirc_quittances_tbl q " +
                           "    inner join (" +
                           "        select q1.eirc_account_id, max(q1.order_number) as maxOrderNumber, max(q1.date_from) as maxDateFrom " +
                           "        from eirc_quittances_tbl q1 " +
                           "            inner join eirc_eirc_accounts_tbl a on q1.eirc_account_id=a.id " +
                           "            left join eirc_quittance_details_quittances_tbl qdq on q.id=qdq.quittance_id " +
                           "            left join eirc_quittance_details_tbl qd on qd.id=qdq.quittance_details_id " +
                           "            left join eirc_consumers_tbl c on c.id=qd.consumer_id " +
                           "            left join payments_services_tbl s on s.id=c.service_id " +
                           "        where q1.eirc_account_id in (:accountIds) and s.type_id=:serviceTypeId " +
                           "        group by q1.eirc_account_id " +
                           "    ) qj on q.eirc_account_id = qj.eirc_account_id and q.order_number = qj.maxOrderNumber and q.date_From = qj.maxDateFrom " +
                           "order by q.date_from desc", new RowMapper<Quittance>() {
                            @Override
                            public Quittance mapRow(ResultSet rs, int i) throws SQLException {
                                log.debug("ResultSet = {}", rs);
                                return new Quittance(rs.getLong("id"));
                            }
                        }, parameters);
    }

    @Override
    public List<Quittance> findQuittances(Collection<Long> consumerIds) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("consumerIds", consumerIds);

        return getSimpleJdbcTemplate().query("select id " +
                           "from eirc_quittances_tbl q " +
                           "    inner join (" +
                           "        select q1.eirc_account_id, max(q1.order_number) as maxOrderNumber, max(q1.date_from) as maxDateFrom " +
                           "        from eirc_quittances_tbl q1 " +
                           "            inner join eirc_consumers_tbl c on q1.eirc_account_id=c.eirc_account_id " +
                           "        where c.id in (:consumerIds) " +
                           "        group by q1.eirc_account_id " +
                           "    ) qj on q.eirc_account_id = qj.eirc_account_id and q.order_number = qj.maxOrderNumber and q.date_From = qj.maxDateFrom " +
                           "order by q.date_from desc", new RowMapper<Quittance>() {
                            @Override
                            public Quittance mapRow(ResultSet rs, int i) throws SQLException {
                                log.debug("ResultSet = {}", rs);
                                return new Quittance(rs.getLong("id"));
                            }
                        }, parameters);
    }
}
