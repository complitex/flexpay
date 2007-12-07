<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form action="<c:url value='/dicts/create_country.action'/>" method="post">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="33%"><spring:message code="ab.language"/></td>
			<td class="th" width="33%"><spring:message code="ab.country_name"/></td>
			<td class="th" width="33%"><spring:message code="ab.country_shortname"/></td>
		</tr>
		<c:forEach items="${requestScope['country_names']}" varStatus="status"
				   var="cName">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><c:out value="${status.index + 1}"/></td>
				<td class="col"><c:out value="${cName.translation.translation}"/></td>
				<td class="col"><input type="text"
									   name="name_<c:out value="${cName.lang.id}" />"
									   value="<c:out value="${cName.name}" />"/></td>
				<td class="col"><input type="text"
									   name="shortname_<c:out value="${cName.lang.id}" />"
									   value="<c:out value="${cName.shortName}" />"/></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="4" height="3" bgcolor="#4a4f4f" />
		<tr>
			<td colspan="4">
				<input type="submit" class="btn-exit"
					   value="<spring:message code="common.create"/>"/>
			</td>
		</tr>
	</form>
</table>
