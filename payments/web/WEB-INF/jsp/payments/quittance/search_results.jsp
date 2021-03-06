<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:if test="quittanceInfos != null && !quittanceInfos.isEmpty()">

<form id="quittancePayForm" action="<s:url action="paymentOperationReportAction" />">

    <s:hidden name="actionName" />
    <s:hidden name="apartmentId" />
    <s:hidden name="format" value="pdf" />
    <s:hidden name="submitted" value="true" />
    <s:hidden name="operationId" value="" />

    <table class="search_results" cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td class="th"><s:text name="payments.quittances.quittance_pay.erc_consumer_account" /></td>
            <td class="th"><s:text name="payments.quittances.quittance_pay.consumer_account" /></td>
            <td class="th"><s:text name="payments.quittances.quittance_pay.fio" /></td>
            <td class="th"><s:text name="payments.quittances.quittance_pay.service" /></td>
            <td class="th"><s:text name="payments.quittances.quittance_pay.service_supplier" /></td>
            <td class="th"><s:text name="payments.quittances.quittance_pay.debt" /></td>
            <td class="th" style="width:80px;"><s:text name="payments.quittances.quittance_pay.pay" /></td>
            <td class="th">&nbsp;</td>
        </tr>
        <s:iterator value="quittanceInfos" id="qi" status="nQI">
            <s:iterator value="serviceDetailses" id="sd" status="status">
                <s:set name="serviceId" value="%{getServiceId(#sd.serviceMasterIndex)}"/>
                <s:set name="serviceIndx" value="%{getServiceFullIndex(#nQI.index, #serviceId)}"/>

                <s:hidden name="payerFios['%{#serviceIndx}']" value="%{#sd.personFio}"/>
                <s:hidden name="addresses['%{#serviceIndx}']" value="%{getApartmentAddress(#qi)}"/>
                <s:hidden name="eircAccounts['%{#serviceIndx}']" value="%{getEircAccount(#qi)}"/>
                <s:hidden name="serviceProviderAccounts['%{#serviceIndx}']" value="%{#sd.serviceProviderAccount}"/>
                <s:hidden name="debts['%{#serviceIndx}']" value="%{#sd.outgoingBalance}"/>
                <s:hidden name="ercAccounts['%{#serviceIndx}']" value="%{getErcAccount(#sd.attributes)}"/>

                <tr class="cols_1_error" style="display:none;">
                    <td colspan="7"></td>
                </tr>
                <tr class="cols_1 service_payment">
                    <td class="col"><s:property value="getErcAccount(#sd.attributes)" /></td>
                    <td class="col"><s:property value="#sd.serviceProviderAccount" /></td>
                    <td class="col">
                        <s:property value="#sd.personFio" />
                    </td>
                    <td class="col"><s:property value="getServiceName(#sd.serviceMasterIndex)" /></td>
                    <td class="col"><s:property value="getProviderName(#sd.serviceMasterIndex)" /></td>
                    <td class="col"><s:property value="#sd.outgoingBalance" /></td>
                    <td class="col">
                        <s:textfield name="payments['%{#serviceIndx}']"
                                     id="payments_%{#serviceIndx}"
                                     value="%{#sd.outgoingBalance}"
                                     onchange="onChangePaymentHandler('payments_%{#serviceIndx}');"
                                     cssStyle="width:100%;text-align:right;" />
                    </td>
                    <td class="col">
                        <img id="payments_<s:property value="#serviceIndx" />_copy"
                             src="<s:url value="/resources/common/img/i_copy.gif" />"
                             alt="<s:text name="payments.quittances.quittance_pay.copy" />" />
                    </td>
                </tr>
            </s:iterator>
        </s:iterator>

        <tr class="cols_1_error" style="display:none;">
            <td colspan="8"></td>
        </tr>
        <tr class="cols_1">
            <td class="col" colspan="6" style="text-align:right;font-weight:bold;">
                <s:text name="payments.quittances.quittance_pay.total_payable" />
            </td>
            <td class="col">
                <s:textfield name="totalToPay" readonly="true" value="%{getTotalToPay()}" cssStyle="width:100%;text-align:right;" />
            </td>
            <td class="col"></td>
        </tr>
        <tr class="cols_1_error" style="display:none;">
            <td colspan="8"></td>
        </tr>
        <tr class="cols_1">
            <td colspan="6" style="font-weight:bold;text-align:right;">
                <s:text name="payments.quittance.payment.input" />
            </td>
            <td>
                <s:textfield name="inputSum" cssStyle="width:100%;text-align:right;" value="%{getTotalToPay()}"
                             onchange="onChangeInputHandler('inputSum');" />
            </td>
            <td class="col"></td>
        </tr>
        <tr class="cols_1">
            <td colspan="6" style="font-weight:bold;text-align:right;">
                <s:text name="payments.quittance.payment.change" />
            </td>
            <td>
                <s:textfield name="changeSum" cssStyle="width:100%;text-align:right;" value="0.00" readonly="true"/>
            </td>
            <td class="col"></td>
        </tr>
        <s:if test="!hasActionErrors()">
            <tr>
                <td colspan="6" style="text-align:right;">
                    <span style="display:none;">&nbsp;</span>
                    <img id="indicator" src="<s:url value="/resources/common/img/indicator.gif" />" style="display:none;" />
                    <input type="button" id="printQuittanceButton" class="btn-exit" style="width:80px;" value="<s:text name="payments.quittances.quittance_pay.print_quittance" />" />
                </td>
                <td style="text-align:right;">
                    <sec:authorize ifAllGranted="ROLE_PAYMENTS_OPERATION_ADD">
                        <input type="button" id="payQuittanceButton" class="btn-exit" style="width:80px;"
                               value="<s:text name="payments.quittances.quittance_pay.pay" />" />
                    </sec:authorize>
                </td>
                <td class="col"></td>
            </tr>
        </s:if>
    </table>
