package org.flexpay.eirc.service;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PaymentPointService extends org.flexpay.orgs.service.PaymentPointService {

	/**
	 * List available payment points
	 *
	 * @param townStub Town stub to lookup points in
	 * @param pager	Pager
	 * @return List of available Payment points
	 */
	@NotNull
	List<PaymentPoint> listPoints(@NotNull Stub<Town> townStub, @NotNull Page<PaymentPoint> pager);
}
