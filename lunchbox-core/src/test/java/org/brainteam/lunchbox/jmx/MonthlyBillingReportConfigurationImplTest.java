package org.brainteam.lunchbox.jmx;import org.mockito.InjectMocks;import org.testng.annotations.Test;@Test(groups= { "jmx", "unit", "broken" })public class MonthlyBillingReportConfigurationImplTest extends BaseConfigurationTest {		private static final String TITLE = "title";		@InjectMocks	private MonthlyBillingReportConfigurationImpl config = new MonthlyBillingReportConfigurationImpl();		@Override	protected String getFilename() {		return MonthlyBillingReportConfigurationImpl.CONFIGURATION_FILENAME;	}		@Test	public void testGetAndSetTitle() {		testGetAndSet(new TestCase() {			@Override			protected String getPropertyName() {				return MonthlyBillingReportConfigurationImpl.TITLE_PROPERTY;			}			@Override			protected Object getDefaultValue() {				return MonthlyBillingReportConfigurationImpl.TITLE_DEFAULT;			}			@Override			protected Object getUpdateValue() {				return TITLE;			}			@Override			protected Object getCurrentValue() {				return config.getTitle();			}			@Override			protected void setValue(Object value) {				config.setTitle((String)value);			}					});	}	}