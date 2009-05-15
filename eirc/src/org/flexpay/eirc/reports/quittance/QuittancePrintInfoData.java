package org.flexpay.eirc.reports.quittance;

import java.util.List;

/**
 * Holder for quittance printing necessary statistics and data
 */
public class QuittancePrintInfoData {

	private QuittancesPrintStats stats;
	private List<QuittancePrintInfo> infos;

	public QuittancesPrintStats getStats() {
		return stats;
	}

	public void setStats(QuittancesPrintStats stats) {
		this.stats = stats;
	}

	public List<QuittancePrintInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<QuittancePrintInfo> infos) {
		this.infos = infos;
	}
}
