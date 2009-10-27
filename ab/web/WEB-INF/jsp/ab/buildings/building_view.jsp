<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

	<s:iterator value="building.buildingses">
		<tr valign="middle" class="cols_1">
			<td class="col">
				<s:property value="getAddress(id)" />
				<s:if test="primaryStatus">(<s:text name="ab.buildings.primary_status" />)</s:if>
			</td>
			<td class="col">
				<a href="<s:url action="editBuildingAddress" includeParams="none"><s:param name="building.id" value="building.id" /><s:param name="address.id" value="id" /></s:url>">
					<s:text name="common.edit" />
				</a>

				<s:if test="!primaryStatus">
                    &nbsp;
                    <a href="<s:url action="buildingSetPrimaryStatus" includeParams="none" ><s:param name="buildings.id" value="id" /><s:param name="redirectBuildingsId" value="building.id" /></s:url>">
                       <s:text name="ab.buildings.set_primary_status" />
                    </a>
                    &nbsp;
                    <a href="<s:url action="buildingDelete" includeParams="none"><s:param name="objectIds" value="id" /><s:param name="redirectBuildingsId" value="building.id" /></s:url>">
                        <s:text name="ab.delete" />
                    </a>
				</s:if>				
			</td>
		</tr>
	</s:iterator>

	<tr>
		<td colspan="2">
			<input type="button" class="btn-exit"
				   onclick="window.location='<s:url action="editBuildingAddress" includeParams="none"><s:param name="building.id" value="building.id" /><s:param name="address.id" value="0" /></s:url>';"
				   value="<s:text name="common.new" />" />
		</td>
	</tr>

</table>
