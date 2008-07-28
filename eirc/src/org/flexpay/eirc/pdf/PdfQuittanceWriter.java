package org.flexpay.eirc.pdf;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.ServiceTypeService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.jetbrains.annotations.NonNls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PdfQuittanceWriter {

	private static final Locale LOCALE_RU = new Locale("ru");

	private QuittanceService quittanceService;
	private ServiceTypeService serviceTypeService;

	private InputStream kvitPattern;
	private PdfReader titlePattern;
	private ByteArrayOutputStream baos;

	public PdfQuittanceWriter(InputStream kvitPattern, InputStream titlePattern)
			throws IOException {
		baos = new ByteArrayOutputStream();
		this.kvitPattern = kvitPattern;
		this.titlePattern = new PdfReader(titlePattern);
	}

	public byte[] writeGetByteArray(Quittance quittance)
			throws DocumentException, IOException, FlexPayException {
		baos.reset();
		write(baos, quittance);
		return baos.toByteArray();
	}

	public void write(OutputStream os, Quittance quittance)
			throws DocumentException, IOException, FlexPayException {
		PdfStamper stamper = new PdfStamper(new PdfReader(kvitPattern), os);
		stamper.setFormFlattening(true);

		AcroFields form = stamper.getAcroFields();
		String[] barcodes = fillForm(form, quittance);

		PdfContentByte cb = stamper.getUnderContent(1);
		Image image = getBarcode1d(cb, barcodes[0]);
		image.scalePercent(80, 60);
		image.setAbsolutePosition(15, PageSize.A4.getHeight() / 2 - 115);
		//image.setRotationDegrees(90);
		cb.addImage(image);

		image = getBarcode2d(barcodes[1]);
		image.setAbsolutePosition(PageSize.A4.getWidth() - 50, PageSize.A4
				.getHeight() / 2 - 220);
		image.setRotationDegrees(90);
		cb.addImage(image);

		stamper.close();
	}

	public byte[] writeTitleGetByteArray(String title)
			throws DocumentException, IOException {
		baos.reset();
		writeTitle(baos, title);
		return baos.toByteArray();
	}

	public void writeTitle(OutputStream os, String title)
			throws DocumentException, IOException {
		PdfStamper stamper = new PdfStamper(titlePattern, os);
		stamper.setFormFlattening(true);
		@NonNls AcroFields form = stamper.getAcroFields();
		form.setField("name", title);
		stamper.close();
	}

	private Image getBarcode1d(PdfContentByte cb, String code) {
		Barcode128 barcode = new Barcode128();
		barcode.setCodeType(Barcode.CODE128_UCC);
		barcode.setCode(code);

		return barcode.createImageWithBarcode(cb, null, null);
	}

	private Image getBarcode2d(String code) throws BadElementException {
		BarcodePDF417 barcode = new BarcodePDF417();
		barcode.setText(code);

		return barcode.getImage();

	}

	private String[] fillForm(@NonNls AcroFields form, Quittance quittance)
			throws IOException, DocumentException, FlexPayException {
		form.setField("header", "КП “ЖИЛКОМСЕРВИС”");
		form.setField("header_copy1", "КП “ЖИЛКОМСЕРВИС”");
		form.setField("lsZks", "1000300660");
		form.setField("lsZks_copy1", "1000300660");
		form
				.setField("properties",
						"р/сч 260005593 в ОАО “МЕГАБАНК” МФО 351629, КОД ОКПО 34467793");
		form.setField("footer", "Киевский филиал");

		@NonNls DateFormat format = new SimpleDateFormat("MMMM yyyy", LOCALE_RU);
		form.setField("date", format.format(quittance.getDateFrom()));
		form.setField("date_copy1", format.format(quittance.getDateFrom()));
		format = new SimpleDateFormat("dd.MM.yyyy");
		form.setField("creationDate", format
				.format(quittance.getCreationDate()));
		form.setField("dateFrom", format.format(quittance.getDateFrom()));
		form.setField("dateTill", format.format(quittance.getDateTill()));

		// form.setField("paySum", kvitForm.paySum);
		String payer = quittanceService.getPayer(quittance);
		form.setField("payer", payer);
		form.setField("payer_copy1", payer);
		String address = quittanceService.getAddressStr(quittance, true);
		form.setField("address", address);
		form.setField("address_copy1", address);

		BigDecimal dateFromSum = new BigDecimal(0);
		BigDecimal dateTillSum = new BigDecimal(0);

		// Services 2-5
		for (int i = 2; i <= 5; i++) {
			QuittanceDetails quittanceDetails = quittanceService
					.calculateTotalQuittanceDetails(quittance,
							serviceTypeService.getServiceType(i));
			if (quittanceDetails != null) {
				BigDecimal outgoingBalance = quittanceDetails
						.getOutgoingBalance();
				BigDecimal incomingBalance = quittanceDetails
						.getIncomingBalance();
				if (outgoingBalance != null) {
					dateTillSum = dateTillSum.add(outgoingBalance);
					String[] amountDigitArray = getStringArray(outgoingBalance);
					form.setField("service" + i + "_amount_digit0",
							amountDigitArray[0]);
					form.setField("service" + i + "_amount_digit1",
							amountDigitArray[1]);
					form.setField("service" + i + "_amount_digit2",
							amountDigitArray[2]);
					form.setField("service" + i + "_amount_digit3",
							amountDigitArray[3]);
					form.setField("service" + i + "_amount_digit4",
							amountDigitArray[4]);
					form.setField("service" + i + "_amount_digit5",
							amountDigitArray[5]);
					form.setField("service" + i + "_amount", outgoingBalance
							.toString());
					form.setField("service" + i + "_dateTill_amount",
							outgoingBalance.toString());
				}
				if (incomingBalance != null) {
					dateFromSum = dateFromSum.add(incomingBalance);
					form.setField("service" + i + "_dateFrom_amount",
							quittanceDetails.getIncomingBalance().toString());
				}
			}
		}

		// Kvartplata services
		QuittanceDetails quittanceDetails = quittanceService
				.calculateTotalQuittanceDetails(quittance, serviceTypeService
						.getServiceType(1));
		if (quittanceDetails != null) {
			BigDecimal outgoingBalance = quittanceDetails
					.getOutgoingBalance();
			BigDecimal incomingBalance = quittanceDetails
					.getIncomingBalance();
			if (outgoingBalance != null) {
				dateTillSum = dateTillSum.add(outgoingBalance);
				String[] amountDigitArray = getStringArray(outgoingBalance);
				form.setField("kvartpl_amount_digit0", amountDigitArray[0]);
				form.setField("kvartpl_amount_digit1", amountDigitArray[1]);
				form.setField("kvartpl_amount_digit2", amountDigitArray[2]);
				form.setField("kvartpl_amount_digit3", amountDigitArray[3]);
				form.setField("kvartpl_amount_digit4", amountDigitArray[4]);
				form.setField("kvartpl_amount_digit5", amountDigitArray[5]);
				form.setField("kvartpl_amount", outgoingBalance.toString());
				form.setField("kvartpl_dateTill_amount", outgoingBalance.toString());

			}
			if (incomingBalance != null) {
				dateFromSum = dateFromSum.add(incomingBalance);
				form.setField("kvartpl_dateFrom_amount", incomingBalance.toString());
			}
		}

		// Services sum
		String[] amountDigitArray = getStringArray(dateTillSum);
		form.setField("sum_amount_digit0", amountDigitArray[0]);
		form.setField("sum_amount_digit1", amountDigitArray[1]);
		form.setField("sum_amount_digit2", amountDigitArray[2]);
		form.setField("sum_amount_digit3", amountDigitArray[3]);
		form.setField("sum_amount_digit4", amountDigitArray[4]);
		form.setField("sum_amount_digit5", amountDigitArray[5]);
		form.setField("sum_amount", dateTillSum.toString());
		form.setField("paySum", dateTillSum.toString());
		form.setField("sum_dateFrom_amount", dateFromSum.toString());
		form.setField("sum_dateTill_amount", dateTillSum.toString());

		String[] barcodes = new String[2];
		StringBuilder barcodeStr = new StringBuilder();
		barcodeStr.append(ApplicationConfig.getEircId());
		// TODO ticketNumber replace by ticketId
		barcodeStr.append(StringUtil.fillLeadingZero(String.valueOf(quittance.getId()), 8));
		// barcodeStr.append("-");
		// format = new SimpleDateFormat("MM/yyyy");
		// barcodeStr.append(format.format(ticketInfo.dateFrom));
		barcodeStr.append(";");
		barcodeStr.append(dateTillSum);

		barcodes[0] = barcodeStr.toString();

		form.setField("ticketNumber", barcodes[0]);
		form.setField("ticketNumber_copy1", barcodes[0]);

		StringBuilder barcode2d = new StringBuilder();
		barcode2d.append(ApplicationConfig.getEircId());
		barcode2d.append(StringUtil.fillLeadingZero(String.valueOf(quittance.getId()), 8));
		barcode2d.append(";");
		barcode2d.append(address);
		barcode2d.append(";");
		barcode2d.append(payer);
		barcode2d.append(";");
		barcode2d.append(dateTillSum);
		barcode2d.append("\n");
		barcode2d.append("2");
		barcode2d.append(";");
		/*if (ticketInfo.serviceAmountInfoMap != null) {
			ServiceAmountInfo serviceAmountInfo = null;
			for (int i = 2; i <= 15; i++) {
				serviceAmountInfo = ticketInfo.serviceAmountInfoMap.get(i);
				if (serviceAmountInfo != null) {
					barcode2d.append(i);
					barcode2d.append(";");
					barcode2d.append(serviceAmountInfo.name);
					barcode2d.append(";");
					barcode2d.append(serviceAmountInfo.dateTillAmount);
					barcode2d.append("\n");
				}
			}
		}*/
		barcodes[1] = barcode2d.toString();

		return barcodes;
	}

	private String[] getStringArray(BigDecimal amount) {
		String[] result = new String[]{"", "", "", "", "", ""};
		String amountStr = amount.toString();
		int amountStrLength = amountStr.length();
		int dotInd = amountStr.indexOf(".");
		if (dotInd == -1) {
			result[5] = "";
			result[4] = "";
			try {
				result[3] = amountStr.substring(amountStrLength - 1,
						amountStrLength);
				result[2] = amountStr.substring(amountStrLength - 2,
						amountStrLength - 1);
				result[1] = amountStr.substring(amountStrLength - 3,
						amountStrLength - 2);
				result[0] = amountStr.substring(amountStrLength - 4,
						amountStrLength - 3);
			} catch (StringIndexOutOfBoundsException e) {
				// ignore
			}
		} else {
			try {
				result[4] = amountStr.substring(dotInd + 1, dotInd + 2);
				result[5] = amountStr.substring(dotInd + 2, dotInd + 3);
			} catch (StringIndexOutOfBoundsException e) {
				// ignore
			}
			try {
				result[3] = amountStr.substring(dotInd - 1, dotInd);
				result[2] = amountStr.substring(dotInd - 2, dotInd - 1);
				result[1] = amountStr.substring(dotInd - 3, dotInd - 2);
				result[0] = amountStr.substring(dotInd - 4, dotInd - 3);

			} catch (StringIndexOutOfBoundsException e) {
				// ignore
			}
		}

		return result;
	}

	/**
	 * @param quittanceService the quittanceService to set
	 */
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

	/**
	 * @param serviceTypeService the serviceTypeService to set
	 */
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

}
