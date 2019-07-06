package org.brainteam.lunchbox.domain;import static org.testng.Assert.assertSame;import java.util.Arrays;import java.util.Collections;import java.util.List;import org.testng.annotations.Test;@Test(groups="unit")public class MealTest {	@Test	public void testComparable() {		Meal m1 = new Meal();		m1.setHeadline("Curry-Wurst");				Meal m2 = new Meal();		m2.setHeadline("Cevapcici");				Meal m3 = new Meal();		m3.setHeadline("Bockwurst");				List<Meal> meals = Arrays.asList(m1, m2, m3);		Collections.sort(meals);				assertSame(meals.get(0), m3);		assertSame(meals.get(1), m2);		assertSame(meals.get(2), m1);	}	}