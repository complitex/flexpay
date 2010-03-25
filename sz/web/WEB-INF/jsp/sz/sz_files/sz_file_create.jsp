<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/flexpay_fileupload.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">

    $(function() {
        FPFile.createFileUploadForm("inputForm", "uploadBtn", {
            action : "<s:url action="szFileUpload" namespace="/sz" includeParams="none" />",
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
        });
    });

</script>

<form id="inputForm">
    <table cellspacing="2" cellpadding="2" width="80%">
        <tr>
            <td width="30%" nowrap="nowrap">
                <s:text name="year" />&nbsp;<s:select id="year" name="year"
                          required="true"
                          list="#{(curYear - 1):(curYear - 1),curYear:curYear}"
                          value="curYear" />
            </td>
            <td width="30%" nowrap="nowrap">
                <s:text name="month" />&nbsp;<s:select name="month" list="months" value="curMonth" required="true" />
            </td>
            <td align="right" nowrap="nowrap">
                <s:text name="sz.oszn" />&nbsp;<s:select name="osznId" list="osznList" listKey="id" listValue="description" required="true" />
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <s:text name="common.file" />&nbsp;<s:file name="upload" label="File" required="true" size="75" />
            </td>
        </tr>
    </table>
    <input id="uploadBtn" type="button" value="<s:text name="common.add_to_upload" />" class="btn-exit" />
</form>

<div id="mainBlock"></div>
