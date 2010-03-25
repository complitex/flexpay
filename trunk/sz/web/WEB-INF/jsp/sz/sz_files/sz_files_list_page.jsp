<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<div id="startProcessResponse"></div>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td id="result"></td>
    </tr>
</table>

<script type="text/javascript">

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        $("#startProcessResponse").html("");
        FP.pagerAjax(element, {
            action:"<s:url action="szFilesListAjax" includeParams="none" />"
        });
    }

</script>
