<%@ taglib prefix="s" uri="/struts-tags" %>

<%@include file="/WEB-INF/jsp/common/includes/flexpay_fileupload.jsp" %>

<script type="text/javascript">

    $(function() {
        FPFile.createFileUploadForm("inputForm", "uploadBtn", {
            action : "<s:url action="doSpFileUploadAjax" namespace="/eirc" includeParams="none" />",
            validate : function() {
                var v = "";
                $('input[type="file"]').each(function(i, el) {
                    if (el.form.id == "inputForm") {
                        v = el.value;
                    }
                });
                if (v == null || v == "") {
                    alert("<s:text name="common.file.upload.error.empty_file_field" />");
                    return false;
                }
                return true;
            }
        })
    });

</script>

<s:form id="inputForm">
    <table cellspacing="2" cellpadding="2" width="80%">
        <tr>
            <td nowrap="nowrap">
                <s:text name="eirc.file" />&nbsp;<s:file name="upload" label="File" required="true" size="75" />
            </td>
        </tr>
    </table>
    <input id="uploadBtn" type="button" value="<s:text name="common.add_to_upload" />" class="btn-exit" />
</s:form>

<div id="mainBlock"></div>
