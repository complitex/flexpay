
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
	function showSearchResults() {
		// here we can check form data
		$('#searchResultsDiv').show();
		return false;
	}
</script>

<s:actionerror/>

<s:form action="searchByAddress">
	<%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_streetname_building_ajax.jsp" %>	
</s:form>

<div id="searchResultsDiv" style="display: none;">
		<%@ include file="search_results.jsp" %>
</div>