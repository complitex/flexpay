<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form action="<c:url value='/dicts/create_town_type.action'/>" method="post">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><spring:message code="ab.language"/></td>
			<td class="th"><spring:message code="ab.town_type_name"/></td>
		</tr>
		<c:forEach items="${requestScope['town_names']}" varStatus="status" var="regionName">
			<tr>
				<td><c:out value="${status.index + 1}"/></td>
				<td><c:out value="${regionName.translation.langTranslation}"/></td>
				<td><input type="text" name="name_<c:out value="${regionName.lang.id}" />"
						   value="<c:out value="${regionName.name}" />"/></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="0">
				<input type="submit" class="btn-exit"
					   value="<spring:message code="common.create"/>"/>
			</td>
		</tr>
	</form>
</table>
