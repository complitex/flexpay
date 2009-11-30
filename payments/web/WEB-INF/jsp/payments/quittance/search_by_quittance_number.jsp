<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">

	$(function() {
		$('#quittanceNumber').focus();
		$('#quittanceNumber').change(function() {
			var quittanceNumber = ($('#quittanceNumber').val()).split(';')[0];
			$('#quittanceNumber').val(quittanceNumber);
		});
	});
		

	function enableSearchButton() {
		$("#searchBtn").attr("disabled", false);
	}

	function validateQuittanceNumber() {
		var quittanceNumber = $("#quittanceNumber").val().trim();

		if (quittanceNumber.length == 0 || quittanceNumber.length != 23) {
			alert("'<s:text name="error.invalid_quittance_number_length"/>'");
			return false;
		}

		var objRegExp = /^\d{11}-\d{2}\/\d{4}-\d{3}/;

		if (!objRegExp.test(quittanceNumber)) {
			alert("'<s:text name="error.invalid_quittance_number_format"/>'");
			return false;
		}

		return true;
	}

	function doSearch() {
		if (validateQuittanceNumber()) {
			$("#searchBtn").attr("disabled", true);
			var quittanceNumber = $('#quittanceNumber').val().trim();
			$('#searchResultsDiv').load('<s:url action="searchResults"/>', {
				'searchType' : 'QUITTANCE_NUMBER',
				'searchCriteria': quittanceNumber,
				'actionName': 'searchByQuittanceNumber' }, function (responseText, textStatus, XMLHttpRequest) {
					if (responseText.indexOf('j_security_check') > 0) {
						$(this).html('');
						window.location = '<s:url action="searchByQuittanceNumber"/>';
					}
					enableSearchBtn();
			});
			$('#searchResultsDiv').show();
		}
	}

	$(function() {
		$('#quittanceNumber').focus();

		$('#quittanceNumber').bind('keypress', function(event) {
			if (event.keyCode == 13) {
				doSearch();
			}
		});
	});
</script>

<s:actionerror />

<s:form action="searchByQuittanceNumber" onsubmit="return false;">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.quittance.number" />:</td>
			<td><s:textfield id="quittanceNumber" name="quittanceNumber" cssStyle="width: 300px;"/></td>
			<td><input id="searchBtn" type="button" value="<s:text name="common.search" />" class="btn-exit"
					   onclick="doSearch();" /></td>
		</tr>

	</table>
</s:form>

<%--<APPLET CODE=org.flexpay.barcode.BarcodeReaderApplet ARCHIVE=<s:url value="/resources/barcode/flexpay_barcode.jar"/> WIDTH=0 HEIGHT=0>
	<PARAM NAME="url" value="<s:url action="searchByQuittanceNumber" forceAddSchemeHostAndPort="true"/>"/>
</APPLET>--%>

<%@ include file="print.jsp" %>

<div id="searchResultsDiv" style="display: none;" />
	

