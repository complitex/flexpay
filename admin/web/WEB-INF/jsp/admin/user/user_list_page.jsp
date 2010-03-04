<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td id="result"></td>
    </tr>
</table>

<script type="text/javascript">

    $(function() {
        FP.pagerAjax(null, {
            action:"<s:url action="userListAjax" namespace="/admin" includeParams="none" />"
        });
    });

    function sorterAjax() {
        pagerAjax();
    }

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="userListAjax" namespace="/admin" includeParams="none" />",
            params:{
            }
        });
    }

</script>