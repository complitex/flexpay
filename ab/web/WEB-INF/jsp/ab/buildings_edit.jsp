
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form>

	<s:hidden name="buildings.id" value="%{buildings.id}" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<s:iterator value="%{alternateBuildingsList}">
			<tr valign="middle" class="cols_1">
				<td class="col">
					<s:property value="%{getAddress(id)}" />
					<s:if test="primaryStatus">(<s:text name="ab.buildings.primary_status" />)</s:if>
				</td>
				<td class="col">
					<a href="<s:url value="/dicts/buildingEdit.action?buildings.id=%{id}"/>">
						<s:text name="common.edit" />
					</a>
					&nbsp;
					<a href="<s:url action="buildingDelete" includeParams="none" />?objectIds=<s:property value="id" />&redirectBuildingsId=<s:property value="buildings.id" />">
						<img src="<s:url value="/resources/common/img/i_delete.gif" />"
							 alt="<s:text name="ab.delete"/>" title="<s:text name="ab.delete"/>" />
					</a>
					<s:if test="!primaryStatus">
						&nbsp;
						<input type="button" class="btn-exit"
							   onclick="window.location='<s:url action="buildingSetPrimaryStatus" includeParams="none" />?buildings.Id=<s:property value="id" />&redirectBuildingsId=<s:property value="buildings.id" />';"
							   value="<s:text name="ab.buildings.set_primary_status"/>" />
					</s:if>
				</td>
			</tr>
		</s:iterator>

		<tr>
			<td class="th" colspan="2">
				<s:property value="%{getAddress(buildings.id)}" />
				<s:if test="buildings.primaryStatus">(<s:text name="ab.buildings.primary_status" />)</s:if>
			</td>
		</tr>


		<s:iterator value="attributeMap">
			<tr valign="middle" class="cols_1">
				<td class="col"><s:property value="%{getTypeName(key)}" /></td>
				<td class="col"><s:textfield name="attributeMap[%{key}]" value="%{value}" /></td>
			</tr>
		</s:iterator>


		<tr>
			<td colspan="2">
				<s:submit name="submitted" value="%{getText('common.save')}" cssClass="btn-exit" />
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="buildingCreate" namespace="/dicts" includeParams="none" />?buildingId=<s:property value="buildings.building.id" />';"
					   value="<s:text name="common.new"/>" />
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="buildingDelete" includeParams="none" />?objectIds=<s:property value="buildings.id" /><s:if test="!alternateBuildingsList.isEmpty()">&redirectBuildingsId=<s:property value="alternateBuildingsList.get(0).id" /></s:if>';"
					   value="<s:text name="ab.delete"/>" />
				<s:if test="!buildings.primaryStatus">
					<input type="button" class="btn-exit"
						   onclick="window.location='<s:url action="buildingSetPrimaryStatus" includeParams="none" />?buildings.id=<s:property value="buildings.id" />';"
						   value="<s:text name="ab.buildings.set_primary_status"/>" />
				</s:if>
			</td>
		</tr>

	</table>

</s:form>
