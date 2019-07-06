package org.brainteam.lunchbox.json;

import java.util.ArrayList;
import java.util.List;

public class JsonOrdersMonthly {

	private String personnelNumber;
	private String userFullname;
	private Integer month;
	private Integer year;
	private List<JsonOrdersDayOfMonth> days;
	private Long sum;
	
	public String getPersonnelNumber() {
		return personnelNumber;
	}
	
	public void setPersonnelNumber(String personnelNumber) {
		this.personnelNumber = personnelNumber;
	}
	
	public String getUserFullname() {
		return userFullname;
	}
	
	public void setUserFullname(String userFullname) {
		this.userFullname = userFullname;
	}
	
	public Integer getMonth() {
		return month;
	}
	
	public void setMonth(Integer month) {
		this.month = month;
	}
	
	public Integer getYear() {
		return year;
	}
	
	public void setYear(Integer year) {
		this.year = year;
	}

	public List<JsonOrdersDayOfMonth> getDays() {
		return days;
	}
	
	public void addToDays(JsonOrdersDayOfMonth day) {
		if (this.days == null) {
			days = new ArrayList<>();
		}
		days.add(day);
	}
	
	public Long getSum() {
		return sum;
	}
	
	public void setSum(Long sum) {
		this.sum = sum;
	}
	
}
