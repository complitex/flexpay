<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="ftypes" method="post" action="">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><input type="checkbox" onclick="select_checkboxes(this.value)">
			</td>
			<td class="th"><spring:message code="ab.town_type_name"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<c:forEach items="${requestScope['town_type_names']}" varStatus="status"
				   var="typeName">
			<tr>
				<td><c:out value="${status.index + 1}"/></td>
				<td><input type="checkbox"></td>
				<td><c:out value="${typeName.name}"/></td>
				<td>
					<a href="<c:url value="/dicts/edit_town_type.action?town_type_id=${typeName.townType.id}"/>"><spring:message
							code="common.edit_selected"/></a></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="0">
				<button class="btn-exit"
						onclick="window.location='<c:url value="/dicts/create_town_type.action"/>'"
						value="<spring:message code="common.new"/>"/>
				<input type="submit" class="btn-exit"
						onclick="$('ftypes').action='<c:url value="/dicts/delete_town_types.action"/>';$('ftypes').submit()"
						value="<spring:message code="common.delete_selected"/>"/>
			</td>
		</tr>
	</form>
</table>
