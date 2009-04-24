package org.flexpay.ab.service;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;

public class TestI18nQuery extends AbSpringBeanAwareTestCase {

	@Test
	public void testFindByI18n() throws Throwable {

		Object[] params = {"Источник - Тестовые данные ПУ из ЦН"};
		String sql = "select id from common_data_source_descriptions_tbl " +
					 "where description=?";
		jdbcTemplate.queryForLong(sql, params);
	}
}
