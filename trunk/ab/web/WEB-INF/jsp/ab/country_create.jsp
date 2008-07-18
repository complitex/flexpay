<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form action="<c:url value='/dicts/countryCreate.action'/>" method="post">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="33%"><s:text name="ab.language"/></td>
			<td class="th" width="33%"><s:text name="ab.country_name"/></td>
			<td class="th" width="33%"><s:text name="ab.country_shortname"/></td>
		</tr>
		<c:forEach items="${requestScope['country_names']}" varStatus="status"
				   var="rNameTrans">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><c:out value="${status.index + 1}"/></td>
				<td class="col"><c:out value="${rNameTrans.langTranslation.translation}"/></td>
				<td class="col"><input type="text"
									   name="name_<c:out value="${rNameTrans.lang.id}" />"
									   value="<c:out value="${rNameTrans.name}" />"/></td>
				<td class="col"><input type="text"
									   name="shortname_<c:out value="${rNameTrans.lang.id}" />"
									   value="<c:out value="${rNameTrans.shortName}" />"/></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="4" height="3" bgcolor="#4a4f4f" /></tr>
		<tr>
			<td colspan="4">
				<input type="submit" class="btn-exit"
					   value="<s:text name="common.create"/>"/>
			</td>
		</tr>
	</form>
</table>
