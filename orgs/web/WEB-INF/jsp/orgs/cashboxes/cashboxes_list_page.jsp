<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
<s:if test="paymentPointFilter != null">
    <tr>
        <td>
            <%@include file="../filters/payment_point_filter.jsp"%>
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
            action:"<s:url action="cashboxesListAjax"/>"
            <s:if test="paymentPointFilter != null && paymentPointFilter.selectedId != null && paymentPointFilter.selectedId > 0">
            , params: {
                "paymentPointFilter.selectedId":<s:property value="paymentPointFilter.selectedId" />
            }
            </s:if>
        });
    }

    function deleteAjax() {
        FP.serviceElements("<s:url action="cashboxDelete" />", "objectIds", pagerAjax);
    }

</script>
