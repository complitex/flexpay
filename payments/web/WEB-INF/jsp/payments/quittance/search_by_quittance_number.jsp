<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>

<script type="text/javascript">

	function doSearch() {
        if (validateQuittanceNumber()){
            document.getElementById("searchBtn").disabled = true;
		    var quittanceNumber = $('#searchByQuittanceNumber_quittanceNumber').val();
		    $('#searchResultsDiv').load('<s:url action="searchResults"/>', { 'searchType' : 'QUITTANCE_NUMBER', 'searchCriteria': quittanceNumber, 'actionName': 'searchByQuittanceNumber' }, enableSearchBtn());
		    $('#searchResultsDiv').show();
        }
	}

    function enableSearchButton(){
        document.getElementById("searchBtn").disabled = false;
    }

    function validateQuittanceNumber(){
        var quittanceNumber = document.getElementById("quittanceNumber").value;

        if (quittanceNumber == "" || quittanceNumber.length == 0 || quittanceNumber.length != 23){
            alert("'<s:text name="error.invalid_quittance_number_length"/>'");
            return false;
        }

        var objRegExp  = /^\d{11}-\d{2}\/\d{4}-\d{3}/;

        if (!objRegExp.test(quittanceNumber)){
            alert("'<s:text name="error.invalid_quittance_number_format"/>'");
            return false;
        }

        return true;
    }

</script>

<s:actionerror/>

<s:form action="searchByQuittanceNumber">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.quittance.number" />:</td>
			<td><s:textfield id="quittanceNumber" name="quittanceNumber" cssStyle="width: 300px;"/></td>
			<td><input id="searchBtn" type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="doSearch();"/></td>
		</tr>

	</table>
</s:form>

<div id="searchResultsDiv" style="display: none;"/>
	

