package org.brainteam.lunchbox.util;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

@Test(groups="unit")
public class PairTest {

	@Test
	public void test() {
		String t1 = "1";
		String t2 = "2";
		Pair<String,String> pair = new Pair<>(t1, t2);
		assertEquals(pair.getFirst(), t1);
		assertEquals(pair.getSecond(), t2);
	}
	
}
