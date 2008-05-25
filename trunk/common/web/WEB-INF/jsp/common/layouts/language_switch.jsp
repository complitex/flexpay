<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<td class="topmenu_form" nowrap="1">
	<table cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td><span class="text-small"><s:text name="header.language"/>:&nbsp;</span></td>
			<s:form method="get">
				<td><select class="form-select" name="request_locale" onchange="this.form.submit()">
					<c:forEach items="${applicationScope['languages']}" var="lang">
						<option value="<c:out value="${lang.language.locale}" />" <c:if test="${sessionScope['current_locale'].id == lang.language.id}">selected</c:if>><c:out value="${lang.translation}"/></option>
					</c:forEach>
				</select>
				</td>
			</s:form>
		</tr>
	</table>
</td>