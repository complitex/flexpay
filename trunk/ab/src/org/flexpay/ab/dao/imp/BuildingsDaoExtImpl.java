package org.flexpay.ab.dao.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.BuildingsDaoExt;
import org.flexpay.ab.persistence.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BuildingsDaoExtImpl extends SimpleJdbcDaoSupport implements BuildingsDaoExt {

	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * Find building by number
	 *
	 * @param street   Building street
	 * @param district Building district
	 * @param number   Building number
	 * @param bulk	 Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	public Buildings findBuildings(final Street street, final District district,
								   final String number, final String bulk) {
		Object obj = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				Query query;
				if (StringUtils.isEmpty(bulk)) {
					query = session.getNamedQuery("Buildings.findByNumberWithDistrict");
				} else {
					query = session.getNamedQuery("Buildings.findByBulkNumberWithDistrict")
							.setString(5, bulk);
				}

				List objects = query.setLong(0, district.getId())
						.setLong(1, street.getId())
						.setInteger(2, BuildingAttributeType.TYPE_NUMBER)
						.setString(3, number)
						.setInteger(4, BuildingAttributeType.TYPE_BULK)
						.setMaxResults(1)
						.list();

				return objects.isEmpty() ? null : objects.get(0);
			}
		});

		return (Buildings) obj;
	}

	/**
	 * Find building by number
	 *
	 * @param street Building street
	 * @param number Building number
	 * @param bulk   Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	public Buildings findBuildings(final Street street, final String number, final String bulk) {

		if (true) {
			if (StringUtils.isNotEmpty(bulk)) {
				return doFindBuildings(street, number, bulk);
			} else {
				return doFindBuildings(street, number);
			}
		}

		Object obj = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				Query query;
				if (StringUtils.isEmpty(bulk)) {
					query = session.getNamedQuery("Buildings.findByNumber");
				} else {
					query = session.getNamedQuery("Buildings.findByBulkNumber")
							.setString(4, bulk);
				}

				List objects = query.setLong(0, street.getId())
						.setInteger(1, BuildingAttributeType.TYPE_NUMBER)
						.setString(2, number)
						.setInteger(3, BuildingAttributeType.TYPE_BULK)
						.setMaxResults(1)
						.list();

				return objects.isEmpty() ? null : objects.get(0);
			}
		});

		return (Buildings) obj;
	}

	private Buildings previousBuildings = new Buildings(-1L);
	private Building cachedBuilding = null;

	/**
	 * Find Building stub by Buildings stub (i.e. object that does not have reference to its
	 * building)
	 *
	 * @param buildings Buildings stub
	 * @return Building if found, or <code>null</code> otherwise
	 */
	public Building findBuilding(Buildings buildings) {
		if (previousBuildings.getId().equals(buildings.getId())) {
			return cachedBuilding;
		}
		Object[] params = {buildings.getId()};
		List result = getJdbcTemplate().query("select building_id from buildingses_tbl where id=?",
				params, new SingleColumnRowMapper(Long.class));
		Building building = result.isEmpty() ? null : new Building((Long) result.get(0));

		if (building != null) {
			previousBuildings = buildings;
			cachedBuilding = building;
		}

		return building;
	}

	String sql1 = "select * from buildingses_tbl bs where bs.street_id=? and exists ( " +
				  "select a.id from building_attributes_tbl a, building_attribute_types_tbl at " +
				  "where a.attribute_type_id=at.id and a.buildings_id=bs.id and at.type=1 and a.value=? " +
				  ") and not exists (" +
				  "select a.id from building_attributes_tbl a, building_attribute_types_tbl at " +
				  "where a.attribute_type_id=at.id and a.buildings_id=bs.id and at.type=2 " +
				  ")";
	String sql2 = "select * from buildingses_tbl bs where bs.street_id=? and exists ( " +
				  "select a.id from building_attributes_tbl a, building_attribute_types_tbl at " +
				  "where a.attribute_type_id=at.id and a.buildings_id=bs.id and at.type=1 and a.value=? " +
				  ") and exists (" +
				  "select a.id from building_attributes_tbl a, building_attribute_types_tbl at " +
				  "where a.attribute_type_id=at.id and a.buildings_id=bs.id and at.type=2 and a.value=? " +
				  ")";

	private Buildings doFindBuildings(Street street, String number, String bulk) {
		return getSimpleJdbcTemplate().queryForObject(sql2, new ParameterizedRowMapper<Buildings>() {
			public Buildings mapRow(ResultSet rs, int i) throws SQLException {
				Buildings buildings = new Buildings(rs.getLong("id"));
				buildings.setBuilding(new Building(rs.getLong("building_id")));
				return buildings;
			}
		}, street.getId(), number, bulk
		);
	}

	private Buildings doFindBuildings(Street street, String number) {
		try {
		return getSimpleJdbcTemplate().queryForObject(sql1, new ParameterizedRowMapper<Buildings>() {
			public Buildings mapRow(ResultSet rs, int i) throws SQLException {
				Buildings buildings = new Buildings(rs.getLong("id"));
				buildings.setBuilding(new Building(rs.getLong("building_id")));
				return buildings;
			}
		}, street.getId(), number);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}
