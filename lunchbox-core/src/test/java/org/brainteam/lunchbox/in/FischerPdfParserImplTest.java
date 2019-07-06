package org.brainteam.lunchbox.in;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.brainteam.lunchbox.jmx.FischerPdfParserConfiguration;
import org.brainteam.lunchbox.test.TestUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups={"unit"})
public class FischerPdfParserImplTest {

	private static final String PDF_FILENAME_1 = "aktuell.pdf";
	private static final String JSON_FILENAME_1 = "aktuell.json";
	private static final String PDF_FILENAME_2 = "kommend.pdf";
	private static final String JSON_FILENAME_2 = "kommend.json";
	private static final String PDF_FILENAME_3 = "feiertage.pdf";
	private static final String JSON_FILENAME_3 = "feiertage.json";
	
	private static final File PDF_FILE_1 = TestUtils.getTestFile(PDF_FILENAME_1, FischerPdfParserImplTest.class);
	private static final File JSON_FILE_1 = TestUtils.getTestFile(JSON_FILENAME_1, FischerPdfParserImplTest.class);
	private static final File PDF_FILE_2 = TestUtils.getTestFile(PDF_FILENAME_2, FischerPdfParserImplTest.class);
	private static final File JSON_FILE_2 = TestUtils.getTestFile(JSON_FILENAME_2, FischerPdfParserImplTest.class);
	private static final File PDF_FILE_3 = TestUtils.getTestFile(PDF_FILENAME_3, FischerPdfParserImplTest.class);
	private static final File JSON_FILE_3 = TestUtils.getTestFile(JSON_FILENAME_3, FischerPdfParserImplTest.class);
	
	@Mock
	private FischerPdfParserConfiguration configuration;
	
	@InjectMocks
	private FischerPdfParserImpl parser = new FischerPdfParserImpl();
	
	@BeforeClass
    public void init() {
		MockitoAnnotations.initMocks(this);
		when(configuration.getMenu1Type()).thenReturn("Menü 1");
		when(configuration.getMenu2Type()).thenReturn("Menü 2");
		when(configuration.getMenu3Type()).thenReturn("Menü 3");
		when(configuration.getSaladType()).thenReturn("Salat");
		when(configuration.getDefaultPrice()).thenReturn(395);
	}
	
	@Test(enabled=false) // does not work aat the moment (manumay, 07/2019)
	public void testParse1() throws IOException {
		test(PDF_FILE_1, JSON_FILE_1);
	}
	
	@Test(enabled=false) // does not work aat the moment (manumay, 07/2019)
	public void testParse2() throws IOException {
		test(PDF_FILE_2, JSON_FILE_2);
	}
	
	@Test(enabled=false) // does not work aat the moment (manumay, 07/2019)
	public void testParse3() throws IOException {
		test(PDF_FILE_3, JSON_FILE_3);
	}
	
	protected void test(File pdfFile, File jsonFile) throws IOException {
		OffersDefinition def = parser.parse(pdfFile);
		assertThat(def, notNullValue());

		OffersDefinition expected = new ObjectMapper().readValue(jsonFile, OffersDefinition.class);
		assertThat(def, samePropertyValuesAs(expected));
	}

}
