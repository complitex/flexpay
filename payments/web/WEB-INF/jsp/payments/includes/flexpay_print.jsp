<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript" src="<s:url value="/resources/payments/js/flexpay_print.js" includeParams="none" />"></script>
<script type="text/javascript">

    FPP.messages = {
        invalidPayValue : "<s:text name="eirc.error.quittances.quittance_pay.invalid_pay_value" />",
        inputSummIsTooSmall : "<s:text name="payments.quittances.quittance_pay.input_sum_is_too_small" />",
        createOpServerError : "<s:text name="payments.error.creating_operation_server_error" />",
        clipboardAccessDenided : "<s:text name="payments.errors.clipboard_access_denied" />",
        clipboardCopyError : "<s:text name="payments.errors.clipboard_copy_error" />"
    };

    FPP.urls = {
        printButUrl : "<s:url action="createBlankOperation" includeParams="none" />",
        payFormUrl : "<s:url action="paymentOperationReportAction" includeParams="none" />",
        payButUrl : "<s:url action="paymentsQuittancePay" includeParams="none" />"
    };

</script>
