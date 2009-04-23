package org.flexpay.payments.actions.reports;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.payments.reports.payments.PaymentReportData;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PaymentsReportAction extends FPActionSupport {

	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtil.now());
	private EndDateFilter endDateFilter = new EndDateFilter(DateUtil.now());

	private FPFile file;

	private FPFileService fpFileService;
	private PaymentsReporter paymentsReporter;

	@NotNull
	protected String doExecute() throws Exception {

		if (!isSubmit()) {
			return INPUT;
		}

		List<PaymentReportData> datum = paymentsReporter.getPaymentsData(
				beginDateFilter.getDate(), endDateFilter.getDate());

		file = new FPFile();
		FPModule module = fpFileService.getModuleByName("payments");
		if (module == null) {
			throw new Exception("Unknown module payments");
		}
		file.setModule(module);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		file.setOriginalName("payments_" + df.format(beginDateFilter.getDate()) +
							 "_" + df.format(endDateFilter.getDate()) + ".csv");
		file.setUserName(SecurityUtil.getUserName());
		FPFileUtil.createEmptyFile(file);

		OutputStream os = file.getOutputStream();
		Writer w = null;
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			w = new OutputStreamWriter(os, "UTF-8");

			if (datum.isEmpty()) {
				df = new SimpleDateFormat("yyyy-MM-dd");
				String[] params = {df.format(beginDateFilter.getDate()), df.format(beginDateFilter.getDate())};
				w.write(getText("payments.report.no_data_for_period", params));
			} else {
				CSVWriter writer = new CSVWriter(w);
				for (PaymentReportData data : datum) {
					String[] row = {
							String.valueOf(data.getBankId()),
							String.valueOf(data.getServiceId()),
							String.valueOf(data.getTotalPayed())
					};
					writer.writeNext(row);
				}
				writer.close();
			}
		} finally {
			IOUtils.closeQuietly(w);
			IOUtils.closeQuietly(os);
		}

		return SUCCESS;
	}

	@NotNull
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

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

	@Required
	public void setPaymentsReporter(PaymentsReporter paymentsReporter) {
		this.paymentsReporter = paymentsReporter;
	}
}
