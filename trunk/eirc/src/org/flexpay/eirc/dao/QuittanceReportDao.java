package org.flexpay.eirc.dao;

import java.util.Date;
import java.util.List;

public interface QuittanceReportDao {

	List<Object[]> listPrintInfos(Long serviceOrganizationId, Date begin, Date end);
}
