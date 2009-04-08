<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>

<script type="text/javascript">

	function showSearchResults() {
		// here we can check form data
		var apartment = $('#selected_apartment_id').val();

		if (apartment != '') {
			$('#searchResultsDiv').show();
		} else {
			$('#searchResultsDiv').hide();
		}

		return false;
	}
	
</script>

<s:actionerror/>

<s:form action="searchByAddress" onsubmit="showSearchResults();">
	<%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_streetname_building_apartment_ajax.jsp" %>
</s:form>

<div id="searchResultsDiv" style="display: none;">
	<%@ include file="search_results.jsp" %>
</div>