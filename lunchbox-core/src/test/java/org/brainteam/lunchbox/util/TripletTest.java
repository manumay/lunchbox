package org.brainteam.lunchbox.util;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

@Test(groups="unit")
public class TripletTest {

	@Test
	public void test() {
		String t1 = "1";
		String t2 = "2";
		String t3 = "3";
		Triplet<String,String,String> triplet = new Triplet<>(t1, t2, t3);
		assertEquals(triplet.getFirst(), t1);
		assertEquals(triplet.getSecond(), t2);
		assertEquals(triplet.getThird(), t3);
	}
	
}
