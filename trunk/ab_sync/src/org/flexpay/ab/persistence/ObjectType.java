package org.flexpay.ab.persistence;

public enum ObjectType {

	Unknown(-1), Town(0), District(1), Street(2), Building(3), StreetType(4), Apartment(5);

	private static ObjectType[] types = {Town, District, Street, Building, StreetType, Apartment};

	private int id;

	private ObjectType(int id) {
		this.id = id;
	}

	public static ObjectType getById(int id) {
		if (id < types.length) {
			return types[id];
		}

		throw new IllegalArgumentException("Invalid ObjectType id: " + id);
	}

	public int getId() {
		return id;
	}
}
