<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>


<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>fp</title>
    <link rel="stylesheet" type="text/css" href="style/fp.css"/>
</head>

<%@include file="/WEB-INF/jsp/ab/login_header.jsp"%>

</br>

<center>
<%
            if(request.getUserPrincipal() != null)
            {
%>
    You are logged on.
    <%
        } else
        {
    %>

<form method="POST" action="j_security_check">
    <table border="0">
        <tr>
            <td>Username:</td>
            <td>
                <input type="text" name="j_username"/>
            </td>
        </tr>
        <tr>
            <td>Password:</td>
            <td>
                <input type="password" name="j_password"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="submit" value="Submit"/>

            </td>
        </tr>
    </table>
</form>
    <%
        }
    %>
</center>

</html>