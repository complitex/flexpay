package org.flexpay.common.persistence.morphology.number;

import org.flexpay.common.persistence.morphology.Gender;
import org.flexpay.common.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.flexpay.common.persistence.morphology.Gender.*;
import static org.flexpay.common.util.CollectionUtils.map;

public class RuNumberToTextConverter implements NumberToTextConverter {

	private static final String[] hundredsNames = {
			"",
			" сто",
			" двести",
			" триста",
			" четыреста",
			" пятьсот",
			" шестьсот",
			" семьсот",
			" восемьсот",
			" девятьсот"
	};

	private static final String[] tensNames = {
			"",
			" десять",
			" двадцать",
			" тридцать",
			" сорок",
			" пятьдесят",
			" шестьдесят",
			" семьдясят",
			" восемьдесят",
			" девяносто"
	};

	@SuppressWarnings ({"unchecked"})
	private static final List<Map<Gender, String>> numNames = CollectionUtils.list(
			map(masculine, "", feminine, "", neuter, ""),
			map(masculine, " один", feminine, " одна", neuter, " одно"),
			map(masculine, " два", feminine, " две", neuter, " два"),
			map(masculine, " три", feminine, " три", neuter, " три"),
			map(masculine, " четыре", feminine, " четыре", neuter, " четыре"),
			map(masculine, " пять", feminine, " пять", neuter, " пять"),
			map(masculine, " шесть", feminine, " шесть", neuter, " шесть"),
			map(masculine, " семь", feminine, " семь", neuter, " семь"),
			map(masculine, " восемь", feminine, " восемь", neuter, " восемь"),
			map(masculine, " девять", feminine, " девять", neuter, " девять"),
			map(masculine, " десять", feminine, " десять", neuter, " десять"),
			map(masculine, " одиннадцать", feminine, " одиннадцать", neuter, " одиннадцать"),
			map(masculine, " двенадцать", feminine, " двенадцать", neuter, " двенадцать"),
			map(masculine, " тринадцать", feminine, " тринадцать", neuter, " тринадцать"),
			map(masculine, " четырнадцать", feminine, " четырнадцать", neuter, " четырнадцать"),
			map(masculine, " пятнадцать", feminine, " пятнадцать", neuter, " пятнадцать"),
			map(masculine, " шестнадцать", feminine, " шестнадцать", neuter, " шестнадцать"),
			map(masculine, " семьнадцать", feminine, " семьнадцать", neuter, " семьнадцать"),
			map(masculine, " восемнадцать", feminine, " восемнадцать", neuter, " восемнадцать"),
			map(masculine, " девятнадцать", feminine, " девятнадцать", neuter, " девятнадцать")
	);

	private static String convertLessThanOneThousand(int number, Gender gender) {
		String soFar;

		if (number % 100 < 20) {
			soFar = numNames.get(number % 100).get(gender);
			number /= 100;
		} else {
			soFar = numNames.get(number % 10).get(gender);
			number /= 10;

			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0) {
			return soFar;
		}
		return hundredsNames[number] + soFar;
	}

	/**
	 * Convert integer to text
	 *
	 * @param l	  Number to convert
	 * @param gender Required number gender
	 * @return Text representation of number
	 */
	public String toText(long l, Gender gender) {
		// 0 to 999 999 999 999
		if (l == 0) {
			return "ноль";
		}

		// pad with "0"
		String mask = "000000000000";
		DecimalFormat df = new DecimalFormat(mask);
		String snumber = df.format(l);

		// XXXnnnnnnnnn
		int billions = Integer.parseInt(snumber.substring(0, 3));
		// nnnXXXnnnnnn
		int millions = Integer.parseInt(snumber.substring(3, 6));
		// nnnnnnXXXnnn
		int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
		// nnnnnnnnnXXX
		int thousands = Integer.parseInt(snumber.substring(9, 12));

		Set<Integer> specialSuffexes = CollectionUtils.set(2, 3, 4);
		String tradBillions = billions == 0 ?
						"" :
						billions % 10 == 1 ?
						convertLessThanOneThousand(billions, masculine) + " миллиард " :
						specialSuffexes.contains(billions % 10) ?
						convertLessThanOneThousand(billions, masculine) + " миллиарда " :
						convertLessThanOneThousand(billions, masculine) + " миллиардов ";

		String tradMillions = millions == 0 ?
						"" :
						millions % 10 == 1 ?
						convertLessThanOneThousand(millions, masculine) + " миллион " :
						specialSuffexes.contains(millions % 10) ?
						convertLessThanOneThousand(millions, masculine) + " миллиона " :
						convertLessThanOneThousand(millions, masculine) + " миллионов ";

		String tradHundredThousands = hundredThousands == 0 ?
						"" :
						hundredThousands % 10 == 1 ?
						convertLessThanOneThousand(hundredThousands, feminine) + " тысяча " :
						specialSuffexes.contains(hundredThousands % 10) ?
						convertLessThanOneThousand(hundredThousands, feminine) + " тысячи " :
						convertLessThanOneThousand(hundredThousands, feminine) + " тысяч ";

		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands, gender);
		String result = tradBillions + tradMillions + tradHundredThousands + tradThousand;

		// remove extra spaces
		return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ").trim();
	}
}
