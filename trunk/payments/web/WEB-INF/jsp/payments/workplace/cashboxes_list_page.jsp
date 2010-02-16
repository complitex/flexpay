<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:actionerror />
<s:actionmessage />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td id="result">
        </td>
    </tr>
</table>

<script type="text/javascript">

    $(function() {
        pagerAjax(null);
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="workplaceCashboxesListAjax" includeParams="none"/>",
			params: {
                "paymentPointsFilter.selectedId": <s:property value="userPreferences.paymentPointId" />
            }
        });
    }

</script>
