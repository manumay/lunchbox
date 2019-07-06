package org.brainteam.lunchbox.report;import static org.mockito.Mockito.when;import static org.testng.Assert.assertTrue;import java.io.ByteArrayOutputStream;import java.io.File;import java.io.FileNotFoundException;import java.io.FileOutputStream;import java.io.IOException;import java.util.Arrays;import java.util.Date;import org.apache.commons.io.FileUtils;import org.brainteam.lunchbox.core.Clock;import org.brainteam.lunchbox.jmx.DailyOrderPdfReportConfiguration;import org.brainteam.lunchbox.json.JsonOfferItemInfo;import org.brainteam.lunchbox.json.JsonOrdererDetail;import org.brainteam.lunchbox.json.JsonOrdersDaily;import org.brainteam.lunchbox.json.JsonOrdersDailyOfferItem;import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;import org.testng.annotations.BeforeClass;import org.testng.annotations.Test;@Test(groups="unit")public class DailyOrderReportPdfImplTest {		private static final String DESCRIPTION1 = "mit Pommes Frites";	private static final String DESCRIPTION2 = "mit Ruccola und frischem Basilikum";	private static final String HEADLINE1 = "Frikadelle";	private static final String HEADLINE2 = "Pizza Margaritha";	private static final String NAME1 = "Menü 1";	private static final String NAME2 = "Menü 2";	@Mock	private JsonOrdersDaily json;		@Mock	private JsonOrdersDailyOfferItem jsonItem1, jsonItem2;		@Mock	private JsonOfferItemInfo jsonItemInfo1, jsonItemInfo2;		@Mock	private JsonOrdererDetail jsonOrderer1, jsonOrderer2, jsonOrderer3;		@Mock	private DailyOrderPdfReportConfiguration configuration;		@Mock	private Clock clock;		@InjectMocks	private DailyOrderReportPdfImpl report = new DailyOrderReportPdfImpl();		@BeforeClass	public void setup() {		MockitoAnnotations.initMocks(this);		when(clock.now()).thenReturn(new Date());		when(json.getDate()).thenReturn(new Date());		when(json.getItems()).thenReturn(Arrays.asList(jsonItem1, jsonItem2));		when(jsonItem1.getOfferItem()).thenReturn(jsonItemInfo1);		when(jsonItem1.getOrderers()).thenReturn(Arrays.asList(jsonOrderer1, jsonOrderer1));		when(jsonItem2.getOfferItem()).thenReturn(jsonItemInfo2);		when(jsonItem2.getOrderers()).thenReturn(Arrays.asList(jsonOrderer3));		when(jsonItemInfo1.getHeadline()).thenReturn(HEADLINE1);		when(jsonItemInfo1.getDescription()).thenReturn(DESCRIPTION1);		when(jsonItemInfo1.getName()).thenReturn(NAME1);		when(jsonItemInfo2.getHeadline()).thenReturn(HEADLINE2);		when(jsonItemInfo2.getDescription()).thenReturn(DESCRIPTION2);		when(jsonItemInfo2.getName()).thenReturn(NAME2);	}		@Test(expectedExceptions=IllegalArgumentException.class)	public void testOutputStreamNull() {		report.write(null, json);	}		@Test(expectedExceptions=IllegalArgumentException.class)	public void testJsonNull() {		report.write(new ByteArrayOutputStream(), null);	}		@Test	public void testWrite() throws FileNotFoundException, IOException {		File tempFile = File.createTempFile("dailyreport", ".pdf");		FileUtils.forceDeleteOnExit(tempFile);		FileOutputStream out = new FileOutputStream(tempFile);		report.write(out, json);		assertTrue(tempFile.exists());		assertTrue(tempFile.length() > 0);	}}