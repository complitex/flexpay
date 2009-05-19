<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">

	function doSearch() {
		if (validateAdress()) {
			$("#searchBtn").attr("disabled", true);
			var apartmentId = $('#selected_apartment_id').val();
			$('#searchResultsDiv').load('<s:url action="searchResults"/>', {
				'searchType' : 'ADDRESS',
				'searchCriteria': apartmentId,
				'actionName': 'searchByAddress' }, function (responseText, textStatus, XMLHttpRequest) {
					if (responseText.indexOf('j_security_check') > 0) {
						$(this).html('');
						window.location = '<s:url action="searchByAddress"/>';
					}
					enableSearchBtn();
			});
			$('#searchResultsDiv').show();
		}
	}

	function enableSearchBtn() {
		$("#searchBtn").attr("disabled", false);
	}

	function validateAdress() {
		//validate town
		if ($("#selected_town_id").val().length == 0) {
			alert("'<s:text name="error.town_is_not_selected"/>'");
			return false;
		}
		//validate street
		if ($("#selected_street_id").val().length == 0) {
			alert("'<s:text name="error.street_is_not_selected"/>'");
			return false;
		}
		//validate house
		if ($("#selected_building_id").val().length == 0) {
			alert("'<s:text name="error.building_is_not_selected"/>'");
			return false;
		}
		//validate appartment
		if ($("#selected_apartment_id").val().length == 0) {
			alert("'<s:text name="error.apartment_is_not_selected"/>'");
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
				action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none"/>"
			});
			FF.createFilter("street", {
				action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none"/>",
				parents: ["town"],
				preRequest:false
			});
			FF.createFilter("building", {
				action: "<s:url action="buildingFilterAjax" namespace="/dicts" includeParams="none"/>",
				isArray: true,
				parents: ["street"],
				preRequest:false
			});
			FF.createFilter("apartment", {
				action: "<s:url action="apartmentFilterAjax" namespace="/dicts" includeParams="none"/>",
				isArray: true,
				parents: ["building"],
				preRequest:false
			});
		});

	</script>

	<table width="100%">
		<tr>
			<td class="filter"><s:text name="payments.town" /></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/ajax/town_search_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="payments.street" /></td>
			<td>
				<s:if test="streetFilter.showSearchString && streetFilter.searchString != null">
					<s:set name="streetFilter.field.value" value="streetFilter.searchString" />
				</s:if>

				<input type="hidden" id="selected_street_id" name="streetFilter.selectedId"
					   value="<s:text name="%{userPreferences.streetFilterValue}" />" />
				<input type="text" class="form-search" id="street_filter" name="streetFilter.searchString" value=""
					   style="width: 200px;" />
			</td>
			<td rowspan="2">
				<input id="searchBtn" type="button" value="<s:text name="common.search" />" class="btn-exit"
					   onclick="doSearch();" tabindex="2" />
			</td>
		</tr>
		<tr>
			<td class="filter"><s:text name="payments.building" /></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/ajax/building_search_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="payments.apartment" /></td>
			<td>
				<input type="hidden" id="selected_apartment_id" name="apartmentFilter.selectedId"
					   value="<s:text name="%{userPreferences.apartmentFilterValue}" />" />
				<input type="text" class="form-search" id="apartment_filter" name="apartmentFilter.searchString"
					   value="" />
			</td>
		</tr>
	</table>

</s:form>

<div id="searchResultsDiv" />
