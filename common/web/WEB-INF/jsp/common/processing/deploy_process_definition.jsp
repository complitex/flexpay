<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">

    function validateForm() {
        var fileVal = $("#inputForm input[name=upload]").val();
        if (fileVal == null || fileVal == "") {
            alert("<s:text name="common.error.no_file" />");
            return false;
        }
        return true;
    }

    function submitForm() {
        if (validateForm()) {
            $("#inputForm").submit();
        }
    }

</script>

<s:form id="inputForm" action="processDefinitionDeploy" enctype="multipart/form-data" method="POST">
    <nobr>
        <s:text name="common.processing.definition_file" />:&nbsp;
        <s:file name="upload" required="true" size="75" /><br />
    </nobr>
    <s:hidden name="submitted" value="true" />
    <input type="button" class="btn-exit" value="<s:text name="common.upload"/>" onclick="submitForm();" />
</s:form>
