package org.flexpay.ab.persistence;

public enum FieldType {
	Unknown(-1), District(0), StreetType(1), StreetName(2), HouseNumber(3), Bulk(4),
	Apartment(5), DistrictId(6), StreetId(7), BuildingId(8), StreetTypeId(9);

	private static FieldType[] types = {
			District, StreetType, StreetName, HouseNumber, Bulk, Apartment,
			DistrictId, StreetId, BuildingId, StreetTypeId
	};

	private int id;

	private FieldType(int id) {
		this.id = id;
	}

	public static FieldType getById(int id) {
		if (0 <= id && id < types.length) {
			return types[id];
		}

		throw new IllegalArgumentException("Invalid field type code: " + id);
	}
}
