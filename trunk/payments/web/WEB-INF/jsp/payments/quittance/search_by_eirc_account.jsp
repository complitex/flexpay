<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>

<script type="text/javascript">

	function doSearch() {
		var accountNumber = $('#searchByEircAccount_accountNumber').val();
		$('#searchResultsDiv').load('<s:url action="searchResults"/>', { 'searchType' : 'EIRC_ACCOUNT', 'searchCriteria': accountNumber, 'actionName': 'searchByEircAccount' });
		$('#searchResultsDiv').show();
	}

</script>

<s:actionerror/>

<s:form action="searchByEircAccount">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.eirc_account" />:</td>
			<td><s:textfield name="accountNumber" cssStyle="width: 300px;"/></td>            
			<td><input type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="doSearch();"/></td>
		</tr>
	</table>
</s:form>

<div id="searchResultsDiv" style="display: none;"/>


