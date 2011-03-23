<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/flexpay_fileupload.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">

    $(function() {
        FPFile.createFileUploadForm("inputForm", "uploadBtn", {
            action : "<s:url action="spFileUpload" namespace="/eirc" />",
            validate : function() {
                var v = "";
                $("input[type='file']").each(function() {
                    if (this.form.id == "inputForm") {
                        v = this.value;
                    }
                });
                if (v == null || v == "") {
                    alert("<s:text name="common.file.upload.error.empty_file_field" />");
                    return false;
                }
                return true;
            }
        });
    });

</script>

<form id="inputForm">
    <table cellspacing="2" cellpadding="2" width="80%">
        <tr>
            <td nowrap>
                <s:text name="eirc.file" />&nbsp;<s:file name="upload" label="File" required="true" size="75" />
            </td>
        </tr>
    </table>
    <input id="uploadBtn" type="button" value="<s:text name="common.add_to_upload" />" class="btn-exit" />
</form>

<div id="mainBlock"></div>
