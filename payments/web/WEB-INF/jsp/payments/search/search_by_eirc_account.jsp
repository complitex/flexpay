
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
	function showSearchResults() {
		// here we can check form data		
		$('#searchResultsDiv').show();
		return false;
	}
</script>

<s:actionerror/>

<s:form>

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.eirc_account" />:</td>
			<td><s:textfield name="accountNumber" cssStyle="width: 300px;"/></td>            
			<td><input type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="showSearchResults();"/></td>
		</tr>
	</table>

	<div id="searchResultsDiv" style="display: none;">
		<%@ include file="search_results.jsp" %>
	</div>

</s:form>
