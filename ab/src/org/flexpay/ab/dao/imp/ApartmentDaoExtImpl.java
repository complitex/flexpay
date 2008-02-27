package org.flexpay.ab.dao.imp;

import org.apache.log4j.Logger;
import org.flexpay.ab.dao.ApartmentDaoExt;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.util.DateIntervalUtil;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.util.List;

public class ApartmentDaoExtImpl extends SimpleJdbcDaoSupport implements ApartmentDaoExt {

	/**
	 * Find apartment by number
	 *
	 * @param building Building to find apartment in
	 * @param number   Building number
	 * @return Apartment instance, or <code>null</null> if not found
	 */
	public Apartment findApartmentStub(final Building building, final String number) {
		String sql = "SELECT id FROM apartments_tbl a WHERE a.building_id=? AND EXISTS " +
					 "(SELECT 1 FROM apartment_numbers_tbl n WHERE n.apartment_id=a.id AND n.value=? AND n.end_date>?)";

		Object[] args = {building.getId(), number, DateIntervalUtil.now()};
		List result = getJdbcTemplate().query(sql, args, new SingleColumnRowMapper(Long.class));

		return result.isEmpty() ? null : new Apartment((Long) result.get(0));
	}
}
