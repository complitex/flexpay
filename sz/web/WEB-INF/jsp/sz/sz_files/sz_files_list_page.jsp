<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:actionerror />
<s:actionmessage />

<div id="startProcessResponse"></div>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td id="result"></td>
    </tr>
</table>

<script type="text/javascript">

    $(function() {
        pagerAjax(null);
    });

    function pagerAjax(element) {
        $("#startProcessResponse").html("");
        FP.pagerAjax(element, {
            action:"<s:url action="szFilesListAjax" includeParams="none" />"
        });
    }

</script>
