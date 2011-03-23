package org.flexpay.common.dao.paging;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestPage {

	private Page<Integer> firstPage = new Page<Integer>(20, 1);
	private Page<Integer> secondPage = new Page<Integer>(20, 2);
	private Page<Integer> fifthPage = new Page<Integer>(20, 5);
	private Page<Integer> sixthPage = new Page<Integer>(20, 6);

	public TestPage() {

		List<Integer> elements = new ArrayList<Integer>(100);
		List<Integer> elements105 = new ArrayList<Integer>(105);

		for (int n = 1; n < 20; ++n) {
			elements.add(n);
			elements105.add(n * n);
		}

		firstPage.setElements(elements);
		firstPage.setTotalElements(100);
		secondPage.setElements(elements);
		secondPage.setTotalElements(100);

		fifthPage.setElements(elements105);
		fifthPage.setTotalElements(105);
		sixthPage.setElements(elements105);
		sixthPage.setTotalElements(105);
	}

	@Test
	public void testPager() {
		assertTrue("First page is not first actually", firstPage.isFirstPage());
		assertFalse("First page is last actually", firstPage.isLastPage());

		assertEquals("Second page number is invalid", 2, secondPage.getPageNumber());
	}
}
