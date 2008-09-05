package org.flexpay.eirc.process.quittance;

import com.lowagie.text.DocumentException;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.eirc.pdf.PdfA3Writer;
import org.flexpay.eirc.pdf.PdfQuittanceWriter;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.ServiceTypeService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.jetbrains.annotations.NonNls;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class GenerateQuittancePDFJob extends Job {

	private QuittanceService quittanceService;
	private ServiceTypeService serviceTypeService;

	public final static String RESULT_FILE_NAME = "RESULT_FILE_NAME";

	public String execute(Map<Serializable, Serializable> contextVariables) throws FlexPayException {

		Long serviceOrganisationId = (Long) contextVariables.get("serviceOrganisationId");
		Date dateFrom = (Date) contextVariables.get("dateFrom");
		Date dateTill = (Date) contextVariables.get("dateTill");

		try {
			long time = System.currentTimeMillis();
			log.info("Starting PDF quittances generation");

			String fileName = print(new Stub<ServiceOrganisation>(serviceOrganisationId), dateFrom, dateTill);
			contextVariables.put(RESULT_FILE_NAME, fileName);

			log.info("Ended PDF quittances generation, time spent: " + (System.currentTimeMillis() - time) + "ms.");
		} catch (IOException e) {
			contextVariables.put(Job.STATUS_ERROR, "File I/O error : " + e.getMessage());
			log.error("File I/O error", e);
			return Job.RESULT_ERROR;
		} catch (DocumentException e) {
			contextVariables.put(Job.STATUS_ERROR, "Document generation error : " + e.getMessage());
			log.error("Document generation error", e);
			return Job.RESULT_ERROR;
		} catch (FlexPayException e) {
			contextVariables.put(Job.STATUS_ERROR, "Quittance list error: " + e.getMessage());
			log.error("Quittance list error", e);
			return Job.RESULT_ERROR;
		}

		return Job.RESULT_NEXT;
	}

	private String print(Stub<ServiceOrganisation> stub, Date dateFrom, Date dateTill)
			throws IOException, DocumentException, FlexPayException {

		Logger plog = ProcessLogger.getLogger(getClass());

		plog.info("Getting tickets with delimeters");
		List<Object> ticketsWithDelimiters = quittanceService
				.getQuittanceListWithDelimiters(stub, dateFrom, dateTill);
		if (ticketsWithDelimiters.isEmpty()) {
			plog.info("No tickets found");
			return null;
		}

		Object[] finalArray = splitQuittancesByBulks(plog, ticketsWithDelimiters);

		InputStream ticketPattern = ApplicationConfig.getResourceAsStream("/resources/eirc/pdf/ticketPattern.pdf");
		InputStream titlePattern = ApplicationConfig.getResourceAsStream("/resources/eirc/pdf/titlePattern.pdf");

		PdfQuittanceWriter quittanceWriter = new PdfQuittanceWriter(ticketPattern, titlePattern);
		quittanceWriter.setQuittanceService(quittanceService);
		quittanceWriter.setServiceTypeService(serviceTypeService);

		@NonNls DateFormat format = new SimpleDateFormat("MM.yyyy");
		File outputA3File = new File(ApplicationConfig.getEircDataRoot(),
				stub.getId() + "_" + format.format(dateFrom) + ".pdf");

		OutputStream os = null;
		PdfA3Writer a3Writer = null;
		long count = 0;
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			os = new FileOutputStream(outputA3File);
			a3Writer = new PdfA3Writer(os);

			for (Object element : finalArray) {
				byte[] byteArray;
				if (element instanceof String) {
					plog.info("Printing address page: " + element);
					byteArray = quittanceWriter.getAddressTitleBytes((String) element);
				}
				else if (element != null) {
					++count;
					if (plog.isInfoEnabled()) {
						if (plog.isDebugEnabled()) {
							plog.debug("Printing quittance #: " + count);
						} else if (count % 10 == 0) {
							plog.info("Printing quittance #: " + count);
						}
					}

					Quittance quittance = (Quittance) element;
					byteArray = quittanceWriter.getQuittanceBytes(quittance);
				} else {
					plog.warn("Empty element");
					continue;
				}
				a3Writer.write(byteArray);

				// TODO remove me
				if (count >= 4000) {
					break;
				}
			}
		} finally {
			if (a3Writer != null) {
				a3Writer.close();
			}
			IOUtils.closeQuietly(os);
		}

		if (plog.isInfoEnabled()) {
			plog.info("Printing finished. Total quittances printed: " + count);
		}

		return outputA3File.getAbsolutePath();
	}

	private Object[] splitQuittancesByBulks(Logger plog, List<Object> ticketsWithDelimiters) {
		// split objects by bulks of 4 quittancs per page
		plog.info("Ordering quittances by bulk");
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
		return finalArray;
	}

	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}
}