</form>

<script type="text/javascript">

    $("#quittancePayForm").ready(function() {
        FPP.bindEvents([
            <s:iterator value="quittanceInfos" id="qi" status="nQI">
                <s:iterator value="serviceDetailses" id="sd" status="status">
                    <s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}" />
                    {
                            index: "<s:property value="getServiceFullIndex(#nQI.index, #serviceId)" />",
                            content: '"<s:property value="%{getErcAccount(#sd.attributes)}" escapeHtml="false" />";' +
                                          '"<s:property value="serviceProviderAccount" escapeHtml="false" />";' +
                                          '"<s:property value="getApartmentAddress(#qi)" escapeHtml="false" />";' +
                                          '"<s:property value="%{#sd.personFio}" escapeHtml="false" />";' +
                                          '"<s:property value="getMBServiceCode(serviceMasterIndex)" escapeHtml="false" />"'
                    }<s:property value="quittanceInfos.size() - 1 == #nQI.index && serviceDetailses.size() - 1 == #status.index ? '' : ','" />
                </s:iterator>
            </s:iterator>
        ]);
        FPP.createFieldChain();
        FPP.endisPayment(false);
    });

    function preProcessPaymentValue(fieldId) {
        var el = $("#" + fieldId);
        var paymentValue = $.trim(el.val());
        el.val(paymentValue == "" ? "0.00" : paymentValue.replace(",", "."));
    }

    function onChangePaymentHandler(id) {
        preProcessPaymentValue(id);
        FPP.validatePaymentValue(id);
        FPP.postProcessPaymentValue(id);
        FPP.updateTotal();
        FPP.endisPayment(false);
    }

    function onChangeInputHandler(id) {
        preProcessPaymentValue(id);
        FPP.validateInputValue();
        FPP.postProcessPaymentValue(id);
        FPP.updateChange();
        FPP.endisPayment(false);
    }
</script>
</s:if><s:else>
	<s:text name="payments.quittances.quittance_pay.no_debts_found" />
</s:else>
