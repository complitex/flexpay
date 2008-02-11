package org.flexpay.ab.dao.importexport.imp;

import org.flexpay.ab.service.importexport.RawDistrictData;
import org.flexpay.ab.service.importexport.RawStreetData;
import org.flexpay.common.dao.paging.Page;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HarkovCenterNachisleniyDataSource extends SimpleJdbcDaoSupport {

	private static String DISTRICTS_QUERY = "SELECT id, District FROM districts LIMIT ?,?";
	public List<RawDistrictData> getDistrictsData(Page<RawDistrictData> pager) {
		return getSimpleJdbcTemplate().query(DISTRICTS_QUERY, new ParameterizedRowMapper<RawDistrictData>() {
			public RawDistrictData mapRow(ResultSet rs, int i) throws SQLException {
				RawDistrictData data = new RawDistrictData();
				data.setExternalSourceId(rs.getString(1));
				data.addNameValuePair(RawDistrictData.FIELD_NAME, rs.getString("District"));
				return data;
			}
		}, pager.getThisPageFirstElementNumber(), pager.getPageSize());
	}

	private static String STREETS_QUERY = "SELECT id, StreetName, StreetTypeId FROM streets LIMIT ?,?";
	public List<RawStreetData> getStreetsData(Page<RawStreetData> pager) {
		return getSimpleJdbcTemplate().query(STREETS_QUERY, new ParameterizedRowMapper<RawStreetData>() {
			public RawStreetData mapRow(ResultSet rs, int i) throws SQLException {
				RawStreetData data = new RawStreetData();
				data.setExternalSourceId(rs.getString(1));
				data.addNameValuePair(RawStreetData.FIELD_NAME, rs.getString("StreetName"));
				data.addNameValuePair(RawStreetData.FIELD_TYPE_ID, rs.getInt("StreetTypeId"));
				return data;
			}
		}, pager.getThisPageFirstElementNumber(), pager.getPageSize());
	}
}
