<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="7">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="certificateUpload"><s:param name="cashbox.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>

	<tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="admin.certificate.alias" /></td>
        <td class="th"><s:text name="admin.certificate.description" /></td>
        <td class="th"><s:text name="admin.certificate.begin_date" /></td>
        <td class="th"><s:text name="admin.certificate.end_date" /></td>
        <td class="th">&nbsp;</td>
    </tr>

    <s:iterator value="certificates" status="status">
        <tr valign="middle"
			<s:if test="%{isExpiring(endDate)}"> class="cols_1_highlighted"</s:if>
			<s:elseif test="%{hasExpired(endDate)}"> class="cols_1_error"</s:elseif>
			<s:else> class="cols_1"</s:else>
			>

			<td class="col" width="1%">
                <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
            </td>

			<td class="col" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>

			<td class="col">
				<s:property value="alias" />
            </td>

			<td class="col">
				<s:property value="description" />
            </td>

			<td class="col">
				<s:property value="format(beginDate)" />
            </td>

			<td class="col">
				<s:property value="format(endDate)" />
            </td>

            <td class="col">
                <a href="<s:url action="certificateEdit"><s:param name="certificate.id" value="%{id}" /></s:url>">
                    <s:text name="common.edit" />
                </a>
            </td>
        </tr>
    </s:iterator>

	<tr>
        <td colspan="7">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="certificateUpload"/>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>