<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form action="<c:url value='/dicts/edit_town_type.action'/>" method="post">
		<input type="hidden" name="town_type_id" value="<c:out value="${requestScope['town_type_id']}"/>"/>
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><spring:message code="ab.language"/></td>
			<td class="th"><spring:message code="ab.town_type_name"/></td>
		</tr>
		<c:forEach items="${requestScope['town_names']}" varStatus="status"
				   var="regionName">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><c:out value="${status.index + 1}"/></td>
				<td class="col"><c:out value="${regionName.langTranslation.translation}"/></td>
				<td class="col"><input type="text" name="name_<c:out value="${regionName.lang.id}" />"
						   value="<c:out value="${regionName.name}" />"/></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit"
					   value="<s:text name="common.save"/>"/>
			</td>
		</tr>
	</form>
</table>
