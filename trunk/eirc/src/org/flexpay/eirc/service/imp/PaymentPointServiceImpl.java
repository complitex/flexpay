package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.service.PaymentPointService;
import org.flexpay.eirc.persistence.PaymentPoint;
import org.flexpay.eirc.dao.PaymentPointDao;
import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Payment points service implementation
 */
@Transactional (readOnly = true)
public class PaymentPointServiceImpl implements PaymentPointService {

	private PaymentPointDao paymentPointDao;

	/**
	 * List available payment paints
	 *
	 * @param townStub Town stub to lookup points in
	 * @param pager	Pager
	 * @return List of available Payment points
	 */
	@NotNull
	public List<PaymentPoint> listPoints(@NotNull Stub<Town> townStub, @NotNull Page<PaymentPoint> pager) {
		return paymentPointDao.listPoints(townStub.getId(), pager);
	}

	public void setPaymentPointDao(PaymentPointDao paymentPointDao) {
		this.paymentPointDao = paymentPointDao;
	}
}
