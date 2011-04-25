<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_simplemodal.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/flexpay_print.jsp"%>
<%@include file="/WEB-INF/jsp/payments/quittance/print.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td><s:text name="payments.quittance.number" />:</td>
        <td><s:textfield name="quittanceNumber" cssStyle="width:300px;" /></td>
        <td>
            <input id="searchBtn" type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="doSearch();" />
        </td>
    </tr>
</table>

<%--<APPLET CODE=org.flexpay.barcode.BarcodeReaderApplet ARCHIVE=<s:url value="/resources/barcode/flexpay_barcode.jar"/> WIDTH=0 HEIGHT=0>
	<PARAM NAME="url" value="<s:url action="searchByQuittanceNumber" forceAddSchemeHostAndPort="true"/>"/>
</APPLET>--%>

<div id="searchResultsDiv"></div>

<script type="text/javascript">

	$(function() {
		$("#quittanceNumber").focus().change(function() {
            var e = $(this);
			e.val(e.val().split(";")[0]);
		}).keypress(function(event) {
            if (event.keyCode == 13) {
                doSearch();
            }
        });

	});

	function doSearch() {
		if (!validateQuittanceNumber()) {
            return;
        }
        FP.endis("#searchBtn", false);
        var quittanceNumber = $.trim($("#quittanceNumber").val());

        var shadowId = "searchResultsDivShadow";
        var resultId = "searchResultsDiv";
        FP.showShadow(shadowId, resultId);

        $("#searchResultsDiv").load("<s:url action="searchResults" />",
            {
                searchType: "QUITTANCE_NUMBER",
                searchCriteria: quittanceNumber,
                actionName: "searchByQuittanceNumber"
            },
            function (responseText) {

                if (responseText.indexOf("j_security_check") > 0) {
                    $(this).html("");
                    location.href = "<s:url action="searchByQuittanceNumber" />";
                }

                FP.endis("#searchBtn", true);
                FP.hideShadow(shadowId);
        });
	}

    function validateQuittanceNumber() {
        var quittanceNumber = $.trim($("#quittanceNumber").val());

        if (quittanceNumber.length != 23) {
            alert("<s:text name="payments.error.invalid_quittance_number_length" />");
            return false;
        }

        var objRegExp = /^\d{11}-\d{2}\/\d{4}-\d{3}/;

        if (!objRegExp.test(quittanceNumber)) {
            alert("<s:text name="payments.error.invalid_quittance_number_format" />");
            return false;
        }

        return true;
    }

</script>
