package org.flexpay.ab.dao.importexport.imp;

import org.flexpay.ab.service.importexport.RawDistrictData;
import org.flexpay.ab.service.importexport.RawStreetData;
import org.flexpay.ab.service.importexport.RawStreetTypeData;
import org.flexpay.ab.service.importexport.RawBuildingsData;
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

	private static String STREETS_QUERY = "SELECT id, StreetName, StreetTypeId FROM streets ORDER BY id DESC LIMIT ?,?";
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

	private static String STREET_TYPES_QUERY = "SELECT id, StreetType FROM street_types LIMIT ?,?";
	public List<RawStreetTypeData> getStreetTypesData(Page<RawStreetTypeData> pager) {
		return getSimpleJdbcTemplate().query(STREET_TYPES_QUERY, new ParameterizedRowMapper<RawStreetTypeData>() {
			public RawStreetTypeData mapRow(ResultSet rs, int i) throws SQLException {
				RawStreetTypeData data = new RawStreetTypeData();
				data.setExternalSourceId(rs.getString(1));
				data.addNameValuePair(RawStreetTypeData.FIELD_NAME, rs.getString("StreetType"));
				return data;
			}
		}, pager.getThisPageFirstElementNumber(), pager.getPageSize());
	}

	private static String BUILDINGS_QUERY = "SELECT id, houseNum, partNum, StreetID, DistrictID  FROM buildings LIMIT ?,?";
	public List<RawBuildingsData> getBuildingsData(Page<RawBuildingsData> pager) {
		return getSimpleJdbcTemplate().query(BUILDINGS_QUERY, new ParameterizedRowMapper<RawBuildingsData>() {
			public RawBuildingsData mapRow(ResultSet rs, int i) throws SQLException {
				RawBuildingsData data = new RawBuildingsData();
				data.setExternalSourceId(rs.getString(1));
				data.addNameValuePair(RawBuildingsData.FIELD_NUMBER, rs.getString("houseNum"));
				data.addNameValuePair(RawBuildingsData.FIELD_NUMBER_BULK, rs.getString("partNum"));
				data.addNameValuePair(RawBuildingsData.FIELD_STREET, rs.getString("StreetID"));
				data.addNameValuePair(RawBuildingsData.FIELD_DISTRICT, rs.getString("DistrictID"));
				return data;
			}
		}, pager.getThisPageFirstElementNumber(), pager.getPageSize());
	}
}
