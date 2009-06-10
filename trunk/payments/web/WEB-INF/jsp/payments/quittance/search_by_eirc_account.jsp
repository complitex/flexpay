<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">

	function doSearch() {
		if (validateAccountNumber()) {
			$("#searchBtn").attr("disabled", true);
			var accountNumber = $('#accountNumber').val().trim();
			$('#searchResultsDiv').load('<s:url action="searchResults"/>', {
				'searchType' : 'EIRC_ACCOUNT',
				'searchCriteria': accountNumber,
				'actionName': 'searchByEircAccount' }, function (responseText, textStatus, XMLHttpRequest) {
					if (responseText.indexOf('j_security_check') > 0) {
						$(this).html('');
						window.location = '<s:url action="searchByEircAccount"/>';
					}
					enableSearchBtn();
			});
			$('#searchResultsDiv').show();
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

</script>

<s:actionerror />

<s:form action="searchByEircAccount">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.eirc_account" />:</td>
			<td><s:textfield id="accountNumber" name="accountNumber" cssStyle="width: 300px;" /></td>
			<td><input id="searchBtn" type="button" value="<s:text name="common.search" />" class="btn-exit"
					   onclick="doSearch();" /></td>
		</tr>
	</table>
</s:form>

<%@ include file="print.jsp" %>

<div id="searchResultsDiv" style="display: none;" />
