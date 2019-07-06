package org.brainteam.lunchbox.mvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.brainteam.lunchbox.json.JsonOrdersBilling;
import org.brainteam.lunchbox.json.JsonOrdersDaily;
import org.brainteam.lunchbox.json.JsonOrdersMonthly;
import org.brainteam.lunchbox.report.DailyOrderReport;
import org.brainteam.lunchbox.report.MonthlyBillingReport;
import org.brainteam.lunchbox.services.ReportService;
import org.brainteam.lunchbox.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ReportResource {
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private DailyOrderReport dailyOrderReport;
	
	@Autowired
	private MonthlyBillingReport monthlyBillingReport;

	@RequestMapping(value="/json/reports/{year}/{month}/{day}", method=RequestMethod.GET)
	public @ResponseBody JsonOrdersDaily getDailyReport(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
		Date date = DateUtils.toDate(year, month, day);
		return getReportService().getDailyReportJson(date);
	}
	
	@RequestMapping(value="/json/reports/{year}/{month}/mine", method=RequestMethod.GET)
	public @ResponseBody JsonOrdersMonthly getMonthlyReport(@PathVariable Integer year, @PathVariable Integer month) {
		return getReportService().getMonthlyReportJson(month, year);
	}
	
	@RequestMapping(value="/json/reports/{year}/{month}/billing", method=RequestMethod.GET)
	public @ResponseBody JsonOrdersBilling getBillingReport(@PathVariable Integer year, @PathVariable Integer month) {
		return getReportService().getBillingReportJson(month, year);
	}
	
	@RequestMapping(value="/reports/{year}/{month}/{day}/Bestellung.pdf", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void getDailyReportBinary(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day,
			HttpServletResponse response) throws IOException {
		final JsonOrdersDaily json = getDailyReport(year, month, day);
    	getDailyOrderReport().write(response.getOutputStream(), json);
	}
	
	@RequestMapping(value="/reports/billing.{month}-{year}.xls", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void getBillingReportBinary(@PathVariable Integer year, @PathVariable Integer month,
			HttpServletResponse response) throws IOException {
		final JsonOrdersBilling json = getBillingReport(year, month);
    	getMonthlyBillingReport().write(response.getOutputStream(), Arrays.asList(json));
	}
	
	protected DailyOrderReport getDailyOrderReport() {
		return dailyOrderReport;
	}
	
	protected MonthlyBillingReport getMonthlyBillingReport() {
		return monthlyBillingReport;
	}
	
	protected ReportService getReportService() {
		return reportService;
	}
}
