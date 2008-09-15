package org.flexpay.eirc.process.quittance.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.process.quittance.report.util.QuittanceInfoGenerator;
import org.flexpay.eirc.service.SPService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JRQuittanceDataSource implements JRDataSource {

	private SPService spService;
	private JRBeanCollectionDataSource jrBeanCollectionDataSource;

	public void setQuittances(List<Quittance> quittances) {

		List<QuittanceInfo> infos = CollectionUtils.list();

		@NotNull Long accountId = -1L;
		int orderNumber = -1;

		for (Quittance q : quittances) {

			initLazyProperties(q);

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

		jrBeanCollectionDataSource = new JRBeanCollectionDataSource(infos);
	}

	private void initLazyProperties(Quittance q) {
		for (QuittanceDetails qd : q.getQuittanceDetails()) {
			Stub<Service> serviceStub = stub(qd.getConsumer().getService());
			qd.getConsumer().setService(spService.read(serviceStub));
		}
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
		// delegate all work to native implementation
		return jrBeanCollectionDataSource != null && jrBeanCollectionDataSource.next();
	}

	public Object getFieldValue(JRField jrField) throws JRException {
		// delegate all work to native implementation
		return jrBeanCollectionDataSource.getFieldValue(jrField);
	}

	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}
