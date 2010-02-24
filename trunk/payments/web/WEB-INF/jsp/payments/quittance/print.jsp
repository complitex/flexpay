<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>

<div id="dialog" style="display:none;">
    <s:text name="payments.reports.choose_print_format_question" /><br />
    <input type="button" class="btn-exit" onclick="FPP.doPrintQuittance('txt');" value="<s:text name="payments.reports.txt_format" />" />
    <input type="button" class="btn-exit" onclick="FPP.doPrintQuittance('pdf');" value="<s:text name="payments.reports.pdf_format" />" />
</div>

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
