<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
			</td>
			<td class="th"><s:text name="eirc.processing.process.id" /></td>
			<td class="th"><s:text name="eirc.processing.process.name" /></td>
			<td class="th"><s:text name="eirc.processing.process.start_date" /></td>
			<td class="th"><s:text name="eirc.processing.process.end_date" /></td>
			<td class="th"><s:text name="eirc.processing.process.user" /></td>
			<td class="th"><s:text name="eirc.processing.process.state" /></td>
			<td class="th"><s:text name="eirc.processing.process.pause" /></td>

			<s:iterator value="processList" status="status">
		<tr valign="middle" class="cols_1">
			<td class="col" width="1%"><s:property value="%{#status.index + 1}" /></td>
			<td class="col" width="1%"><input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>" /></td>
			<td class="col"><s:property value="id" /></td>
			<td class="col">
				<a href="<s:url action="processViewAction"><s:param name="process.id" value="%{id}" /></s:url>">
					<s:property value="processDefinitionName" />
				</a>
			</td>
			<td class="col"><s:date name="processStartDate" format="yyyy/MM/dd" /></td>
			<td class="col"><s:date name="processEndDate" format="yyyy/MM/dd" /></td>
			<td class="col">&nbsp;</td>
			<td class="col"><s:property value="processState" /></td>
			<td class="col">&nbsp;</td>
		</tr>
		</s:iterator>

		<tr>
			<td colspan="2" align="center">
				<s:submit name="submitted" value="%{getText('ab.delete')}" cssClass="btn-exit" />
			</td>
		</tr>

	</table>
</s:form>

	