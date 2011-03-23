<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
	<tr>
		<td class="th" width="1%">&nbsp;</td>
		<td class="th"><s:text name="eirc.registry.file.file_name" /></td>
		<td class="th"><s:text name="eirc.registry.file.import_date" /></td>
		<td class="th"><s:text name="eirc.registry.file.actions" /></td>
	</tr>
	<s:iterator value="spFilesList" status="status">
		<tr valign="middle" class="cols_1">
            <td class="col" align="right">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
			<td class="col"><s:property value="originalName"/></td>
			<td class="col"><s:date name="creationDate" format="yyyy/MM/dd HH:mm:ss" /></td>
			<td class="col">
				<s:if test="!isLoaded(id)">
					<a href="<s:url action="spFileAction"><s:param name="spFile.id" value="%{id}" /><s:param name="action" value="'loadToDb'" /></s:url>">
                        <s:text name="eirc.registry.file.load" /></a>
					&nbsp;&nbsp;
					<a href="<s:url action="spFileAction"><s:param name="spFile.id" value="%{id}" /><s:param name="action" value="'fullDelete'" /></s:url>">
                        <s:text name="eirc.registry.file.delete" /></a>
                    <s:if test="nameOnServer != null">
                        &nbsp;&nbsp;
                    </s:if>
				</s:if>
				<s:if test="nameOnServer != null">
                    <a href="<s:url value="/download/%{id}" />"><s:property value="originalName" /></a>
				</s:if>
			</td>

		</tr>
	</s:iterator>
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>
