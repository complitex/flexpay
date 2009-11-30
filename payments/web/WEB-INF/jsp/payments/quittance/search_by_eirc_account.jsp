<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">

	function doSearch() {
		if (validateAccountNumber()) {
			$("#searchBtn").attr("disabled", true);
			var accountNumber = $('#accountNumber').val().trim();

			var shadowId = 'searchResultsDivShadow';
			var resultId = 'searchResultsDiv';
			FP.showShadow(shadowId, resultId);

			$('#searchResultsDiv').load('<s:url action="searchResults"/>',
				{
					'searchType' : 'EIRC_ACCOUNT',
					'searchCriteria': accountNumber,
					'actionName': 'searchByEircAccount'
				},
				function (responseText, textStatus, XMLHttpRequest) {

					if (responseText.indexOf('j_security_check') > 0) {
						$(this).html('');
						window.location = '<s:url action="searchByEircAccount"/>';
					}
					
					enableSearchBtn();
					FP.hideShadow(shadowId);
				}
			);
		}
	}

	function enableSearchBtn() {
		$("#searchBtn").attr("disabled", false);
	}

	function validateAccountNumber() {
		var accNum = $("#accountNumber").val().trim();

		if (accNum.length == 0 || accNum.length != 11) {
			alert("<s:text name="error.invalid_account_number_length"/>");
			return false;
		}

		var objRegExp = /(^-?\d\d*$)/;

		if (!objRegExp.test(accNum)) {
			alert("<s:text name="error.invalid_account_number_format"/>");
			return false;
		}

		return true;
	}

	$(function() {
		$('#accountNumber').focus();

		$('#accountNumber').bind('keypress', function(event) {
			if (event.keyCode == 13) {
				doSearch();
			}
		});
	});

</script>

<s:actionerror/>

<s:form action="searchByEircAccount" onsubmit="return false;">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.eirc_account"/>:</td>
			<td><s:textfield id="accountNumber" name="accountNumber" cssStyle="width: 300px;"/></td>
			<td><input id="searchBtn" type="button" value="<s:text name="common.search" />" class="btn-exit"
					   onclick="doSearch();"/></td>
		</tr>
	</table>
</s:form>

<%@ include file="print.jsp" %>

<div id="searchResultsDiv"></div>