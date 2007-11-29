<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form action="<c:url value='/dicts/edit_town_type.action'/>" method="post">
		<input type="hidden" name="town_type_id" value="<c:out value="${requestScope['town_type_id']}"/>"/>
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><spring:message code="ab.language"/></td>
			<td class="th"><spring:message code="ab.town_type_name"/></td>
		</tr>
		<c:forEach items="${requestScope['town_names']}" varStatus="status"
				   var="typeName">
			<tr>
				<td><c:out value="${status.index + 1}"/></td>
				<td><c:out value="${typeName.translation.translation}"/></td>
				<td><input type="text" name="name_<c:out value="${typeName.language.id}" />"
						   value="<c:out value="${typeName.name}" />"/></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="0">
				<input type="submit" class="btn-exit"
					   value="<spring:message code="common.save"/>"/>
			</td>
		</tr>
	</form>
</table>
