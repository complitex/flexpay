<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:if test="!quittanceInfos.isEmpty()">

<form id="quittancePayForm" action="<s:url action="paymentOperationReportAction" includeParams="none" />">

    <s:hidden name="actionName" />
    <s:hidden name="apartmentId" />
    <s:hidden name="format" value="pdf" />
    <s:hidden name="submitted" value="true" />
    <s:hidden name="operationId" value="" />

    <table class="search_results" cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td class="th"><s:text name="payments.quittances.quittance_pay.consumer_account" /></td>
            <td class="th"><s:text name="payments.quittances.quittance_pay.fio" /></td>
            <td class="th"><s:text name="payments.quittances.quittance_pay.service" /></td>
            <td class="th"><s:text name="payments.quittances.quittance_pay.service_supplier" /></td>
            <td class="th"><s:text name="payments.quittances.quittance_pay.debt" /></td>
            <td class="th" style="width:80px;"><s:text name="payments.quittances.quittance_pay.pay" /></td>
            <td class="th">&nbsp;</td>
        </tr>
        <s:iterator value="quittanceInfos" id="qi" status="nQI">
            <s:iterator value="serviceDetailses" status="status">
                <s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}"/>
                <s:set name="serviceIndx" value="%{getServiceFullIndex(#nQI.index, #serviceId)}"/>

                <s:hidden name="payerFios['%{#serviceIndx}']" value="%{getPersonFio(#qi)}"/>
                <s:hidden name="addresses['%{#serviceIndx}']" value="%{getApartmentAddress(#qi)}"/>
                <s:hidden name="eircAccounts['%{#serviceIndx}']" value="%{getEircAccount(#qi)}"/>
                <s:hidden name="serviceProviderAccounts['%{#serviceIndx}']" value="%{serviceProviderAccount}"/>
                <s:hidden name="debts['%{#serviceIndx}']" value="%{outgoingBalance}"/>
                <s:hidden name="ercAccounts['%{#serviceIndx}']" value="%{getErcAccount(attributes)}"/>

                <tr class="cols_1_error" style="display:none;">
                    <td colspan="7"></td>
                </tr>
                <tr class="cols_1 service_payment">
                    <td class="col"><s:property value="serviceProviderAccount" /></td>
                    <td class="col"><s:property value="getPersonFio(#qi)" /></td>
                    <td class="col"><s:property value="getServiceName(serviceMasterIndex)" /></td>
                    <td class="col"><s:property value="getProviderName(serviceMasterIndex)" /></td>
                    <td class="col"><s:property value="outgoingBalance" /></td>
                    <td class="col">
                        <s:textfield name="payments['%{#serviceIndx}']"
                                     id="payments_%{#serviceIndx}"
                                     value="%{outgoingBalance}"
                                     onchange="onChangePaymentHandler('payments_%{#serviceIndx}');"
                                     cssStyle="width:100%;text-align:right;"/>
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
            <td colspan="7"></td>
        </tr>
        <tr class="cols_1">
            <td class="col" colspan="5" style="text-align:right;font-weight:bold;">
                <s:text name="payments.quittances.quittance_pay.total_payable" />
            </td>
            <td class="col">
                <s:textfield name="totalToPay" readonly="true" value="%{getTotalToPay()}" cssStyle="width:100%;text-align:right;" />
            </td>
            <td class="col"></td>
        </tr>
        <tr class="cols_1_error" style="display:none;">
            <td colspan="7"></td>
        </tr>
        <tr class="cols_1">
            <td colspan="5" style="font-weight:bold;text-align:right;">
                <s:text name="payments.quittance.payment.input" />
            </td>
            <td>
                <s:textfield name="inputSum" cssStyle="width:100%;text-align:right;" value="%{getTotalToPay()}"
                             onchange="onChangeInputHandler('inputSum');" />
            </td>
            <td class="col"></td>
        </tr>
        <tr class="cols_1">
            <td colspan="5" style="font-weight:bold;text-align:right;">
                <s:text name="payments.quittance.payment.change" />
            </td>
            <td>
                <s:textfield name="changeSum" cssStyle="width:100%;text-align:right;" value="0.00" readonly="true"/>
            </td>
            <td class="col"></td>
        </tr>
        <s:if test="!hasActionErrors()">
            <tr>
                <td colspan="5" style="text-align:right;">
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

    $(function() {
        FPP.bindEvents([
            <s:iterator value="quittanceInfos" id="qi" status="nQI">
                <s:iterator value="serviceDetailses" status="status">
                    <s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}" />
                    //<s:property value="#serviceId" />
                    {
                            index: "<s:property value="getServiceFullIndex(#nQI.index, #serviceId)" />",
                            content: '"<s:property value="%{getErcAccount(attributes)" />";' +
                                          '"<s:property value="serviceProviderAccount" />";' +
                                          '"<s:property value="getApartmentAddress(#qi)" />";' +
                                          '"<s:property value="getPersonFio(#qi)" />";' +
                                          '"<s:property value="getMBServiceCode(serviceMasterIndex)" />"'
                    }<s:property value="quittanceInfos.length - 1 == #nQI.index && detailses.length - 1 == #status.index ? '' : ','" />
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
