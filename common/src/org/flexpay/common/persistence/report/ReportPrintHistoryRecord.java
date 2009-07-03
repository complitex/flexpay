package org.flexpay.common.persistence.report;

import org.flexpay.common.persistence.DomainObject;

import java.util.Date;

public class ReportPrintHistoryRecord extends DomainObject {

	private String userName;
	private Date printDate;
	private int reportType;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}
}
