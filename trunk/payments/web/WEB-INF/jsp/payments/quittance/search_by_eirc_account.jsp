<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_simplemodal.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/flexpay_print.jsp"%>
<%@include file="/WEB-INF/jsp/payments/quittance/print.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td><s:text name="payments.eirc_account" />:</td>
        <td><s:textfield name="accountNumber" cssStyle="width:300px;" /></td>
        <td>
            <input id="searchBtn" type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="doSearch();" />
        </td>
    </tr>
</table>

<div id="searchResultsDiv"></div>

<script type="text/javascript">

    $(function() {
        $("#accountNumber").focus().keypress(function(event) {
            if (event.keyCode == 13) {
                doSearch();
            }
        });
    });

	function doSearch() {
        if (!validateAccountNumber()) {
            return;
        }
        FP.endis("#searchBtn", false);
        var accountNumber = $.trim($("#accountNumber").val());

        var shadowId = "searchResultsDivShadow";
        var resultId = "searchResultsDiv";
        FP.showShadow(shadowId, resultId);

        $("#searchResultsDiv").load("<s:url action="searchResults" />",
            {
                searchType : "EIRC_ACCOUNT",
                searchCriteria: accountNumber,
                actionName: "searchByEircAccount"
            },
            function (responseText) {

                if (responseText.indexOf("j_security_check") > 0) {
                    $(this).html("");
                    location.href = "<s:url action="searchByEircAccount" />";
                }

                FP.endis("#searchBtn", true);
                FP.hideShadow(shadowId);
        });
	}

	function validateAccountNumber() {
		var accNum = $.trim($("#accountNumber").val());

		if (accNum.length != 11) {
			alert("<s:text name="error.invalid_account_number_length" />");
			return false;
		}

		var objRegExp = /(^-?\d\d*$)/;

		if (!objRegExp.test(accNum)) {
			alert("<s:text name="error.invalid_account_number_format" />");
			return false;
		}

		return true;
	}

</script>
