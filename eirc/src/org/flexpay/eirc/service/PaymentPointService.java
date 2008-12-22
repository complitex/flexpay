package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.PaymentPoint;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.ab.persistence.Town;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PaymentPointService {

	/**
	 * List available payment paints
	 *
	 * @param townStub Town stub to lookup points in
	 * @param pager Pager
	 * @return List of available Payment points
	 */
	@NotNull
	List<PaymentPoint> listPoints(@NotNull Stub<Town> townStub, @NotNull Page<PaymentPoint> pager);
}
