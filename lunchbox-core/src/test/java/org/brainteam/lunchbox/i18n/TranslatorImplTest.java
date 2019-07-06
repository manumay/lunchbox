package org.brainteam.lunchbox.i18n;import static org.mockito.Mockito.when;import static org.testng.Assert.assertEquals;import static org.testng.Assert.assertTrue;import java.util.Locale;import org.apache.commons.lang.StringUtils;import org.brainteam.lunchbox.jmx.InternationalizationConfiguration;import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;import org.testng.annotations.BeforeClass;import org.testng.annotations.BeforeMethod;import org.testng.annotations.Test;@Test(groups="unit")public class TranslatorImplTest {		private static final String APPLICATION_NAME = "application.name";	private static final String APPLICATION_NAME_DE = "Essbox";	private static final String APPLICATION_NAME_EN = "Lunchbox";	private static final String APPLICATION_VERSION = "application.version";	private static final String APPLICATION_VERISON_ARG = "1.0";	private static final String APPLICATION_VERSION_RESULT = "Version 1.0";	private static final String UNKNOW = "bla";	private static final String UNKNOW_RESULT = "!bla!";	@Mock	private InternationalizationConfiguration configuration;		@InjectMocks	private TranslatorImpl translator = new TranslatorImpl();		@BeforeClass	public void setup() {		MockitoAnnotations.initMocks(this);	}		@BeforeMethod	public void updateLocale() {		when(configuration.getLocale()).thenReturn(Locale.GERMAN);	}		@Test	public void testGetApplicationName() {		String result = translator.getApplicationName();		assertTrue(!StringUtils.isEmpty(result) && !result.startsWith("!"));	}		@Test	public void testTranslate() {		assertEquals(translator.t(APPLICATION_NAME), APPLICATION_NAME_DE);		when(configuration.getLocale()).thenReturn(Locale.ENGLISH);		assertEquals(translator.t(APPLICATION_NAME), APPLICATION_NAME_EN);	}		@Test	public void testTranslateUnknow() {		assertEquals(translator.t(UNKNOW), UNKNOW_RESULT);		when(configuration.getLocale()).thenReturn(Locale.ENGLISH);		assertEquals(translator.t(UNKNOW), UNKNOW_RESULT);	}		@Test	public void testTranslateWithArguments() {		assertEquals(translator.t(APPLICATION_VERSION, APPLICATION_VERISON_ARG),				APPLICATION_VERSION_RESULT);	}	}