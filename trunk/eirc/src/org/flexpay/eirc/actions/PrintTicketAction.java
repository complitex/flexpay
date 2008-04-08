package org.flexpay.eirc.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.pdf.PdfA3Writer;
import org.flexpay.eirc.pdf.PdfTicketWriter;
import org.flexpay.eirc.pdf.PdfTicketWriter.TicketForm;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.Ticket;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.flexpay.eirc.service.TicketService;
import org.flexpay.eirc.util.config.ApplicationConfig;

import com.lowagie.text.DocumentException;

public class PrintTicketAction extends CommonAction {

	private TicketService tickerService;
	private ServiceOrganisationService serviceOrganisationService;

	private Integer year;
	private Integer month;
	private Long serviceOrganisationId;
	private List<ServiceOrganisation> serviceOrganizationList;
	
	private String resultFile;

	public String execute() throws IOException, DocumentException, FlexPayException {
		if (isSubmitted()) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date dateFrom = cal.getTime();
			cal.add(Calendar.MONTH, 1);
			Date dateTill = cal.getTime();

			resultFile = print(serviceOrganisationId, dateFrom, dateTill);

		}
		initDefaultDate();
		serviceOrganizationList = serviceOrganisationService
				.listServiceOrganisation();

		return "success";
	}

	private void initDefaultDate() {
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
	}

	private String print(Long serviceOrganisationId, Date dateFrom, Date dateTill)
			throws IOException, DocumentException, FlexPayException {
		List<Object> ticketsWithDelimiters = tickerService
				.getTicketsWithDelimiters(serviceOrganisationId, dateFrom,
						dateTill);
		if(ticketsWithDelimiters.isEmpty()) {
			return null;
		}

		int length = ticketsWithDelimiters.size();
		Object[] finalArray = new Object[length];
		int pageNumber = length / 6 + ((length % 6) != 0 ? 1 : 0);
		int a1Ind;
		int a2Ind;
		for (int i = 0; i < pageNumber; i++) {
			for (int j = 0; (j < 6) && ((a2Ind = i * 6 + j) < length); j++) {
				a1Ind = j * pageNumber + i;
				finalArray[a2Ind] = (a1Ind < length) ? ticketsWithDelimiters
						.get(a1Ind) : null;
			}
		}

		File ticketPatternFile = new File(ApplicationConfig.getInstance()
				.getWebAppRoot(), "/resources/eirc/pdf/ticketPattern.pdf");
		File titlePatternFile = new File(ApplicationConfig.getInstance()
				.getWebAppRoot(), "/resources/eirc/pdf/titlePattern.pdf");
		InputStream titlePattern = new FileInputStream(titlePatternFile);
		PdfTicketWriter ticketWriter = new PdfTicketWriter(ticketPatternFile,
				titlePattern);
		DateFormat format = new SimpleDateFormat("MM.yyyy");
		File outputA3File = new File(ApplicationConfig.getInstance()
				.getEircDataRoot(), serviceOrganisationId + "_" + format.format(dateFrom) + ".pdf");
		OutputStream os = new FileOutputStream(outputA3File);
		PdfA3Writer a3Writer = new PdfA3Writer(os);

		for (Object element : finalArray) {
			try {
				byte[] byteArray = null;
				if (element instanceof String) {
					byteArray = ticketWriter.writeTitleGetByteArray((String) element);
				} else {
					Ticket ticket = (Ticket) element;
					TicketForm ticketForm = tickerService.getTicketForm(ticket
							.getId());
					if(ticketForm == null) {
						continue;
					}
					byteArray = ticketWriter.writeGetByteArray(ticketForm);
				}
				a3Writer.write(byteArray);
			} catch (FlexPayException e) {
				// ignore. just print next ticket.
				int i = 1;
			} catch (IOException e) {
				// ignore. just print next ticket.
				int i = 1;
			} catch (DocumentException e) {
				// ignore. just print next ticket.
				int i = 1;
			}
		}

		a3Writer.close();

		return outputA3File.getAbsolutePath();
	}

	/**
	 * @param tickerService
	 *            the tickerService to set
	 */
	public void setTickerService(TicketService tickerService) {
		this.tickerService = tickerService;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the month
	 */
	public Integer getMonth() {
		return month;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * @param serviceOrganisationId
	 *            the serviceOrganisationId to set
	 */
	public void setServiceOrganisationId(Long serviceOrganisationId) {
		this.serviceOrganisationId = serviceOrganisationId;
	}

	/**
	 * @return the serviceOrganizationList
	 */
	public List<ServiceOrganisation> getServiceOrganizationList() {
		return serviceOrganizationList;
	}

	/**
	 * @param serviceOrganisationService
	 *            the serviceOrganisationService to set
	 */
	public void setServiceOrganisationService(
			ServiceOrganisationService serviceOrganisationService) {
		this.serviceOrganisationService = serviceOrganisationService;
	}

	/**
	 * @return the resultFile
	 */
	public String getResultFile() {
		return resultFile;
	}

}
