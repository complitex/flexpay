<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
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
            action:"<s:url action="serviceProviderCorrectionsListAjax" includeParams="none"/>",
            params: {
                "provider.id":<s:property value="provider.id" />
            }
        });
    }

    function deleteAjax() {
        FP.serviceElements("<s:url action="serviceProviderCorrectionDelete" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>