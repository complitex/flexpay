<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%
    Cookie[] requesCookies = request.getCookies();
    if (requesCookies != null) {
        for (Cookie cookie : requesCookies) {
%>
            <%=cookie.getName()%>:<%=cookie.getValue()%><br/>
<%
        }
    }
%>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr class="verytop">
		<td>
            <a href="<s:url value="/" includeParams="none" />">
                <img src="<s:url value="/resources/common/img/logo.gif" includeParams="none" />"
                     width="123" height="37" alt="FlexPay" border="0" hspace="25" vspace="6" />
            </a>
		</td>
		<sec:authorize ifAnyGranted="ROLE_BASIC">
			<td align="right">
    			<span class="text-small">
                    <s:text name="login.username" />: <sec:authentication property="principal.username" />
				    <a href="<s:url value="/logout" includeParams="none" />"><s:text name="logout.link.title" /></a>
                </span>
			</td>
		</sec:authorize>
	</tr>
</table>

<p>

<s:i18n name="/i18n/common-messages">

	<div style="text-align:center;">
		<h1><s:text name="forbidden_page_message" /></h1>
<%--
        <h2>User: <%=request.getUserPrincipal().getName()%></h2>
        <h2>Role: <%=request.isUserInRole("ROLE_BASIC")%></h2>
--%>
        <h2>User: <sec:authentication property="principal.username" /></h2>
        <sec:authorize ifAnyGranted="ROLE_BASIC">
            <h2>Congrats!! You have the Basic role</h2>
        </sec:authorize>
	</div>

</s:i18n>