package org.flexpay.ab.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestI18nQuery extends SpringBeanAwareTestCase {

	private DataSource source;

	@Autowired
	public void setSource(@Qualifier ("dataSource")DataSource source) {
		this.source = source;
	}

	@Test
	public void testFindByI18n() throws Throwable {
		Connection c = source.getConnection();

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String param = "Источник - Тестовые данные ПУ из ЦН";
			String sql = "select id from common_data_source_descriptions_tbl " +
						 "where description=?";
			st = c.prepareStatement(sql);
			st.setString(1, param);
			rs = st.executeQuery();

			if (!rs.next()) {
				fail("No data source description found");
			}
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(st);
			JdbcUtils.closeConnection(c);
		}
	}
}
