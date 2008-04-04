package org.flexpay.eirc.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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

	public byte[] writeGetByteArray(KvitForm kvitForm)
			throws DocumentException, IOException {
		baos.reset();
		write(baos, kvitForm);
		return baos.toByteArray();
	}

	public void write(OutputStream os, KvitForm kvitForm)
			throws DocumentException, IOException {
		PdfStamper stamper = new PdfStamper(kvitPattern, os);
		stamper.setFormFlattening(true);
		AcroFields form = stamper.getAcroFields();
		fillForm(form, kvitForm);
		stamper.close();
	}

	private void fillForm(AcroFields form, KvitForm kvitForm)
			throws IOException, DocumentException {
		form.setField("date", kvitForm.date);
		form.setField("creationDate", kvitForm.creationDate);
		form.setField("paySum", kvitForm.paySum);
		form.setField("payer", kvitForm.payer);
		form.setField("address", kvitForm.address);
		form.setField("totalArea", kvitForm.totalArea);
		form.setField("heatArea", kvitForm.heatArea);
		form.setField("personNumber", kvitForm.personNumber);
		form.setField("privilegePersonNumber", kvitForm.privilegePersonNumber);
		form.setField("debt1Date", kvitForm.debt1Date);
		form.setField("debt1Total", kvitForm.debt1Total);
		form.setField("debt2Date", kvitForm.debt2Date);
		form.setField("debt2Total", kvitForm.debt2Total);

		int i = 0;
		if (kvitForm.rentPartList != null) {
			for (RentPartForm rentPartForm : kvitForm.rentPartList) {
				form.setField("rentPartName_" + i, rentPartForm.name);
				form.setField("rentPartTarif_" + i, rentPartForm.tarif);
				form.setField("rentPartPereraschet_" + i,
						rentPartForm.pereraschet);
				i++;
			}
		}

		if (kvitForm.serviceList != null) {
			i = 0;
			for (ServiceForm serviceForm : kvitForm.serviceList) {
				form.setField("name_" + i, serviceForm.name);
				form.setField("meterMeasuringResult1_" + i,
						serviceForm.meterMeasuringResult1);
				form.setField("meterMeasuringResult2_" + i,
						serviceForm.meterMeasuringResult2);
				form.setField("paySum_" + i, serviceForm.paySum);
				// ...
				i++;
			}
		}
	}

	public static class KvitForm {
		public String date = "";
		public String creationDate = "";
		public String paySum = "";
		public String payer = "";
		public String address = "";
		public String totalArea = "";
		public String heatArea = "";
		public String personNumber = "";
		public String privilegePersonNumber = "";
		public String debt1Date = "";
		public String debt1Total = "";
		public String debt2Date = "";
		public String debt2Total = "";
		public List<ServiceForm> serviceList;
		public List<RentPartForm> rentPartList;
	}

	public static class ServiceForm {
		public String name = "";
		public String meterMeasuringResult1 = "";
		public String meterMeasuringResult2 = "";
		public String paySum = "";
		public String debt1 = "";
		public String debt2 = "";
		public String tarif = "";
		public String rashod = "";
		public String nachisleno = "";
		public String lgota = "";
		public String subsidiya = "";
		public String pereraschet = "";
		public String oplacheno = "";
	}

	public static class RentPartForm {
		public String name = "";
		public String tarif = "";
		public String pereraschet = "";
	}

}
