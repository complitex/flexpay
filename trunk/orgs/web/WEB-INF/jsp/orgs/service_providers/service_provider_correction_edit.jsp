<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="serviceProviderCorrectionEdit">

	<s:hidden name="provider.id" />
    <s:hidden name="dataCorrection.internalObjectId" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="orgs.service_provider.data_source" />:</td>
            <td class="col">
                <select name="dataCorrection.dataSourceDescription.id" class="form-select">
                    <s:iterator value="dataSources" status="status">
                    <option value="<s:property value="id" />"<s:if test="id == dataCorrection.dataSourceDescription.id"> selected</s:if>><s:property value="description" /></option></s:iterator>
                </select>
            </td>
        </tr>
        <tr valign="top" class="cols_1">
			<td class="col"><s:text name="orgs.service_provider.extrenal_id" />:</td>
            <td class="col"><s:textfield name="dataCorrection.externalId" /></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td colspan="2">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save" />" />
            </td>
		</tr>
	</table>

</s:form>
