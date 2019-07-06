package org.brainteam.lunchbox.in;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.brainteam.lunchbox.jmx.FischerPdfParserConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FischerPdfParserImpl implements MenuParser {
	
	private static final String REGION_DAY_1 = "day1";
	private static final String REGION_DAY_2 = "day2";
	private static final String REGION_DAY_3 = "day3";
	private static final String REGION_DAY_4 = "day4";
	private static final String REGION_DAY_5 = "day5";
	private static final String REGION_MONDAY_MENU_1 = "mondayMenu1";
	private static final String REGION_MONDAY_MENU_2 = "mondayMenu2";
	private static final String REGION_MONDAY_MENU_3 = "mondayMenu3";
	private static final String REGION_TUESDAY_MENU_1 = "tuesdayMenu1";
	private static final String REGION_TUESDAY_MENU_2 = "tuesdayMenu2";
	private static final String REGION_TUESDAY_MENU_3 = "tuesdayMenu3";
	private static final String REGION_WEDNESDAY_MENU_1 = "wednesday1";
	private static final String REGION_WEDNESDAY_MENU_2 = "wednesday2";
	private static final String REGION_WEDNESDAY_MENU_3 = "wednesday3";
	private static final String REGION_THURSDAY_MENU_1 = "thursday1";
	private static final String REGION_THURSDAY_MENU_2 = "thursday2";
	private static final String REGION_THURSDAY_MENU_3 = "thursday3";
	private static final String REGION_FRIDAY_MENU_1 = "friday1";
	private static final String REGION_FRIDAY_MENU_2 = "friday2";
	private static final String REGION_FRIDAY_MENU_3 = "friday3";
	private static final String REGION_SALAD = "salad";
	
	private static final String REGION_SUFFIX_HEADLINE = ".headline";
	private static final String REGION_SUFFIX_DESCRIPTION = ".description";
	private static final String REGION_SUFFIX_INGREDIENTS = ".ingredients";
	
	private static final double X_DAY = 29.91;
	private static final double WIDTH_DAY = 46.79;
	
	private static final double X_MENU1 = 77.97;
	private static final double X_MENU2 = 222.47;
	private static final double X_MENU3 = 366.92;
	private static final double WIDTH_MENU = 143.33;
	
	private static final double HEIGHT_HEADLINE = 32.63;
	private static final double HEIGHT_DESCRIPTION = 57.84;
	private static final double HEIGHT_INGREDIENTS = 11.23;
	private static final double HEIGHT_ROW = HEIGHT_HEADLINE + HEIGHT_DESCRIPTION + HEIGHT_INGREDIENTS;
	
	private static final double Y_HEADLINE_MENU1 = 183.76;
	private static final double Y_DESCRIPTION_MENU1 = Y_HEADLINE_MENU1 + HEIGHT_HEADLINE;
	private static final double Y_INGREDIENTS_MENU1 = Y_DESCRIPTION_MENU1 + HEIGHT_DESCRIPTION;
	
	private static final double Y_HEADLINE_MENU2 = 286.91;
	private static final double Y_DESCRIPTION_MENU2 = Y_HEADLINE_MENU2 + HEIGHT_HEADLINE;
	private static final double Y_INGREDIENTS_MENU2 = Y_DESCRIPTION_MENU2 + HEIGHT_DESCRIPTION;
	
	private static final double Y_HEADLINE_MENU3 = 389.79;
	private static final double Y_DESCRIPTION_MENU3 = Y_HEADLINE_MENU3 + HEIGHT_HEADLINE;
	private static final double Y_INGREDIENTS_MENU3 = Y_DESCRIPTION_MENU3 + HEIGHT_DESCRIPTION;
	
	private static final double Y_HEADLINE_MENU4 = 493.24;
	private static final double Y_DESCRIPTION_MENU4 = Y_HEADLINE_MENU4 + HEIGHT_HEADLINE;
	private static final double Y_INGREDIENTS_MENU4 = Y_DESCRIPTION_MENU4 + HEIGHT_DESCRIPTION;
	
	private static final double Y_HEADLINE_MENU5 = 596.17;
	private static final double Y_DESCRIPTION_MENU5 = Y_HEADLINE_MENU5 + HEIGHT_HEADLINE;
	private static final double Y_INGREDIENTS_MENU5 = Y_DESCRIPTION_MENU5 + HEIGHT_DESCRIPTION;
	
	private static final double Y_SALAD = 699.56;
	private static final double WIDTH_SALAD = 432.24;
	private static final double HEIGHT_SALAD_HEADLINE = 30.23;
	private static final double HEIGHT_SALAD_INGREDIENTS = HEIGHT_INGREDIENTS;
	
	@Autowired
	private FischerPdfParserConfiguration configuration;
	
	private PDFTextStripperByArea stripper;
	
	@Override
	public OffersDefinition parse(File file) throws IOException {
		PDDocument doc = PDDocument.load(file);
		try {
			stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);
			stripper.setLineSeparator("");
			stripper.setParagraphEnd("");
			addRegions(stripper);
			stripper.extractRegions((PDPage)doc.getDocumentCatalog().getAllPages().get(0));
			
			OfferPeriodicDefinition weeklyOffer = parseSaladWeeklyOffer();
			List<MealDefinition> meals = weeklyOffer.getMeals();
			
			OffersDefinition result = new OffersDefinition();
			addMondayOffers(result, meals);
			addTuesdayOffers(result, meals);
			addWednesdayOffers(result, meals);
			addThursdayOffers(result, meals);
			addFridayOffers(result, meals);
			return result;
		} finally {
			doc.close();
		}
	}
	
	private void addRegions(PDFTextStripperByArea stripper) {
		stripper.addRegion(REGION_DAY_1, new Rectangle2D.Double(X_DAY, Y_HEADLINE_MENU1, WIDTH_DAY, HEIGHT_ROW));
		stripper.addRegion(REGION_DAY_2, new Rectangle2D.Double(X_DAY, Y_HEADLINE_MENU2, WIDTH_DAY, HEIGHT_ROW));
		stripper.addRegion(REGION_DAY_3, new Rectangle2D.Double(X_DAY, Y_HEADLINE_MENU3, WIDTH_DAY, HEIGHT_ROW));
		stripper.addRegion(REGION_DAY_4, new Rectangle2D.Double(X_DAY, Y_HEADLINE_MENU4, WIDTH_DAY, HEIGHT_ROW));
		stripper.addRegion(REGION_DAY_5, new Rectangle2D.Double(X_DAY, Y_HEADLINE_MENU5, WIDTH_DAY, HEIGHT_ROW));
		
		stripper.addRegion(REGION_MONDAY_MENU_1 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU1, Y_HEADLINE_MENU1, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_MONDAY_MENU_1 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU1, Y_DESCRIPTION_MENU1, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_MONDAY_MENU_1 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU1, Y_INGREDIENTS_MENU1, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_MONDAY_MENU_2 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU2, Y_HEADLINE_MENU1, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_MONDAY_MENU_2 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU2, Y_DESCRIPTION_MENU1, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_MONDAY_MENU_2 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU2, Y_INGREDIENTS_MENU1, WIDTH_MENU, HEIGHT_INGREDIENTS));

		stripper.addRegion(REGION_MONDAY_MENU_3 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU3, Y_HEADLINE_MENU1, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_MONDAY_MENU_3 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU3, Y_DESCRIPTION_MENU1, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_MONDAY_MENU_3 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU3, Y_INGREDIENTS_MENU1, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_TUESDAY_MENU_1 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU1, Y_HEADLINE_MENU2, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_TUESDAY_MENU_1 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU1, Y_DESCRIPTION_MENU2, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_TUESDAY_MENU_1 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU1, Y_INGREDIENTS_MENU2, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_TUESDAY_MENU_2 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU2, Y_HEADLINE_MENU2, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_TUESDAY_MENU_2 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU2, Y_DESCRIPTION_MENU2, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_TUESDAY_MENU_2 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU2, Y_INGREDIENTS_MENU2, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_TUESDAY_MENU_3 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU3, Y_HEADLINE_MENU2, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_TUESDAY_MENU_3 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU3, Y_DESCRIPTION_MENU2, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_TUESDAY_MENU_3 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU3, Y_INGREDIENTS_MENU2, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_WEDNESDAY_MENU_1 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU1, Y_HEADLINE_MENU3, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_WEDNESDAY_MENU_1 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU1, Y_DESCRIPTION_MENU3, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_WEDNESDAY_MENU_1 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU1, Y_INGREDIENTS_MENU3, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_WEDNESDAY_MENU_2 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU2, Y_HEADLINE_MENU3, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_WEDNESDAY_MENU_2 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU2, Y_DESCRIPTION_MENU3, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_WEDNESDAY_MENU_2 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU2, Y_INGREDIENTS_MENU3, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_WEDNESDAY_MENU_3 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU3, Y_HEADLINE_MENU3, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_WEDNESDAY_MENU_3 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU3, Y_DESCRIPTION_MENU3, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_WEDNESDAY_MENU_3 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU3, Y_INGREDIENTS_MENU3, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_THURSDAY_MENU_1 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU1, Y_HEADLINE_MENU4, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_THURSDAY_MENU_1 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU1, Y_DESCRIPTION_MENU4, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_THURSDAY_MENU_1 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU1, Y_INGREDIENTS_MENU4, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_THURSDAY_MENU_2 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU2, Y_HEADLINE_MENU4, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_THURSDAY_MENU_2 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU2, Y_DESCRIPTION_MENU4, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_THURSDAY_MENU_2 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU2, Y_INGREDIENTS_MENU4, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_THURSDAY_MENU_3 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU3, Y_HEADLINE_MENU4, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_THURSDAY_MENU_3 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU3, Y_DESCRIPTION_MENU4, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_THURSDAY_MENU_3 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU3, Y_INGREDIENTS_MENU4, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_FRIDAY_MENU_1 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU1, Y_HEADLINE_MENU5, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_FRIDAY_MENU_1 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU1, Y_DESCRIPTION_MENU5, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_FRIDAY_MENU_1 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU1, Y_INGREDIENTS_MENU5, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_FRIDAY_MENU_2 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU2, Y_HEADLINE_MENU5, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_FRIDAY_MENU_2 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU2, Y_DESCRIPTION_MENU5, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_FRIDAY_MENU_2 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU2, Y_INGREDIENTS_MENU5, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_FRIDAY_MENU_3 + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU3, Y_HEADLINE_MENU5, WIDTH_MENU, HEIGHT_HEADLINE));
		stripper.addRegion(REGION_FRIDAY_MENU_3 + REGION_SUFFIX_DESCRIPTION, new Rectangle2D.Double(X_MENU3, Y_DESCRIPTION_MENU5, WIDTH_MENU, HEIGHT_DESCRIPTION));
		stripper.addRegion(REGION_FRIDAY_MENU_3 + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU3, Y_INGREDIENTS_MENU5, WIDTH_MENU, HEIGHT_INGREDIENTS));
		
		stripper.addRegion(REGION_SALAD + REGION_SUFFIX_HEADLINE, new Rectangle2D.Double(X_MENU1, Y_SALAD, WIDTH_SALAD, HEIGHT_SALAD_HEADLINE));
		stripper.addRegion(REGION_SALAD + REGION_SUFFIX_INGREDIENTS, new Rectangle2D.Double(X_MENU1, Y_SALAD, WIDTH_SALAD, HEIGHT_SALAD_INGREDIENTS));
	}
	
	private void addMondayOffers(OffersDefinition result, List<MealDefinition> weeklyMeals) {
		OfferDailyDefinition day = new OfferDailyDefinition();
		day.setDate(toDate(stripper.getTextForRegion(REGION_DAY_1)));
		parseMondayOffer(day);
		if (!day.getMeals().isEmpty()) {
			addMealsIfNotNull(day, weeklyMeals);
		}
		result.addDailyOffer(day);
	}
	
	private void parseMondayOffer(OfferDailyDefinition day) {
		addMealIfNotNull(day, parseMeal1(REGION_MONDAY_MENU_1));
		addMealIfNotNull(day, parseMeal2(REGION_MONDAY_MENU_2));
		addMealIfNotNull(day, parseMeal3(REGION_MONDAY_MENU_3));
	}
	
	private void addTuesdayOffers(OffersDefinition result, List<MealDefinition> weeklyMeals) {
		OfferDailyDefinition day = new OfferDailyDefinition();
		day.setDate(toDate(stripper.getTextForRegion(REGION_DAY_2)));
		parseTuesdayOffer(day);
		if (!day.getMeals().isEmpty()) {
			addMealsIfNotNull(day, weeklyMeals);
		}
		result.addDailyOffer(day);
	}
	
	private void parseTuesdayOffer(OfferDailyDefinition day) {
		addMealIfNotNull(day, parseMeal1(REGION_TUESDAY_MENU_1));
		addMealIfNotNull(day, parseMeal2(REGION_TUESDAY_MENU_2));
		addMealIfNotNull(day, parseMeal3(REGION_TUESDAY_MENU_3));
	}
	
	private void addWednesdayOffers(OffersDefinition result, List<MealDefinition> weeklyMeals) {
		OfferDailyDefinition day = new OfferDailyDefinition();
		day.setDate(toDate(stripper.getTextForRegion(REGION_DAY_3)));
		parseWednesdayOffer(day);
		if (!day.getMeals().isEmpty()) {
			addMealsIfNotNull(day, weeklyMeals);
		}
		result.addDailyOffer(day);
	}
	
	private void parseWednesdayOffer(OfferDailyDefinition day) {
		addMealIfNotNull(day, parseMeal1(REGION_WEDNESDAY_MENU_1));
		addMealIfNotNull(day, parseMeal2(REGION_WEDNESDAY_MENU_2));
		addMealIfNotNull(day, parseMeal3(REGION_WEDNESDAY_MENU_3));
	}
	
	private void addThursdayOffers(OffersDefinition result, List<MealDefinition> weeklyMeals) {
		OfferDailyDefinition day = new OfferDailyDefinition();
		day.setDate(toDate(stripper.getTextForRegion(REGION_DAY_4)));
		parseThursdayOffer(day);
		if (!day.getMeals().isEmpty()) {
			addMealsIfNotNull(day, weeklyMeals);
		}
		result.addDailyOffer(day);
	}
	
	private void parseThursdayOffer(OfferDailyDefinition day) {
		addMealIfNotNull(day, parseMeal1(REGION_THURSDAY_MENU_1));
		addMealIfNotNull(day, parseMeal2(REGION_THURSDAY_MENU_2));
		addMealIfNotNull(day, parseMeal3(REGION_THURSDAY_MENU_3));
	}
	
	private void addFridayOffers(OffersDefinition result, List<MealDefinition> weeklyMeals) {
		OfferDailyDefinition day = new OfferDailyDefinition();
		day.setDate(toDate(stripper.getTextForRegion(REGION_DAY_5)));
		parseFridayOffer(day);
		if (!day.getMeals().isEmpty()) {
			addMealsIfNotNull(day, weeklyMeals);
		}
		result.addDailyOffer(day);
	}
	
	private void parseFridayOffer(OfferDailyDefinition day) {
		addMealIfNotNull(day, parseMeal1(REGION_FRIDAY_MENU_1));
		addMealIfNotNull(day, parseMeal2(REGION_FRIDAY_MENU_2));
		addMealIfNotNull(day, parseMeal3(REGION_FRIDAY_MENU_3));
	}
	
	protected void addMealsIfNotNull(OfferDailyDefinition day, List<MealDefinition> meals) {
		for (MealDefinition meal : meals) {
			addMealIfNotNull(day, meal);
		}
	}
	
	protected void addMealIfNotNull(OfferDailyDefinition day, MealDefinition meal) {
		if (meal != null) {
			day.addMeal(meal);
		}
	}
	
	private OfferPeriodicDefinition parseSaladWeeklyOffer() {
		OfferPeriodicDefinition week = new OfferPeriodicDefinition();
		week.setStartDate(toDate(stripper.getTextForRegion(REGION_DAY_1)));
		week.setEndDate(toDate(stripper.getTextForRegion(REGION_DAY_5)));
		week.addMeal(parseMealSingle(REGION_SALAD, getConfiguration().getSaladType(), getConfiguration().getDefaultPrice()));
		return week;
	}
	
	private MealDefinition parseMeal1(String regionPrefix) {
		return parseMeal(regionPrefix, getConfiguration().getMenu1Type(), getConfiguration().getDefaultPrice());
	}
	
	private MealDefinition parseMeal2(String regionPrefix) {
		return parseMeal(regionPrefix, getConfiguration().getMenu2Type(), getConfiguration().getDefaultPrice());
	}
	
	private MealDefinition parseMeal3(String regionPrefix) {
		return parseMeal(regionPrefix, getConfiguration().getMenu3Type(), getConfiguration().getDefaultPrice());
	}
	
	private MealDefinition parseMeal(String regionPrefix, String type, int priceInCents) {
		String headline = StringUtils.trim(stripper.getTextForRegion(regionPrefix + REGION_SUFFIX_HEADLINE));
		if (ArrayUtils.contains(getConfiguration().getSkip(), headline.toUpperCase())) {
			return null;
		}
		
		headline = StringUtils.trim(StringUtils.replaceOnce(headline, "1,6", "")); // ekliger Hack für unsichtbarer Text in Vorlage
		int extraAmount = getExtraAmount(headline);
		
		MealDefinition meal = new MealDefinition();
		meal.setHeadline(headline); 
		meal.setDescription(StringUtils.trim(stripper.getTextForRegion(regionPrefix + REGION_SUFFIX_DESCRIPTION)));
		meal.setIngredients(StringUtils.trim(stripper.getTextForRegion(regionPrefix + REGION_SUFFIX_INGREDIENTS)));
		meal.setType(type);
		meal.setPriceInCents(priceInCents + extraAmount);
		return meal;
	}
	
	private int getExtraAmount(String headline) {
		String digits = headline.replaceAll("[^\\d.]+", "");
		if (NumberUtils.isNumber(digits) && StringUtils.contains(headline, '€')) {
			try {
				Number n = NumberFormat.getIntegerInstance(Locale.GERMANY).parse(digits);
				return n.intValue();
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return 0;
	}
	
	private MealDefinition parseMealSingle(String regionPrefix, String type, int priceInCents) {
		MealDefinition meal = new MealDefinition();
		meal.setHeadline(StringUtils.trim(stripper.getTextForRegion(regionPrefix + REGION_SUFFIX_HEADLINE)));
		meal.setDescription("");
		meal.setIngredients(StringUtils.trim(stripper.getTextForRegion(regionPrefix + REGION_SUFFIX_INGREDIENTS)));
		meal.setType(type);
		meal.setPriceInCents(priceInCents);
		return meal;
	}
	
	private Date toDate(String dateText) {
		String dayRemoved = StringUtils.replaceEach(dateText, 
				new String[] { "MO", "DI", "MI", "DO", "FR" },  
				new String[] { "", "", "", "", "" });
		String trimmed = StringUtils.trim(dayRemoved);
		try {
			return new SimpleDateFormat("dd.MM.yyyy").parse(trimmed);
		} catch (ParseException e) {
			return null;
		}
	}
	
	protected FischerPdfParserConfiguration getConfiguration() {
		return configuration;
	}
	
}
