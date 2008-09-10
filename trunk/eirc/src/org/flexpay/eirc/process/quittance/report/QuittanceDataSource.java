package org.flexpay.eirc.process.quittance.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.quittance.report.util.QuittanceInfoGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class QuittanceDataSource implements JRDataSource {

	private JRBeanCollectionDataSource collectionDataSource;

	public QuittanceDataSource(List<Quittance> quittances) {
		Collection<QuittanceInfo> infos = initInfos(quittances);
		collectionDataSource = new JRBeanCollectionDataSource(infos);
	}

	private Collection<QuittanceInfo> initInfos(List<Quittance> quittances) {

		List<QuittanceInfo> infos = CollectionUtils.list();

		@NotNull Long accountId = -1L;
		int orderNumber = -1;

		for (Quittance q : quittances) {

			// check account number
			if (isNewAccount(accountId, q)) {
				accountId = q.getEircAccountId();
				orderNumber = q.getOrderNumber();
			}
			// check for quittance order number, only quittances with max order number
			// are taken into account
			else if (orderNumber != q.getOrderNumber()) {
				if (orderNumber < q.getOrderNumber()) {
					throw new IllegalStateException("Invalid order number, was quittances sorted by orderNumber??");
				}

				// OK, just skip quittances with lower numbers
				continue;
			}

			// now build quittance into quittance info
			infos.add(QuittanceInfoGenerator.buildInfo(q));
		}

		return infos;
	}

	/**
	 * Check if next quittance has different account
	 *
	 * @param oldId Old quittance id
	 * @param q	 next Quittance
	 * @return <code>true</code> if quittance has different account
	 */
	private boolean isNewAccount(Long oldId, Quittance q) {
		return !oldId.equals(q.getEircAccountId());
	}


	public boolean next() throws JRException {
		return collectionDataSource.next();
	}

	public Object getFieldValue(JRField jrField) throws JRException {
		return collectionDataSource.getFieldValue(jrField);
	}
}
