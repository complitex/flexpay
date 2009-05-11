<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>

<script type="text/javascript">

	function doSearch() {
        if (validateAccountNumber()){
            document.getElementById("searchBtn").disabled = true;
		    var accountNumber = $('#searchByEircAccount_accountNumber').val();
		    $('#searchResultsDiv').load('<s:url action="searchResults"/>', { 'searchType' : 'EIRC_ACCOUNT', 'searchCriteria': accountNumber, 'actionName': 'searchByEircAccount' }, enableSearchBtn());
		    $('#searchResultsDiv').show();
        }
	}

    function enableSearchBtn(){
        document.getElementById("searchBtn").disabled = false;
    }
    
    function validateAccountNumber(){
        var accNum = document.getElementById("accountNumber").value;

        if (accNum == "" || accNum.length == 0 || accNum.length != 11){
            alert("'<s:text name="error.invalid_account_number_length"/>'");
            return false;
        }

        var objRegExp  = /(^-?\d\d*$)/;

        if (!objRegExp.test(accNum)){
            alert("'<s:text name="error.invalid_account_number_format"/>'");
            return false;
        }
        
        return true;
    }

</script>

<s:actionerror/>

<s:form action="searchByEircAccount">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.eirc_account" />:</td>
			<td><s:textfield id="accountNumber" name="accountNumber" cssStyle="width: 300px;"/></td>
			<td><input id="searchBtn" type="button" value="<s:text name="common.search" />" class="btn-exit" onclick="doSearch();"/></td>
		</tr>
	</table>
</s:form>

<div id="searchResultsDiv" style="display: none;"/>


