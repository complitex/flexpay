package org.flexpay.common.persistence.morphology.number;

import org.flexpay.common.persistence.morphology.Gender;

/**
 * Converter of numbers to their text representation
 * <p/>
 * For example 123 in english is one hundred twenty three
 */
public interface NumberToTextConverter {

	/**
	 * Convert integer to text
	 *
	 * @param l	  Number to convert
	 * @param gender Required number gender
	 * @return Text representation of number
	 */
	String toText(long l, Gender gender);
}
