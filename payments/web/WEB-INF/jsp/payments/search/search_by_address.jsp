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
	<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp" %>

	<script type="text/javascript">

		$(function() {
			FF.createFilter("country", {
				action: "<s:url action="countryFilterAjax" namespace="/dicts" includeParams="none"/>"
			});
			FF.createFilter("region", {
				action: "<s:url action="regionFilterAjax" namespace="/dicts" includeParams="none"/>",
				parents: ["country"]
			});
			FF.createFilter("town", {
				action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none"/>",
				parents: ["region"]
			});
			FF.createFilter("street", {
				action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none"/>",
				parents: ["town"]
			});
			FF.createFilter("building", {
				action: "<s:url action="buildingFilterAjax" namespace="/dicts" includeParams="none"/>",
				isArray: true,
				parents: ["street"]
			});
			FF.createFilter("apartment", {
				action: "<s:url action="apartmentFilterAjax" namespace="/dicts" includeParams="none"/>",
				isArray: true,
				parents: ["building"]
			});
		});

	</script>

	<table width="100%">
		<tr>
			<td class="filter"><s:text name="payments.country"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/ajax/country_search_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="payments.region"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/ajax/region_search_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="payments.town"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/ajax/town_search_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="filter"><s:text name="payments.street"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/ajax/street_search_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="payments.building"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/ajax/building_search_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="payments.apartment"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/ajax/apartment_search_filter.jsp" %>
			</td>
		</tr>
	</table>

	<input type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="showSearchResults();"/>
</s:form>

<div id="searchResultsDiv" style="display: none;">
	<%@ include file="search_results.jsp" %>
</div>