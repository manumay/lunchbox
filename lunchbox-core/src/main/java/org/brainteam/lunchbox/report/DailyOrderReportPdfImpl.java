package org.brainteam.lunchbox.report;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.brainteam.lunchbox.core.Clock;
import org.brainteam.lunchbox.jmx.DailyOrderPdfReportConfiguration;
import org.brainteam.lunchbox.json.JsonOrdersDaily;
import org.brainteam.lunchbox.json.JsonOrdersDailyOfferItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component
public class DailyOrderReportPdfImpl implements DailyOrderReport {
	
	@Autowired
	private DailyOrderPdfReportConfiguration configuration;
	
	@Autowired
	private Clock clock;
	
	public DailyOrderReportPdfImpl() {
		super();
	}

	@Override
	public void write(OutputStream out, JsonOrdersDaily json) {
		if (out == null) {
			throw new IllegalArgumentException("out must not be null");
		}
		if (json == null) {
			throw new IllegalArgumentException("json must not be null");
		}
		
		try {
	        writePdf(out, json);
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void writePdf(OutputStream out, JsonOrdersDaily json) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        
        document.add(createHeadline(json));
        
        Paragraph infoLine = createInfoLine();
        if (infoLine != null) {
        	document.add(infoLine);
        }
        
        List<PdfPTable> tables = createTables(json);
        if (!tables.isEmpty()) {
        	for (PdfPTable table : tables) {
        		document.add(table);
        	}
        } else {
        	writeEmptyParagraph(writer);
        }
        
        writeCreatedLine(writer);
        
        document.close();
	}
	
	private Phrase createHeadline(JsonOrdersDaily json) {
		Chunk chunk = new Chunk("Bestellung für " + new SimpleDateFormat("EEEE, dd. MMMM yyyy").format(json.getDate()), FontFactory.getFont(BaseFont.HELVETICA_BOLD, 12F));
		Phrase phrase = new Phrase();
		phrase.add(chunk);
		return phrase;
	}
	
	private Paragraph createInfoLine() {
		String orderInfo = getConfiguration().getInfo();
		if (StringUtils.isEmpty(orderInfo)) {
			return null;
		}
		Chunk chunk = new Chunk(orderInfo, FontFactory.getFont(BaseFont.HELVETICA, 8F));
		Phrase phrase = new Phrase();
		phrase.add(chunk);
		Paragraph paragraph = new Paragraph(phrase);
		paragraph.setSpacingBefore(0F);
		paragraph.setSpacingAfter(30F);
		return paragraph;
	}
	
	private void writeEmptyParagraph(PdfWriter writer) throws DocumentException, IOException {
		PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        cb.beginText();
        cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 12.0F);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Keine Bestellungen für diesen Tag.", cb.getPdfDocument().getPageSize().getWidth() / 2.0F, cb.getPdfDocument().getPageSize().getHeight() / 2.0F, 0F);
        cb.endText();
    	cb.restoreState();
	}

	
	private void writeCreatedLine(PdfWriter writer) throws DocumentException, IOException {
		PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        cb.beginText();
        cb.moveText(cb.getPdfDocument().leftMargin(), cb.getPdfDocument().bottomMargin());
        cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 8F);
        cb.showText("Erstellt mit Lunchbox 360 am " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(getClock().now()) + " Uhr");
        cb.endText();
        cb.restoreState();
	}
	
	private List<PdfPTable> createTables(JsonOrdersDaily json) throws DocumentException {
		List<PdfPTable> tables = new ArrayList<>();
		for (JsonOrdersDailyOfferItem item : json.getItems()) {
			PdfPTable table = createTable(item);
			table.setWidthPercentage(100F);
			table.setWidths(new float[] { 80F, 20F });
			table.setSpacingAfter(20F);
			tables.add(table);
		}
		return tables;
	}
	
	private PdfPTable createTable(JsonOrdersDailyOfferItem json) {
		PdfPTable table = new PdfPTable(2);
		Chunk chunk;
        Phrase phrase;
        PdfPCell cell;
        
        cell = new PdfPCell();
        phrase = new Phrase();
        chunk = new Chunk(json.getOfferItem().getName() + ": " + json.getOfferItem().getHeadline(), FontFactory.getFont(BaseFont.HELVETICA_BOLD, 12F));
        phrase.add(chunk);
        cell.addElement(phrase);
        phrase = new Phrase();
        chunk = new Chunk(json.getOfferItem().getDescription(), FontFactory.getFont(BaseFont.HELVETICA, 12F));
        phrase.add(chunk);
        cell.setPadding(10F);
        cell.setPaddingBottom(15F);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.addElement(phrase);
        table.addCell(cell);

        phrase = new Phrase();
        chunk = new Chunk(json.getOrderers().size() + "x", FontFactory.getFont(BaseFont.HELVETICA_BOLD, 25F));
        phrase.add(chunk);
        cell = new PdfPCell(phrase);
        cell.setPadding(10F);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        table.addCell(cell);

        return table;
	}
	
	protected DailyOrderPdfReportConfiguration getConfiguration() {
		return configuration;
	}
	
	protected Clock getClock() {
		return clock;
	}
	
}
