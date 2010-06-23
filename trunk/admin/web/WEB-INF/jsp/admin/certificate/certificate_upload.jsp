<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="certificateUpload" method="post" enctype="multipart/form-data">

	<s:hidden name="certificate.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="admin.certificate.alias" />:</td>
			<td class="col"><s:textfield name="alias" /></td>
		</tr>

		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="admin.certificate.description" />:</td>
			<td class="col"><s:textfield name="description" /></td>
		</tr>

		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="admin.certificate.file" />:</td>
			<td class="col"><s:file name="certificateFile" /></td>
		</tr>

		<tr valign="middle" class="cols_1">
			<td colspan="2">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.upload" />" />
            </td>
		</tr>

	</table>

</s:form>