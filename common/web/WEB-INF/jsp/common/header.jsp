<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:i18n name="/i18n/common-messages">

<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr class="verytop">
        <td>
            <a href="<c:url value="/" />">
                <img src="<c:url value="/img/logo.gif" />"
                     width="123" height="37"
                     alt="FlexPay" border="0" hspace="25"
                     vspace="6"/>
            </a>
        </td>
        <td align="right">
            <%
                if (request.getUserPrincipal() != null) {
            %>
            <span class="text-small">User: <%=request.getUserPrincipal()%>
                <a href="<c:url value="/logout.jsp" />">
                    &nbsp
                    <s:text name="logout.link.title" />
                </a>
            </span>
            <%
                }
            %>
        </td>
    </tr>
</table>

</s:i18n>