package org.flexpay.eirc.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class PdfKvitWriter {
	private PdfReader kvitPattern;
	private ByteArrayOutputStream baos;

	public PdfKvitWriter(InputStream kvitPattern) throws IOException {
		baos = new ByteArrayOutputStream();
		this.kvitPattern = new PdfReader(kvitPattern);
	}

	public byte[] writeGetByteArray() throws DocumentException, IOException {
		baos.reset();
		write(baos);
		return baos.toByteArray();
	}

	public void write(OutputStream os) throws DocumentException, IOException {
		PdfStamper stamper = new PdfStamper(kvitPattern, os);
		stamper.setFormFlattening(true);
		AcroFields form = stamper.getAcroFields();
		fillForm(form);
		stamper.close();
	}

	private void fillForm(AcroFields form) throws IOException,
			DocumentException {
		// this is fake
		form.setField("raschet_za_date", "Сентябрь 2007");
		form.setField("summa_k_oplate", "262.62");
		form.setField("platelshik", "Коваль Н.А.");
		form.setField("address", "ул. Дарвина, д. 1, кв. 3");
		form.setField("serv1", "Подогр. воды");
		form.setField("serv2", "Подогр. воды");
		form.setField("serv3", "Подогр. воды");
		form.setField("serv4", "Подогр. воды");
		form.setField("serv5", "Подогр. воды");
		form.setField("serv6", "Подогр. воды");
		form.setField("serv7", "Подогр. воды");
		form.setField("serv8", "Подогр. воды");
		form.setField("serv9", "Подогр. воды");
	}

}
