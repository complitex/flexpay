<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
<s:if test="paymentPointsFilter != null">
    <tr>
        <td>
            <%@include file="../filters/payment_points_filter.jsp"%>
        </td>
    </tr>
</s:if>
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
            action:"<s:url action="cashboxesListAjax" includeParams="none"/>"
            <s:if test="paymentPointsFilter != null && paymentPointsFilter.selectedId != null && paymentPointsFilter.selectedId > 0">
            , params: {
                "paymentPointsFilter.selectedId":<s:property value="paymentPointsFilter.selectedId" />
            }
            </s:if>
        });
    }

    function deleteAjax() {
        FP.serviceElements("<s:url action="cashboxDelete" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>
