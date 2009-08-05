<%@ taglib prefix="s" uri="/struts-tags" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

	<tr>
		<td class="th" width="1%">&nbsp;</td>
		<td class="th"><s:text name="eirc.registry.file.file_name"/></td>
		<td class="th"><s:text name="eirc.registry.file.import_date"/></td>
		<td class="th"><s:text name="eirc.registry.file.actions"/></td>
	</tr>
	<s:iterator value="spFileList" status="rowstatus">
		<tr valign="middle" class="cols_1">
			<td class="col_1s"><s:property value="#rowstatus.index + 1"/></td>
			<td class="col"><s:property value="originalName"/></td>
			<td class="col"><s:date name="creationDate" format="yyyy/MM/dd HH:mm:ss" /></td>
			<!-- <td class="col"><s:property value="userName"/></td> -->
			<td class="col">
				<s:if test="%{!isLoaded(id)}">
					<a href="<s:url action='spFileAction'><s:param name="spFileId" value="%{id}"/><s:param name="action" value="'loadToDb'"/></s:url>"><s:text name="eirc.registry.file.load"/></a>
                    <s:if test="nameOnServer != null">
                        &nbsp;&nbsp;
                    </s:if>
				</s:if>
				<s:if test="nameOnServer != null">
					<a href="<s:url value='/spFileDownloadServlet'><s:param name="spFileId" value="%{id}"/></s:url>"><s:property value="originalName"/></a>
				</s:if>
			</td>

		</tr>
	</s:iterator>

</table>
