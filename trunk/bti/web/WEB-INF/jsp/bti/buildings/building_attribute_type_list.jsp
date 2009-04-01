
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form id="fObjects">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td colspan="7">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
			</td>
		<tr>
		<tr>
			<td class="th" width="1%">
				&nbsp;
			</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
			</td>
			<td class="th">
				<s:text name="bti.building.attribute.type.name" />
			</td>
			<td class="th">
				<s:text name="bti.building.attribute.type.type" />
			</td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="types" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s">
					<s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
				</td>
				<td class="col" width="1%"><input type="checkbox" name="objectIds"
												  value="<s:property value="%{id}"/>" />
				</td>
				<td class="col">
					<s:property value="%{getName(id)}" />
				</td>
				<td class="col">
					<s:text name="%{i18nTitle}" />
				</td>
				<td class="col">
					<a href="<s:url action="buildingAttributeTypeEdit"><s:param name="attributeType.id" value="%{id}" /></s:url>"><s:text
							name="common.edit" /></a>
				</td>
			</tr>
		</s:iterator>

		<tr>
			<td colspan="7">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				<input type="button" class="btn-exit"
					   onclick="location.href='<s:url action="buildingAttributeTypeEdit"><s:param name="attributeType.id" value="0" /></s:url>';"
					   value="<s:text name="common.new"/>" />
			</td>
		</tr>

	</table>
</s:form>
