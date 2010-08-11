package org.flexpay.payments.actions.reports;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.reports.payments.PaymentReportData;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.DateUtil.now;

public class ImportPaymentsReportAction extends AccountantAWPActionSupport {

	private BeginDateFilter beginDateFilter = new BeginDateFilter(now());
	private EndDateFilter endDateFilter = new EndDateFilter(now());
	private BeginTimeFilter beginTimeFilter = new BeginTimeFilter();
	private EndTimeFilter endTimeFilter = new EndTimeFilter();

	private List<PaymentReportData> reportContent = list();

	private FPFile file;

	private FPFileService fpFileService;
	private PaymentsReporter paymentsReporter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isNotSubmit()) {
			return INPUT;
		}

		Date begin = beginTimeFilter.setTime(beginDateFilter.getDate());
		Date end = endTimeFilter.setTime(endDateFilter.getDate());
		if (begin.after(end)) {
			addActionError(getText("error.from_after_till_tm"));
			return INPUT;
		}

		log.debug("Time interval: {} - {}", begin, end);

		reportContent = paymentsReporter.getPaymentsData(begin, end);

		List<PaymentReportData> datum = reportContent;
		file = new FPFile();
		FPModule module = fpFileService.getModuleByName("payments");
		if (module == null) {
			throw new Exception("Unknown module payments");
		}
		file.setModule(module);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		file.setOriginalName("payments_" + df.format(begin) + "_" + df.format(end) + ".csv");
		file.setUserName(SecurityUtil.getUserName());
		FPFileUtil.createEmptyFile(file);

		OutputStream os = file.getOutputStream();
		Writer w = null;
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			w = new OutputStreamWriter(os, "CP866");

			if (!datum.isEmpty()) {
				CSVWriter writer = new CSVWriter(w);
				int documentCount = 1;

				for (PaymentReportData data : datum) {
					String[] row = {
							String.valueOf(documentCount++),
							String.valueOf(data.getPaymentPointId()),
							String.valueOf(data.getOperationCount()),
							String.valueOf(data.getOperationId()),
							data.getServiceProviderAccount(),
							data.getFio(),
							String.valueOf(data.getServiceTypeCode()),
							String.valueOf(data.getDocumentSum()),
							String.valueOf(data.getDocumentId())							
					};
					writer.writeNext(row);
				}
				writer.close();
			}
			file.updateSize();
		} finally {
			IOUtils.closeQuietly(w);
			IOUtils.closeQuietly(os);
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public EndDateFilter getEndDateFilter() {
		return endDateFilter;
	}

	public void setEndDateFilter(EndDateFilter endDateFilter) {
		this.endDateFilter = endDateFilter;
	}

	public FPFile getFile() {
		return file;
	}

	public InputStream getInputStream() throws IOException {
		return file.getInputStream();
	}

	public BeginTimeFilter getBeginTimeFilter() {
		return beginTimeFilter;
	}

	public void setBeginTimeFilter(BeginTimeFilter beginTimeFilter) {
		this.beginTimeFilter = beginTimeFilter;
	}

	public EndTimeFilter getEndTimeFilter() {
		return endTimeFilter;
	}

	public void setEndTimeFilter(EndTimeFilter endTimeFilter) {
		this.endTimeFilter = endTimeFilter;
	}

	public List<PaymentReportData> getReportContent() {
		return reportContent;
	}

	public boolean reportContentIsNotEmpty() {
		return !reportContent.isEmpty();
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

	@Required
	public void setPaymentsReporter(PaymentsReporter paymentsReporter) {
		this.paymentsReporter = paymentsReporter;
	}

}
