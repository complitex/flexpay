<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">

	function doSearch() {
		if (validateAdress()) {
			$("#searchBtn").attr("disabled", true);
			$("#searchResultsDiv").load("<s:url action="searchResults"/>", {
				"searchType" : "ADDRESS",
				"searchCriteria": FF.filters["apartment"].value.val(),
				"actionName": "searchByAddress"
            }, function (responseText, textStatus, XMLHttpRequest) {
					if (responseText.indexOf('j_security_check') > 0) {
						$(this).html('');
						window.location = "<s:url action="searchByAddress" includeParams="none" />";
					}
					enableSearchBtn();
			});
			$("#searchResultsDiv").show();
		}
	}

	function enableSearchBtn() {
		$("#searchBtn").attr("disabled", false);
	}

	function validateAdress() {
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

</script>

<s:actionerror />

<s:form action="searchByAddress">

	<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp" %>

	<script type="text/javascript">

		$(function() {
			FF.createFilter("town", {
				action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none"/>",
                defaultValue: "<s:text name="%{userPreferences.townFilter}" />"
			});
			FF.createFilter("street", {
				action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none"/>",
				parents: ["town"],
				preRequest:false,
                defaultValue: "<s:text name="%{userPreferences.streetFilter}" />"
			});
			FF.createFilter("building", {
				action: "<s:url action="buildingFilterAjax" namespace="/dicts" includeParams="none"/>",
				isArray: true,
				parents: ["street"],
				preRequest:false,
                defaultValue: "<s:text name="%{userPreferences.buildingFilter}" />"
			});
			FF.createFilter("apartment", {
				action: "<s:url action="apartmentFilterAjax" namespace="/dicts" includeParams="none"/>",
				isArray: true,
				parents: ["building"],
				preRequest:false,
                defaultValue: "<s:text name="%{userPreferences.apartmentFilter}" />"
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

<%@include file="print.jsp" %>

<div id="searchResultsDiv"></div>
