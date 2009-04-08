<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">

	function showSearchResults() {
		// here we can check form data
		$('#searchResultsDiv').show();

		return false;
	}

</script>

<s:actionerror/>

<s:form action="searchByAddress">
	<%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_streetname_building_apartment_ajax.jsp" %>
	<input type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="showSearchResults();"/>
</s:form>

<div id="searchResultsDiv" style="display: none;">
	<%@ include file="search_results.jsp" %>
</div>