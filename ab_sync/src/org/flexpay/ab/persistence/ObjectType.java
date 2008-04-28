package org.flexpay.ab.persistence;

public enum ObjectType {

	Unknown(-1, -1), Town(0, 0), District(1, 1), Street(2, 3), Building(3, 4),
	StreetType(4, 2), Apartment(5, 5), Person(6, 6);

	private static ObjectType[] types = {Town, District, Street, Building, StreetType, Apartment, Person};

	private int id;
	private int orderWeight;

	private ObjectType(int id, int orderWeight) {
		this.id = id;
		this.orderWeight = orderWeight;
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

	/**
	 * Get object order in sync queue
	 * @return relative order weight
	 */
	public int getOrderWeight() {
		return orderWeight;
	}
}
