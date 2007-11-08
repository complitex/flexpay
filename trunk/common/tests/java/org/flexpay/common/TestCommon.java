package org.flexpay.common;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Test stub for the common module
 */
public class TestCommon {

	SampleClass sc = new SampleClass();

	@Test(expected = IndexOutOfBoundsException.class) public void empty() {
		new ArrayList<Object>().get(0);
	}

}
