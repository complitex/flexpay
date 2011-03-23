<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_simplemodal.jsp"%>
<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/flexpay_print.jsp"%>
<%@include file="/WEB-INF/jsp/payments/quittance/print.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

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

<div id="searchResultsDiv"></div>

<script type="text/javascript">

    $(function() {

        // creating filters
        FF.createFilter("town", {
            action: "<s:url action="townFilterAjax" namespace="/dicts" />",
            defaultValue: "<s:property value="userPreferences.townFilter" />"
        });
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" />",
            parents: ["town"],
            preRequest:false
        });
        FF.createFilter("building", {
            action: "<s:url action="buildingFilterAjax" namespace="/dicts" />",
            isArray: true,
            parents: ["street"],
            preRequest:false
        });
        FF.createFilter("apartment", {
            action: "<s:url action="apartmentFilterAjax" namespace="/dicts" />",
            isArray: true,
            parents: ["building"],
            preRequest:false
        });

        FF.addListener("town", function() {
            clearResults();
        });
        FF.addListener("street", function() {
            clearResults();
        });
        FF.addListener("building", function() {
            clearResults();
        });
        FF.addListener("apartment", function() {
            clearResults();
        });

    });

    function clearResults() {
        $("#searchResultsDiv").empty();
    }

	function doSearch() {
		if (!validateAddress()) {
            return;
        }
        FP.endis("#searchBtn", false);

        var shadowId = "searchResultsDivShadow";
        var resultId = "searchResultsDiv";
        FP.showShadow(shadowId, resultId);

        $("#searchResultsDiv").load("<s:url action="searchResults" />",
        {
            searchType: "ADDRESS",
            searchCriteria: FF.filters["apartment"].value.val(),
            actionName: "searchByAddress"
        },
        function(response) {

            if (response.indexOf("j_security_check") > 0) {
                $(this).empty();
                location.href = "<s:url action="searchByAddress" />";
            }

            FP.endis("#searchBtn", true);
            FP.hideShadow(shadowId);
        });
	}

	function validateAddress() {
		if (FF.filters["apartment"].value.val().length == 0) {
			alert("<s:text name="payments.error.apartment_is_not_selected" />");
			return false;
		}
		return true;
	}

</script>
