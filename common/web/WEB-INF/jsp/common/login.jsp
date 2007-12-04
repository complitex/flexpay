<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:i18n name="/i18n/common-messages">

<p>
<center>
    <s:text name="login.please_login" />
    <p>
    <%
        if (request.getUserPrincipal() != null) {
    %>
    <s:text name="login.you_are_logged_on" />
    <%
    } else {
    %>

    <form method="POST" action="j_security_check">
        <table border="0">
            <tr>
                <td><s:text name="login.username" />:</td>
                <td>
                    <input type="text" name="j_username"/>
                </td>
            </tr>
            <tr>
                <td><s:text name="login.password" />:</td>
                <td>
                    <input type="password" name="j_password"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value='<s:text name="login.submit" />'/>

                </td>
            </tr>
        </table>
    </form>
    <%
        }
    %>
</center>
<p>

</s:i18n>