package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.QuittanceDetailsPayment;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.payments.persistence.Service;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.service.QuittancePaymentService;
import org.flexpay.eirc.service.QuittancePaymentStatusService;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QuittancePayAction extends FPActionSupport {

	// form data
	private Quittance quittance = new Quittance();
	private List<QuittancePayment> payments = Collections.emptyList();
	private String quittanceNumber;
	private String source = "number";


	private BigDecimal totalPayed;
	private Map<Long, BigDecimal> servicePayments = CollectionUtils.map();

	// required services
	private QuittanceService quittanceService;
	private QuittanceNumberService numberService;
	private QuittancePaymentService quittancePaymentService;
	private QuittancePaymentStatusService paymentStatusService;

	@NotNull
	protected String doExecute() throws Exception {

		if (quittance.getId() == null && StringUtils.isBlank(quittanceNumber)) {
			addActionError(getText("common.error.invalid_id"));
			return doRedirect();
		}

		if (quittance.isNew()) {
			quittance = quittanceService.findByNumber(quittanceNumber);
			if (quittance == null) {
				addActionError(getText("eirc.error.quittance.no_quittance_found"));
				return doRedirect();
			}
		}

		quittance = quittanceService.readFull(stub(quittance));
		if (quittance == null) {
			addActionError(getText("eirc.error.quittance.no_quittance_found"));
			return doRedirect();
		}

		quittanceNumber = numberService.getNumber(quittance);
		payments = quittancePaymentService.getQuittancePayments(stub(quittance));

		log.info("Total payed: {}", totalPayed);
		log.info("Service payments {}", servicePayments);

		if (isSubmit()) {
			doPay();
			addActionError(getText("eirc.quittances.quittance_pay.payed_successfully"));
			return doRedirect();
		}

		// prepare summs to pay
		for (QuittanceDetails details : quittance.getQuittanceDetails()) {
			Service service = details.getConsumer().getService();
			servicePayments.put(service.getId(), details.getOutgoingBalance());
		}

		initTotalPayed();

		if (quittancePayed()) {
			addActionError(getText("eirc.quittance.payment.was_paid"));
		}

		return INPUT;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void doPay() throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		QuittancePayment payment = new QuittancePayment();
		payment.setQuittance(quittance);
		payment.setAmount(totalPayed);
		BigDecimal summ = new BigDecimal("0.00");
		boolean hasPartialPayments = false;
		for (Map.Entry<Long, BigDecimal> entry : servicePayments.entrySet()) {
			Long serviceId = entry.getKey();
			BigDecimal payed = entry.getValue();

			QuittanceDetailsPayment detailsPayment = new QuittanceDetailsPayment();
			detailsPayment.setAmount(payed);

			QuittanceDetails details = quittance.getServiceDetails(new Stub<Service>(serviceId));
			if (details == null) {
				ex.addException(new FlexPayException("No details ",
						"eirc.error.quittances.pay.no_details_found", serviceId));
				continue;
			}
			detailsPayment.setQuittanceDetails(details);

			// check if payed summ is equals or more then needed to pay
			BigDecimal alreadyPayed = getPayedSumm(details);
			if (alreadyPayed.add(payed).compareTo(details.getOutgoingBalance()) >= 0) {
				detailsPayment.setPaymentStatus(paymentStatusService.getPayedFullStatus());
			} else {
				detailsPayment.setPaymentStatus(paymentStatusService.getPayedPartiallyStatus());
				hasPartialPayments = true;
			}

			payment.addDetailsPayment(detailsPayment);
			summ = summ.add(payed);
		}

		if (!summ.equals(totalPayed)) {
			ex.addException(new FlexPayException("invalid division",
					"eirc.error.quittances.pay.invalid_summ_division", summ, totalPayed));
		}

		payment.setPaymentStatus(hasPartialPayments ?
								 paymentStatusService.getPayedPartiallyStatus() :
								 paymentStatusService.getPayedFullStatus());

		if (ex.isNotEmpty()) {
			throw ex;
		}

		quittancePaymentService.cashPayment(payment);
	}

	private String doRedirect() {
		if ("number".equals(source)) {
			return "redirectNumber";
		}
		if ("address".equals(source)) {
			return "redirectAddress";
		}
		if ("account".equals(source)) {
			return "redirectAccount";
		}
		return "redirectNumber";
	}

	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

	// rendering utility methods
	public String getAddress() {
		return quittance.getEircAccount().getConsumerInfo().getAddress();
	}

	public String getFIO() {
		return quittance.getEircAccount().getConsumerInfo().getFIO();
	}

	public String getServiceName(QuittanceDetails qd) {

		return getTranslation(qd.getConsumer().getService().getServiceType().getTypeNames()).getName();
	}

	public String getServiceProviderName(QuittanceDetails qd) {
		return qd.getConsumer().getService().getServiceProvider().getName();
	}

	public BigDecimal getPayable(QuittanceDetails qd) {
		return servicePayments.get(qd.getConsumer().getService().getId());
	}

	private void initTotalPayed() {
		totalPayed = getTotalPayable();
	}

	public BigDecimal getTotalPayable() {

		BigDecimal total = new BigDecimal("0.00");

		for (QuittanceDetails qd : quittance.getQuittanceDetails()) {
			if (!qd.getConsumer().getService().isSubService()) {
				total = total.add(qd.getOutgoingBalance());
			}
		}

		return total;
	}

	public boolean quittancePayed() {

		for (QuittanceDetails details : quittance.getQuittanceDetails()) {
			if (!details.getConsumer().getService().isSubService()) {
				BigDecimal summToPay = details.getOutgoingBalance();
				if (summToPay.compareTo(getPayedSumm(details)) > 0) {
					return false;
				}
			}
		}

		return true;
	}

	public BigDecimal getTotalPayedBefore() {

		BigDecimal summ = new BigDecimal("0.00");
		for (QuittanceDetails details : quittance.getQuittanceDetails()) {
			if (!details.getConsumer().getService().isSubService()) {
				BigDecimal summPayed = getPayedSumm(details);
				summ = summ.add(summPayed);
			}
		}

		return summ;
	}

	@NotNull
	public BigDecimal getPayedSumm(QuittanceDetails qd) {
		BigDecimal summ = new BigDecimal("0.00");
		for (QuittancePayment payment : payments) {
			QuittanceDetailsPayment detailsPayment = payment.getPayment(qd);
			if (detailsPayment != null) {
				summ = summ.add(detailsPayment.getAmount());
			}
		}

		return summ;
	}

	// set/get form data
	public void setQuittance(Quittance quittance) {
		this.quittance = quittance;
	}

	public Quittance getQuittance() {
		return quittance;
	}

	public String getQuittanceNumber() {
		return quittanceNumber;
	}

	public void setQuittanceNumber(String quittanceNumber) {
		this.quittanceNumber = quittanceNumber;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Map<Long, BigDecimal> getServicePayments() {
		return servicePayments;
	}

	public void setServicePayments(Map<Long, BigDecimal> servicePayments) {
		this.servicePayments = servicePayments;
	}

	public BigDecimal getTotalPayed() {
		return totalPayed;
	}

	public void setTotalPayed(BigDecimal totalPayed) {
		this.totalPayed = totalPayed;
	}

	@Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

	@Required
	public void setQuittancePaymentService(QuittancePaymentService quittancePaymentService) {
		this.quittancePaymentService = quittancePaymentService;
	}

	@Required
	public void setNumberService(QuittanceNumberService numberService) {
		this.numberService = numberService;
	}

	@Required
	public void setPaymentStatusService(QuittancePaymentStatusService paymentStatusService) {
		this.paymentStatusService = paymentStatusService;
	}
}
