package org.flexpay.common.persistence.morphology;

/**
 * Human language words gender enum
 */
public enum Gender {

	masculine, feminine, neuter;

	public static Gender fromOrdinal(int ordinal) {
		return values()[ordinal];
	}
}
