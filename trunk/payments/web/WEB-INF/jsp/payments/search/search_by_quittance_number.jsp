<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>

<script type="text/javascript">
	function showSearchResults() {
		// here we can check form data
		$('#searchResultsDiv').show();
		return false;
	}
</script>

<s:actionerror/>

<s:form action="quittancePaySearch">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.quittance.number" />:</td>
			<td><s:textfield name="quittanceNumber" cssStyle="width: 300px;"/></td>
			<td><input type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="showSearchResults();"/></td>
		</tr>

	</table>

	<div id="searchResultsDiv" style="display: none;">
		<%@ include file="search_results.jsp" %>
	</div>

</s:form>
