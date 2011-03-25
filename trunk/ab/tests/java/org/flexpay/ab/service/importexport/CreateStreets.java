package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateStreets extends AbSpringBeanAwareTestCase {

	private static final String sqlGetStreets =
			"select s.name, sc.cn_obj_id, tt.int_id " +
			"from Streets s " +
			"	inner join StreetTypes st on s.TypeId=st.id" +
			"	inner join tmp_types_corr tt on st.id=tt.ext_id" +
			"	left join tmp_street_corr sc on sc.street_id=s.id";

	private static final String sqlInsertStreet = "insert into ab_streets_tbl (status, town_id) values (0, ?)";
	private static final String sqlInsertStreetName = "insert into ab_street_names_tbl (street_id) values (?)";
	private static final String sqlInsertStreetNameTemporal =
			"insert into ab_street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)\n" +
			"values (?, ?, '1900-01-01', '2100-12-31', '2008-08-11', '2100-12-31')";
	private static final String sqlInsertStreetTypeTemporal =
			"insert into ab_street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)\n" +
			"values (?, ?, '1900-01-01', '2100-12-31', '2008-08-11', '2100-12-31')";
	private static final String sqlInsertStreetNameTr =
			"insert into ab_street_name_translations_tbl (name, street_name_id, language_id) values (?, ?, ?)";
	private static final String sqlInsertStreetCorrection =
			"insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)" +
			"values (?, ?, ?, ?)";

	private static final Long townId = 1L;
	private static final Long ruId = 1L;
	private static final Long dataSourceId = 1L;

	@Autowired
	private ClassToTypeRegistry typeRegistry;

	private int streetTypeNumber;

	@Test
	@Ignore
	public void testInsertStreets() throws Exception {

		jdbcTemplate.query(sqlGetStreets, new RowCallbackHandler() {
            @Override
			public void processRow(ResultSet rs) throws SQLException {
				String name = rs.getString("name");
				Long cnObjectId = rs.getLong("cn_obj_id");
				Long internalTypeId = rs.getLong("int_id");

				log.info("inserting street (name, cn_obj_id, int_id) values ({}, {}, {})",
						new Object[] {name, cnObjectId, internalTypeId});

				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(new StreetInsertCreator(), keyHolder);

				Number streetId = keyHolder.getKey();
				keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(new StreetNameInsertCreator(streetId.longValue()), keyHolder);

				Number streetNameId = keyHolder.getKey();
				Object[] params = {name, streetNameId.longValue(), ruId};
				jdbcTemplate.update(sqlInsertStreetNameTr, params);

				Object[] paramsNT = {streetId.longValue(), streetNameId.longValue()};
				jdbcTemplate.update(sqlInsertStreetNameTemporal, paramsNT);

				Object[] paramsTT = {streetId.longValue(), internalTypeId};
				jdbcTemplate.update(sqlInsertStreetTypeTemporal, paramsTT);

				// add correction
				if (cnObjectId != 0) {
					Object[] params2 = {streetId.longValue(), streetTypeNumber, cnObjectId, dataSourceId};
					jdbcTemplate.update(sqlInsertStreetCorrection, params2);
				}
			}
		});
	}

	@Before
	public void setStreetTypeNumber() {
		streetTypeNumber = typeRegistry.getType(Street.class);
	}

	private static final class StreetInsertCreator implements PreparedStatementCreator {
        @Override
		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			PreparedStatement ps = con.prepareStatement(sqlInsertStreet, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setLong(1, townId);
			return ps;
		}
	}

	private static final class StreetNameInsertCreator implements PreparedStatementCreator {

		private Long streetId;

		private StreetNameInsertCreator(Long streetId) {
			this.streetId = streetId;
		}

        @Override
		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			PreparedStatement ps = con.prepareStatement(sqlInsertStreetName, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setLong(1, streetId);
			return ps;
		}
	}
}
