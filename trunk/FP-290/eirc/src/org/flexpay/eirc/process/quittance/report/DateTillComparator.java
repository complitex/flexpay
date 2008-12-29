package org.flexpay.eirc.process.quittance.report;

import org.flexpay.eirc.persistence.account.QuittanceDetails;

import java.util.Comparator;
import java.io.Serializable;

public class DateTillComparator implements Comparator<QuittanceDetails>, Serializable {

	public int compare(QuittanceDetails o1, QuittanceDetails o2) {
		return o1.getMonth().compareTo(o2.getMonth());
	}
}
