<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<s:actionerror />

<table cellpadding="3" cellspacing="1" border="0" width="100%">

	<form id="fobjects" method="post"
		  action="<s:url value="/dicts/buildingsCreate.action" includeParams="none" />">
		<s:hidden name="buildingId" value="%{buildingId}" />

		<tr>
			<td colspan="2">
				<%@ include file="filters/groups/country_region_town_streetname.jsp" %>
			</td>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col">
				<s:text name="ab.district" />
			</td>
			<td class="col">
				<%@ include file="filters/district_filter.jsp" %>
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
				<input type="submit" class="btn-exit" name="submitted"
					   value="<s:text name="common.save"/>" />
			</td>
		</tr>

	</form>

</table>
