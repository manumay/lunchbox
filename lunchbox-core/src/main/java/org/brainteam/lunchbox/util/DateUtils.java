package org.brainteam.lunchbox.util;

import java.util.Date;

import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public final class DateUtils {

	private DateUtils() {
	}
	
	public static Date toDate(Integer year, Integer month, Integer day) {
		LocalDate localDate = new LocalDate(year, month, day);
		return localDate.toDate();
	}
	
	public static final Date getDay(int dayOfMonth, int monthOfYear, int year) {
		return new LocalDate(year, monthOfYear, dayOfMonth).toDate();
	}
	
	public static final Date getFirstDayOfMonth(int month, int year) {
		return firstDayOfMonth(month, year).toDate();
	}
	
	protected static final LocalDate firstDayOfMonth(int month, int year) {
		 return new LocalDate(year, month, 1);
	}
	
	public static final Date getLastDayOfMonth(int month, int year) {
		return firstDayOfMonth(month, year).dayOfMonth().withMaximumValue().toDate();
	}
	
	public static final Date getEndOfMonth(int month, int year) {
		return getEndOfDay(getLastDayOfMonth(month, year));
	}
	
	public static final Date todayMidnight() {
		return LocalDate.now().toDateMidnight().toDate();
	}
	
	public static final Date tomorrowMidnight() {
		return LocalDate.now().toDateMidnight().plusDays(1).toDate();
	}
	
	public static final Date dayAfterTomorrowMidnight() {
		return LocalDate.now().toDateMidnight().plusDays(2).toDate();
	}
	
	public static final Date tomorrow(int hour) {
		return LocalDateTime.now().withHourOfDay(hour).plusDays(1).toDate();
	}
	
	public static final Date midnight(Date date) {
		return new DateMidnight(date).toDate();
	}
	
	public static final Date now() {
		return LocalDateTime.now().toDate();
	}
	
	public static final int lastMonth() {
		return LocalDate.now().minusMonths(1).getMonthOfYear();
	}
	
	public static final int lastMonthYear() {
		return LocalDate.now().minusMonths(1).getYear();
	}
	
	public static final Date getStartOfDay(Date date) {
		return LocalDate.fromDateFields(date).toDateTimeAtStartOfDay().toDate();
	}
	
	public static final Date getEndOfDay(Date date) {
		return LocalDate.fromDateFields(date).plusDays(1).toDateTimeAtStartOfDay().toDate();
	}
	
	public static final Date getWithHourOfDay(Date date, int hour) {
		return LocalDateTime.fromDateFields(date).withHourOfDay(hour).toDate();
	}
	
	public static final boolean isWeekday(Date date) {
		int dayOfWeek = new LocalDate(date).getDayOfWeek();
		return dayOfWeek <= 5;
	}
	
	public static final boolean isPastOrWithinNextWeek(Date date) {
		return LocalDateTime.fromDateFields(date).minusDays(7).compareTo(LocalDateTime.now()) < 0;
	}
	
}
