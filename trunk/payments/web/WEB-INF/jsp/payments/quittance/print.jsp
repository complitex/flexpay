<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="%{operation.notNew}">
	<script type="text/javascript">
		function printQuittance() {
			if (confirm('<s:text name="payments.quittance.payment.ask_print"/>')) {
				window.open('<s:url action="paymentOperationReportAction" ><s:param name="operation.id" value="operation.id" /></s:url>', "_blank");
			}
		}
		$(printQuittance);
	</script>
</s:if>
