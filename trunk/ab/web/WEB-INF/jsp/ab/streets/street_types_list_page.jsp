<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

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
            action:"<s:url action="streetTypesListAjax" namespace="/dicts" includeParams="none"/>"
        });
    }

    function deleteAjax() {
        FP.deleteElements("<s:url action="streetTypeDelete" namespace="/dicts" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>
