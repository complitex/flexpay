package org.flexpay.ab.dao.importexport.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.service.importexport.*;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HarkovCenterNachisleniyDataSource extends SimpleJdbcDaoSupport {

	private static String DISTRICTS_QUERY = "SELECT id, District FROM districts LIMIT ?,?";

	public List<RawDistrictData> getDistrictsData(Page<RawDistrictData> pager) {
		return getSimpleJdbcTemplate().query(DISTRICTS_QUERY, new RowMapper<RawDistrictData>() {
			@Override
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
		return getSimpleJdbcTemplate().query(STREETS_QUERY, new RowMapper<RawStreetData>() {
			@Override
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
		return getSimpleJdbcTemplate().query(STREET_TYPES_QUERY, new RowMapper<RawStreetTypeData>() {
			@Override
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
		return getSimpleJdbcTemplate().query(BUILDINGS_QUERY, new RowMapper<RawBuildingsData>() {
			@Override
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

	private static String APARTMENTS_QUERY = "SELECT id, Apartment, BuildingId FROM apartments ORDER BY BuildingId LIMIT ?,?";

	public List<RawApartmentData> getApartmentsData(Page<RawApartmentData> pager) {
		return getSimpleJdbcTemplate().query(APARTMENTS_QUERY, new RowMapper<RawApartmentData>() {
			@Override
			public RawApartmentData mapRow(ResultSet rs, int i) throws SQLException {
				RawApartmentData data = new RawApartmentData();
				data.setExternalSourceId(rs.getString(1));
				data.addNameValuePair(RawApartmentData.FIELD_NUMBER, rs.getString("Apartment"));
				data.addNameValuePair(RawApartmentData.FIELD_BUILDING, rs.getString("buildingId"));
				return data;
			}
		}, pager.getThisPageFirstElementNumber(), pager.getPageSize());
	}

	private static String PERSONAL_ACCOUNTS_QUERY = "SELECT distinct Name, SureName, MiddleName FROM InpAcc LIMIT ?,?";

	public List<RawPersonData> getPersonalAccountData(Page<RawPersonData> pager) {
		return getSimpleJdbcTemplate().query(PERSONAL_ACCOUNTS_QUERY, new RowMapper<RawPersonData>() {
			@Override
			public RawPersonData mapRow(ResultSet rs, int i) throws SQLException {
				RawPersonData data = new RawPersonData();
				data.addNameValuePair(RawPersonData.FIELD_FIRST_NAME, rs.getString("Name"));
				data.addNameValuePair(RawPersonData.FIELD_MIDDLE_NAME, rs.getString("MiddleName"));
				data.addNameValuePair(RawPersonData.FIELD_LAST_NAME, rs.getString("SureName"));
				data.addNameValuePair(RawPersonData.FIELD_BIRTH_DATE, ApplicationConfig.getPastInfinite());
				data.addNameValuePair(RawPersonData.FIELD_DOCUMENT_TYPE, "");
				data.addNameValuePair(RawPersonData.FIELD_DOCUMENT_SERIA, "");
				data.addNameValuePair(RawPersonData.FIELD_DOCUMENT_NUMBER, "");
				data.addNameValuePair(RawPersonData.FIELD_DOCUMENT_FROM_DATE, ApplicationConfig.getPastInfinite());
				data.addNameValuePair(RawPersonData.FIELD_DOCUMENT_EXPIRE_DATE, ApplicationConfig.getFutureInfinite());
				data.addNameValuePair(RawPersonData.FIELD_DOCUMENT_ORGANIZATION, "");

				String[] idParts = {data.getFirstName(), data.getMiddleName(), data.getLastName()};
				data.setExternalSourceId(StringUtils.join(idParts, "-"));

				return data;
			}
		}, pager.getThisPageFirstElementNumber(), pager.getPageSize());
	}

}
