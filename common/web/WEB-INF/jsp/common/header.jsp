
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:i18n name="/i18n/common-messages">

<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr class="verytop">
        <td>
            <a href="<s:url value="/" />">
                <img src="<s:url value="/resources/common/img/logo.gif" includeParams="none" />"
                     width="123" height="37" alt="FlexPay" border="0" hspace="25" vspace="6"/>
            </a>
        </td>
		<sec:authorize ifAnyGranted="ROLE_BASIC">
            <td align="right">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <tr>
                    <td width="100%" align="right">
                        <span class="text-small">
                            <a href="#"><s:text name="login.user" /></a>: <sec:authentication property="principal.username"/>
                        </span>
                    </td>
                    <td><img src="<s:url value="/resources/common/img/p.gif" includeParams="none" />" width="10" height="25" alt="" /></td>
                    <td><input type="button" value="<s:text name="logout.link.title" />" class="btn-exit" onclick="location.href='<s:url value="/logout" includeParams="none" />';" /></td>
                    <td><img src="<s:url value="/resources/common/img/p.gif" includeParams="none" />" width="25" height="25" alt="" /></td>
                </tr>
                </table>
            </td>
		</sec:authorize>
    </tr>
</table>

</s:i18n>