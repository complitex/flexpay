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

		<%@ include file="/WEB-INF/jsp/common/layouts/language_switch.jsp" %>

		<!--<form name="search">-->
		<td class="topmenu_form_search" nowrap="1" >&nbsp;
			<!--<input type="text" class="form-search">&nbsp;<input type="button" value="Найти" class="btn-search" />-->
		</td>
        <!--</form>-->
    </tr>
</table>