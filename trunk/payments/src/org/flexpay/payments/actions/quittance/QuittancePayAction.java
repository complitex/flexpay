package org.flexpay.payments.actions.quittance;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.BigDecimalUtil;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ContextCallback;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.flexpay.payments.actions.PaymentOperationAction;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.*;
import org.flexpay.payments.util.ServiceFullIndexUtil;
import org.flexpay.payments.process.export.TradingDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.jbpm.JbpmContext;
import org.jbpm.graph.exe.ProcessInstance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class QuittancePayAction extends PaymentOperationAction {

	private Operation operation = new Operation();

	// required services
	private OperationService operationService;

	private ProcessManager processManager;
	@NotNull
	protected String doExecute() throws Exception {

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
		log.debug("Found cashbox {}", cashbox);
		if (cashbox == null) {
			throw new IllegalArgumentException("Invalid cashbox id: " + cashboxId);
		}

		//@TODO reformat if then else shit
		final Long paymentProcessId = cashbox.getPaymentPoint().getTradingDayProcessInstanceId();
		if (paymentProcessId != null && paymentProcessId != 0 ){
			log.debug("Found process id {} for cashbox {}", new Object[]{paymentProcessId, cashboxId});
//			Boolean opened = processManager.execute(new ContextCallback<Boolean>(){
//				public Boolean doInContext(@NotNull JbpmContext context) {
//					ProcessInstance processInstance = context.getProcessInstance(paymentProcessId);
//					if (processInstance == null || processInstance.hasEnded()){
//						return false;
//					}
//					return new Boolean((String)context.getProcessInstance(paymentProcessId).getContextInstance().getVariable(TradingDay.CAN_UPDATE_OR_CRETAE_OPERATION));
//				}
//			});
			if (TradingDay.isOpened(processManager, paymentProcessId)){
				operation = createOperation(cashbox);
				if (BigDecimalUtil.isZero(operation.getOperationSumm()) || operation.getDocuments()== null || operation.getDocuments().size() == 0){
					log.debug("Zero summ for operation or zero documents for operation created. Operation was not created");
				}else{
					operationService.save(operation);
				}
				return REDIRECT_SUCCESS;
			}else{
				addActionError(getText("payments.quittance.pay.payment_not_alowed_due_closed_trading_day"));
				return REDIRECT_SUCCESS;
			}
		}else{
			if (log.isDebugEnabled()){
				log.debug("TradingDay process id not found for Payment point id = {}", cashbox.getPaymentPoint().getId());
			}
		}
		operation = createOperation(cashbox);
		if (BigDecimalUtil.isZero(operation.getOperationSumm()) || operation.getDocuments()== null || operation.getDocuments().size() == 0){
			log.debug("Zero summ for operation or zero documents for operation created. Operation was not created");
		}else{
			operationService.save(operation);
		}
		return REDIRECT_SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
