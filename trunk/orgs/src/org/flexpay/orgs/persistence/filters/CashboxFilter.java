package org.flexpay.orgs.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Cashbox;

import java.util.List;

public class CashboxFilter extends PrimaryKeyFilter<Cashbox> {

	private List<Cashbox> cashboxes = CollectionUtils.list();

	public CashboxFilter() {
		super(-1L);
	}

	public List<Cashbox> getCashboxes() {
		return cashboxes;
	}

	public void setCashboxes(List<Cashbox> cashboxes) {
		this.cashboxes = cashboxes;
		if (!containsSuchId(cashboxes, getSelectedId())) {
			setSelectedId(getDefaultId());
		}
	}

	private boolean containsSuchId(List<Cashbox> points, Long id) {

		for (Cashbox cashbox : cashboxes) {
			if (cashbox.getId().equals(id)) {
				return true;
			}
		}

		return false;
	}

	public Cashbox getSelected() {
		for (Cashbox cashbox : cashboxes) {
			if (cashbox.getId().equals(getSelectedId())) {
				return cashbox;
			}
		}

		return null;
	}
}
