package org.flexpay.eirc.process.quittance.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.List;
import java.util.Collection;

import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.common.util.CollectionUtils;

public class QuittanceDataSource implements JRDataSource {

	private JRBeanCollectionDataSource collectionDataSource;

	public QuittanceDataSource(List<Quittance> quittances) {
		Collection<QuittanceInfo> infos = initInfos(quittances);
		collectionDataSource = new JRBeanCollectionDataSource(infos);
	}

	private Collection<QuittanceInfo> initInfos(List<Quittance> quittances) {

		List<QuittanceInfo> infos = CollectionUtils.list();
		for (Quittance q : quittances) {

		}

		return infos;
	}

	public boolean next() throws JRException {
		return collectionDataSource.next();
	}

	public Object getFieldValue(JRField jrField) throws JRException {
		return collectionDataSource.getFieldValue(jrField);
	}
}
