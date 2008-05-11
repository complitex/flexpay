<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr>
        <td class="topmenu" nowrap="1">
            <a href="<c:url value="/ab/main.action"/>" class="menu"><s:text name="menu1.ab"/></a>
        </td>
        <td class="topmenu_s" nowrap="1">
            <span class="menu"><s:text name="eirc.menu1.title"/></span>
        </td>
        <td class="topmenu" nowrap="1">
            <a href="<c:url value="/sz/main.action"/>" class="menu"><s:text name="sz.menu1.title"/></a>
        </td>

        <td class="topmenu_form" nowrap="1">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td><span class="text-small"><s:text name="header.language"/>:&nbsp;</span></td>
					<form action="<c:url value="/set_language.action"/>" method="get">
						<td><select class="form-select" name="request_locale" onchange="this.form.submit()">
							<c:forEach items="${applicationScope['languages']}" var="lang">
								<option value="<c:out value="${lang.language.locale}" />" <c:if test="${sessionScope['current_locale'].id == lang.language.id}">selected</c:if>><c:out value="${lang.translation}"/></option>
							</c:forEach>
						</select>
						</td>
					</form>
				</tr>
			</table>
		</td>
        <!--<form name="search">-->
		<!--<td class="topmenu_form_search" nowrap="1" >-->
			<!--<input type="text" class="form-search">&nbsp;<input type="button" value="Найти" class="btn-search" />-->
		<!--</td></form>-->
    </tr>
</table>