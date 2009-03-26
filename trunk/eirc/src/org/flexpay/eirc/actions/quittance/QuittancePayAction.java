package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.QuittanceDetailsPayment;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.service.QuittancePaymentService;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class QuittancePayAction extends FPActionSupport {

	// form data
	private Quittance quittance = new Quittance();
	private List<QuittancePayment> payments = Collections.emptyList();
	private String quittanceNumber;
	private String source = "number";


	// required services
	private QuittanceService quittanceService;
	private QuittanceNumberService numberService;
	private QuittancePaymentService quittancePaymentService;

	@NotNull
	protected String doExecute() throws Exception {

		if (quittance.getId() == null) {
			addActionError(getText("error.no_id"));
			return doRedirect();
		}

		quittance = quittanceService.readFull(stub(quittance));
		if (quittance == null) {
			addActionError(getText("eirc.error.quittance.no_quittance_found"));
			return doRedirect();
		}

		if (isSubmit()) {
			addActionError("eirc.quittances.quittance_pay.payed_successfully");
			return doRedirect();
		}

		quittanceNumber = numberService.getNumber(quittance);

		payments = quittancePaymentService.getQuittancePayments(stub(quittance));

		if (quittancePayed()) {
			addActionError(getText("eirc.quittance.payment.was_paid"));
		}

		return INPUT;
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

	public String getPayable(QuittanceDetails qd) {
		return qd.getOutgoingBalance().toString();
	}

	public String getTotalPayable() {
		BigDecimal total = new BigDecimal("0.00");

		for (QuittanceDetails qd : quittance.getQuittanceDetails()) {
			if (!qd.getConsumer().getService().isSubService()) {
				total = total.add(qd.getOutgoingBalance());
			}
		}

		return total.toString();
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

	@NotNull
	public BigDecimal getTotalPayed() {

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

	public void setSource(String source) {
		this.source = source;
	}

	// required services setters
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
}
