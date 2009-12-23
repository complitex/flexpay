<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp" %>

<script type="text/javascript">

	function doSearch() {
		if (validateAddress()) {
			$("#searchBtn").attr("disabled", true);

			var shadowId = 'searchResultsDivShadow';
			var resultId = 'searchResultsDiv';
			FP.showShadow(shadowId, resultId);

			$("#searchResultsDiv").load("<s:url action="searchResults"/>",
			{
				"searchType" : "ADDRESS",
				"searchCriteria": FF.filters["apartment"].value.val(),
				"actionName": "searchByAddress"
            },
			function (responseText, textStatus, XMLHttpRequest) {

				if (responseText.indexOf('j_security_check') > 0) {
					$(this).html('');
					window.location = "<s:url action="searchByAddress" includeParams="none" />";
				}

				enableSearchBtn();
				FP.hideShadow(shadowId);
			});
		}
	}

	function enableSearchBtn() {
		$("#searchBtn").attr("disabled", false);
	}

	function validateAddress() {
		//validate town
		if (FF.filters["town"].value.val().length == 0) {
			alert("<s:text name="payments.error.town_is_not_selected" />");
			return false;
		}
		//validate street
		if (FF.filters["street"].value.val().length == 0) {
			alert("<s:text name="payments.error.street_is_not_selected" />");
			return false;
		}
		//validate house
		if (FF.filters["building"].value.val().length == 0) {
			alert("<s:text name="payments.error.building_is_not_selected" />");
			return false;
		}
		//validate apartment
		if (FF.filters["apartment"].value.val().length == 0) {
			alert("<s:text name="payments.error.apartment_is_not_selected" />");
			return false;
		}
		return true;
	}

	function clearResults() {
		$('#searchResultsDiv').html('');
	}
</script>

<s:actionerror />

<s:form action="searchByAddress">

	<script type="text/javascript">

		$(function() {

			// creating filters
			FF.createFilter("town", {
				action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none"/>",
                defaultValue: "<s:property value="userPreferences.townFilter" />"
			});
			FF.createFilter("street", {
				action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none"/>",
				parents: ["town"],
				preRequest:false,
                defaultValue: "<s:property value="userPreferences.streetFilter" />"
			});
			FF.createFilter("building", {
				action: "<s:url action="buildingFilterAjax" namespace="/dicts" includeParams="none"/>",
				isArray: true,
				parents: ["street"],
				preRequest:false,
                defaultValue: "<s:property value="userPreferences.buildingFilter" />"
			});
			FF.createFilter("apartment", {
				action: "<s:url action="apartmentFilterAjax" namespace="/dicts" includeParams="none"/>",
				isArray: true,
				parents: ["building"],
				preRequest:false,
                defaultValue: "<s:property value="userPreferences.apartmentFilter" />"
			});

			// initializing listeners
			FF.addListener('town', function() {	
				clearResults();
			});

			FF.addListener('street', function() {
				clearResults();
			});

			FF.addListener('building', function() {
				clearResults();
			});

			FF.addListener('apartment', function() {
				clearResults();
			});
		});

	</script>

	<table width="100%">
		<tr>
			<td class="filter"><s:text name="payments.town" /></td>
			<td id="town_raw"></td>
			<td class="filter"><s:text name="payments.street" /></td>
			<td id="street_raw"></td>
			<td rowspan="2">
				<input id="searchBtn" type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="doSearch();" tabindex="2" />
			</td>
		</tr>
		<tr>
			<td class="filter"><s:text name="payments.building" /></td>
			<td id="building_raw"></td>
			<td class="filter"><s:text name="payments.apartment" /></td>
			<td id="apartment_raw"></td>
		</tr>
	</table>

</s:form>

<%@include file="print.jsp"%>

<div id="searchResultsDiv"></div>
