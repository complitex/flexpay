<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

	$(function() {
		disablePayment();
	});

	/**
	 * UI buttons controlling functions
	 */
	var paymentEnabled = false;

	function disableButton(button) {
		$(button).attr('disabled', 'disabled');
		$(button).removeClass('btn-exit');
        $(button).addClass('btn-search');
	}

	function enableButton(button) {
		$(button).removeAttr('disabled');
		$(button).removeClass('btn-search');
        $(button).addClass('btn-exit');
	}

	function disablePayment() {
		disableButton('#payQuittanceButton');
		paymentEnabled = false;
	}

	function enablePayment() {
		enableButton('#payQuittanceButton');
		paymentEnabled = true;
	}

	function enableButtons() {
		enableButton('#payQuittanceButton');
		enableButton('#printQuittanceButton');

		if (paymentEnabled) {
			enablePayment();
		} else {
			disablePayment();
		}
	}

	function disableButtons() {
		disableButton('#payQuittanceButton');
		disableButton('#printQuittanceButton');
	}

	function doPayQuittance() {
		$("#quittancePayForm").attr("action", "<s:url action="paymentsQuittancePay" />").submit();
		$("#quittancePayForm").removeAttr('action');
	}

	function doPrintQuittance() {
		$("#quittancePayForm").attr("action", "<s:url action="paymentOperationReportAction" />").attr("target", "_blank").submit();
		$("#quittancePayForm").removeAttr('action');
		$("#quittancePayForm").removeAttr('target');
		enablePayment();
	}
</script>