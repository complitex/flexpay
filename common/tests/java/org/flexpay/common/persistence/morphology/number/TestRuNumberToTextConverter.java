package org.flexpay.common.persistence.morphology.number;

import org.flexpay.common.persistence.morphology.Gender;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRuNumberToTextConverter {

	@Test
	public void testConverter() {

		NumberToTextConverter converter = new RuNumberToTextConverter();

		assertEquals("Incorrect", "ноль", converter.toText(0, Gender.masculine));
		assertEquals("Incorrect", "один", converter.toText(1, Gender.masculine));
		assertEquals("Incorrect", "одна", converter.toText(1, Gender.feminine));
		assertEquals("Incorrect", "шестнадцать", converter.toText(16, Gender.masculine));
		assertEquals("Incorrect", "шестнадцать", converter.toText(16, Gender.feminine));
		assertEquals("Incorrect", "сто", converter.toText(100, Gender.masculine));
		assertEquals("Incorrect", "сто восемнадцать", converter.toText(118, Gender.masculine));
		assertEquals("Incorrect", "двести", converter.toText(200, Gender.masculine));
		assertEquals("Incorrect", "двести девятнадцать", converter.toText(219, Gender.masculine));
		assertEquals("Incorrect", "восемьсот", converter.toText(800, Gender.masculine));
		assertEquals("Incorrect", "восемьсот один", converter.toText(801, Gender.masculine));
		assertEquals("Incorrect", "восемьсот одна", converter.toText(801, Gender.feminine));
		assertEquals("Incorrect", "одна тысяча триста шестнадцать", converter.toText(1316, Gender.masculine));
		assertEquals("Incorrect", "один миллион", converter.toText(1000000, Gender.masculine));
		assertEquals("Incorrect", "два миллиона", converter.toText(2000000, Gender.masculine));
		assertEquals("Incorrect", "три миллиона двести", converter.toText(3000200, Gender.masculine));
		assertEquals("Incorrect", "семьсот тысяч", converter.toText(700000, Gender.masculine));
		assertEquals("Incorrect", "девять миллионов", converter.toText(9000000, Gender.masculine));
		assertEquals("Incorrect", "девяносто один миллион", converter.toText(91000000, Gender.masculine));
		assertEquals("Incorrect", "девять миллионов одна тысяча", converter.toText(9001000, Gender.masculine));
		assertEquals("Incorrect", "сто двадцать три миллиона четыреста " +
					 "пятьдесят шесть тысяч семьсот восемьдесят девять", converter.toText(123456789, Gender.masculine));
		assertEquals("Incorrect", "два миллиарда сто сорок семь миллионов " +
					 "четыреста восемьдесят три тысячи шестьсот сорок семь", converter.toText(2147483647, Gender.masculine));
		assertEquals("Incorrect", "три миллиарда десять", converter.toText(3000000010L, Gender.masculine));
	}
}
