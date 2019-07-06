package org.brainteam.lunchbox.services;

import java.util.Date;
import java.util.List;

import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.json.JsonOrdersBilling;
import org.brainteam.lunchbox.json.JsonOrdersDaily;
import org.brainteam.lunchbox.json.JsonOrdersMonthly;

public interface ReportService {
	
	JsonOrdersDaily getDailyReportJson(Date date);
	
	JsonOrdersDaily getDailyReportJson(Offer offer);
	
	JsonOrdersMonthly getMonthlyReportJson(Integer month, Integer year);
	
	JsonOrdersBilling getBillingReportJson(Integer month, Integer year);
	
	List<JsonOrdersBilling> getBillingReportJson(Integer month, Integer year, int count);

}
