<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="serviceTypeCorrectionCreate" method="POST">

	<s:hidden name="serviceType.id" />
    <s:hidden name="dataCorrection.internalObjectId" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="payments.service_type.data_source" />:</td>
            <td class="col"><%@include file="/WEB-INF/jsp/common/filter/data_source_filter.jsp"%></td>
        </tr>
        <tr valign="top" class="cols_1">
			<td class="col"><s:text name="payments.service_type.extrenal_id" />:</td>
            <td class="col"><s:textfield name="dataCorrection.externalId" /></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td colspan="2">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save" />" />
            </td>
		</tr>
	</table>

</s:form>
