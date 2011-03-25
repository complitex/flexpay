package org.flexpay.common.util;

import org.flexpay.common.persistence.DateInterval;
import org.flexpay.common.persistence.Pair;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.util.config.ApplicationConfig;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.*;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings ({"AsymmetricFieldRead"})
public class TestDateIntervalUtil {

	@Test
	public void testDateFormat() throws ParseException {
		Date dt = DateUtil.parseBeginDate("2007/11/10");
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(dt);

		assertEquals("Year is invalid", 2007, calendar.get(Calendar.YEAR));
		assertEquals("Month is invalid", 11, calendar.get(Calendar.MONTH) + 1);
		assertEquals("Day is invalid", 10, calendar.get(Calendar.DAY_OF_MONTH));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testDateIntervalCreateFailure() {
		Date now = DateUtil.now();
		Date yesterday = DateUtil.previous(now);

		// should fail, interval is invalid
		new DI(now, yesterday, new Price());
	}

	private List<DI> simple;
	private Date date_2004_12_31;
	private Date date_2005_01_01;
	private Date date_2005_12_31;
	private Date date_2006_01_01;
	private Date datePastInfinite = ApplicationConfig.getPastInfinite();
	private Date dateFutureInfinite = ApplicationConfig.getFutureInfinite();
	private Price price15 = new Price(15);
	private Price priceNull = new Price();

	@Before
	public void initTest() {
		simple = list();
		simple.add(new DI(priceNull));
		date_2004_12_31 = DateUtil.parseBeginDate("2004/12/31");
		date_2005_01_01 = DateUtil.next(date_2004_12_31);
		date_2005_12_31 = DateUtil.parseBeginDate("2005/12/31");
		date_2006_01_01 = DateUtil.next(date_2005_12_31);
	}

	/**
	 * Test intervals joining
	 */
	@Test
	public void testIntervalJoin() {
		List<DI> dis = list();
		dis.add(new DI(datePastInfinite, date_2004_12_31, price15));
		dis.add(new DI(date_2005_01_01, date_2005_12_31, price15));
		dis.add(new DI(date_2006_01_01, dateFutureInfinite, price15));
		TimeLine<Price, DI> tl1 = new TimeLine<Price, DI>(dis);
		TimeLine<Price, DI> tlJoined = DateIntervalUtil.joinEqualIntervals(tl1);

		assertEquals("Intervals join failed, invalid number of intervals",
				1, tlJoined.getIntervals().size());
		assertTrue("Joined interval does not cover whole time line",
				DateIntervalUtil.coversTimeLine(tlJoined.getIntervals().get(0)));
	}

	/**
	 * Test intervals joining, time line should stay the same
	 */
	@Test
	public void testIntervalJoinFail() {
		List<DI> dis = new ArrayList<DI>(2);
		dis.add(new DI(datePastInfinite, date_2004_12_31, price15));
		dis.add(new DI(date_2005_01_01, date_2005_12_31, priceNull));
		dis.add(new DI(date_2006_01_01, dateFutureInfinite, price15));
		TimeLine<Price, DI> tl1 = new TimeLine<Price, DI>(dis);
		TimeLine<Price, DI> tlJoined = DateIntervalUtil.joinEqualIntervals(tl1);

		assertEquals("Intervals join failed, invalid number of intervals",
				3, tlJoined.getIntervals().size());
	}

	@Test
	public void testInsertInterval() throws Exception {
		TimeLine<Price, DI> tl = new TimeLine<Price, DI>(simple);
		DI di = new DI(date_2004_12_31, date_2005_12_31, price15);

		TimeLine<Price, DI> tl2 = DateIntervalUtil.addInterval(tl, di);

		assertEquals("Interval insert failed, wrong number of intervals",
				3, tl2.getIntervals().size());
		assertEquals("First interval is invalid", priceNull,
				tl2.getIntervals().get(0).getValue());
	}

	@Test
	public void testInsertInterval2() throws Exception {
		TimeLine<Price, DI> tl = new TimeLine<Price, DI>(simple);
		DI di = new DI(datePastInfinite, dateFutureInfinite, price15);

		TimeLine<Price, DI> tl2 = DateIntervalUtil.addInterval(tl, di);

		assertEquals("Interval insert failed, wrong number of intervals",
				1, tl2.getIntervals().size());
		assertEquals("First interval is invalid", price15,
				tl2.getIntervals().get(0).getValue());
	}

	@Test
	public void testDeleteInterval() throws Exception {
		List<DI> dis = new ArrayList<DI>(2);
		dis.add(new DI(datePastInfinite, date_2004_12_31, price15));
		dis.add(new DI(date_2005_01_01, date_2005_12_31, priceNull));
		dis.add(new DI(date_2006_01_01, dateFutureInfinite, price15));
		TimeLine<Price, DI> tl1 = new TimeLine<Price, DI>(dis);

		TimeLine<Price, DI> tl2 = DateIntervalUtil.deleteInterval(tl1,
				new DI(date_2006_01_01, dateFutureInfinite, price15));

		assertEquals("Interval insert failed, wrong number of intervals",
				2, tl2.getIntervals().size());
		assertEquals("First interval is invalid", price15,
				tl2.getIntervals().get(0).getValue());
		assertEquals("Second interval is invalid", priceNull,
				tl2.getIntervals().get(1).getValue());
	}

	@Test
	public void testDeleteInterval1() throws Exception {
		List<DI> dis = new ArrayList<DI>(2);
		dis.add(new DI(datePastInfinite, date_2004_12_31, price15));
		dis.add(new DI(date_2005_01_01, date_2005_12_31, priceNull));
		dis.add(new DI(date_2006_01_01, dateFutureInfinite, price15));
		TimeLine<Price, DI> tl1 = new TimeLine<Price, DI>(dis);

		TimeLine<Price, DI> tl2 = DateIntervalUtil.deleteInterval(tl1,
				new DI(datePastInfinite, date_2004_12_31, price15));

		assertEquals("Interval insert failed, wrong number of intervals",
				2, tl2.getIntervals().size());
		assertEquals("First interval is invalid", priceNull,
				tl2.getIntervals().get(0).getValue());
		assertEquals("Second interval is invalid", price15,
				tl2.getIntervals().get(1).getValue());
	}

	@Test
	public void testDiff() {
		List<DI> dis = new ArrayList<DI>(2);
		dis.add(new DI(datePastInfinite, date_2004_12_31, price15));
		dis.add(new DI(date_2005_01_01, date_2005_12_31, priceNull));
		dis.add(new DI(date_2006_01_01, dateFutureInfinite, price15));
		TimeLine<Price, DI> tl1 = new TimeLine<Price, DI>(dis);

		dis = new ArrayList<DI>(2);
		dis.add(new DI(price15));
		TimeLine<Price, DI> tl2 = new TimeLine<Price, DI>(dis);

		List<Pair<DI, DI>> diffs =
				DateIntervalUtil.diff(tl1, tl2);

		assertEquals("Invalid number of diffs", 1, diffs.size());
		assertEquals("Invalid diff",
				PairUtil.pair(new DI(date_2005_01_01, date_2005_12_31, priceNull),
						new DI(date_2005_01_01, date_2005_12_31, price15)),
				diffs.get(0));
	}
}

class Price extends TemporaryValue<Price> {

	int value = 0;

	Price() {
	}

	Price(int value) {
		this.value = value;
	}

	/**
	 * Check if this value is empty
	 *
	 * @return <code>true</code> if this value is empty, or <code>false</code> otherwise
	 */
    @Override
	public boolean isEmpty() {
		return false;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
    @Override
	public Price getEmpty() {
		return new Price(0);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Price)) {
			return false;
		}

		Price price = (Price) o;
		return value == price.value;
	}

	@Override
	public int hashCode() {
		return value;
	}

}

class DI extends DateInterval<Price, DI> {
	/**
	 * Constructs a new DateInterval.
	 *
	 * @param value Temporary values assigned to this interval
	 */
	public DI(Price value) {
		super(value);
	}

	/**
	 * Construct a new DateInterval
	 *
	 * @param begin Interval begin date
	 * @param end   Interval end date
	 * @param value Temporary values assigned to this interval
	 */
	public DI(Date begin, Date end, Price value) {
		super(begin, end, value);
	}

	/**
	 * Create a new copy of this interval.
	 *
	 * @return Date interval copy
	 */
    @Override
	public DI copy() {
		return new DI(getBegin(), getEnd(), getValue());
	}

}
