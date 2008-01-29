<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="ftypes" method="post" action="">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><input type="checkbox"
								  onchange="FP.setCheckboxes(this.checked, 'townTypeIds')">
			</td>
			<td class="th"><s:text name="ab.town_type"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<c:forEach items="${requestScope['town_type_names']}" varStatus="status"
				   var="townName">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><c:out value="${status.index + 1}"/></td>
				<td class="col"><input type="checkbox" value="<c:out value="${townName.translatable.id}"/>"
						   name="<c:out value="townTypeIds"/>"></td>
				<td class="col"><c:out value="${townName.name}"/></td>
				<td class="col">
					<a href="<c:url value="/dicts/edit_town_type.action?town_type_id=${townName.translatable.id}"/>"><s:text
							name="common.edit_selected"/></a></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="4" height="3" bgcolor="#4a4f4f"/>
		<tr>
			<td colspan="4">
				<input type="submit" class="btn-exit"
					   onclick="$('ftypes').action='<c:url value="/dicts/delete_town_types.action"/>';$('ftypes').submit()"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<c:url value="/dicts/create_town_type.action"/>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</form>
</table>
