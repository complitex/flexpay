<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript" src="<s:url value="/resources/payments/js/flexpay_print.js" />"></script>
<script type="text/javascript">

    FPP.messages = {
        invalidPayValue : "<s:text name="payments.error.quittances.quittance_pay.invalid_pay_value" />",
        inputSumIsTooSmall : "<s:text name="payments.quittances.quittance_pay.input_sum_is_too_small" />",
        createOpServerError : "<s:text name="payments.error.creating_operation_server_error" />",
        clipboardAccessDenided : "<s:text name="payments.error.clipboard_access_denied" />",
        clipboardCopyError : "<s:text name="payments.error.clipboard_copy_error" />"
    };

    FPP.urls = {
        printButUrl : "<s:url action="createBlankOperation" />",
        payFormUrl : "<s:url action="paymentOperationReportAction" />",
        payButUrl : "<s:url action="paymentsQuittancePay" />"
    };

</script>
