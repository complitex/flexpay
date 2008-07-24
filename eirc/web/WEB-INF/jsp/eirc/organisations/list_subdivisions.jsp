<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />
<s:form method="post" id="fobjects">

	<s:hidden name="organisation.id" />
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds')"></td>
            <td class="th"><s:text name="eirc.subdivision.name"/></td>
			<td class="th"><s:text name="eirc.subdivision.description"/></td>
			<td class="th"><s:text name="eirc.subdivision.real_address"/></td>
			<td class="th"><s:text name="eirc.subdivision.head_organisation"/></td>
			<td class="th"><s:text name="eirc.subdivision.juridical_person"/></td>
			<td class="th"><s:text name="eirc.subdivision.parent_subdivision"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="subdivisions" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col"><s:property value="%{#status.index + 1}"/></td>
				<td class="col"><input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
				</td>
                <td class="col"><s:property value="repeatLevelTimes('=')"/><s:property value="getTranslation(names).name"/></td>
                <td class="col"><s:property value="getTranslation(descriptions).name"/></td>
				<td class="col"><s:property value="realAddress"/></td>
				<td class="col"><s:property value="getOrganisationName(headOrganisation)"/></td>
				<td class="col"><s:property value="getOrganisationName(juridicalPerson)"/></td>
				<td class="col"><s:property value="getSubdivisionName(parentSubdivision)"/></td>
				<td class="col"><a href="<s:url value="/eirc/subdivisionEdit.action?headOrganisation.id=%{organisation.id}&subdivision.id=%{id}" includeParams="none"/>">
						 <s:text name="common.edit"/></a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="10">
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
				<input type="submit" value="<s:text name="common.delete_selected" />" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action="subdivisionsDelete" includeParams="none"/>';"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="subdivisionEdit" includeParams="none"><s:param name="headOrganisation.id" value="%{organisation.id}"/><s:param name="subdivision.id" value="0"/></s:url>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</table>
</s:form>
