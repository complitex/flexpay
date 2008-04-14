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

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

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
		fillForm(form, ticketInfo);
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

	private void fillForm(AcroFields form, TicketInfo ticketInfo)
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
		String ticketNumberStr = StringUtil.fillLeadingZero(
				ticketInfo.ticketNumber.toString(), 10);
		form.setField("ticketNumber", ticketNumberStr);
		form.setField("ticketNumber_copy1", ticketNumberStr);
		// form.setField("paySum", kvitForm.paySum);
		form.setField("payer", ticketInfo.payer);
		form.setField("payer_copy1", ticketInfo.payer);
		form.setField("address", ticketInfo.address);
		form.setField("address_copy1", ticketInfo.address);

		BigDecimal dateFromSum = new BigDecimal(0);
		BigDecimal dateTillSum = new BigDecimal(0);

		// Services 2-5
		ServiceAmountInfo serviceAmountInfo = null;
		for (int i = 2; i <= 5; i++) {
			serviceAmountInfo = ticketInfo.serviceAmountInfoMap.get(i);
			if (serviceAmountInfo != null) {
				if (serviceAmountInfo.dateFromAmount.compareTo(BigDecimal.ZERO) == -1) {
					dateFromSum = dateFromSum
							.add(serviceAmountInfo.dateFromAmount);
				}
				if (serviceAmountInfo.dateTillAmount.compareTo(BigDecimal.ZERO) == -1) {
					dateTillSum = dateTillSum
							.add(serviceAmountInfo.dateTillAmount);
				}
				String[] amountDigitArray = getStringArray(serviceAmountInfo.dateTillAmount.abs());
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
						serviceAmountInfo.dateTillAmount.abs().toString());
				form.setField("service" + i + "_dateFrom_amount",
						serviceAmountInfo.dateFromAmount.abs().toString());
				form.setField("service" + i + "_dateTill_amount",
						serviceAmountInfo.dateTillAmount.abs().toString());
			}
		}

		// Services 8-15
		BigDecimal dateFromAmount = new BigDecimal(0);
		BigDecimal dateTillAmount = new BigDecimal(0);
		for (int i = 8; i <= 15; i++) {
			serviceAmountInfo = ticketInfo.serviceAmountInfoMap.get(i);
			if (serviceAmountInfo != null) {
				if (serviceAmountInfo.dateFromAmount.compareTo(BigDecimal.ZERO) == -1) {
					dateFromAmount = dateFromAmount
							.add(serviceAmountInfo.dateFromAmount);
				}
				if (serviceAmountInfo.dateTillAmount.compareTo(BigDecimal.ZERO) == -1) {
					dateTillAmount = dateTillAmount
							.add(serviceAmountInfo.dateTillAmount);
				}
			}
		}
		if (dateFromAmount.compareTo(BigDecimal.ZERO) == -1) {
			dateFromSum = dateFromSum
					.add(dateFromAmount);
		}
		if (dateTillAmount.compareTo(BigDecimal.ZERO) == -1) {
			dateTillSum = dateTillSum
					.add(dateTillAmount);
		}
		String[] amountDigitArray = getStringArray(dateTillAmount.abs());
		form.setField("kvartpl_amount_digit0", amountDigitArray[0]);
		form.setField("kvartpl_amount_digit1", amountDigitArray[1]);
		form.setField("kvartpl_amount_digit2", amountDigitArray[2]);
		form.setField("kvartpl_amount_digit3", amountDigitArray[3]);
		form.setField("kvartpl_amount_digit4", amountDigitArray[4]);
		form.setField("kvartpl_amount_digit5", amountDigitArray[5]);
		form.setField("kvartpl_amount", dateTillAmount.abs().toString());
		form.setField("kvartpl_dateFrom_amount", dateFromAmount.abs().toString());
		form.setField("kvartpl_dateTill_amount", dateTillAmount.abs().toString());

		//services 6-7
		dateFromAmount = new BigDecimal(0);
		dateTillAmount = new BigDecimal(0);
		for (int i = 6; i <= 7; i++) {
			serviceAmountInfo = ticketInfo.serviceAmountInfoMap.get(i);
			if (serviceAmountInfo != null) {
				if (serviceAmountInfo.dateFromAmount.compareTo(BigDecimal.ZERO) == -1) {
					dateFromAmount = dateFromAmount
							.add(serviceAmountInfo.dateFromAmount);
				}
				if (serviceAmountInfo.dateTillAmount.compareTo(BigDecimal.ZERO) == -1) {
					dateTillAmount = dateTillAmount
							.add(serviceAmountInfo.dateTillAmount);
				}
			}
		}
		if (dateFromAmount.compareTo(BigDecimal.ZERO) == -1) {
			dateFromSum = dateFromSum
					.add(dateFromAmount);
		}
		if (dateTillAmount.compareTo(BigDecimal.ZERO) == -1) {
			dateTillSum = dateTillSum
					.add(dateTillAmount);
		}
		amountDigitArray = getStringArray(dateTillAmount.abs());
		form.setField("waterin_amount_digit0", amountDigitArray[0]);
		form.setField("waterin_amount_digit1", amountDigitArray[1]);
		form.setField("waterin_amount_digit2", amountDigitArray[2]);
		form.setField("waterin_amount_digit3", amountDigitArray[3]);
		form.setField("waterin_amount_digit4", amountDigitArray[4]);
		form.setField("waterin_amount_digit5", amountDigitArray[5]);
		form.setField("waterin_amount", dateTillAmount.abs().toString());
		form.setField("waterin_dateFrom_amount", dateFromAmount.abs().toString());
		form.setField("waterin_dateTill_amount", dateTillAmount.abs().toString());
		
		//Services sum
		amountDigitArray = getStringArray(dateTillSum.abs());
		form.setField("sum_amount_digit0", amountDigitArray[0]);
		form.setField("sum_amount_digit1", amountDigitArray[1]);
		form.setField("sum_amount_digit2", amountDigitArray[2]);
		form.setField("sum_amount_digit3", amountDigitArray[3]);
		form.setField("sum_amount_digit4", amountDigitArray[4]);
		form.setField("sum_amount_digit5", amountDigitArray[5]);
		form.setField("sum_amount", dateTillSum.abs().toString());
		form.setField("paySum", dateTillSum.abs().toString());
		form.setField("sum_dateFrom_amount", dateFromSum.abs().toString());
		form.setField("sum_dateTill_amount", dateTillSum.abs().toString());

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

	}

	public static class ServiceAmountInfo {
		public String name;
		public Integer code;
		public BigDecimal dateFromAmount;
		public BigDecimal dateTillAmount;
	}

	

}
