<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>

<script type="text/javascript">

	function doSearch() {
		var quittanceNumber = $('#searchByQuittanceNumber_quittanceNumber').val();
		$('#searchResultsDiv').load('<s:url action="searchResults"/>', { 'searchType' : 'QUITTANCE_NUMBER', 'searchCriteria': quittanceNumber });
		$('#searchResultsDiv').show();
	}

</script>

<s:actionerror/>

<s:form action="searchByQuittanceNumber">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.quittance.number" />:</td>
			<td><s:textfield name="quittanceNumber" cssStyle="width: 300px;"/></td>
			<td><input type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="doSearch();"/></td>
		</tr>

	</table>

	<div id="searchResultsDiv" style="display: none;"/>
	
</s:form>
