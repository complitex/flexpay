package org.flexpay.eirc.dao.importexport.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.service.importexport.RawPersonalAccountData;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HarkovCenterNachisleniyDataSource extends SimpleJdbcDaoSupport {

	private static String PERSONAL_ACCOUNTS_QUERY = "SELECT id, District, district_id, StreetName, StreetId, StreetType, HouseNum, PartNum, BuildingId, Appartment, AppartmentId, AccoutEIRC, Name, SureName, MiddleName FROM InpAcc where id>200000 LIMIT ?,?";

	public List<RawPersonalAccountData> getPersonalAccountData(Page<RawPersonalAccountData> pager) {
		return getSimpleJdbcTemplate().query(PERSONAL_ACCOUNTS_QUERY, new ParameterizedRowMapper<RawPersonalAccountData>() {
			public RawPersonalAccountData mapRow(ResultSet rs, int i) throws SQLException {
				RawPersonalAccountData data = new RawPersonalAccountData();
				data.setExternalSourceId(rs.getString(1));
				data.addNameValuePair(RawPersonalAccountData.FIELD_DISTRICT, rs.getString("District"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_DISTRICT_ID, rs.getString("district_id"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_STREET, rs.getString("StreetName"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_STREET_ID, rs.getString("StreetId"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_STREET_TYPE, rs.getString("StreetType"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_BUILDING, rs.getString("HouseNum"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_BULK, rs.getString("PartNum"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_BUILDING_ID, rs.getString("BuildingId"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_APARTMENT, rs.getString("Appartment"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_APARTMENT_ID, rs.getString("AppartmentId"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_EXT_ACCOUNT, rs.getString("AccoutEIRC"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_FIRST_NAME, rs.getString("Name"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_MIDDLE_NAME, rs.getString("MiddleName"));
				data.addNameValuePair(RawPersonalAccountData.FIELD_LAST_NAME, rs.getString("SureName"));
				return data;
			}
		}, pager.getThisPageFirstElementNumber(), pager.getPageSize());
	}
}
