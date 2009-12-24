<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title><tiles:getAsString name="title" /></title>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/common/style/fp.css" />" />

	<%@include file="scripts.jsp"%>

</head>
<body>
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <%--<tr>--%>
            <%--<td>--%>
                <%--<tiles:insertAttribute name="header" />--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr>
            <td>
                <tiles:insertAttribute name="body" />
            </td>
        </tr>
    </table>

    <tiles:insertAttribute name="footer" />

</body>
</html>
