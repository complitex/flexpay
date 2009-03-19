
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:i18n name="/i18n/common-messages">

<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr class="verytop">
        <td>
            <a href="<c:url value="/" />">
                <img src="<c:url value="/resources/common/img/logo.gif" />"
                     width="123" height="37" alt="FlexPay" border="0" hspace="25" vspace="6"/>
            </a>
        </td>
		<sec:authorize ifAnyGranted="ROLE_BASIC">
            <td align="right">
                <span class="text-small">
                    <s:text name="login.username" />: <sec:authentication property="principal.username"/>
                    <a href="<s:url value="/logout" includeParams="none" />"><s:text name="logout.link.title" /></a>
                </span>
            </td>
		</sec:authorize>
    </tr>
</table>

</s:i18n>