package org.flexpay.eirc.actions;

import com.lowagie.text.DocumentException;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.pdf.PdfA3Writer;
import org.flexpay.eirc.pdf.PdfQuittanceWriter;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.flexpay.eirc.service.ServiceTypeService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.jetbrains.annotations.NonNls;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PrintTicketAction extends FPActionSupport {

	private ServiceOrganisationService serviceOrganisationService;
	private QuittanceService quittanceService;
	private ServiceTypeService serviceTypeService;

	private Integer year;
	private Integer month;
	private Long serviceOrganisationId;
	private List<ServiceOrganisation> serviceOrganizationList;

	private String resultFile;

	public String doExecute() throws Exception {
		if (isSubmit()) {
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

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	private void initDefaultDate() {
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
	}

	private String print(Long serviceOrganisationId, Date dateFrom,
						 Date dateTill) throws IOException, DocumentException,
											   FlexPayException {
		List<Object> ticketsWithDelimiters = quittanceService
				.getQuittanceListWithDelimiters(serviceOrganisationId, dateFrom, dateTill);
		if (ticketsWithDelimiters.isEmpty()) {
			return null;
		}

		int length = ticketsWithDelimiters.size();
		Object[] finalArray = new Object[length];
		int pageNumber = length / 4 + ((length % 4) != 0 ? 1 : 0);
		int a1Ind;
		int a2Ind;
		for (int i = 0; i < pageNumber; i++) {
			for (int j = 0; (j < 4) && ((a2Ind = i * 4 + j) < length); j++) {
				a1Ind = j * pageNumber + i;
				finalArray[a2Ind] = (a1Ind < length) ? ticketsWithDelimiters.get(a1Ind) : null;
			}
		}

		InputStream ticketPattern = ApplicationConfig.getResourceAsStream("/resources/eirc/pdf/ticketPattern.pdf");
		InputStream titlePattern = ApplicationConfig.getResourceAsStream("/resources/eirc/pdf/titlePattern.pdf");
		/*PdfTicketWriter ticketWriter = new PdfTicketWriter(ticketPatternFile,
				titlePattern);*/
		PdfQuittanceWriter quittanceWriter = new PdfQuittanceWriter(ticketPattern, titlePattern);
		quittanceWriter.setQuittanceService(quittanceService);
		quittanceWriter.setServiceTypeService(serviceTypeService);
		@NonNls DateFormat format = new SimpleDateFormat("MM.yyyy");
		File outputA3File = new File(ApplicationConfig.getEircDataRoot(), serviceOrganisationId + "_"
									+ format.format(dateFrom) + ".pdf");
		OutputStream os = new FileOutputStream(outputA3File);
		PdfA3Writer a3Writer = new PdfA3Writer(os);

		for (Object element : finalArray) {
			byte[] byteArray;
			if (element instanceof String) {
				byteArray = quittanceWriter.writeTitleGetByteArray((String) element);
			} else {
				Quittance quittance = (Quittance) element;
				byteArray = quittanceWriter.writeGetByteArray(quittance);
			}
			a3Writer.write(byteArray);
		}

		a3Writer.close();

		return outputA3File.getAbsolutePath();
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
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
	 * @param month the month to set
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * @param serviceOrganisationId the serviceOrganisationId to set
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
	 * @param serviceOrganisationService the serviceOrganisationService to set
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
