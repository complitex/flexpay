<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="certificateEdit"  method="post" enctype="multipart/form-data">

	<s:hidden name="alias" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="admin.certificate.alias" />:</td>
            <td class="col"><s:property value="alias" /></td>
        </tr>
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="admin.certificate.description" />:</td>
            <td class="col"><s:textfield name="certificate.description" value="%{certificate.description}"/></td>
        </tr>
        <s:if test="certificate.id > 0">
            <tr valign="top" class="cols_1">
                <td class="col"><s:text name="admin.certificate.begin_date" />:</td>
                <td class="col"><s:date name="certificate.beginDate" format="yyyy/MM/dd" /></td>
            </tr>
            <tr valign="top" class="cols_1">
                <td class="col"><s:text name="admin.certificate.end_date" />:</td>
                <td class="col"><s:date name="certificate.endDate" format="yyyy/MM/dd" /></td>
            </tr>
        </s:if>
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="admin.certificate.blocked" />:</td>
            <td class="col"><s:checkbox name="certificate.blocked" /></td>
        </tr>
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="admin.certificate.file" />:</td>
            <td class="col"><s:file name="certificateFile" required="false" /></td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td colspan="2">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save" />" />
            </td>
        </tr>
    </table>

</s:form>