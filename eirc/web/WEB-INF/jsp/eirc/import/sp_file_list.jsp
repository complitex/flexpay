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
			<td class="col"><s:property value="requestFileName"/></td>
			<td class="col"><s:date name="importDate" format="yyyy/MM/dd HH:mm:ss" /></td>
			<!-- <td class="col"><s:property value="userName"/></td> -->
			<td class="col">
				<s:if test="!isLoaded(id)">
					<a href="<s:url action='spFileAction'><s:param name="spFileId" value="%{id}"/><s:param name="action" value="'loadToDb'"/></s:url>">
						<s:text name="eirc.registry.file.load"/>
					</a>
				</s:if>
				<!-- <s:if test="loadedToDb">
						  <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="action" value="'deleteFromDb'"/></s:url>">
							delete from DB
						  </a>
						  </s:if> -->
				<!-- <s:if test="loadedToDb">
						  <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="action" value="'loadFromDb'"/></s:url>">
							load from DB
						  </a>
						</s:if> -->
				<s:if test="internalResponseFileName != null">
					<a href="<s:url value='/spFileDownloadServlet'><s:param name="spFileId" value="%{id}"/></s:url>">
						<s:property value="requestFileName"/>
					</a>
				</s:if>
				<!--  <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="action" value="'fullDelete'"/></s:url>">
						  delete
						</a> -->
			</td>

		</tr>
	</s:iterator>

</table>
