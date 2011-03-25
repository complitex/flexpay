package org.flexpay.common.persistence.morphology.number;

import org.flexpay.common.persistence.morphology.Gender;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestEnNumberToTextConverter {

	@Test
	public void testConverter() {

		NumberToTextConverter converter = new EnNumberToTextConverter();

		assertEquals("Incorrect", "zero", converter.toText(0, Gender.masculine));
		assertEquals("Incorrect", "one", converter.toText(1, Gender.masculine));
		assertEquals("Incorrect", "sixteen", converter.toText(16, Gender.masculine));
		assertEquals("Incorrect", "one hundred", converter.toText(100, Gender.masculine));
		assertEquals("Incorrect", "one hundred eighteen", converter.toText(118, Gender.masculine));
		assertEquals("Incorrect", "two hundred", converter.toText(200, Gender.masculine));
		assertEquals("Incorrect", "two hundred nineteen", converter.toText(219, Gender.masculine));
		assertEquals("Incorrect", "eight hundred", converter.toText(800, Gender.masculine));
		assertEquals("Incorrect", "eight hundred one", converter.toText(801, Gender.masculine));
		assertEquals("Incorrect", "one thousand three hundred sixteen", converter.toText(1316, Gender.masculine));
		assertEquals("Incorrect", "one million", converter.toText(1000000, Gender.masculine));
		assertEquals("Incorrect", "two millions", converter.toText(2000000, Gender.masculine));
		assertEquals("Incorrect", "three millions two hundred", converter.toText(3000200, Gender.masculine));
		assertEquals("Incorrect", "seven hundred thousand", converter.toText(700000, Gender.masculine));
		assertEquals("Incorrect", "nine millions", converter.toText(9000000, Gender.masculine));
		assertEquals("Incorrect", "nine millions one thousand", converter.toText(9001000, Gender.masculine));
		assertEquals("Incorrect", "one hundred twenty three millions four hundred " +
					 "fifty six thousand seven hundred eighty nine", converter.toText(123456789, Gender.masculine));
		assertEquals("Incorrect", "two billion one hundred forty seven millions " +
					 "four hundred eighty three thousand six hundred forty seven", converter.toText(2147483647, Gender.masculine));
		assertEquals("Incorrect", "three billion ten", converter.toText(3000000010L, Gender.masculine));
	}
}
