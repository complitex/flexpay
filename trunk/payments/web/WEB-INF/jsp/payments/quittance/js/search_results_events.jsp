<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

	$(function() {
		bindEvents();
	});

	/**
	 * Event binding
	 */
	function bindEvents() {
	<s:iterator value="quittanceInfos" id="qi" status="nQI">
	<s:iterator value="detailses" status="status">
		<s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}"/>
		<s:set name="serviceIndx" value="%{getServiceFullIndex(#nQI.index, #serviceId)}"/>
		<%--$('#payments_<s:property value="#serviceIndx"/>').bind('blur', function(event) {--%>
			<%--replaceEmptyValueWithZero('payments_<s:property value="#serviceIndx"/>');--%>
		<%--});--%>

		<%--$('#payments_<s:property value="#serviceIndx"/>').bind('change', function(event) {--%>
			<%--disablePayment();--%>
		<%--});--%>

		$('#payments_<s:property value="#serviceIndx"/>').bind('focus', function(event) {
			setCurrentFieldIndex(event);
		});

		$('#payments_<s:property value="#serviceIndx"/>').bind('keypress', function(event) {
			updateCurrentFieldIndex(event);
		});
	</s:iterator>
	</s:iterator>

		$('#totalToPay').bind('keypress', function(event) {
			return FP.disableEnterKey(event);
		});

		$('#inputSumm').bind('keypress', function(event) {
			if (event.keyCode == 13 || event.keyCode == 9) {
				updateChange();
				if ($("#quittancePayForm").valid()) {
					   doPrintQuittance();
				}                   				
			}
    	});

		$('#changeSumm').bind('keypress', function(event) {
			return FP.disableEnterKey(event);
		});

		$('#printQuittanceButton').bind('click', function(event) {
			doPrintQuittance();
		});

		$('#payQuittanceButton').bind('click', function(event) {
			doPayQuittance();
		});
	};
</script>