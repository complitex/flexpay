package org.flexpay.eirc.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.util.config.ApplicationConfig;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BarcodePDF417;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 * @deprecated
 */
public class PdfTicketWriter {
	private static final Locale LOCALE_RU = new Locale("ru");

	// private PdfReader kvitPattern;
	private File kvitPatternFile;
	private PdfReader titlePattern;
	private ByteArrayOutputStream baos;

	public PdfTicketWriter(File kvitPatternFile, InputStream titlePattern)
			throws IOException {
		baos = new ByteArrayOutputStream();
		this.kvitPatternFile = kvitPatternFile;
		this.titlePattern = new PdfReader(titlePattern);
	}

	public byte[] writeGetByteArray(TicketInfo ticketInfo)
			throws DocumentException, IOException {
		baos.reset();
		write(baos, ticketInfo);
		return baos.toByteArray();
	}

	public void write(OutputStream os, TicketInfo ticketInfo)
			throws DocumentException, IOException {
		PdfStamper stamper = new PdfStamper(new PdfReader(new FileInputStream(
				kvitPatternFile)), os);
		stamper.setFormFlattening(true);
		
		AcroFields form = stamper.getAcroFields();
		String[] barcodes = fillForm(form, ticketInfo);
		
		PdfContentByte cb = stamper.getUnderContent(1);
		Image image = getBarcode1d(cb, barcodes[0]);
		image.scalePercent(40, 100);
		image.setAbsolutePosition(15, PageSize.A4.getHeight() / 2 - 180);
		image.setRotationDegrees(90);
		cb.addImage(image);
		
		image = getBarcode2d(barcodes[1]); 
		image.setAbsolutePosition(PageSize.A4.getWidth() - 50, PageSize.A4.getHeight() / 2 - 220);
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
		AcroFields form = stamper.getAcroFields();
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

	private String[] fillForm(AcroFields form, TicketInfo ticketInfo)
			throws IOException, DocumentException {
		form.setField("header", "КП “ЖИЛКОМСЕРВИС”");
		form.setField("header_copy1", "КП “ЖИЛКОМСЕРВИС”");
		form.setField("lsZks", "1000300660");
		form.setField("lsZks_copy1", "1000300660");
		form
				.setField("properties",
						"р/сч 260005593 в ОАО “МЕГАБАНК” МФО 351629, КОД ОКПО 34467793");
		form.setField("footer", "Киевский филиал");

		DateFormat format = new SimpleDateFormat("MMMM yyyy", LOCALE_RU);
		form.setField("date", format.format(ticketInfo.dateFrom));
		form.setField("date_copy1", format.format(ticketInfo.dateFrom));
		format = new SimpleDateFormat("dd.MM.yyyy");
		form.setField("creationDate", format.format(ticketInfo.creationDate));
		form.setField("dateFrom", format.format(ticketInfo.dateFrom));
		form.setField("dateTill", format.format(ticketInfo.dateTill));
		
		
		
		// form.setField("paySum", kvitForm.paySum);
		form.setField("payer", ticketInfo.payer);
		form.setField("payer_copy1", ticketInfo.payer);
		form.setField("address", ticketInfo.address);
		form.setField("address_copy1", ticketInfo.address);

		// Services 2-5
		if (ticketInfo.serviceAmountInfoMap != null) {
			ServiceAmountInfo serviceAmountInfo;
			for (int i = 2; i <= 5; i++) {
				serviceAmountInfo = ticketInfo.serviceAmountInfoMap.get(i);
				if (serviceAmountInfo != null) {
					String[] amountDigitArray = getStringArray(serviceAmountInfo.dateTillAmount);
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
					form.setField("service" + i + "_amount",
							serviceAmountInfo.dateTillAmount.toString());
					form.setField("service" + i + "_dateFrom_amount",
							serviceAmountInfo.dateFromAmount.toString());
					form.setField("service" + i + "_dateTill_amount",
							serviceAmountInfo.dateTillAmount.toString());
				}
			}
		}

		// Kvartplata services
		BigDecimal dateFromSum = ticketInfo.getKvartplataDateFromSum();
		BigDecimal dateTillSum = ticketInfo.getKvartplataDateTillSum();
		String[] amountDigitArray = getStringArray(dateTillSum);
		form.setField("kvartpl_amount_digit0", amountDigitArray[0]);
		form.setField("kvartpl_amount_digit1", amountDigitArray[1]);
		form.setField("kvartpl_amount_digit2", amountDigitArray[2]);
		form.setField("kvartpl_amount_digit3", amountDigitArray[3]);
		form.setField("kvartpl_amount_digit4", amountDigitArray[4]);
		form.setField("kvartpl_amount_digit5", amountDigitArray[5]);
		form.setField("kvartpl_amount", dateTillSum.toString());
		form.setField("kvartpl_dateFrom_amount", dateFromSum.toString());
		form.setField("kvartpl_dateTill_amount", dateTillSum.toString());

		// Waterin services
		dateFromSum = ticketInfo.getWaterinDateFromSum();
		dateTillSum = ticketInfo.getWaterinDateTillSum();
		amountDigitArray = getStringArray(dateTillSum);
		form.setField("waterin_amount_digit0", amountDigitArray[0]);
		form.setField("waterin_amount_digit1", amountDigitArray[1]);
		form.setField("waterin_amount_digit2", amountDigitArray[2]);
		form.setField("waterin_amount_digit3", amountDigitArray[3]);
		form.setField("waterin_amount_digit4", amountDigitArray[4]);
		form.setField("waterin_amount_digit5", amountDigitArray[5]);
		form.setField("waterin_amount", dateTillSum.toString());
		form.setField("waterin_dateFrom_amount", dateFromSum.toString());
		form.setField("waterin_dateTill_amount", dateTillSum.toString());

		// Services sum
		dateFromSum = ticketInfo.getDateFromSum();
		dateTillSum = ticketInfo.getDateTillSum();
		amountDigitArray = getStringArray(dateTillSum);
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
		barcodeStr.append(StringUtil.fillLeadingZero(
				ticketInfo.ticketNumber.toString(), 8)); // TODO ticketNumber replace by ticketId
		//barcodeStr.append("-");
		//format = new SimpleDateFormat("MM/yyyy");
		//barcodeStr.append(format.format(ticketInfo.dateFrom));
		barcodeStr.append(";");
		barcodeStr.append(dateTillSum);
		
		barcodes[0] = barcodeStr.toString();
		
		form.setField("ticketNumber", barcodes[0]);
		form.setField("ticketNumber_copy1", barcodes[0]);
		
		StringBuilder barcode2d = new StringBuilder();
		barcode2d.append(ApplicationConfig.getEircId());
		barcode2d.append(StringUtil.fillLeadingZero(
				ticketInfo.ticketNumber.toString(), 8));
		barcode2d.append(";");
		barcode2d.append(ticketInfo.address);
		barcode2d.append(";");
		barcode2d.append(ticketInfo.payer);
		barcode2d.append(";");
		barcode2d.append(dateTillSum);
		barcode2d.append("\n");
		barcode2d.append("2");
		barcode2d.append(";");
		if (ticketInfo.serviceAmountInfoMap != null) {
			ServiceAmountInfo serviceAmountInfo;
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
		}
		barcodes[1] = barcode2d.toString();
		
		return barcodes;
	}

	private String[] getStringArray(BigDecimal amount) {
		String[] result = new String[] { "", "", "", "", "", "" };
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

	public static class TicketInfo {
		public Date creationDate;
		public Date dateFrom;
		public Date dateTill;
		public Long ticketNumber;
		public String payer;
		public String address;
		public Map<Integer, ServiceAmountInfo> serviceAmountInfoMap;

		public BigDecimal getKvartplataDateFromSum() {
			return getServicesAmount(8, 15, true);
		}

		public BigDecimal getKvartplataDateTillSum() {
			return getServicesAmount(8, 15, false);
		}

		public BigDecimal getWaterinDateFromSum() {
			return getServicesAmount(6, 7, true);
		}

		public BigDecimal getWaterinDateTillSum() {
			return getServicesAmount(6, 7, false);
		}

		public BigDecimal getDateFromSum() {
			return getServicesAmount(2, 15, true);
		}

		public BigDecimal getDateTillSum() {
			return getServicesAmount(2, 15, false);
		}
		
		private BigDecimal getServicesAmount(int ind1, int ind2, boolean flag) {
			BigDecimal sum = BigDecimal.ZERO;
			ServiceAmountInfo serviceAmountInfo;
			if (serviceAmountInfoMap != null) {
				for (int i = ind1; i <= ind2; i++) {
					serviceAmountInfo = serviceAmountInfoMap.get(i);
					if (serviceAmountInfo != null) {
						if (flag) {
							if (serviceAmountInfo.dateFromAmount != null) {
								sum = sum.add(serviceAmountInfo.dateFromAmount);
							}
						} else {
							if (serviceAmountInfo.dateTillAmount != null) {
								sum = sum.add(serviceAmountInfo.dateTillAmount);
							}
						}
					}
				}
			}

			return sum;
		}

	}

	public static class ServiceAmountInfo {
		public String name;
		public Integer code;
		public BigDecimal dateFromAmount;
		public BigDecimal dateTillAmount;
		
		
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the code
		 */
		public Integer getCode() {
			return code;
		}
		/**
		 * @param code the code to set
		 */
		public void setCode(Integer code) {
			this.code = code;
		}
		/**
		 * @return the dateFromAmount
		 */
		public BigDecimal getDateFromAmount() {
			return dateFromAmount;
		}
		/**
		 * @param dateFromAmount the dateFromAmount to set
		 */
		public void setDateFromAmount(BigDecimal dateFromAmount) {
			this.dateFromAmount = dateFromAmount;
		}
		/**
		 * @return the dateTillAmount
		 */
		public BigDecimal getDateTillAmount() {
			return dateTillAmount;
		}
		/**
		 * @param dateTillAmount the dateTillAmount to set
		 */
		public void setDateTillAmount(BigDecimal dateTillAmount) {
			this.dateTillAmount = dateTillAmount;
		}
	}

}
