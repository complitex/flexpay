package org.flexpay.ab.dao.imp;

import org.flexpay.ab.dao.ApartmentDaoExt;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.util.DateUtil;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ApartmentDaoExtImpl extends SimpleJdbcDaoSupport implements ApartmentDaoExt {

	/**
	 * Find apartment by number
	 *
	 * @param building Building to find apartment in
	 * @param number   Building number
	 * @return Apartment instance, or <code>null</null> if not found
	 */
	@Nullable
	public Apartment findApartmentStub(@NotNull Building building, final String number) {
		String sql = "SELECT id FROM ab_apartments_tbl a WHERE a.building_id=? AND EXISTS " +
					 "(SELECT 1 FROM ab_apartment_numbers_tbl n WHERE n.apartment_id=a.id AND n.value=? AND n.end_date>?)";

		Object[] args = {building.getId(), number, DateUtil.now()};
		List result = getJdbcTemplate().query(sql, args, new SingleColumnRowMapper(Long.class));

		return result.isEmpty() ? null : new Apartment((Long) result.get(0));
	}
}
