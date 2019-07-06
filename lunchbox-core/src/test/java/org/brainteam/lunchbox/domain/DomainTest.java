package org.brainteam.lunchbox.domain;

import static org.testng.AssertJUnit.fail;

import org.brainteam.lunchbox.test.EMF;
import org.testng.annotations.Test;

@Test(groups="integration")
public class DomainTest {

	public void testValid() {
		try {
			EMF.get();
		} catch (Exception e) {
			e.printStackTrace();
			fail("EMF instantiation should not throw an exception");
		}
	}
	
}
