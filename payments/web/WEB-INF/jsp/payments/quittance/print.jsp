<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
    function printQuittance() {
        if (confirm("<s:text name="payments.quittance.payment.ask_print" />")) {
            $("#quittancePayForm").attr("action", "<s:url action="paymentOperationReportAction" />").attr("target", "_blank").submit();
            if (confirm("<s:text name="payments.quittance.payment.ask_pay" />")) {
                $("#quittancePayForm").attr("action", "<s:url action="paymentsQuittancePay" />").removeAttr("target").submit();
            }
        }
    }
</script>
