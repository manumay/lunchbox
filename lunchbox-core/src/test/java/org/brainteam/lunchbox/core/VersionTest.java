package org.brainteam.lunchbox.core;import static org.testng.Assert.assertEquals;import org.testng.annotations.Test;@Test(groups="unit")public class VersionTest {		private static final String VERSION = "1.0beta";	@Test	public void test() {		assertEquals(Version.get(), Version.UNINITIALIZED);		Version.set(VERSION);		assertEquals(Version.get(), VERSION);		Version.set(null);		assertEquals(Version.get(), Version.UNINITIALIZED);	}	}