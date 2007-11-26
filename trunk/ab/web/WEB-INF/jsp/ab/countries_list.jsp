<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th"><input type="checkbox" onclick="select_checkboxes(this.value)"></td>
		<td class="th"><spring:message code="ab.country_name"/></td>
		<td class="th"><spring:message code="ab.country_shortname"/></td>
	</tr>
	<c:forEach items="${requestScope['country_names']}" varStatus="status" var="cName">
		<tr>
			<td><c:out value="${status}"/></td>
			<td><c:out value="${cName.lang}"/></td>
			<td><input type="text" name="name_<c:out value="${cName.langId}" />"
					   value="<c:out value="${cName.name}" />"/></td>
			<td><input type="text" name="shortname_<c:out value="${cName.langId}" />"
					   value="<c:out value="${cName.shortName}" />"/></td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="0">
			<input type="submit" class="btn-exit" onclick="window.location='<c:url value="/dicts/create_country.action"/>'"
				   value="<spring:message code="common.create"/>"/>
		</td>
	</tr>
</table>
