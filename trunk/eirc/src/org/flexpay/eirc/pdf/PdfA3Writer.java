package org.flexpay.eirc.pdf;

import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @deprecated
 */
public class PdfA3Writer {
	private PdfWriter writer;
	private PdfContentByte cb;
	private Document document;
	private int kvitCount;

	public PdfA3Writer(OutputStream os) throws DocumentException {
		document = new Document(PageSize.A3);
		writer = PdfWriter.getInstance(document, os);
		document.open();
		cb = writer.getDirectContent();
	}

	private PdfReader kvitReader = null;
	public void write(byte[] kvit) throws IOException {
		kvitReader = new PdfReader(kvit);
		//if(kvitReader == null) { kvitReader = new PdfReader(kvit); }
		write(kvitReader);
		kvitReader.close();
	}

	public void write(PdfReader kvit) throws IOException {
		PdfImportedPage page = writer.getImportedPage(kvit, 1);
		float kvitHeight = page.getHeight();
		float delta = (PageSize.A3.getWidth() / 2 - kvitHeight) / 2;
		if (kvitCount == 0) {
			document.newPage();
			cb.addTemplate(page, 0f, 1f, -1f, 0f, delta + kvitHeight,
					PageSize.A3.getHeight() / 2);
		} else if (kvitCount == 1) {
			cb.addTemplate(page, 0f, 1f, -1f, 0f, delta * 3 + kvitHeight * 2,
					PageSize.A3.getHeight() / 2);
		} else if (kvitCount == 2) {
			cb.addTemplate(page, 0f, 1f, -1f, 0f, delta + kvitHeight, 0);
		} else if (kvitCount == 3) {
			cb.addTemplate(page, 0f, 1f, -1f, 0f, delta * 3 + kvitHeight
							* 2, 0);
		}
		
		writer.freeReader(kvit);

		if (kvitCount < 3) {
			kvitCount++;
		} else {
			kvitCount = 0;
		}
	}
	
	public void close() {
		document.close();
	}

}
