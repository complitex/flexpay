<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="../filters/payment_collector_filter_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td id="result">
        </td>
    </tr>
</table>

<script type="text/javascript">

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="paymentPointsListAjax" includeParams="none"/>",
            params:{
                paymentCollectorFilter:$("select[name='paymentCollectorFilter.selectedId']").get(0).value
            }
        });
    }

    function deleteAjax() {
        FP.serviceElements("<s:url action="paymentPointDelete" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>
