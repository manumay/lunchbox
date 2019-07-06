package org.brainteam.lunchbox.jmx;

import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.config:name=Statistiken", description = "Einstellungen für Statistiken")
public class StatisticsDAOConfigurationImpl extends PropertyConfiguration implements StatisticsDAOConfiguration {

	private static final String FILENAME = "statistics.properties";
	
	private static final String CHINESE_DEFAULT = "Asia,Noodles";
	private static final String CHINESE_PROPERTY = "chinese";
	private static final String FISH_DEFAULT = "Lachs,Forelle,Heilbutt,Pangasius,Dorsch,Thunfisch,Kabeljau,Karpfen,Barsch,barsch,Fisch,fisch";
	private static final String FISH_PROPERTY = "fish";
	private static final Integer GOURMETMINPRICE_DEFAULT = Integer.valueOf(400);
	private static final String GOURMETMINPRICE_PROPERTY = "gourmetminprice";
	private static final String MAMA_DEFAULT = "Mama";
	private static final String MAMA_PROPERTY = "mama";
	private static final String ITALIAN_DEFAULT = "Spaghetti,Lasagne,Pasta,Italienisch,italienisch";
	private static final String ITALIAN_PROPERTY = "italian";
	private static final String SCHNITZEL_DEFAULT = "Schnitzel,schnitzel";
	private static final String SCHNITZEL_PROPERTY = "schnitzel";
	private static final String SWABIAN_DEFAULT = "Schwäbisch,schwäbisch,Maultaschen";
	private static final String SWABIAN_PROPERTY = "swabian";
	
	public StatisticsDAOConfigurationImpl() {
		super(FILENAME);
	}
	
	@Override
	@ManagedAttribute
	public String[] getChineseSearchWords() {
		return getPropertyAsStringArray(CHINESE_PROPERTY, CHINESE_DEFAULT);
	}
	
	@ManagedAttribute
	public void setChineseSearchWords(String chinese) {
		setProperty(CHINESE_PROPERTY, chinese);
	}
	
	@Override
	@ManagedAttribute
	public String[] getFishSearchWords() {
		return getPropertyAsStringArray(FISH_PROPERTY, FISH_DEFAULT);
	}
	
	@ManagedAttribute
	public void setFishSearchWords(String fish) {
		setProperty(FISH_PROPERTY, fish);
	}
	
	@Override
	@ManagedAttribute
	public String[] getItalianSearchWords() {
		return getPropertyAsStringArray(ITALIAN_PROPERTY, ITALIAN_DEFAULT);
	}
	
	@ManagedAttribute
	public void setItalianSearchWords(String italian) {
		setProperty(ITALIAN_PROPERTY, italian);
	}
	
	@Override
	@ManagedAttribute
	public String[] getMamaSearchWords() {
		return getPropertyAsStringArray(MAMA_PROPERTY, MAMA_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMamaSearchWords(String mama) {
		setProperty(MAMA_PROPERTY, mama);
	}
	
	@Override
	@ManagedAttribute
	public String[] getSchnitzelSearchWords() {
		return getPropertyAsStringArray(SCHNITZEL_PROPERTY, SCHNITZEL_DEFAULT);
	}
	
	@ManagedAttribute
	public void setSchnitzelSearchWords(String schnitzel) {
		setProperty(SCHNITZEL_PROPERTY, schnitzel);
	}
	
	@Override
	@ManagedAttribute
	public String[] getSwabianSearchWords() {
		return getPropertyAsStringArray(SWABIAN_PROPERTY, SWABIAN_DEFAULT);
	}
	
	@ManagedAttribute
	public void setSwabianSearchWords(String swabian) {
		setProperty(SWABIAN_PROPERTY, swabian);
	}
	
	@Override
	@ManagedAttribute
	public Integer getGourmetMinPrice() {
		return getPropertyAsInteger(GOURMETMINPRICE_PROPERTY, GOURMETMINPRICE_DEFAULT);
	}
	
	@ManagedAttribute
	public void setGourmetMinPrice(Integer gourmetMinPrice) {
		setProperty(GOURMETMINPRICE_PROPERTY, String.valueOf(gourmetMinPrice));
	}
	
	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.setProperty(CHINESE_PROPERTY, CHINESE_DEFAULT);
		p.setProperty(FISH_PROPERTY, FISH_DEFAULT);
		p.setProperty(ITALIAN_PROPERTY, ITALIAN_DEFAULT);
		p.setProperty(MAMA_PROPERTY, MAMA_DEFAULT);
		p.setProperty(SCHNITZEL_PROPERTY, SCHNITZEL_DEFAULT);
		p.setProperty(SWABIAN_PROPERTY, SWABIAN_DEFAULT);
		return p;
	}
	
}
